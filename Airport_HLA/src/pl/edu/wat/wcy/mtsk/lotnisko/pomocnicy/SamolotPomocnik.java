package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

/**
 * @author Łukasz Kotowski
 *
 */
public class SamolotPomocnik {
    
    private int numerLinni;
    /**
     * Prośba o lądowanie / start / ...
     */
    private String prosba;
    
    
    public int getNumerLinni() {
        return numerLinni;
    }
    public void setNumerLinni(int numerLinni) {
        this.numerLinni = numerLinni;
    }
    public String getProsba() {
        return prosba;
    }
    public void setProsba(String prosba) {
        this.prosba = prosba;
    } 
}
