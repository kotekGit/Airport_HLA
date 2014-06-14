package pl.edu.wat.wcy.mtsk.lotnisko.federacje;

import hla.rti.AttributeHandleSet;
import hla.rti.FederatesCurrentlyJoined;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.LogicalTime;
import hla.rti.LogicalTimeInterval;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;
import hla.rti.ResignAction;
import hla.rti.SuppliedAttributes;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;
import hla.rti.jlc.RtiFactoryFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.portico.impl.hla13.types.DoubleTime;
import org.portico.impl.hla13.types.DoubleTimeInterval;

import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.Ambasador;

public abstract class Federat<T extends Ambasador> {
	//----------------------------------------------------------
		//                    STATIC VARIABLES
		//----------------------------------------------------------
		/** The number of times we will update our attributes and send an interaction */
		public static final int ITERATIONS = 20;

		/** The sync point all federates will sync up on before starting */
		public static final String READY_TO_RUN = "ReadyToRun";

		//----------------------------------------------------------
		//                   INSTANCE VARIABLES
		//----------------------------------------------------------
	    RTIambassador rtiamb;
		T fedamb;

		//----------------------------------------------------------
		//                      CONSTRUCTORS
		//----------------------------------------------------------

		//----------------------------------------------------------
		//                    INSTANCE METHODS
		//----------------------------------------------------------
		/**
		 * This is just a helper method to make sure all logging it output in the same form
		 */
		protected void log( String message )
		{
			System.out.println( "ExampleFederate   : " + message );
		}

		/**
		 * This method will block until the user presses enter
		 */
		protected void waitForUser()
		{
			log( " >>>>>>>>>> Press Enter to Continue <<<<<<<<<<" );
			BufferedReader reader = new BufferedReader( new InputStreamReader(System.in) );
			try
			{
				reader.readLine();
			}
			catch( Exception e )
			{
				log( "Error while waiting for user input: " + e.getMessage() );
				e.printStackTrace();
			}
		}
		
		/**
		 * As all time-related code is Portico-specific, we have isolated it into a 
		 * single method. This way, if you need to move to a different RTI, you only need
		 * to change this code, rather than more code throughout the whole class.
		 */
		private LogicalTime convertTime( double time )
		{
			// PORTICO SPECIFIC!!
			return new DoubleTime( time );
		}
		
		/**
		 * Same as for {@link #convertTime(double)}
		 */
		private LogicalTimeInterval convertInterval( double time )
		{
			// PORTICO SPECIFIC!!
			return new DoubleTimeInterval( time );
		}

		///////////////////////////////////////////////////////////////////////////
		////////////////////////// Main Simulation Method /////////////////////////
		///////////////////////////////////////////////////////////////////////////

		/**
		 * This is the main simulation loop. It can be thought of as the main method of
		 * the federate. For a description of the basic flow of this federate, see the
		 * class level comments
		 */
		public abstract void runFederate( String federateName ) throws RTIexception;
		
		////////////////////////////////////////////////////////////////////////////
		////////////////////////////// Helper Methods //////////////////////////////
		////////////////////////////////////////////////////////////////////////////
		/**
		 * This method will attempt to enable the various time related properties for
		 * the federate
		 */
		protected void enableTimePolicy() throws RTIexception
		{
			// NOTE: Unfortunately, the LogicalTime/LogicalTimeInterval create code is
			//       Portico specific. You will have to alter this if you move to a
			//       different RTI implementation. As such, we've isolated it into a
			//       method so that any change only needs to happen in a couple of spots 
			LogicalTime currentTime = convertTime( fedamb.federateTime );
			LogicalTimeInterval lookahead = convertInterval( fedamb.federateLookahead );
			
			////////////////////////////
			// enable time regulation //
			////////////////////////////
			this.rtiamb.enableTimeRegulation( currentTime, lookahead );

			// tick until we get the callback
			while( fedamb.isRegulating == false )
			{
				rtiamb.tick();
			}
			
			/////////////////////////////
			// enable time constrained //
			/////////////////////////////
			this.rtiamb.enableTimeConstrained();
			
			// tick until we get the callback
			while( fedamb.isConstrained == false )
			{
				rtiamb.tick();
			}
		}
		
