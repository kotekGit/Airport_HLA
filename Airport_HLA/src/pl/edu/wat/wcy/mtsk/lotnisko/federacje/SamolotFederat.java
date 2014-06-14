package pl.edu.wat.wcy.mtsk.lotnisko.federacje;

import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.RTIinternalError;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.SamolotAmbasador;

public class SamolotFederat extends Federat<SamolotAmbasador> {

	public SamolotFederat(String nazwa) {
		super(nazwa);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void zainicjujPublikacje() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void zainicjujSubskrybcje() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void uruchom() {
		// TODO Auto-generated method stub
		
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
}
