package com.bbva.rbvd.lib.r041.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantParameter;
import com.bbva.rbvd.lib.r041.pattern.factory.FactoryProduct;
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
            String personType = getFirstLegalPerson(input);
            LOGGER.info("** validateAllParticipantsByIndicatedType :: Person type {} ", personType);
            ValidationUtil.verifyAllParticipantAsSameLegalPerson(input.getParticipants(), personType);
            ParticipantParameter participantParameter = new ParticipantParameter(pisdR601, pisdR012, rbvdR048, participantProperties);
            QuotationCustomerDTO quotationProduct = participantParameter.getQuotationProductByQuoteId(input.getQuotationId());
            LOGGER.info(" :: executeValidateAddParticipant :: productId -> {}", quotationProduct.getInsuranceProduct().getInsuranceProductType());
            ParticipantDataValidator productObject = FactoryProduct.getProductObject(quotationProduct.getInsuranceProduct().getInsuranceProductType(),rbvdR048,participantParameter);
            LOGGER.info(" :: executeValidateAddParticipant :: quotationId -> {}", quotationProduct.getQuotation().getInsuranceCompanyQuotaId());
            PayloadStore payloadStore = productObject.start(input, quotationProduct, this.rbvdR048,this.applicationConfigurationService);
            LOGGER.info(" :: executeValidateAddParticipant :: PayloadStore -> {}",payloadStore);
            return payloadStore.getResponseRimac();
        }catch (BusinessException businessException){
            this.addAdviceWithDescription(businessException.getAdviceCode(),businessException.getMessage());
            return null;
        }
    }

    private static String getFirstLegalPerson(InputParticipantsDTO input) {
        return input.getParticipants().get(0).getPerson().getPersonType();
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }
}