		/**
		 * This method will inform the RTI about the types of data that the federate will
		 * be creating, and the types of data we are interested in hearing about as other
		 * federates produce it.
		 */
		protected void publishAndSubscribe() throws RTIexception
		{
			////////////////////////////////////////////
			// publish all attributes of ObjectRoot.A //
			////////////////////////////////////////////
			// before we can register instance of the object class ObjectRoot.A and
			// update the values of the various attributes, we need to tell the RTI
			// that we intend to publish this information

			// get all the handle information for the attributes of ObjectRoot.A
			int classHandle = rtiamb.getObjectClassHandle( "ObjectRoot.A" );
			int aaHandle    = rtiamb.getAttributeHandle( "aa", classHandle );
			int abHandle    = rtiamb.getAttributeHandle( "ab", classHandle );
			int acHandle    = rtiamb.getAttributeHandle( "ac", classHandle );

			// package the information into a handle set
			AttributeHandleSet attributes =
				RtiFactoryFactory.getRtiFactory().createAttributeHandleSet();
			attributes.add( aaHandle );
			attributes.add( abHandle );
			attributes.add( acHandle );
			
			// do the actual publication
			rtiamb.publishObjectClass( classHandle, attributes );

			/////////////////////////////////////////////////
			// subscribe to all attributes of ObjectRoot.A //
			/////////////////////////////////////////////////
			// we also want to hear about the same sort of information as it is
			// created and altered in other federates, so we need to subscribe to it
			
			rtiamb.subscribeObjectClassAttributes( classHandle, attributes );

			/////////////////////////////////////////////////////
			// publish the interaction class InteractionRoot.X //
			/////////////////////////////////////////////////////
			// we want to send interactions of type InteractionRoot.X, so we need
			// to tell the RTI that we're publishing it first. We don't need to
			// inform it of the parameters, only the class, making it much simpler
			int interactionHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.X" );
			
			// do the publication
			rtiamb.publishInteractionClass( interactionHandle );

			////////////////////////////////////////////////////
			// subscribe to the InteractionRoot.X interaction //
			////////////////////////////////////////////////////
			// we also want to receive other interaction of the same type that are
			// sent out by other federates, so we have to subscribe to it first
			rtiamb.subscribeInteractionClass( interactionHandle );
		}
		
		/**
		 * This method will register an instance of the class ObjectRoot.A and will
		 * return the federation-wide unique handle for that instance. Later in the
		 * simulation, we will update the attribute values for this instance
		 */
		protected int registerObject() throws RTIexception
		{
			int classHandle = rtiamb.getObjectClassHandle( "ObjectRoot.A" );
			return rtiamb.registerObjectInstance( classHandle );
		}
		
