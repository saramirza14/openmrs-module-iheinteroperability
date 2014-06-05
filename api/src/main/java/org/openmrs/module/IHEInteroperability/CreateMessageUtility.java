package org.openmrs.module.IHEInteroperability;
import java.io.IOException;

import org.openmrs.Patient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.message.ADT_A05;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class CreateMessageUtility {
	
public String createHL7Message(Patient patient) throws IOException, HL7Exception {
		
	  ADT_A05 adt = new ADT_A05();
      
      // Populate the MSH Segment
      MSH mshSegment = adt.getMSH();
      mshSegment.getFieldSeparator().setValue("|");
      mshSegment.getEncodingCharacters().setValue("^~\\&");
      mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
      mshSegment.getSequenceNumber().setValue("123");
      mshSegment.getMessageType().getTriggerEvent().setValue("A05");
      mshSegment.getMessageType().getMessageStructure().setValue("ADT A05");
      
      // Populate the PID Segment
      PID pid = adt.getPID(); 
      pid.getPatientName(0).getFamilyName().getSurname().setValue(patient.getFamilyName());
      pid.getPatientName(0).getGivenName().setValue(patient.getGivenName());
      
      if(patient.getAttribute(2) != null)      
    	  pid.getBirthPlace().setValue(patient.getAttribute(2).toString());
      
      if(patient.getAttribute(3) != null)
    	  pid.getCitizenship(0).getText().setValue(patient.getAttribute(3).toString());
      
          
      // Now, let's encode the message and look at the output
      Parser parser = new PipeParser();
      String encodedMessage = parser.encode(adt);
      System.out.println("Printing ER7 Encoded Message:");
      System.out.println(encodedMessage);
      
       return encodedMessage;
		  
		}

}
