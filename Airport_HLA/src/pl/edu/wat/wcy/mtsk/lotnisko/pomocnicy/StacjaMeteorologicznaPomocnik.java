package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

public class StacjaMeteorologicznaPomocnik implements HelpModel
{

    private double silaWiatru;
    private double temperatura;
    private double wilgotnosc;
    private double zachmurzenie;
    private double cisnienie;

    public double getSilaWiatru()
    {
        return silaWiatru;
    }

    public void setSilaWiatru( double silaWiatru )
    {
        this.silaWiatru = silaWiatru;
    }

    public double getTemperatura()
    {
        return temperatura;
    }

    public void setTemperatura( double temperatura )
    {
        this.temperatura = temperatura;
    }

    public double getWilgotnosc()
    {
        return wilgotnosc;
    }

    public void setWilgotnosc( double wilgotnosc )
    {
        this.wilgotnosc = wilgotnosc;
    }

    public double getZachmurzenie()
    {
        return zachmurzenie;
    }

    public void setZachmurzenie( double zachmurzenie )
    {
        this.zachmurzenie = zachmurzenie;
    }

    public double getCisnienie()
    {
        return cisnienie;
    }

    public void setCisnienie( double cisnienie )
    {
        this.cisnienie = cisnienie;
    }

    @Override
    public String toString()
    {
        return "StacjaMeteorologicznaPomocnik [silaWiatru=" + silaWiatru + ", temperatura=" + temperatura
               + ", wilgotnosc=" + wilgotnosc + ", zachmurzenie=" + zachmurzenie + ", cisnienie=" + cisnienie + "]";
    }
    
    public double[] toArray() {
    	return new double[] { cisnienie, silaWiatru, temperatura, wilgotnosc, zachmurzenie };
    }

}
