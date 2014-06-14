package pl.edu.wat.wcy.mtsk.lotnisko.federacje;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.FederateOwnsAttributes;
import hla.rti.FederatesCurrentlyJoined;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InvalidResignAction;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.OwnershipAcquisitionPending;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ResignAction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.jlc.RtiFactoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import pl.edu.wat.wcy.mtsk.lotnisko.Utils;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.GuiAmbasador;

public class GuiFederat extends Federat<GuiAmbasador> {

	private String plikFederacji;
	int objectHandle;

	public GuiFederat(String nazwa) {
		super(nazwa);

		plikFederacji = "resources/test.fed";
	}

	// ////////////////////////////
	// 2. create the federation //
	// ////////////////////////////
	// create
	// NOTE: some other federate may have already created the federation,
	// in that case, we'll just try and join it
	private void utworzFederacje() throws CouldNotOpenFED, ErrorReadingFED,
			RTIinternalError, ConcurrentAccessAttempted {

		try {
			File fom = new File(plikFederacji);
			rtiamb.createFederationExecution(Utils.NAZWA_FEDERACJI, fom.toURI()
					.toURL());
			log("Created Federation");
		} catch (FederationExecutionAlreadyExists exists) {
			log("Didn't create federation, it already existed");
		} catch (MalformedURLException urle) {
			log("Exception processing fom: " + urle.getMessage());
			urle.printStackTrace();
			return;
		}
	}

	/**
	 * This method will block until the user presses enter
	 * 
	 * WAIT FOR USER TO KICK US OFF So that there is time to add other
	 * federates, we will wait until the user hits enter before proceeding. That
	 * was, you have time to start other federates.
	 */
	protected void czekajNaFederatow() {
		log(" >>>>>>>>>> Press Enter to Continue <<<<<<<<<<");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			reader.readLine();
		} catch (Exception e) {
			log("Error while waiting for user input: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected void poDolaczeniu() {
		utworzPunktSynchronizacji();
	}

	/**
	 * 4. announce the sync point announce a sync point to get everyone on the
	 * same page. if the point has already been registered, we'll get a callback
	 * saying it failed, but we don't care about that, as long as someone
	 * registered it
	 */
	protected void utworzPunktSynchronizacji() {

		try {
			rtiamb.registerFederationSynchronizationPoint(READY_TO_RUN, null);
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SaveInProgress e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RestoreInProgress e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConcurrentAccessAttempted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void zainicjujPublikacje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			ObjectClassNotDefined, AttributeNotDefined,
			OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
			ConcurrentAccessAttempted, InteractionClassNotDefined {
		// //////////////////////////////////////////
		// publish all attributes of ObjectRoot.A //
		// //////////////////////////////////////////
		// before we can register instance of the object class ObjectRoot.A and
		// update the values of the various attributes, we need to tell the RTI
		// that we intend to publish this information

		// get all the handle information for the attributes of ObjectRoot.A
		int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.A");
		int aaHandle = rtiamb.getAttributeHandle("aa", classHandle);
		int abHandle = rtiamb.getAttributeHandle("ab", classHandle);
		int acHandle = rtiamb.getAttributeHandle("ac", classHandle);

		// package the information into a handle set
		AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory()
				.createAttributeHandleSet();
		attributes.add(aaHandle);
		attributes.add(abHandle);
		attributes.add(acHandle);

		// do the actual publication
		rtiamb.publishObjectClass(classHandle, attributes);

		// ///////////////////////////////////////////////////
		// publish the interaction class InteractionRoot.X //
		// ///////////////////////////////////////////////////
		// we want to send interactions of type InteractionRoot.X, so we need
		// to tell the RTI that we're publishing it first. We don't need to
		// inform it of the parameters, only the class, making it much simpler
		int interactionHandle = rtiamb
				.getInteractionClassHandle("InteractionRoot.X");

		// do the publication
		rtiamb.publishInteractionClass(interactionHandle);
	}

	@Override
	public void zainicjujSubskrybcje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			ObjectClassNotDefined, AttributeNotDefined,
			OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
			ConcurrentAccessAttempted, InteractionClassNotDefined,
			FederateLoggingServiceCalls {
		// TODO Auto-generated method stub
		// get all the handle information for the attributes of ObjectRoot.A
		int classHandle = rtiamb.getObjectClassHandle("ObjectRoot.A");
		int aaHandle = rtiamb.getAttributeHandle("aa", classHandle);
		int abHandle = rtiamb.getAttributeHandle("ab", classHandle);
		int acHandle = rtiamb.getAttributeHandle("ac", classHandle);

		// package the information into a handle set
		AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory()
				.createAttributeHandleSet();
		attributes.add(aaHandle);
		attributes.add(abHandle);
		attributes.add(acHandle);

		// ///////////////////////////////////////////////
		// subscribe to all attributes of ObjectRoot.A //
		// ///////////////////////////////////////////////
		// we also want to hear about the same sort of information as it is
		// created and altered in other federates, so we need to subscribe to it

		rtiamb.subscribeObjectClassAttributes(classHandle, attributes);

		int interactionHandle = rtiamb
				.getInteractionClassHandle("InteractionRoot.X");

		// //////////////////////////////////////////////////
		// subscribe to the InteractionRoot.X interaction //
		// //////////////////////////////////////////////////
		// we also want to receive other interaction of the same type that are
		// sent out by other federates, so we have to subscribe to it first
		rtiamb.subscribeInteractionClass(interactionHandle);

	}

	@Override
	public void uruchom() {
		// //////////////////////////////////
		// 9. do the main simulation loop //
		// //////////////////////////////////
		// here is where we do the meat of our work. in each iteration, we will
		// update the attribute values of the object we registered, and will
		// send an interaction.

		try {
			for (int i = 0; i < ITERATIONS; i++) {
				// 9.1 update the attribute values of the instance //
				updateAttributeValues(objectHandle);

				// 9.2 send an interaction
				sendInteraction();

				// 9.3 request a time advance and wait until we get it
				advanceTime(1.0);
				log("Time Advanced to " + fedamb.federateTime);
			}
		} catch (RTIexception exc) {
			exc.printStackTrace();
		}

	}

	@Override
	public void zarejestrujObiekty() {
		// ///////////////////////////////////
		// 8. register an object to update //
		// ///////////////////////////////////

		try {
			objectHandle = zarejestrujObjekt("ObjectRoot.A");
		} catch (RTIexception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log("Registered Object, handle=" + objectHandle);

	}

	/**
	 * 10. delete the object we created
	 */
	@Override
	public void usunZarejestrowaneObiekty() {
		try {
			usunObjekt(objectHandle);
		} catch (RTIexception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log("Deleted Object, handle=" + objectHandle);
	}

	/**
	 * 11. resign from the federation
	 */
	@Override
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
	 * 12. try and destroy the federation NOTE: we won't die if we can't do this
	 * because other federates remain. in that case we'll leave it for them to
	 * clean up
	 */
	@Override
	public void naKoniec() {
		try {
			rtiamb.destroyFederationExecution(Utils.NAZWA_FEDERACJI);
			log("Destroyed Federation");
		} catch (FederationExecutionDoesNotExist dne) {
			log("No need to destroy federation, it doesn't exist");
		} catch (FederatesCurrentlyJoined fcj) {
			log("Didn't destroy federation, federates still joined");
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConcurrentAccessAttempted e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void poDolaczeniuDoFederacji() {
		czekajNaFederatow();
	}

	@Override
	public void naPoczatek() throws CouldNotOpenFED, ErrorReadingFED,
			RTIinternalError, ConcurrentAccessAttempted {
		utworzFederacje();
	}
}
