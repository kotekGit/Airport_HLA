package pl.edu.wat.wcy.mtsk.lotnisko.federacje;

import hla.rti.FederatesCurrentlyJoined;
import hla.rti.FederationExecutionAlreadyExists;
import hla.rti.FederationExecutionDoesNotExist;
import hla.rti.RTIexception;
import hla.rti.ResignAction;
import hla.rti.jlc.RtiFactoryFactory;

import java.io.File;
import java.net.MalformedURLException;

import pl.edu.wat.wcy.mtsk.lotnisko.ambasadorzy.GuiAmbasador;

public class GuiFederat extends Federat<GuiAmbasador> {

	@Override
	public void runFederate(String federateName) throws RTIexception {

		/////////////////////////////////
		// 1. create the RTIambassador //
		/////////////////////////////////
		rtiamb = RtiFactoryFactory.getRtiFactory().createRtiAmbassador();

		//////////////////////////////
		// 2. create the federation //
		//////////////////////////////
		// create
		// NOTE: some other federate may have already created the federation,
		//       in that case, we'll just try and join it
		try
		{
			File fom = new File( "resources/test.fed" );
			rtiamb.createFederationExecution( "ExampleFederation",
			                                  fom.toURI().toURL() );
			log( "Created Federation" );
		}
		catch( FederationExecutionAlreadyExists exists )
		{
			log( "Didn't create federation, it already existed" );
		}
		catch( MalformedURLException urle )
		{
			log( "Exception processing fom: " + urle.getMessage() );
			urle.printStackTrace();
			return;
		}
		
		////////////////////////////
		// 3. join the federation //
		////////////////////////////
		// create the federate ambassador and join the federation
		fedamb = new GuiAmbasador();
		rtiamb.joinFederationExecution( federateName, "ExampleFederation", fedamb );
		log( "Joined Federation as " + federateName );

		////////////////////////////////
		// 4. announce the sync point //
		////////////////////////////////
		// announce a sync point to get everyone on the same page. if the point
		// has already been registered, we'll get a callback saying it failed,
		// but we don't care about that, as long as someone registered it
		rtiamb.registerFederationSynchronizationPoint( READY_TO_RUN, null );
		// wait until the point is announced
		while( fedamb.isAnnounced == false )
		{
			rtiamb.tick();
		}

		// WAIT FOR USER TO KICK US OFF
		// So that there is time to add other federates, we will wait until the
		// user hits enter before proceeding. That was, you have time to start
		// other federates.
		waitForUser();

		///////////////////////////////////////////////////////
		// 5. achieve the point and wait for synchronization //
		///////////////////////////////////////////////////////
		// tell the RTI we are ready to move past the sync point and then wait
		// until the federation has synchronized on
		rtiamb.synchronizationPointAchieved( READY_TO_RUN );
		log( "Achieved sync point: " +READY_TO_RUN+ ", waiting for federation..." );
		while( fedamb.isReadyToRun == false )
		{
			rtiamb.tick();
		}

		/////////////////////////////
		// 6. enable time policies //
		/////////////////////////////
		// in this section we enable/disable all time policies
		// note that this step is optional!
		enableTimePolicy();
		log( "Time Policy Enabled" );

		//////////////////////////////
		// 7. publish and subscribe //
		//////////////////////////////
		// in this section we tell the RTI of all the data we are going to
		// produce, and all the data we want to know about
		publishAndSubscribe();
		log( "Published and Subscribed" );

		/////////////////////////////////////
		// 8. register an object to update //
		/////////////////////////////////////
		int objectHandle = registerObject();
		log( "Registered Object, handle=" + objectHandle );
		
		////////////////////////////////////
		// 9. do the main simulation loop //
		////////////////////////////////////
		// here is where we do the meat of our work. in each iteration, we will
		// update the attribute values of the object we registered, and will
		// send an interaction.
		for( int i = 0; i < ITERATIONS; i++ )
		{
			// 9.1 update the attribute values of the instance //
			updateAttributeValues( objectHandle );
			
			// 9.2 send an interaction
			sendInteraction();
			
			// 9.3 request a time advance and wait until we get it
			advanceTime( 1.0 );
			log( "Time Advanced to " + fedamb.federateTime );
		}

		//////////////////////////////////////
		// 10. delete the object we created //
		//////////////////////////////////////
		deleteObject( objectHandle );
		log( "Deleted Object, handle=" + objectHandle );

		////////////////////////////////////
		// 11. resign from the federation //
		////////////////////////////////////
		rtiamb.resignFederationExecution( ResignAction.NO_ACTION );
		log( "Resigned from Federation" );

		////////////////////////////////////////
		// 12. try and destroy the federation //
		////////////////////////////////////////
		// NOTE: we won't die if we can't do this because other federates
		//       remain. in that case we'll leave it for them to clean up
		try
		{
			rtiamb.destroyFederationExecution( "ExampleFederation" );
			log( "Destroyed Federation" );
		}
		catch( FederationExecutionDoesNotExist dne )
		{
			log( "No need to destroy federation, it doesn't exist" );
		}
		catch( FederatesCurrentlyJoined fcj )
		{
			log( "Didn't destroy federation, federates still joined" );
		}
		
	}

}
