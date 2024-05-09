package com.bbva.rbvd.lib.r048.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.elara.utility.api.connector.APIConnectorBuilder;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r040.PISDR040;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r048.RBVDR048;
import com.bbva.rbvd.lib.r066.RBVDR066;

/**
 * This class automatically defines the libraries and utilities that it will use.
 */
public abstract class RBVDR048Abstract extends AbstractLibrary implements RBVDR048 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected APIConnector externalApiConnector;

	protected APIConnectorBuilder apiConnectorBuilder;

	protected PISDR014 pisdR014;

	protected PISDR403 pisdR403;

	protected PBTQR002 pbtqR002;

	protected KSMKR002 ksmkR002;

	protected RBVDR066 rbvdR066;

	protected PISDR040 pisdR040;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}

	/**
	* @param externalApiConnector the this.externalApiConnector to set
	*/
	public void setExternalApiConnector(APIConnector externalApiConnector) {
		this.externalApiConnector = externalApiConnector;
	}

	/**
	* @param apiConnectorBuilder the this.apiConnectorBuilder to set
	*/
	public void setApiConnectorBuilder(APIConnectorBuilder apiConnectorBuilder) {
		this.apiConnectorBuilder = apiConnectorBuilder;
	}

	/**
	* @param pisdR014 the this.pisdR014 to set
	*/
	public void setPisdR014(PISDR014 pisdR014) {
		this.pisdR014 = pisdR014;
	}

	/**
	* @param pisdR403 the this.pisdR403 to set
	*/
	public void setPisdR403(PISDR403 pisdR403) {
		this.pisdR403 = pisdR403;
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
	* @param rbvdR066 the this.rbvdR066 to set
	*/
	public void setRbvdR066(RBVDR066 rbvdR066) {
		this.rbvdR066 = rbvdR066;
	}

	/**
	* @param pisdR040 the this.pisdR040 to set
	*/
	public void setPisdR040(PISDR040 pisdR040) {
		this.pisdR040 = pisdR040;
	}

}