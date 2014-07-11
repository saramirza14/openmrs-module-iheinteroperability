package org.openmrs.module.IHEInteroperability;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import org.openmrs.Patient;
import org.openmrs.PersonAddress;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.message.ADT_A01;
import ca.uhn.hl7v2.model.v25.message.ADT_A05;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;

public class CreateMessageUtility {

	public String createHL7Message(Patient patient) throws IOException, HL7Exception {

		ADT_A05 adt = new ADT_A05();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ENGLISH);
		// Populate the MSH Segment
		MSH mshSegment = adt.getMSH();
		mshSegment.getFieldSeparator().setValue("|");
		mshSegment.getEncodingCharacters().setValue("^~\\&");
		mshSegment.getSendingApplication().getNamespaceID().setValue("TestSendingSystem");
		mshSegment.getReceivingApplication().getNamespaceID().setValue("PAMSimulator");
		mshSegment.getProcessingID().getProcessingID().setValue("P");
		mshSegment.getSequenceNumber().setValue("123");
		mshSegment.getMessageType().getTriggerEvent().setValue("A28");
		mshSegment.getMessageType().getMessageStructure().setValue("ADT_A05");
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
		//pid.getPatientID().getIDNumber().setValue(patient.getPatientId().toString());
		// pid.getDateTimeOfBirth().getTime().setValue(patient.getBirthdate().toString());

		CX cx;
		cx = pid.getPatientIdentifierList(0);
		cx.getAssigningAuthority().getNamespaceID().setValue("OPENMRS");
		cx.getIdentifierTypeCode().setValue("QWER");
		cx.getIDNumber().setValue(patient.getId().toString());


		if(patient.getAttribute(1) != null)
			pid.getRace(0).getText().setValue(patient.getAttribute(1).toString());

		if(patient.getAttribute(2) != null)      
			pid.getBirthPlace().setValue(patient.getAttribute(2).toString());

		if(patient.getAttribute(3) != null)
			pid.getCitizenship(0).getText().setValue(patient.getAttribute(3).toString());

		if(patient.getAttribute(4) != null){
			pid.getMotherSMaidenName(0).getGivenName().setValue(patient.getAttribute(3).toString());
			pid.getMotherSMaidenName(0).getNameTypeCode().setValue("L");
		}

		if(patient.getAttribute(5) != null)
			pid.getMaritalStatus().getText().setValue(patient.getAttribute(5).toString());

		Set<PersonAddress> addressSet = patient.getAddresses();
		if(!addressSet.isEmpty()){
			PersonAddress[] addressArray = addressSet.toArray(new PersonAddress[0]);
			pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(addressArray[0].getAddress1());
			pid.getPatientAddress(0).getCity().setValue(addressArray[0].getCityVillage());
			pid.getPatientAddress(0).getCountry().setValue(addressArray[0].getCountry());
			pid.getPatientAddress(0).getStateOrProvince().setValue(addressArray[0].getStateProvince());
			pid.getPatientAddress(0).getZipOrPostalCode().setValue(addressArray[0].getPostalCode());
		}
		// Now, let's encode the message and look at the output
		Parser parser = new PipeParser();
		String encodedMessage = parser.encode(adt);
		System.out.println(encodedMessage);

		return encodedMessage;

	}
	
	public String createPIXHL7Message(Patient patient) throws IOException, HL7Exception {

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


		if(patient.getAttribute(1) != null)
			pid.getRace(0).getText().setValue(patient.getAttribute(1).toString());

		if(patient.getAttribute(2) != null)      
			pid.getBirthPlace().setValue(patient.getAttribute(2).toString());

		if(patient.getAttribute(3) != null)
			pid.getCitizenship(0).getText().setValue(patient.getAttribute(3).toString());

		if(patient.getAttribute(4) != null){
			pid.getMotherSMaidenName(0).getGivenName().setValue(patient.getAttribute(3).toString());
			pid.getMotherSMaidenName(0).getNameTypeCode().setValue("L");
		}

		if(patient.getAttribute(5) != null)
			pid.getMaritalStatus().getText().setValue(patient.getAttribute(5).toString());

		Set<PersonAddress> addressSet = patient.getAddresses();
		if(!addressSet.isEmpty()){
			PersonAddress[] addressArray = addressSet.toArray(new PersonAddress[0]);
			pid.getPatientAddress(0).getStreetAddress().getStreetName().setValue(addressArray[0].getAddress1());
			pid.getPatientAddress(0).getCity().setValue(addressArray[0].getCityVillage());
			pid.getPatientAddress(0).getCountry().setValue(addressArray[0].getCountry());
			pid.getPatientAddress(0).getStateOrProvince().setValue(addressArray[0].getStateProvince());
			pid.getPatientAddress(0).getZipOrPostalCode().setValue(addressArray[0].getPostalCode());
		}
		// Now, let's encode the message and look at the output
		Parser parser = new PipeParser();
		String encodedMessage = parser.encode(adt);
		System.out.println(encodedMessage);

		return encodedMessage;

	}

}
