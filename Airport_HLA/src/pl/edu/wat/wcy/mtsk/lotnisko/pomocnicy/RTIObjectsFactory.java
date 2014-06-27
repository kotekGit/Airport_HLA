package pl.edu.wat.wcy.mtsk.lotnisko.pomocnicy;

import org.portico.impl.hla13.types.DoubleTime;

import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.Ambasador;
import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.ConcurrentAccessAttempted;
import hla.rti.FederateNotExecutionMember;
import hla.rti.InteractionClassNotDefined;
import hla.rti.InteractionClassNotPublished;
import hla.rti.InteractionParameterNotDefined;
import hla.rti.InvalidFederationTime;
import hla.rti.LogicalTime;
import hla.rti.NameNotFound;
import hla.rti.RTIambassador;
import hla.rti.RTIinternalError;
import hla.rti.ReceivedInteraction;
import hla.rti.RestoreInProgress;
import hla.rti.SaveInProgress;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

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

    public static void setInteractionModelAndSend( HelpModel model, RTIambassador rtIambassador, Ambasador ambasador )
    {
        if ( model.getClass() == SamolotPomocnik.class )
        {

        }
        else if ( model.getClass() == StacjaMeteorologicznaPomocnik.class )
        {
            pobierzModelInteracjiStacjiIwyslij( (StacjaMeteorologicznaPomocnik) model, rtIambassador, ambasador );

        }
        else if ( model.getClass() == WiezaKontrolnaPomocnik.class )
        {

        }
    }

    private static void pobierzModelInteracjiStacjiIwyslij(
            StacjaMeteorologicznaPomocnik stacjaMeteorologicznaPomocnik, RTIambassador rtiamb, Ambasador fedamb )
    {
        // utworzenie kolejcji do zachowania wartości
        SuppliedParameters parameters;
        int stacjaMeteorologinczaUchwyt;
        int silaWiatruUchwyt;
        int temperaturaUchwyt;
        int zachmurzenieUchwyt;
        int wilgotnoscUchwyt;
        int cisnienieUchwyt;

        try
        {
            parameters = RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
            stacjaMeteorologinczaUchwyt = rtiamb.getInteractionClassHandle( WspolneZmienne.INTERAKCJA_STACJA_METEOROLOGICZNA );
            silaWiatruUchwyt = rtiamb.getParameterHandle( WspolneZmienne.STACJA_SILA_WIATRU,
                    stacjaMeteorologinczaUchwyt );
            temperaturaUchwyt = rtiamb.getParameterHandle( WspolneZmienne.STACJA_ZACHMURZENIE,
                    stacjaMeteorologinczaUchwyt );
            zachmurzenieUchwyt = rtiamb.getParameterHandle( WspolneZmienne.STACJA_TEMPERATURA,
                    stacjaMeteorologinczaUchwyt );
            wilgotnoscUchwyt = rtiamb.getParameterHandle( WspolneZmienne.STACJA_WILGOTNOSC, stacjaMeteorologinczaUchwyt );
            cisnienieUchwyt = rtiamb.getParameterHandle( WspolneZmienne.STACJA_CISNIENIE, stacjaMeteorologinczaUchwyt );

            // przekodowanie wartości
            // ewentualnie może być np coś takiego: "100.0".getBytes() zamiast
            // encodeString

            byte[] silaWiatruValue = EncodingHelpers.encodeString( "" + stacjaMeteorologicznaPomocnik.getSilaWiatru() );
            byte[] temperaturaValue = EncodingHelpers.encodeString( "" + stacjaMeteorologicznaPomocnik.getTemperatura() );
            byte[] zachmurzenieValue = EncodingHelpers.encodeString( ""
                                                                     + stacjaMeteorologicznaPomocnik.getZachmurzenie() );
            byte[] wilgotnoscValue = EncodingHelpers.encodeString( "" + stacjaMeteorologicznaPomocnik.getWilgotnosc() );
            byte[] cisnienieValue = EncodingHelpers.encodeString( "" + stacjaMeteorologicznaPomocnik.getCisnienie() );

            // ustawienie wartości
            parameters.add( silaWiatruUchwyt, silaWiatruValue );
            parameters.add( temperaturaUchwyt, temperaturaValue );
            parameters.add( zachmurzenieUchwyt, zachmurzenieValue );
            parameters.add( wilgotnoscUchwyt, wilgotnoscValue );
            parameters.add( cisnienieUchwyt, cisnienieValue );

            // wysłanie
            LogicalTime time = convertTime( fedamb.federateTime + fedamb.federateLookahead );
            rtiamb.sendInteraction( stacjaMeteorologinczaUchwyt, parameters, generateTag(), time );

        }
        catch ( NameNotFound | InteractionClassNotDefined | FederateNotExecutionMember | InteractionClassNotPublished
                | InteractionParameterNotDefined | InvalidFederationTime | SaveInProgress | RestoreInProgress
                | ConcurrentAccessAttempted | RTIinternalError e )
        {
            e.printStackTrace();
        }

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
            stacjaMeteorologicznaPomocnik.setZachmurzenie( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 1 ) ) ) );
            stacjaMeteorologicznaPomocnik.setTemperatura( Double.parseDouble( EncodingHelpers.decodeString( interakcja.getValue( 2 ) ) ) );
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
            wiezaKontrolnaPomocnik.setIdSamolotu( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 1 ) ) ) );
            wiezaKontrolnaPomocnik.setNumerPasu( Integer.parseInt( EncodingHelpers.decodeString( interakcja.getValue( 2 ) ) ) );

        }
        catch ( NumberFormatException | ArrayIndexOutOfBounds e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return wiezaKontrolnaPomocnik;
    }

    // additional method
    private static LogicalTime convertTime( double time )
    {
        // PORTICO SPECIFIC!!
        return new DoubleTime( time );
    }

    private static byte[] generateTag()
    {
        return ( "" + System.currentTimeMillis() ).getBytes();
    }
}
