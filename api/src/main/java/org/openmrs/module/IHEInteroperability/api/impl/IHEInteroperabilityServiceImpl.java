/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.IHEInteroperability.api.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.IHEInteroperability.CreateMessageUtility;
import org.openmrs.module.IHEInteroperability.api.IHEInteroperabilityService;
import org.openmrs.module.IHEInteroperability.api.db.IHEInteroperabilityDAO;
import org.springframework.aop.AfterReturningAdvice;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.springframework.aop.AfterReturningAdvice;
 
/**
 * It is a default implementation of {@link IHEInteroperabilityService}.
 */
public class IHEInteroperabilityServiceImpl extends BaseOpenmrsService implements IHEInteroperabilityService, AfterReturningAdvice {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private IHEInteroperabilityDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(IHEInteroperabilityDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public IHEInteroperabilityDAO getDao() {
	    return dao;
    }

	@Override
	public void afterReturning(Object arg0, Method arg1, Object[] arg2,
			Object arg3) throws Throwable {
		// TODO Auto-generated method stub
		if (arg1.getName().equals("savePatient")){
			Patient patientObj = new Patient();
			patientObj = (Patient)arg0;
			System.out.println("Called From Service" + patientObj.getFamilyName() + patientObj.getGender());
			
			//Create HL7 message
			CreateMessageUtility obj = new CreateMessageUtility();
			String HL7Message = obj.createHL7Message(patientObj);
			System.out.println("HL7 message is  " + HL7Message);
		}

	}
}