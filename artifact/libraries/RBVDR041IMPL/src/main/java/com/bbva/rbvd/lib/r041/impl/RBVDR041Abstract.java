package com.bbva.rbvd.lib.r041.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.rbvd.lib.r041.RBVDR041;
import com.bbva.rbvd.lib.r048.RBVDR048;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class RBVDR041Abstract extends AbstractLibrary implements RBVDR041 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected RBVDR048 rbvdR048;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param rbvdR048 the this.rbvdR048 to set
	*/
	public void setRbvdR048(RBVDR048 rbvdR048) {
		this.rbvdR048 = rbvdR048;
	}

}