package pl.edu.wat.wcy.mtsk.lotnisko.start;

import hla.rti.RTIexception;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.Ambasador;
import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.WiezaKontroliAmbasador;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;
import pl.edu.wat.wcy.mtsk.lotnisko.federaci.WiezaKontroliFederat;

/**
 * Uruchomienie federacji wiezy kontroli lotow
 * 
 * @author mariusz
 * 
 */
public class WiezaKontroli {
	public static void main(String[] args) {
		String federateName = "Wieza kontroli lotow: HUSTON";
		
		if (args.length != 0) {
			federateName = args[0];
		}

		try {
			Federat federat = new WiezaKontroliFederat(federateName);
			Ambasador ambasador = new WiezaKontroliAmbasador(federat);
			federat.uruchom(ambasador);
		} catch (RTIexception rtie) {
			rtie.printStackTrace();
		}

	}
}
