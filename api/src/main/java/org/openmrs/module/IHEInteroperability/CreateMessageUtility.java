package org.openmrs.module.IHEInteroperability;
import java.io.IOException;

import org.openmrs.Patient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v24.message.ADT_A01;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.parser.DefaultXMLParser;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class CreateMessageUtility {
	
public String createHL7Message(Patient patient) throws IOException, HL7Exception {
		
	  ADT_A01 adt = new ADT_A01();
      
      // Populate the MSH Segment
      MSH mshSegment = adt.getMSH();
      mshSegment.getFieldSeparator().setValue("|");
      mshSegment.getEncodingCharacters().setValue("^~\\&");
      mshSegment.getDateTimeOfMessage().getTimeOfAnEvent().setValue("200701011539");
      mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
      mshSegment.getSequenceNumber().setValue("123");
      mshSegment.getMessageType().getMessageType().setValue("ADT");
      mshSegment.getMessageType().getTriggerEvent().setValue("A01");
      mshSegment.getMessageType().getMessageStructure().setValue("ADT A01");
      
      // Populate the PID Segment
      PID pid = adt.getPID(); 
      pid.getPatientName(0).getFamilyName().getSurname().setValue("Doe");
      pid.getPatientName(0).getGivenName().setValue("John");
      pid.getPatientIdentifierList(0).getID().setValue("123456");

      /*
       * In a real situation, of course, many more segments and fields would be populated
       */
              
      // Now, let's encode the message and look at the output
      Parser parser = new PipeParser();
      String encodedMessage = parser.encode(adt);
      System.out.println("Printing ER7 Encoded Message:");
      System.out.println(encodedMessage);
      
       return encodedMessage;
		  
		}

}
