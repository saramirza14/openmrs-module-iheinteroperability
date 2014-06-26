package org.openmrs.module.IHEInteroperability;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.app.Application;
import ca.uhn.hl7v2.app.ApplicationException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import java.io.IOException;

import ca.uhn.hl7v2.app.DefaultApplication;
import ca.uhn.hl7v2.model.v25.message.ACK;
import ca.uhn.hl7v2.model.v25.segment.MSH;

public class ExampleReceiverApplication implements Application {

	 

		@Override
		public boolean canProcess(Message arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Message processMessage(Message arg0)
				throws ApplicationException, HL7Exception {
			// TODO Auto-generated method stub
			 String encodedMessage = new PipeParser().encode(arg0);
		        System.out.println("Received message:\n" + encodedMessage + "\n\n");

		        // Now we need to generate a message to return. This will generally be an ACK message.
		        MSH msh = (MSH)arg0.get("MSH");
		        ACK retVal;
		        try {
		            // This method takes in the MSH segment of an incoming message, and generates an
		            // appropriate ACK
		            retVal = (ACK)DefaultApplication.makeACK(msh);
		        } catch (IOException e) {
		            throw new HL7Exception(e);
		        }
				return retVal;

		}
}
