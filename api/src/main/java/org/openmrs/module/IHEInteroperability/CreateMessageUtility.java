package org.openmrs.module.IHEInteroperability;
import java.io.IOException;

import org.openmrs.Patient;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v24.message.ADT_A05;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.parser.Parser;

public class CreateMessageUtility {
	
public String createHL7Message(Patient patient) throws IOException, HL7Exception {
		
		ADT_A05 adt = new ADT_A05();
		          adt.initQuickstart("ADT", "A05", "P");
		          
		          // Populate the MSH Segment
		          MSH mshSegment = adt.getMSH();
		          mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
		          mshSegment.getSequenceNumber().setValue("123");
		          
		          // Populate the PID Segment
		          PID pid = adt.getPID(); 
		          pid.getPatientName(0).getFamilyName().getSurname().setValue(patient.getFamilyName());
		          pid.getPatientName(0).getGivenName().setValue(patient.getGivenName());
		          pid.getPatientIdentifierList(0).getID().setValue(patient.getPatientIdentifier().toString());
		  
		         /*
		           * In a real situation, of course, many more segments and fields would be populated
		           */
		          
		          // Now, let's encode the message and look at the output
		          HapiContext context = new DefaultHapiContext();
		         Parser parser = context.getPipeParser();
		          String encodedMessage = parser.encode(adt);
		          System.out.println("Printing ER7 Encoded Message:");
		          System.out.println(encodedMessage);
		          
		          /*
		           * Prints:
		           * 
		           * MSH|^~\&|TestSendingSystem||||200701011539||ADT^A01^ADT A01||||123
		           * PID|||123456||Doe^John
		           */
		          return encodedMessage;
		  
		}

}
