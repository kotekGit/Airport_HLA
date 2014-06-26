package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

public class StacjaMeteorologicznaPomocnik {
    
    private double silaWiatru;
    private double temperatura;
    private double wilgotnosc;
    private String zachmurzenie;
    
    
    public double getSilaWiatru() {
        return silaWiatru;
    }
    public void setSilaWiatru(double silaWiatru) {
        this.silaWiatru = silaWiatru;
    }
    public double getTemperatura() {
        return temperatura;
    }
    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }
    public double getWilgotnosc() {
        return wilgotnosc;
    }
    public void setWilgotnosc(double wilgotnosc) {
        this.wilgotnosc = wilgotnosc;
    }
    public String getZachmurzenie() {
        return zachmurzenie;
    }
    public void setZachmurzenie(String zachmurzenie) {
        this.zachmurzenie = zachmurzenie;
    }
}