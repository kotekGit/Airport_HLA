package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import hla.rti.AttributeNotDefined;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.FederateAlreadyExecutionMember;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.FederateOwnsAttributes;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InvalidResignAction;
import hla.rti.LogicalTime;
import hla.rti.LogicalTimeInterval;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.OwnershipAcquisitionPending;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ResignAction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.jlc.RtiFactoryFactory;

import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import pl.edu.wat.wcy.mtsk.lotnisko.Utils;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.Ambasador;

/**
 * Abstrakt federata.
 * 
 * @author mariusz
 * 
 * @param <T>
 *            - dziedziczy po Ambasador
 * @see Ambasador
 */
public abstract class Federat<T extends Ambasador> implements Runnable {
	// ----------------------------------------------------------
	// STATIC VARIABLES
	// ----------------------------------------------------------
	/**
	 * The number of times we will update our attributes and send an interaction
	 */
	public static final int ITERATIONS = 20;

	/** The sync point all federates will sync up on before starting */
	public static final String READY_TO_RUN = "ReadyToRun";

	// ----------------------------------------------------------
	// INSTANCE VARIABLES
	// ----------------------------------------------------------
	RTIambassador rtiamb;
	T fedamb;
	String nazwaFederata;

	// ----------------------------------------------------------
	// CONSTRUCTORS
	// ----------------------------------------------------------

	public Federat(String nazwa) {
		nazwaFederata = nazwa;
	}

	// ----------------------------------------------------------
	// INSTANCE METHODS
	// ----------------------------------------------------------

	/**
	 * Dołączenie do federacji
	 * 
	 * @param nazwaFederacji
	 * @param ambasador
	 * @throws FederateAlreadyExecutionMember
	 * @throws FederationExecutionDoesNotExist
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws RTIinternalError
	 * @throws ConcurrentAccessAttempted
	 * @throws FederateNotExecutionMember
	 */
	public void dolaczDoFederacji(String nazwaFederacji, T ambasador)
			throws FederateAlreadyExecutionMember,
			FederationExecutionDoesNotExist, SaveInProgress, RestoreInProgress,
			RTIinternalError, ConcurrentAccessAttempted,
			FederateNotExecutionMember {

		fedamb = ambasador;
		rtiamb.joinFederationExecution(nazwaFederata, nazwaFederacji, fedamb);
		log("Dołączono do federacji: " + nazwaFederacji);

		poDolaczeniu();

		// wait until the point is announced
		while (fedamb.isAnnounced == false) {
			rtiamb.tick();
		}
	}

