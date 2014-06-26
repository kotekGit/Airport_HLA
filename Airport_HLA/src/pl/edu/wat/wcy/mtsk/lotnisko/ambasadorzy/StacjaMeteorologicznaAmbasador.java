package pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy;

import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;

/**
 * Ambasador stacji meterologinczej
 * @since 14.06.2014
 */
public class StacjaMeteorologicznaAmbasador extends Ambasador {

	@Override
	public void pobierzInterakcje(int klasaInterakcji,
			ReceivedInteraction otrzymanaInterakcja, byte[] tag,
			LogicalTime time, EventRetractionHandle eventRetractionHandle) {
		// TODO Auto-generated method stub
		
	}

}
