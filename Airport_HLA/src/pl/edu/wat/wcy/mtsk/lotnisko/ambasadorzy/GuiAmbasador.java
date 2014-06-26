package pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.jlc.EncodingHelpers;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;

/**
 * Ambasador GUI
 * 
 * @since 14.06.2014
 * 
 */
public class GuiAmbasador extends Ambasador {


	public GuiAmbasador(Federat federat) {
		super(federat);
	}

	@Override
	public void pobierzInterakcje(int klasaInterakcji,
			ReceivedInteraction otrzymanaInterakcja, byte[] tag,
			LogicalTime time, EventRetractionHandle eventRetractionHandle) {

		System.out.println("Zmiana pogody na stacji meteorologicznej");
		if (EncodingHelpers.decodeString(tag).equalsIgnoreCase(
				"StacjaMeterologiczna")) {
			federat.przeniesInterakcje(otrzymanaInterakcja, time);
		}
	}

}
