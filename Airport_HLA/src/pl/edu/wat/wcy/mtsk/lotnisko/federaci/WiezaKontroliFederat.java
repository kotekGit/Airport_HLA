package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.FederateLoggingServiceCalls;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;

import java.util.ArrayList;

import pl.edu.wat.wcy.mtsk.lotnisko.Utils;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.DecyzjaPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.RTIObjectsFactory;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.SamolotPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.StacjaMeteorologicznaPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.ThreadPomocnik;
import pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy.WspolneZmienne;

/**
 * @since 14.06.2014
 */
public class WiezaKontroliFederat extends Federat {

	private StacjaMeteorologicznaPomocnik modelMeteo = null;

	private ArrayList<SamolotPomocnik> zgloszeniaSamolotu;
	private SamolotPomocnik odpowiedzDlaSamolotu = null;

	public WiezaKontroliFederat(String nazwa) {
		super(nazwa);

		zgloszeniaSamolotu = new ArrayList<SamolotPomocnik>();
	}

	@Override
	public void zainicjujPublikacje() throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InteractionClassNotDefined, SaveInProgress, RestoreInProgress, ConcurrentAccessAttempted {
		int samolotUchwyt = rtiamb
				.getInteractionClassHandle(WspolneZmienne.INTERAKCJA_SAMOLOT);

		// powiadomienie RTI o możliwych publikacjach
		rtiamb.publishInteractionClass(samolotUchwyt);
	}

	@Override
	public void zainicjujSubskrybcje() throws NameNotFound,
			FederateNotExecutionMember, RTIinternalError,
			InteractionClassNotDefined, FederateLoggingServiceCalls,
			SaveInProgress, RestoreInProgress, ConcurrentAccessAttempted {

		// zainicjowanie dla stacji meteorologicznej
		int interactionHandle = rtiamb
				.getInteractionClassHandle(WspolneZmienne.INTERAKCJA_STACJA_METEOROLOGICZNA);

		rtiamb.subscribeInteractionClass(interactionHandle);

		// zainicjowanie dla samolotów
		int airplaneHandle = rtiamb.getInteractionClassHandle(WspolneZmienne.INTERAKCJA_SAMOLOT);

		rtiamb.subscribeInteractionClass(airplaneHandle);
	}

	@Override
	public void uruchom() {
		while (true) {
			// rozpoznanie wniosku
			if (zgloszeniaSamolotu.size() > 0 && modelMeteo != null) {
				SamolotPomocnik s = zgloszeniaSamolotu.remove(0);
				
				//logika decyzji
				DecyzjaPomocnik.avg(modelMeteo.toArray());
				
				//TODO sprawdzenie dostępności pasów
				
				odpowiedzDlaSamolotu = new SamolotPomocnik();
				odpowiedzDlaSamolotu.setIdSamolotu(s.getIdSamolotu());
				odpowiedzDlaSamolotu.setZezwolenie(0);
				odpowiedzDlaSamolotu.setPolozenie(0); // jako 1. pas 
				
				// wystawienie odpowiedzi
				try {
					wyslijInterakcje();
				} catch (RTIexception e) {
					log("Błąd wysyłania interakcji");
					e.printStackTrace();
				}
			}
			
			try {
				advanceTime(1.0);
			} catch (RTIexception e) {
				// TODO Auto-generated catch block
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

	@Override
	protected void wyslijInterakcje() throws RTIexception {
		RTIObjectsFactory.setInteractionModelAndSend(odpowiedzDlaSamolotu, rtiamb, fedamb);
		
		log("Wysłano decyzje do samolotu " + odpowiedzDlaSamolotu.getIdSamolotu());
		odpowiedzDlaSamolotu = null;
	}

	@Override
	public void przeniesInterakcje(ReceivedInteraction otrzymanaInterakcja,
			LogicalTime time, int idInterakciji) {
		
		Object model = RTIObjectsFactory.getRTIObjectForInteraction(
				otrzymanaInterakcja, idInterakciji, rtiamb);

		if (model.getClass() == StacjaMeteorologicznaPomocnik.class) {
			// zgłoszenie od stacji meteorologicznej

			modelMeteo = (StacjaMeteorologicznaPomocnik) model;
			log("Odebrano prognozy ze stacji meteorologicznej");
		} else if (model.getClass() == SamolotPomocnik.class) {
			// zgłoszenie od samolotu

			SamolotPomocnik m = (SamolotPomocnik) model;
			zgloszeniaSamolotu.add(m);
			log("Odebrano zgłoszenie samolotu nr " + m.getIdSamolotu());
		}
	}
}
