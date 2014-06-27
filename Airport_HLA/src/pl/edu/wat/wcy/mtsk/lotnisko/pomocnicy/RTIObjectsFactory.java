package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.jlc.EncodingHelpers;

public class RTIObjectsFactory
{

    public static HelpModel getRTIObjectForInteraction( ReceivedInteraction interakcja, int idInterakcji,
            RTIambassador rtIambassador )
    {
        String nazwaInterakcji = null;
        
        try
        {

            nazwaInterakcji = rtIambassador.getInteractionClassName( idInterakcji );
        }
        catch ( InteractionClassNotDefined | FederateNotExecutionMember | RTIinternalError e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ( nazwaInterakcji.equalsIgnoreCase( WspolneZmienne.INTERAKCJA_SAMOLOT ) )
        {
            return parsujObiektSamolot( interakcja );
        }
        else if ( nazwaInterakcji.equalsIgnoreCase( WspolneZmienne.INTERAKCJA_STACJA_METEOROLOGICZNA ) )
        {
            return parsujObiektStacjiMeteorolgicznej( interakcja );
        }
        else if ( nazwaInterakcji.equalsIgnoreCase( WspolneZmienne.INTERAKCJA_WIEZA_KONTROLNA ) )
        {
            return parsujObiektWiezaKontroliLotow( interakcja );
        }
        return null;

    }

    private static SamolotPomocnik parsujObiektSamolot( ReceivedInteraction interakcja )
    {
        SamolotPomocnik samolotPomocnik = new SamolotPomocnik();
        try
        {
            samolotPomocnik.setIdSamolotu( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 0 ) ) ) );
            samolotPomocnik.setZezwolenie( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 1 ) ) ) );
            samolotPomocnik.setPolozenie( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 2 ) ) ) );
        }
        catch ( NumberFormatException | ArrayIndexOutOfBounds e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return samolotPomocnik;
    }

    private static StacjaMeteorologicznaPomocnik parsujObiektStacjiMeteorolgicznej( ReceivedInteraction interakcja )
    {
        StacjaMeteorologicznaPomocnik stacjaMeteorologicznaPomocnik = new StacjaMeteorologicznaPomocnik();

        try
        {
            stacjaMeteorologicznaPomocnik.setSilaWiatru( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 0 ) ) ) );
            stacjaMeteorologicznaPomocnik.setZachmurzenie( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 1) ) ) );
            stacjaMeteorologicznaPomocnik.setTemperatura( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 2) ) ) );
            stacjaMeteorologicznaPomocnik.setWilgotnosc( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 3 ) ) ) );
            stacjaMeteorologicznaPomocnik.setCisnienie( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 4 ) ) ) );

        }
        catch ( NumberFormatException | ArrayIndexOutOfBounds e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return stacjaMeteorologicznaPomocnik;
    }

    private static WiezaKontrolnaPomocnik parsujObiektWiezaKontroliLotow( ReceivedInteraction interakcja )
    {
        WiezaKontrolnaPomocnik wiezaKontrolnaPomocnik = new WiezaKontrolnaPomocnik();
        try
        {
            wiezaKontrolnaPomocnik.setDecyzja( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 0 ) ) ) );
            wiezaKontrolnaPomocnik.setIdSamolotu( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 1) ) ) );
            wiezaKontrolnaPomocnik.setNumerPasu( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 2 ) ) ) );

        }
        catch ( NumberFormatException | ArrayIndexOutOfBounds e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return wiezaKontrolnaPomocnik;
    }
}
