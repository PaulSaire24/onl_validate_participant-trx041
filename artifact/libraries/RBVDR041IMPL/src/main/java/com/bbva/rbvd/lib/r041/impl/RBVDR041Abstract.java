package com.bbva.rbvd.lib.r041.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.lib.r041.RBVDR041;
import com.bbva.rbvd.lib.r066.RBVDR066;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class RBVDR041Abstract extends AbstractLibrary implements RBVDR041 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected RBVDR066 rbvdR066;

	protected PBTQR002 pbtqR002;

	protected KSMKR002 ksmkR002;

	protected PISDR352 pisdR352;

	protected PISDR601 pisdR601;

	protected PISDR012 pisdR012;

	protected IValidateParticipant iValidateParticipant;
	protected ICustomerInformationDAO iCustomerInformationDAO;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param rbvdR066 the this.rbvdR066 to set
	*/
	public void setRbvdR066(RBVDR066 rbvdR066) {
		this.rbvdR066 = rbvdR066;
	}

	/**
	* @param pbtqR002 the this.pbtqR002 to set
	*/
	public void setPbtqR002(PBTQR002 pbtqR002) {
		this.pbtqR002 = pbtqR002;
	}

	/**
	* @param ksmkR002 the this.ksmkR002 to set
	*/
	public void setKsmkR002(KSMKR002 ksmkR002) {
		this.ksmkR002 = ksmkR002;
	}

	/**
	* @param pisdR352 the this.pisdR352 to set
	*/
	public void setPisdR352(PISDR352 pisdR352) {
		this.pisdR352 = pisdR352;
	}

	/**
	* @param pisdR601 the this.pisdR601 to set
	*/
	public void setPisdR601(PISDR601 pisdR601) {
		this.pisdR601 = pisdR601;
	}

	/**
	* @param pisdR012 the this.pisdR012 to set
	*/
	public void setPisdR012(PISDR012 pisdR012) {
		this.pisdR012 = pisdR012;
	}

	public IValidateParticipant getiValidateParticipant() {
		return iValidateParticipant;
	}

	public void setiValidateParticipant(IValidateParticipant iValidateParticipant) {
		this.iValidateParticipant = iValidateParticipant;
	}

	public ICustomerInformationDAO getiCustomerInformationDAO() {
		return iCustomerInformationDAO;
	}

	public void setiCustomerInformationDAO(ICustomerInformationDAO iCustomerInformationDAO) {
		this.iCustomerInformationDAO = iCustomerInformationDAO;
	}
}