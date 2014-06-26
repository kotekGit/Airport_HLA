package pl.edu.wat.wcy.mtsk.lotnisko.start;

import hla.rti.RTIexception;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.StacjaMeteorologicznaAmbasador;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.StacjaMeteorologicznaFederat;

/**
 * Uruchomienie federacji stacji meterologicznej
 * 
 */
public class StacjaMeteorologiczna {

    public static void main(String[] args) {
	String federateName = "Stacja meteorologiczna: Pogodynka";
	if (args.length != 0) {
	    federateName = args[0];
	}

	try {
	    new StacjaMeteorologicznaFederat(federateName)
		    .runFederate(new StacjaMeteorologicznaAmbasador());
	} catch (RTIexception rtie) {
	    rtie.printStackTrace();
	}

    }
}
