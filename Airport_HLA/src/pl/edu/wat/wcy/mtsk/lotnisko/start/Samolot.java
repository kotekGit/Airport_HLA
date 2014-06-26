package pl.edu.wat.wcy.mtsk.lotnisko.start;

import hla.rti.RTIexception;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.Ambasador;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.SamolotAmbasador;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.SamolotFederat;

/**
 * Uruchomienie federacji samolotu
 * 
 * @author mariusz
 * 
 */
public class Samolot {
    public static void main(String[] args) {
	String federateName = "Samolot: JEDYNAKI Airline's";
	if (args.length != 0) {
	    federateName = args[0];
	}

	try {
	    Federat federat = new SamolotFederat(federateName);
	    Ambasador ambasador = new SamolotAmbasador(federat);
	    federat.uruchom(ambasador);
	} catch (RTIexception rtie) {
	    rtie.printStackTrace();
	}
    }
}
