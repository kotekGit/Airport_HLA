/**
 * Łukasz Kotowski
 */
package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

/**
 * @author Łukasz Kotowski
 *
 */
public class ThreadPomocnik {
    
    public static void spij(long czas) {
	try {
	    Thread.sleep(czas);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }

}
