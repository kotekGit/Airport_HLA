package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

/**
 * @author Łukasz Kotowski
 * 
 */
public class SamolotPomocnik implements HelpModel
{

    private int idSamolotu;
    private int zezwolenie;
    private int polozenie;

    public int getIdSamolotu()
    {
        return idSamolotu;
    }

    public void setIdSamolotu( int idSamolotu )
    {
        this.idSamolotu = idSamolotu;
    }

    public int getZezwolenie()
    {
        return zezwolenie;
    }

    public void setZezwolenie( int zezwolenie )
    {
        this.zezwolenie = zezwolenie;
    }

    public int getPolozenie()
    {
        return polozenie;
    }

    public void setPolozenie( int polozenie )
    {
        this.polozenie = polozenie;
    }

    @Override
    public String toString()
    {
        return "SamolotPomocnik [idSamolotu=" + idSamolotu + ", zezwolenie=" + zezwolenie + ", polozenie=" + polozenie
               + "]";
    }

}
