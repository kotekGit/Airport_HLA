package pl.edu.wat.wcy.mtsk.lotnisko.start;

import pl.edu.wat.wcy.mtsk.lotnisko.federacje.GuiFederat;
import hla.rti.RTIexception;

/**
 * Uruchomienie federacji interfejsu użytkownika
 *
 */
public class Gui {

	//----------------------------------------------------------
	//                     STATIC METHODS
	//----------------------------------------------------------
	public static void main( String[] args )
	{
		String federateName = "guiFederate";
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