	protected void czekajNaSynchronizacjeFederacji() {
		while (fedamb.isReadyToRun == false) {
			try {
				rtiamb.tick();
			} catch (RTIinternalError | ConcurrentAccessAttempted e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This is just a helper method to make sure all logging it output in the
	 * same form
	 */
	protected void log(String wiadomosc) {
		System.out.println(nazwaFederata + "   : " + wiadomosc);
	}

	/**
	 * As all time-related code is Portico-specific, we have isolated it into a
	 * single method. This way, if you need to move to a different RTI, you only
	 * need to change this code, rather than more code throughout the whole
	 * class.
	 */
	protected LogicalTime convertTime(double time) {
		// PORTICO SPECIFIC!!
		return new DoubleTime(time);
	}

	/**
	 * Same as for {@link #convertTime(double)}
	 */
	protected LogicalTimeInterval convertInterval(double time) {
		// PORTICO SPECIFIC!!
		return new DoubleTimeInterval(time);
	}

	// //////////////////////////////////////////////////////////////////////////
	// //////////////////////////// Helper Methods
	// //////////////////////////////
	// //////////////////////////////////////////////////////////////////////////
	/**
	 * This method will attempt to enable the various time related properties
	 * for the federate
	 */
	protected void enableTimePolicy() throws RTIexception {
		// NOTE: Unfortunately, the LogicalTime/LogicalTimeInterval create code
		// is
		// Portico specific. You will have to alter this if you move to a
		// different RTI implementation. As such, we've isolated it into a
		// method so that any change only needs to happen in a couple of spots
		LogicalTime currentTime = convertTime(fedamb.federateTime);
		LogicalTimeInterval lookahead = convertInterval(fedamb.federateLookahead);

		// enable time regulation
		this.rtiamb.enableTimeRegulation(currentTime, lookahead);

		// tick until we get the callback
		while (fedamb.isRegulating == false) {
			rtiamb.tick();
		}

		// enable time constrained
		this.rtiamb.enableTimeConstrained();

		// tick until we get the callback
		while (fedamb.isConstrained == false) {
			rtiamb.tick();
		}
	}

	/**
	 * Rozpoczyna publikację.
	 * 
	 * @throws RTIinternalError
	 * @throws FederateNotExecutionMember
	 * @throws NameNotFound
	 * @throws ObjectClassNotDefined
	 * @throws AttributeNotDefined
	 * @throws ConcurrentAccessAttempted
	 * @throws RestoreInProgress
	 * @throws SaveInProgress
	 * @throws OwnershipAcquisitionPending
	 * @throws InteractionClassNotDefined
	 */
	public abstract void zainicjujPublikacje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			ObjectClassNotDefined, AttributeNotDefined,
			OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
			ConcurrentAccessAttempted, InteractionClassNotDefined;

	/**
	 * Rozpoczyna subskrybcję.
	 * 
	 * @throws ConcurrentAccessAttempted
	 * @throws RestoreInProgress
	 * @throws SaveInProgress
	 * @throws OwnershipAcquisitionPending
	 * @throws AttributeNotDefined
	 * @throws ObjectClassNotDefined
	 * @throws RTIinternalError
	 * @throws FederateNotExecutionMember
	 * @throws NameNotFound
	 * @throws FederateLoggingServiceCalls
	 * @throws InteractionClassNotDefined
	 */
	public abstract void zainicjujSubskrybcje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			ObjectClassNotDefined, AttributeNotDefined,
			OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
			ConcurrentAccessAttempted, InteractionClassNotDefined,
			FederateLoggingServiceCalls;

	/**
	 * Metoda do rejestrowania obiektów.
	 */
	public abstract void zarejestrujObiekty();

	/**
	 * Metoda do utylizacji zarejestrowanych obiektów.
	 */
	public abstract void usunZarejestrowaneObiekty();

	/**
	 * Metoda odpalana przed końcem działania federata. (Opcjonalna)
	 */
	public abstract void naKoniec();

	/**
	 * Metoda odpalana po dołączeniu federata do federacji.
	 * 
	 */
	public abstract void poDolaczeniuDoFederacji();

	public abstract void naPoczatek() throws CouldNotOpenFED, ErrorReadingFED,
			RTIinternalError, ConcurrentAccessAttempted;

	protected abstract void poDolaczeniu();

	/**
	 * Uruchamia główną pętle federata.
	 */
	@Override
	public void run() {
		uruchom();
	}

	/**
	 * Metoda głównego zadania federata.
	 */
	public abstract void uruchom();
	
	/**
	 * This is the main simulation loop. It can be thought of as the main method
	 * of the federate. For a description of the basic flow of this federate,
	 * see the class level comments
	 */
	public void uruchom(T ambasador) throws RTIexception {
		// 1. create the RTIambassador
		rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

		naPoczatek();

		dolaczDoFederacji(Utils.NAZWA_FEDERACJI, ambasador);

		poDolaczeniuDoFederacji();

		// 5. achieve the point and wait for synchronization
		// tell the RTI we are ready to move past the sync point and then wait
		// until the federation has synchronized on
		rtiamb.synchronizationPointAchieved(READY_TO_RUN);
		log("Osignięto punkt synchronizacji. Gotowy do uruchomienia. "
				+ READY_TO_RUN + ", waiting for federation...");

		czekajNaSynchronizacjeFederacji();

		// 6. enable time policies

		// in this section we enable/disable all time policies
		// note that this step is optional!
		// TODO na potem
		// enableTimePolicy();
		// log("Time Policy Enabled");

		// 7. publish and subscribe
		// in this section we tell the RTI of all the data we are going to
		// produce, and all the data we want to know about
		zainicjujPublikacje();
		zainicjujSubskrybcje();
		log("Published and Subscribed");

		zarejestrujObiekty();

		// uruchomienie głównej pętli federata
		this.run();

		usunZarejestrowaneObiekty();

		odlaczSieOdFederacji();

		naKoniec();
	}

	/**
	 * Odłączenie federata z federacji.
	 */
	public void odlaczSieOdFederacji() {
		try {
			rtiamb.resignFederationExecution(ResignAction.NO_ACTION);
		} catch (FederateOwnsAttributes e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResignAction e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConcurrentAccessAttempted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log("Resigned from Federation");

	}

	/**
	 * This method will register an instance of the class ObjectRoot.A and will
	 * return the federation-wide unique handle for that instance. Later in the
	 * simulation, we will update the attribute values for this instance
	 */
	protected int zarejestrujObjekt(String nazwaObiektu) throws RTIexception {
		int classHandle = rtiamb
				.getObjectClassHandle(nazwaObiektu/* "ObjectRoot.A" */);
		return rtiamb.registerObjectInstance(classHandle);
	}

	/**
	 * This method will send out an interaction of the type InteractionRoot.X.
	 * Any federates which are subscribed to it will receive a notification the
	 * next time they tick(). Here we are passing only two of the three
	 * parameters we could be passing, but we don't actually have to pass any at
	 * all!
	 */
	protected abstract void wyslijInterakcje() throws RTIexception;

	/*
	 * Sample dla wysyłania interakcji
	 * 
	 * // ///////////////////////////////////////////// // create the necessary
	 * container and values // // /////////////////////////////////////////////
	 * // create the collection to store the values in SuppliedParameters
	 * parameters = RtiFactoryFactory.getRtiFactory()
	 * .createSuppliedParameters();
	 * 
	 * // generate the new values // we use EncodingHelpers to make things nice
	 * friendly for both Java and // C++ byte[] xaValue =
	 * EncodingHelpers.encodeString("xa:" + getLbts()); byte[] xbValue =
	 * EncodingHelpers.encodeString("xb:" + getLbts());
	 * 
	 * // get the handles int classHandle =
	 * rtiamb.getInteractionClassHandle("InteractionRoot.X"); int xaHandle =
	 * rtiamb.getParameterHandle("xa", classHandle); int xbHandle =
	 * rtiamb.getParameterHandle("xb", classHandle);
	 * 
	 * // put the values into the collection parameters.add(xaHandle, xaValue);
	 * parameters.add(xbHandle, xbValue);
	 * 
	 * // //////////////////////// // send the interaction // //
	 * //////////////////////// rtiamb.sendInteraction(classHandle, parameters,
	 * generateTag());
	 * 
	 * // if you want to associate a particular timestamp with the //
	 * interaction, you will have to supply it to the RTI. Here // we send
	 * another interaction, this time with a timestamp: LogicalTime time =
	 * convertTime(fedamb.federateTime + fedamb.federateLookahead);
	 * rtiamb.sendInteraction(classHandle, parameters, generateTag(), time);
	 */

	/**
	 * This method will request a time advance to the current time, plus the
	 * given timestep. It will then wait until a notification of the time
	 * advance grant has been received.
	 */
	protected void advanceTime(double timestep) throws RTIexception {
		// request the advance
		fedamb.isAdvancing = true;
		LogicalTime newTime = convertTime(fedamb.federateTime + timestep);
		rtiamb.timeAdvanceRequest(newTime);

		// wait for the time advance to be granted. ticking will tell the
		// LRC to start delivering callbacks to the federate
		while (fedamb.isAdvancing) {
			rtiamb.tick();
		}
	}

	/**
	 * This method will attempt to delete the object instance of the given
	 * handle. We can only delete objects we created, or for which we own the
	 * privilegeToDelete attribute.
	 */
	void usunObjekt(int handle) throws RTIexception {
		rtiamb.deleteObjectInstance(handle, generateTag());
	}

	protected double getLbts() {
		return fedamb.federateTime + fedamb.federateLookahead;
	}

	protected byte[] generateTag() {
		return ("" + System.currentTimeMillis()).getBytes();
	}
}
