package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

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
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;
import pl.edu.wat.wcy.mtsk.lotnisko.Utils;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.StacjaMeteorologicznaPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.ThreadPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.WspolneZmienne;

public class StacjaMeteorologicznaFederat extends Federat {

	private int uchwytDoObiektu;

	public StacjaMeteorologicznaFederat(String nazwa) {
		super(nazwa);
	}

	@Override
	public void zainicjujPublikacje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			InteractionClassNotDefined, SaveInProgress, RestoreInProgress,
			ConcurrentAccessAttempted {
		
		int stacjaMeteorologicznaUchwyt = rtiamb
				.getInteractionClassHandle("InteractionRoot.StacjaMeterologiczna");

		// powiadomienie RTI o możliwych publikacjach
		rtiamb.publishInteractionClass(stacjaMeteorologicznaUchwyt);
	}

	@Override
	public void zainicjujSubskrybcje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			ObjectClassNotDefined, AttributeNotDefined, SaveInProgress,
			RestoreInProgress, ConcurrentAccessAttempted {
	    
		/*
		 * uchwytDoObiektu = rtiamb.getObjectClassHandle("ObjectRoot.A");
		 * uchwytDoObiektuDwa = rtiamb.getAttributeHandle("aa",
		 * uchwytDoObiektu);
		 * 
		 * AttributeHandleSet attributes = RtiFactoryFactory.getRtiFactory()
		 * .createAttributeHandleSet(); attributes.add(uchwytDoObiektuDwa);
		 * 
		 * rtiamb.subscribeObjectClassAttributes(uchwytDoObiektu, attributes);
		 */
	}

	@Override
	public void uruchom() {
		System.out.println("Uruchomiono Stację meteorologiczną!!!");

		try {
			while (true) {
				ThreadPomocnik.spij(Utils.CZAS_SPANIA);
				// aktualizacja atrybutu
				// byte[] a = aktualizuj(uchwytDoObiektu);
				// System.out.println("Zwrócono: " + a);

				wyslijInterakcje();

				// request a time advance and wait until we get it
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
	public void naKoniec() {
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

	/** Jeszcze nie modyfikowane
	 * 
	 * @param uchwyt
	 * @return
	 * @throws RTIinternalError
	 * @throws ObjectNotKnown
	 * @throws FederateNotExecutionMember
	 * @throws ObjectClassNotDefined
	 * @throws NameNotFound
	 * @throws AttributeNotDefined
	 * @throws AttributeNotOwned
	 * @throws SaveInProgress
	 * @throws RestoreInProgress
	 * @throws ConcurrentAccessAttempted
	 * @throws InvalidFederationTime
	 */
	private byte[] aktualizuj(int uchwyt) throws RTIinternalError,
			ObjectNotKnown, FederateNotExecutionMember, ObjectClassNotDefined,
			NameNotFound, AttributeNotDefined, AttributeNotOwned,
			SaveInProgress, RestoreInProgress, ConcurrentAccessAttempted,
			InvalidFederationTime {
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
		rtiamb.updateAttributeValues(uchwytDoObiektu, attributes,
				generateTag(), time);

		return aaValue;
	}

	@Override
	protected void wyslijInterakcje() throws RTIexception {
		// utworzenie kolejcji do zachowania wartości
		SuppliedParameters parameters = RtiFactoryFactory.getRtiFactory()
				.createSuppliedParameters();

		int stacjaMeteorologinczaUchwyt = rtiamb
				.getInteractionClassHandle("InteractionRoot.StacjaMeterologiczna");
		int silaWiatruUchwyt = rtiamb.getParameterHandle(WspolneZmienne.STACJA_SILA_WIATRU,
				stacjaMeteorologinczaUchwyt);
		int temperaturaUchwyt = rtiamb.getParameterHandle(WspolneZmienne.STACJA_ZACHMURZENIE,
				stacjaMeteorologinczaUchwyt);
		int zachmurzenieUchwyt = rtiamb.getParameterHandle(WspolneZmienne.STACJA_TEMPERATURA,
				stacjaMeteorologinczaUchwyt);
	    int wilgotnoscUchwyt = rtiamb.getParameterHandle(WspolneZmienne.STACJA_WILGOTNOSC,
	                stacjaMeteorologinczaUchwyt);
	    int cisnienieUchwyt = rtiamb.getParameterHandle(WspolneZmienne.STACJA_CISNIENIE,
                   stacjaMeteorologinczaUchwyt);


		// przekodowanie wartości
		// ewentualnie może być np coś takiego: "100.0".getBytes() zamiast encodeString
		
		byte[] silaWiatruValue = EncodingHelpers.encodeString("" + getLbts());
		byte[] temperaturaValue = EncodingHelpers.encodeString("" + getLbts());
		byte[] zachmurzenieValue = EncodingHelpers.encodeString("" +getLbts());
	    byte[] wilgotnoscValue = EncodingHelpers.encodeString("" +getLbts());
	    byte[] cisnienieValue = EncodingHelpers.encodeString("" +getLbts());

		// ustawienie wartości
		parameters.add(silaWiatruUchwyt, silaWiatruValue);
		parameters.add(temperaturaUchwyt, temperaturaValue);
		parameters.add(zachmurzenieUchwyt, zachmurzenieValue);
	    parameters.add(wilgotnoscUchwyt, wilgotnoscValue);
	    parameters.add(cisnienieUchwyt, cisnienieValue);

		// wysłanie
		LogicalTime time = convertTime(fedamb.federateTime
				+ fedamb.federateLookahead);
		rtiamb.sendInteraction(stacjaMeteorologinczaUchwyt, parameters,
				generateTag(), time);
		log("Wysłano statystyki pogodowe");
	}

    @Override
    public void przeniesInterakcje( ReceivedInteraction otrzymanaInterakcja, LogicalTime time, int idInterakciji )
    {
        // TODO Auto-generated method stub
        
    }


}
