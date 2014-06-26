package pl.edu.wat.wcy.mtsk.lotnisko.start;

import hla.rti.RTIexception;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.GuiAmbasador;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.GuiFederat;

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
			GuiFederat federat =  new GuiFederat(federateName);
			GuiAmbasador ambasador = new GuiAmbasador(federat);
			federat.uruchom(ambasador);
		}
		catch( RTIexception rtie )
		{
			// an exception occurred, just log the information and exit
			rtie.printStackTrace();
		}
	}
}
