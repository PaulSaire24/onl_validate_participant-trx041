package com.bbva.rbvd.lib.r041.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.factory.FactoryProductValidate;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR041Impl extends RBVDR041Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR041Impl.class);
    private ParticipantProperties participantProperties;

    @Override
    public AgregarTerceroBO executeValidateAddParticipant(InputParticipantsDTO input) {
        LOGGER.info(" :: executeValidateAddParticipant :: START");
        LOGGER.info(" :: executeValidateAddParticipant :: ValidateParticipant :: {}",input);

        try{
            String personType = input.getParticipants().get(0).getPerson().getPersonType();
            LOGGER.info("** validateAllParticipantsByIndicatedType :: Person type {} ", personType);
            ValidationUtil.validateAllParticipantsByIndicatedType(input.getParticipants(), personType);
            ValidationParameter validationParameter = new ValidationParameter(pisdR601, pisdR012, rbvdR048, participantProperties);
            QuotationJoinCustomerInformationDTO quotationInformation = validationParameter.getCustomerBasicInformation(input.getQuotationId());
            LOGGER.info(" :: executeValidateAddParticipant :: productId -> {}",quotationInformation.getInsuranceProduct().getInsuranceProductType());
            ParticipantValidations validate = FactoryProductValidate.getProductType(quotationInformation.getInsuranceProduct().getInsuranceProductType(),rbvdR048,validationParameter);
            LOGGER.info(" :: executeValidateAddParticipant :: quotationId -> {}",quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
            PayloadStore payloadStore = validate.start(input,quotationInformation,this.applicationConfigurationService);
            LOGGER.info(" :: executeValidateAddParticipant :: PayloadStore -> {}",payloadStore);
            return payloadStore.getResponseRimac();
        }catch (BusinessException businessException){
            this.addAdviceWithDescription(businessException.getAdviceCode(),businessException.getMessage());
            return null;
        }
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }
}
