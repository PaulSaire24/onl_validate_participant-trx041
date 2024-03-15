package com.bbva.rbvd.lib.r041.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.RBVDR041;
import com.bbva.rbvd.lib.r048.RBVDR048;

public abstract class RBVDR041Abstract extends AbstractLibrary implements RBVDR041 {

	protected ApplicationConfigurationService applicationConfigurationService;

	protected PISDR601 pisdR601;

	protected RBVDR048 rbvdR048;

    protected PISDR012 pisdR012;


	/**
	* @param applicationConfigurationService the this.applicationConfigurationService to set
	*/
	public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {
		this.applicationConfigurationService = applicationConfigurationService;
	}


	/**
	* @param pisdR601 the this.pisdR601 to set
	*/
	public void setPisdR601(PISDR601 pisdR601) {
		this.pisdR601 = pisdR601;
	}

	/**
	* @param rbvdR048 the this.rbvdR048 to set
	*/
	public void setRbvdR048(RBVDR048 rbvdR048) {
		this.rbvdR048 = rbvdR048;
	}

    /**
     * @param pisdR012 the this.pisdR012 to set
     */
    public void setPisdR012(PISDR012 pisdR012) {
        this.pisdR012 = pisdR012;
    }

    public abstract AgregarTerceroBO executeValidateAddParticipant(InputParticipantsDTO input);
}