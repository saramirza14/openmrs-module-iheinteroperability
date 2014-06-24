package org.openmrs.module.IHEInteroperability;
import java.io.IOException;
import java.util.Set;

import org.openmrs.Patient;
import org.openmrs.PersonAddress;

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
      pid.getDateTimeOfBirth().getTime().setValue(patient.getBirthdate().toString());
      
      if(patient.getAttribute(1) != null)
    	  pid.getRace(0).getText().setValue(patient.getAttribute(1).toString());
      
      if(patient.getAttribute(2) != null)      
    	  pid.getBirthPlace().setValue(patient.getAttribute(2).toString());
      
      if(patient.getAttribute(3) != null)
    	  pid.getCitizenship(0).getText().setValue(patient.getAttribute(3).toString());
      
      if(patient.getAttribute(4) != null)
    	  pid.getMotherSMaidenName(0).getGivenName().setValue(patient.getAttribute(3).toString());
      
      if(patient.getAttribute(5) != null)
    	  	pid.getMaritalStatus().getText().setValue(patient.getAttribute(5).toString());
      
      Set<PersonAddress> addressSet = patient.getAddresses();
      PersonAddress[] addressArray = addressSet.toArray(new PersonAddress[0]);
      
      if(addressArray[0].getAddress1() != null)
    	  pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(addressArray[0].getAddress1());
      
      if(addressArray[0].getCityVillage() != null)
    	  pid.getPatientAddress(0).getCity().setValue(addressArray[0].getCityVillage());
      
      if(addressArray[0].getCountry() != null)
    	  pid.getPatientAddress(0).getCountry().setValue(addressArray[0].getCountry());
      
      if(addressArray[0].getStateProvince() !=null)
    	  pid.getPatientAddress(0).getStateOrProvince().setValue(addressArray[0].getStateProvince());
      
      if(addressArray[0].getPostalCode() != null)
    	  pid.getPatientAddress(0).getZipOrPostalCode().setValue(addressArray[0].getPostalCode());
      
      // Now, let's encode the message and look at the output
      Parser parser = new PipeParser();
      String encodedMessage = parser.encode(adt);
      System.out.println(encodedMessage);
      
       return encodedMessage;
		  
	}

}
