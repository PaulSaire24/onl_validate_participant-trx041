package com.bbva.rbvd.lib.r041.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.lib.r041.RBVDR041;

public abstract class RBVDR041Abstract extends AbstractLibrary implements RBVDR041 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected PBTQR002 pbtqR002;

	protected PISDR352 pisdR352;

	protected PISDR601 pisdR601;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param pbtqR002 the this.pbtqR002 to set
	*/
	public void setPbtqR002(PBTQR002 pbtqR002) {
		this.pbtqR002 = pbtqR002;
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

}