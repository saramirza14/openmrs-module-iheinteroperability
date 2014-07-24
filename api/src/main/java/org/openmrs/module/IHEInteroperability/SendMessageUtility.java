package org.openmrs.module.IHEInteroperability;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.openmrs.Patient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.ConnectionHub;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.app.SimpleServer;
import ca.uhn.hl7v2.hoh.api.DecodeException;
import ca.uhn.hl7v2.hoh.api.EncodeException;
import ca.uhn.hl7v2.hoh.api.IAuthorizationClientCallback;
import ca.uhn.hl7v2.hoh.api.IReceivable;
import ca.uhn.hl7v2.hoh.api.ISendable;
import ca.uhn.hl7v2.hoh.api.MessageMetadataKeys;
import ca.uhn.hl7v2.hoh.auth.SingleCredentialClientCallback;
import ca.uhn.hl7v2.hoh.hapi.api.MessageSendable;
import ca.uhn.hl7v2.hoh.hapi.client.HohClientSimple;
import ca.uhn.hl7v2.hoh.raw.api.RawSendable;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.llp.LowerLayerProtocol;
import ca.uhn.hl7v2.llp.MinLowerLayerProtocol;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
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
	
	public void hl7OverHttp(Patient patient) throws IOException, HL7Exception{
	
		String host = "iol.sandbox.ohie.org";
		int port = 5001;
		String uri = "/ws/rest/v1/patients/";
		
		// Create a parser
		Parser parser = new DefaultXMLParser();
		
		// Create a client
		HohClientSimple client = new HohClientSimple(host,port,uri,parser);

		// Optionally, if credentials should be sent, they 
		// can be provided using a credential callback
		IAuthorizationClientCallback authCalback = new SingleCredentialClientCallback("admin", "admin");
		client.setAuthorizationCallback(authCalback);
		
		ADT_A01 adt = new ADT_A01();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
		// Populate the MSH Segment
		MSH mshSegment = adt.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
		mshSegment.getReceivingApplication().getNamespaceID().setValue("PatientManager");
		mshSegment.getProcessingID().getProcessingID().setValue("P");
		mshSegment.getSequenceNumber().setValue("456");
		mshSegment.getMessageType().getTriggerEvent().setValue("A01");
		mshSegment.getMessageType().getMessageStructure().setValue("ADT_A01");
		mshSegment.getMessageType().getMessageCode().setValue("ADT");
		mshSegment.getVersionID().getVersionID().setValue("2.5");
		mshSegment.getMessageControlID().setValue(sdf.format(Calendar.getInstance().getTime()));
		mshSegment.getSendingFacility().getNamespaceID().setValue("OpenMRS");
		mshSegment.getReceivingFacility().getNamespaceID().setValue("IHE");
		mshSegment.getDateTimeOfMessage().getTime().setValue(sdf.format(Calendar.getInstance().getTime()));


		EVN evnSegment = adt.getEVN();
		evnSegment.getRecordedDateTime().getTime().setValue(sdf.format(Calendar.getInstance().getTime()));
		evnSegment.getEventFacility().getNamespaceID().setValue("OpenMRS");

		PV1 pv1 = adt.getPV1();
		pv1.getPatientClass().setValue("N");

		// Populate the PID Segment
		PID pid = adt.getPID(); 
		pid.getPatientName(0).getFamilyName().getSurname().setValue(patient.getFamilyName());
		pid.getPatientName(0).getGivenName().setValue(patient.getGivenName());
		pid.getPatientName(0).getNameTypeCode().setValue("L");

		CX cx;
		cx = pid.getPatientIdentifierList(0);
		cx.getAssigningAuthority().getNamespaceID().setValue("OPENMRS");
		cx.getIdentifierTypeCode().setValue("QWER");
		cx.getIDNumber().setValue(patient.getId().toString());
		
		
		ISendable sendable = new MessageSendable(adt);
		
		try {
	        // sendAndReceive actually sends the message
	        IReceivable<Message> receivable = client.sendAndReceiveMessage(sendable);
	        
	        // receivavle.getRawMessage() provides the response
	        Message message = receivable.getMessage();
	       // System.out.println("Response was:\n" + (String)message.encode());
	        
	        // IReceivable also stores metadata about the message
	        String remoteHostIp = (String) receivable.getMetadata().get(MessageMetadataKeys.REMOTE_HOST_ADDRESS);
	        System.out.println("From:\n" + remoteHostIp);
	        
	        /*
	         * Note that the client may be reused as many times as you like,
	         * by calling sendAndReceiveMessage repeatedly
	         */
	        
	} catch (DecodeException e) {
	        // Thrown if the response can't be read
	        e.printStackTrace();
	} catch (IOException e) {
	        // Thrown if communication fails
	        e.printStackTrace();
	} catch (EncodeException e) {
	        // Thrown if the message can't be encoded (generally a programming bug)
	        e.printStackTrace();
	}


	}
	
	
}
