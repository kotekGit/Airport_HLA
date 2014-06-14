package pl.edu.wat.wcy.mtsk.lotnisko.start;

import hla.rti.RTIexception;
import pl.edu.wat.wcy.mtsk.lotnisko.federacje.GuiFederat;

/**
 * Uruchomienie federacji samolotu
 * @author mariusz
 *
 */
public class Samolot {
	public static void main(String[] args) {
		String federateName = "samolotFederate";
		if( args.length != 0 )
		{
			federateName = args[0];
		}
		
		try
		{
			// run the example federate
			new GuiFederat().runFederate( federateName );
		}
		catch( RTIexception rtie )
		{
			// an exception occurred, just log the information and exit
			rtie.printStackTrace();
		}
	}
}
