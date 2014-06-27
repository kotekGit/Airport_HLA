package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import java.util.ArrayList;

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
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.WiezaKontrolnaPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.WspolneZmienne;
import pl.edu.wat.wcy.mtsk.lotnisko.start.WiezaKontroli;

/**
 * Federat samolotu
 * 
 * @since 14.06.2014
 */
public class SamolotFederat extends Federat {

    RTIObjectsFactory rtiObjectsFactory;
    private ArrayList<WiezaKontrolnaPomocnik> zgloszeniaWiezy;

    public SamolotFederat(String nazwa) {
	super(nazwa);
	zgloszeniaWiezy = new ArrayList<WiezaKontrolnaPomocnik>();
    }

    @Override
    public void zainicjujPublikacje() throws NameNotFound,
	    FederateNotExecutionMember, RTIinternalError,
	    InteractionClassNotDefined, SaveInProgress, RestoreInProgress,
	    ConcurrentAccessAttempted {

	int wiezaUchwyt = rtiamb
		.getInteractionClassHandle(WspolneZmienne.INTERAKCJA_WIEZA_KONTROLNA);
	rtiamb.publishInteractionClass(wiezaUchwyt);
    }

    @Override
    public void zainicjujSubskrybcje() throws NameNotFound,
	    FederateNotExecutionMember, RTIinternalError,
	    ObjectClassNotDefined, AttributeNotDefined,
	    OwnershipAcquisitionPending, SaveInProgress, RestoreInProgress,
	    ConcurrentAccessAttempted, InteractionClassNotDefined,
	    FederateLoggingServiceCalls {

	int interactionHandle = rtiamb
		.getInteractionClassHandle(WspolneZmienne.INTERAKCJA_WIEZA_KONTROLNA);

	rtiamb.subscribeInteractionClass(interactionHandle);
    }

    @Override
    public void uruchom() {
	// TODO Auto-generated method stub
	System.out.println("Uruchomiono Samolot!!!");

	while (true) {
	    if (zgloszeniaWiezy.size() > 0) {
		WiezaKontrolnaPomocnik w = zgloszeniaWiezy.remove(0);
		if (w.getDecyzja() == 0) {
		    //odmowa lądowania.
		    log("tu cos powinno byc");
		} else {
		    //Zezwolono na lądowanie.
		    log("tu cos powinno byc 2");
		}
	    }
	    
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

    @Override
    public void przeniesInterakcje(ReceivedInteraction otrzymanaInterakcja,
	    LogicalTime time, int idInterakciji) {
	
	Object model = RTIObjectsFactory.getRTIObjectForInteraction(
		otrzymanaInterakcja, idInterakciji, rtiamb);
	if (model.getClass() == WiezaKontrolnaPomocnik.class) {
	    WiezaKontrolnaPomocnik wkp = (WiezaKontrolnaPomocnik) model; 
	    zgloszeniaWiezy.add(wkp);
	    log("Odebrano zgłoszenie wiezy: " + wkp.getDecyzja());
	}

    }

}
