package org.openmrs.module.IHEInteroperability;

import java.io.IOException;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class SendMessageUtility {
	
	public String sendMessageToSimulator(String hl7Message, int port, String host) throws EncodingNotSupportedException, HL7Exception, LLPException,
    IOException, RuntimeException, DataTypeException{
		  /*
         * Create a server to listen for incoming messages
         */
		    LowerLayerProtocol llp = LowerLayerProtocol.makeLLP(); // The transport protocol
	        PipeParser parser = new PipeParser(); // The message parser
	        SimpleServer server = new SimpleServer(port, llp, parser);

	        /*
	         * The server may have any number of "application" objects registered to handle messages. We
	         * are going to create an application to listen to ADT^A01 messages.
	         */
	           server.start();

	        /*
	         * Now, create a connection to that server, and send a message
	         */

	        // Create a message to send
	        Parser p = new GenericParser();
	        Message adt = p.parse(hl7Message);

	        // The connection hub connects to listening servers
	        ConnectionHub connectionHub = ConnectionHub.getInstance();

	        // A connection object represents a socket attached to an HL7 server
	        Connection connection = connectionHub
	                .attach(host, port, new PipeParser(), MinLowerLayerProtocol.class);

	        // The initiator is used to transmit unsolicited messages
	        Initiator initiator = connection.getInitiator();
	        Message response = initiator.sendAndReceive(adt);

	        String responseString = parser.encode(response);
	        System.out.println("Received response:\n" + responseString);

	        /*
	         * MSH|^~\&|||||20070218200627.515-0500||ACK|54|P|2.2 MSA|AA|12345
	         */

	        // Close the connection and server
	        connection.close();
	        server.stop();


		return responseString;
		
	}

}
