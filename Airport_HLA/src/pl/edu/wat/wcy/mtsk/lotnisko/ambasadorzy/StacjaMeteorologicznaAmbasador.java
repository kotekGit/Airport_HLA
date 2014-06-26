package pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy;

import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;
import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

/**
 * Ambasador stacji meterologinczej
 * @since 14.06.2014
 */
public class StacjaMeteorologicznaAmbasador extends Ambasador {

	public StacjaMeteorologicznaAmbasador(Federat federat) {
		super(federat);
	}

	@Override
	public void pobierzInterakcje(int klasaInterakcji,
			ReceivedInteraction otrzymanaInterakcja, byte[] tag,
			LogicalTime time, EventRetractionHandle eventRetractionHandle) {
		// TODO Auto-generated method stub
		
	}

}
