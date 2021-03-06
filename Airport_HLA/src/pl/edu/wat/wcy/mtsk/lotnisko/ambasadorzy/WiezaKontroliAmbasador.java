package pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy;

import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;
import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

/**
 * Ambasador wieży kontroli lotów.
 * @since 14.06.2014
 * 
 */
public class WiezaKontroliAmbasador extends Ambasador {

	public WiezaKontroliAmbasador(Federat federat) {
		super(federat);
	}

	@Override
	public void pobierzInterakcje(int klasaInterakcji,
			ReceivedInteraction otrzymanaInterakcja, byte[] tag,
			LogicalTime time, EventRetractionHandle eventRetractionHandle) {
		

		log("Federat odebrał interakcję od RTI");
		//if (EncodingHelpers.decodeString(tag).equalsIgnoreCase(
		//		"StacjaMeterologiczna")) {
		federat.przeniesInterakcje(otrzymanaInterakcja, time, klasaInterakcji);
	}

}
