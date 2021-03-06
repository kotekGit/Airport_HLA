package pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy;

import org.portico.impl.hla13.types.DoubleTime;

import pl.edu.wat.wcy.mtsk.lotnisko.federaci.Federat;
import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.EventRetractionHandle;
import hla.rti.LogicalTime;
import hla.rti.ReceivedInteraction;
import hla.rti.ReflectedAttributes;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.NullFederateAmbassador;

/**
 * Klasa abstarkcyjna dla ambasadorów
 * @since 16.06.2014
 *
 */
public abstract class Ambasador extends NullFederateAmbassador {
	//----------------------------------------------------------
		//                    STATIC VARIABLES
		//----------------------------------------------------------

		//----------------------------------------------------------
		//                   INSTANCE VARIABLES
		//----------------------------------------------------------
		// these variables are accessible in the package
		public double federateTime        = 0.0;
		public double federateLookahead   = 1.0;
		 
		public boolean isRegulating       = false;
		public boolean isConstrained      = false;
		public boolean isAdvancing        = false;
		 
		public boolean isAnnounced        = false;
		public boolean isReadyToRun       = false;

	    Federat federat;
	    
		//----------------------------------------------------------
		//                      CONSTRUCTORS
		//----------------------------------------------------------
		public Ambasador(Federat federat)
		{
			this.federat = federat;
		}

		/**
		 * Pobieranie interakcji przez ambsadora. Każdy ambasador powinien implementować
		 * handlera dla swoich interakcji.
		 * @param klasaInterakcji
		 * @param otrzymanaInterakcja
		 * @param tag
		 * @param time
		 * @param eventRetractionHandle
		 */
		public abstract void pobierzInterakcje(int klasaInterakcji, ReceivedInteraction otrzymanaInterakcja, byte[] tag, LogicalTime time, EventRetractionHandle eventRetractionHandle);
		
		//----------------------------------------------------------
		//                    INSTANCE METHODS
		//----------------------------------------------------------
		/**
		 * As all time-related code is Portico-specific, we have isolated it into a single
		 * method. This way, if you need to move to a different RTI, you only need to
		 * change this code, rather than more code throughout the whole class.
		 */
		private double convertTime( LogicalTime logicalTime )
		{
			// PORTICO SPECIFIC!!
			return ((DoubleTime)logicalTime).getTime();
		}
		
		protected void log( String message )
		{
			System.out.println( "FederateAmbassador: " + message );
		}
		
		//////////////////////////////////////////////////////////////////////////
		////////////////////////// RTI Callback Methods //////////////////////////
		//////////////////////////////////////////////////////////////////////////
		public void synchronizationPointRegistrationFailed( String label )
		{
			log( "Failed to register sync point: " + label );
		}

		public void synchronizationPointRegistrationSucceeded( String label )
		{
			log( "Successfully registered sync point: " + label );
		}

		public void announceSynchronizationPoint( String label, byte[] tag )
		{
			log( "Synchronization point announced: " + label );
			if( label.equals(Federat.READY_TO_RUN) )
				this.isAnnounced = true;
		}

		public void federationSynchronized( String label )
		{
			log( "Federation Synchronized: " + label );
			if( label.equals(Federat.READY_TO_RUN) )
				this.isReadyToRun = true;
		}

		/**
		 * The RTI has informed us that time regulation is now enabled.
		 */
		public void timeRegulationEnabled( LogicalTime theFederateTime )
		{
			this.federateTime = convertTime( theFederateTime );
			this.isRegulating = true;
		}

		public void timeConstrainedEnabled( LogicalTime theFederateTime )
		{
			this.federateTime = convertTime( theFederateTime );
			this.isConstrained = true;
		}

		public void timeAdvanceGrant( LogicalTime theTime )
		{
			this.federateTime = convertTime( theTime );
			this.isAdvancing = false;
		}

		public void discoverObjectInstance( int theObject,
		                                    int theObjectClass,
		                                    String objectName )
		{
			log( "Discoverd Object: handle=" + theObject + ", classHandle=" +
			     theObjectClass + ", name=" + objectName );
		}