		/**
		 * This method will update all the values of the given object instance. It will
		 * set each of the values to be a string which is equal to the name of the
		 * attribute plus the current time. eg "aa:10.0" if the time is 10.0.
		 * <p/>
		 * Note that we don't actually have to update all the attributes at once, we
		 * could update them individually, in groups or not at all!
		 */
		protected void updateAttributeValues( int objectHandle ) throws RTIexception
		{
			///////////////////////////////////////////////
			// create the necessary container and values //
			///////////////////////////////////////////////
			// create the collection to store the values in, as you can see
			// this is quite a lot of work
			SuppliedAttributes attributes =
				RtiFactoryFactory.getRtiFactory().createSuppliedAttributes();
			
			// generate the new values
			// we use EncodingHelpers to make things nice friendly for both Java and C++
			byte[] aaValue = EncodingHelpers.encodeString( "aa:" + getLbts() );
			byte[] abValue = EncodingHelpers.encodeString( "ab:" + getLbts() );
			byte[] acValue = EncodingHelpers.encodeString( "ac:" + getLbts() );
			
			// get the handles
			// this line gets the object class of the instance identified by the
			// object instance the handle points to
			int classHandle = rtiamb.getObjectClass( objectHandle );
			int aaHandle = rtiamb.getAttributeHandle( "aa", classHandle );
			int abHandle = rtiamb.getAttributeHandle( "ab", classHandle );
			int acHandle = rtiamb.getAttributeHandle( "ac", classHandle );

			// put the values into the collection
			attributes.add( aaHandle, aaValue );
			attributes.add( abHandle, abValue );
			attributes.add( acHandle, acValue );

			//////////////////////////
			// do the actual update //
			//////////////////////////
			rtiamb.updateAttributeValues( objectHandle,attributes, generateTag() );
			
			// note that if you want to associate a particular timestamp with the
			// update. here we send another update, this time with a timestamp:
			LogicalTime time = convertTime( fedamb.federateTime + fedamb.federateLookahead );
			rtiamb.updateAttributeValues( objectHandle, attributes, generateTag(), time );
		}
		
		/**
		 * This method will send out an interaction of the type InteractionRoot.X. Any
		 * federates which are subscribed to it will receive a notification the next time
		 * they tick(). Here we are passing only two of the three parameters we could be
		 * passing, but we don't actually have to pass any at all!
		 */
		protected void sendInteraction() throws RTIexception
		{
			///////////////////////////////////////////////
			// create the necessary container and values //
			///////////////////////////////////////////////
			// create the collection to store the values in
			SuppliedParameters parameters =
				RtiFactoryFactory.getRtiFactory().createSuppliedParameters();
			
			// generate the new values
			// we use EncodingHelpers to make things nice friendly for both Java and C++
			byte[] xaValue = EncodingHelpers.encodeString( "xa:" + getLbts() );
			byte[] xbValue = EncodingHelpers.encodeString( "xb:" + getLbts() );
			
			// get the handles
			int classHandle = rtiamb.getInteractionClassHandle( "InteractionRoot.X" );
			int xaHandle = rtiamb.getParameterHandle( "xa", classHandle );
			int xbHandle = rtiamb.getParameterHandle( "xb", classHandle );

			// put the values into the collection
			parameters.add( xaHandle, xaValue );
			parameters.add( xbHandle, xbValue );

			//////////////////////////
			// send the interaction //
			//////////////////////////
			rtiamb.sendInteraction( classHandle, parameters, generateTag() );
			
			// if you want to associate a particular timestamp with the
			// interaction, you will have to supply it to the RTI. Here
			// we send another interaction, this time with a timestamp:
			LogicalTime time = convertTime( fedamb.federateTime +
			                                fedamb.federateLookahead );
			rtiamb.sendInteraction( classHandle, parameters, generateTag(), time );
		}

		/**
		 * This method will request a time advance to the current time, plus the given
		 * timestep. It will then wait until a notification of the time advance grant
		 * has been received.
		 */
		protected void advanceTime( double timestep ) throws RTIexception
		{
			// request the advance
			fedamb.isAdvancing = true;
			LogicalTime newTime = convertTime( fedamb.federateTime + timestep );
			rtiamb.timeAdvanceRequest( newTime );
			
			// wait for the time advance to be granted. ticking will tell the
			// LRC to start delivering callbacks to the federate
			while( fedamb.isAdvancing )
			{
				rtiamb.tick();
			}
		}

		/**
		 * This method will attempt to delete the object instance of the given
		 * handle. We can only delete objects we created, or for which we own the
		 * privilegeToDelete attribute.
		 */
		void deleteObject( int handle ) throws RTIexception
		{
			rtiamb.deleteObjectInstance( handle, generateTag() );
		}

		protected double getLbts()
		{
			return fedamb.federateTime + fedamb.federateLookahead;
		}

		protected byte[] generateTag()
		{
			return (""+System.currentTimeMillis()).getBytes();
		}
}
