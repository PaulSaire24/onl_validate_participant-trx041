package com.bbva.rbvd.lib.r041.impl;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.factory.FactoryProductValidate;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR041Impl extends RBVDR041Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR041Impl.class);

	@Override
	public AgregarTerceroBO executeValidateAddParticipant(InputParticipantsDTO input) {
		LOGGER.info(" :: executeValidateAddParticipant :: [ START ] ");
		LOGGER.info(" :: executeValidateAddParticipant :: [ ValidateParticipant :: {} ] ",input);
		ValidationParameter validationParameter = new ValidationParameter(pisdR601,rbvdR048);
		QuotationJoinCustomerInformationDTO quotationInformation = validationParameter.getCustomerBasicInformation(input.getQuotationId());
		LOGGER.info(" :: executeValidateAddParticipant :: productId -> {}",quotationInformation.getInsuranceProduct().getInsuranceProductType());
		ParticipantValidations validate = FactoryProductValidate.getProductType(quotationInformation.getInsuranceProduct().getInsuranceProductType(),rbvdR048);
		LOGGER.info(" :: executeValidateAddParticipant :: quotationId -> {}",quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
		PayloadStore payloadStore = validate.start(input,quotationInformation.getQuotation().getInsuranceCompanyQuotaId(),this.applicationConfigurationService);
		LOGGER.info(" :: executeValidateAddParticipant :: PayloadStore -> {}",payloadStore);
		return payloadStore.getResponseRimac();
	}
}
