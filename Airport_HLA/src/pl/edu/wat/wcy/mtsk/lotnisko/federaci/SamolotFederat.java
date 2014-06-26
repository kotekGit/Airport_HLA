package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import hla.rti.AttributeHandleSet;
import hla.rti.AttributeNotDefined;
import hla.rti.AttributeNotOwned;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.ObjectNotKnown;
import hla.rti.OwnershipAcquisitionPending;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.SamolotAmbasador;

/**
 * Federat samolotu
 * 
 * @since 14.06.2014
 */
public class SamolotFederat extends Federat<SamolotAmbasador> {

	private int uchwytDoObiektu;
	private int uchwytDoObiektuDwa;
	
	public SamolotFederat(String nazwa) {
		super(nazwa);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void zainicjujPublikacje() {
	}

	@Override
	public void zainicjujSubskrybcje() throws NameNotFound, FederateNotExecutionMember, RTIinternalError, ObjectClassNotDefined, AttributeNotDefined, OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress, ConcurrentAccessAttempted, InteractionClassNotDefined{
		// TODO Auto-generated method stub
		uchwytDoObiektu = rtiamb.getObjectClassHandle("ObjectRoot.A");
		uchwytDoObiektuDwa = rtiamb.getAttributeHandle("aa", uchwytDoObiektu);
		
		// package the information into a handle set
		AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory()
				.createAttributeHandleSet();
		attributes.add(uchwytDoObiektuDwa);
		
		rtiamb.subscribeObjectClassAttributes(uchwytDoObiektu, attributes);
		
	}

	@Override
	public void uruchom() {
		// TODO Auto-generated method stub
		System.out.println("Uruchomiono Samolot!!!");
		
		
		try {
			for (int i = 0; i < ITERATIONS; i++) {
				// 9.1 update the attribute values of the instance //
				byte[] a = aktualizuj(uchwytDoObiektu);
				System.out.println("Zwrócono: " + a); 
				// 9.2 send an interaction
				//sendInteraction();

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void usunZarejestrowaneObiekty() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void poDolaczeniuDoFederacji() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void naPoczatek() throws CouldNotOpenFED, ErrorReadingFED,
			RTIinternalError, ConcurrentAccessAttempted {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void poDolaczeniu() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void naKoniec() {}
	
	
	private byte[] aktualizuj(int uchwyt) throws RTIinternalError,
			ObjectNotKnown, FederateNotExecutionMember, ObjectClassNotDefined,
			NameNotFound, AttributeNotDefined, AttributeNotOwned,
			SaveInProgress, RestoreInProgress, ConcurrentAccessAttempted,
			InvalidFederationTime
	{
		SuppliedAttributes attributes = RtiFactoryFactory.getRtiFactory()
				.createSuppliedAttributes();

		byte[] aaValue = EncodingHelpers.encodeString("aa:" + getLbts());


		// get the handles
		// this line gets the object class of the instance identified by the
		// object instance the handle points to
		int classHandle = rtiamb.getObjectClass(uchwytDoObiektu);
		int aaHandle = rtiamb.getAttributeHandle("aa", classHandle);

		// put the values into the collection
		attributes.add(aaHandle, aaValue);

		// ////////////////////////
		// do the actual update //
		// ////////////////////////
		rtiamb.updateAttributeValues(uchwytDoObiektu, attributes, generateTag());

		// note that if you want to associate a particular timestamp with the
		// update. here we send another update, this time with a timestamp:
		LogicalTime time = convertTime(fedamb.federateTime
				+ fedamb.federateLookahead);
		rtiamb.updateAttributeValues(uchwytDoObiektu, attributes, generateTag(),
				time);
		
		return aaValue;
	}

	@Override
	public void przeniesInterakcje(ReceivedInteraction otrzymanaInterakcja,
			LogicalTime time) {
		// TODO Auto-generated method stub
		
	}
}