		public void reflectAttributeValues( int theObject,
		                                    ReflectedAttributes theAttributes,
		                                    byte[] tag )
		{
			// just pass it on to the other method for printing purposes
			// passing null as the time will let the other method know it
			// it from us, not from the RTI
			reflectAttributeValues( theObject, theAttributes, tag, null, null );
		}

		public void reflectAttributeValues( int theObject,
		                                    ReflectedAttributes theAttributes,
		                                    byte[] tag,
		                                    LogicalTime theTime,
		                                    EventRetractionHandle retractionHandle )
		{
			StringBuilder builder = new StringBuilder( "Reflection for object:" );
			
			// print the handle
			builder.append( " handle=" + theObject );
			// print the tag
			builder.append( ", tag=" + EncodingHelpers.decodeString(tag) );
			// print the time (if we have it) we'll get null if we are just receiving
			// a forwarded call from the other reflect callback above
			if( theTime != null )
			{
				builder.append( ", time=" + convertTime(theTime) );
			}
			
			// print the attribute information
			builder.append( ", attributeCount=" + theAttributes.size() );
			builder.append( "\n" );
			for( int i = 0; i < theAttributes.size(); i++ )
			{
				try
				{
					// print the attibute handle
					builder.append( "\tattributeHandle=" );
					builder.append( theAttributes.getAttributeHandle(i) );
					// print the attribute value
					builder.append( ", attributeValue=" );
					builder.append(
					    EncodingHelpers.decodeString(theAttributes.getValue(i)) );
					builder.append( "\n" );
				}
				catch( ArrayIndexOutOfBounds aioob )
				{
					// won't happen
				}
			}
			
			log( builder.toString() );
		}

		public void receiveInteraction( int interactionClass,
		                                ReceivedInteraction theInteraction,
		                                byte[] tag )
		{
			// just pass it on to the other method for printing purposes
			// passing null as the time will let the other method know it
			// it from us, not from the RTI
			receiveInteraction( interactionClass, theInteraction, tag, null, null );
		}

		public void receiveInteraction( int interactionClass,
		                                ReceivedInteraction theInteraction,
		                                byte[] tag,
		                                LogicalTime theTime,
		                                EventRetractionHandle eventRetractionHandle )
		{
			StringBuilder builder = new StringBuilder( "Interaction Received:" );
			
			// print the handle
			builder.append( " handle=" + interactionClass );
			// print the tag
			builder.append( ", tag=" + EncodingHelpers.decodeString(tag) );
			// print the time (if we have it) we'll get null if we are just receiving
			// a forwarded call from the other reflect callback above
			if( theTime != null )
			{
				builder.append( ", time=" + convertTime(theTime) );
			}
			
			// print the parameer information
			builder.append( ", parameterCount=" + theInteraction.size() );
			builder.append( "\n" );
			for( int i = 0; i < theInteraction.size(); i++ )
			{
				try
				{
					// print the parameter handle
					builder.append( "\tparamHandle=" );
					builder.append( theInteraction.getParameterHandle(i) );
					// print the parameter value
					builder.append( ", paramValue=" );
					builder.append(
					    EncodingHelpers.decodeString(theInteraction.getValue(i)) );
					builder.append( "\n" );
				}
				catch( ArrayIndexOutOfBounds aioob )
				{
					log(aioob.getMessage());
					// won't happen
				}
			}
			this.pobierzInterakcje(interactionClass, theInteraction, tag, theTime, eventRetractionHandle);
			log( builder.toString() );
		}

		public void removeObjectInstance( int theObject, byte[] userSuppliedTag )
		{
			log( "Object Removed: handle=" + theObject );
		}

		public void removeObjectInstance( int theObject,
		                                  byte[] userSuppliedTag,
		                                  LogicalTime theTime,
		                                  EventRetractionHandle retractionHandle )
		{
			log( "Object Removed: handle=" + theObject );
		}


		//----------------------------------------------------------
		//                     STATIC METHODS
		//----------------------------------------------------------

		
		
}
