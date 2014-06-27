package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import hla.rti.AttributeNotDefined;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.ObjectClassNotDefined;
import hla.rti.OwnershipAcquisitionPending;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import pl.edu.wat.wcy.mtsk.lotnisko.Utils;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.RTIObjectsFactory;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.SamolotPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.ThreadPomocnik;

/**
 * Federat samolotu
 * 
 * @since 14.06.2014
 */
public class SamolotFederat extends Federat {

    RTIObjectsFactory rtiObjectsFactory;
    

    public SamolotFederat(String nazwa) {
	super(nazwa);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void zainicjujPublikacje() {
    }

    @Override
    public void zainicjujSubskrybcje() throws NameNotFound,
	    FederateNotExecutionMember, RTIinternalError,
	    ObjectClassNotDefined, AttributeNotDefined,
	    OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
	    ConcurrentAccessAttempted, InteractionClassNotDefined, FederateLoggingServiceCalls {

	int interactionHandle = rtiamb
		.getInteractionClassHandle("WiezaKontrolna");
	rtiamb.subscribeInteractionClass(interactionHandle);
    }

    @Override
    public void uruchom() {
	// TODO Auto-generated method stub
	System.out.println("Uruchomiono Samolot!!!");

	while (true) {
	    try {
		advanceTime(1.0);
	    } catch (RTIexception e) {
		e.printStackTrace();
	    }
	    log("Time Advanced to " + fedamb.federateTime);
	    ThreadPomocnik.spij(Utils.CZAS_SPANIA);
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
    public void naKoniec() {
    }

    @Override
    protected void wyslijInterakcje() throws RTIexception {

    }

    @SuppressWarnings("static-access")
    @Override
    public void przeniesInterakcje(ReceivedInteraction otrzymanaInterakcja,
	    LogicalTime time, int idInterakciji) {
	
	SamolotPomocnik samolot = (SamolotPomocnik) rtiObjectsFactory.getRTIObjectForInteraction(otrzymanaInterakcja, idInterakciji, rtiamb);
	log("Dane z samolotu: " + samolot);
    }

}
