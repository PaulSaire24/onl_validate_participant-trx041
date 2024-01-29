package com.bbva.rbvd.lib.r041.impl;

import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.ResponseLibrary;
import com.bbva.rbvd.lib.r041.business.AddParticipantBusiness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The RBVDR041Impl class...
 */
public class RBVDR041Impl extends RBVDR041Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR041Impl.class);

	private AddParticipantBusiness addParticipantBusiness;

	/**
	 * @param participant  participant send by application
	 * @executeValidateAddParticipant(participant) -> Method to add participants in the insurance company.
	 * */
	@Override
	public ResponseLibrary<Void> executeValidateAddParticipant(ValidateParticipantDTO participant) {
		LOGGER.info(" :: executeValidateAddParticipant :: [ START ] ");
		LOGGER.info(" :: executeValidateAddParticipant :: [ ValidateParticipant :: {} ] ",participant);
		return this.addParticipantBusiness.executeAddParticipantAllProduct(participant);
	}

	public void setAddParticipantBusiness(AddParticipantBusiness addParticipantBusiness) {
		this.addParticipantBusiness = addParticipantBusiness;
	}
}
