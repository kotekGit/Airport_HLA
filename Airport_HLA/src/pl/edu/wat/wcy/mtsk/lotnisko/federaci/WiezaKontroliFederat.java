package pl.edu.wat.wcy.mtsk.lotnisko.federaci;

import hla.rti.ConcurrentAccessAttempted;
import hla.rti.CouldNotOpenFED;
import hla.rti.ErrorReadingFED;
import hla.rti.RTIexception;
import hla.rti.RTIinternalError;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.WiezaKontroliAmbasador;

/**
 * @since 14.06.2014
 */
public class WiezaKontroliFederat extends Federat<WiezaKontroliAmbasador> {

	public WiezaKontroliFederat(String nazwa) {
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
		// TODO Auto-generated method stub
		
	}

}
