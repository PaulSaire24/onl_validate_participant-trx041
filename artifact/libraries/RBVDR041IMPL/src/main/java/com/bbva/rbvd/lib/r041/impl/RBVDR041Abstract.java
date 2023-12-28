package com.bbva.rbvd.lib.r041.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.lib.r041.RBVDR041;
import com.bbva.rbvd.lib.r041.impl.util.MapperHelper;
import com.bbva.rbvd.lib.r066.RBVDR066;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class RBVDR041Abstract extends AbstractLibrary implements RBVDR041 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected PISDR352 pisdR352;

	protected RBVDR066 rbvdR066;

	protected PBTQR002 pbtqR002;

	protected MapperHelper mapperHelper;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param pisdR352 the this.pisdR352 to set
	*/
	public void setPisdR352(PISDR352 pisdR352) {
		this.pisdR352 = pisdR352;
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

	public void setMapperHelper(MapperHelper mapperHelper) {
		this.mapperHelper = mapperHelper;
	}
}