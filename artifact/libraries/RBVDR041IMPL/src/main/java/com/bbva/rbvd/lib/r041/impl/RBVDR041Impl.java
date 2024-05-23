package com.bbva.rbvd.lib.r041.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.pattern.composite.ParticipantHandler;
import com.bbva.rbvd.lib.r041.pattern.factory.FactoryProduct;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.dao.ConsumerInternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR041Impl extends RBVDR041Abstract {
	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR041Impl.class);
    private ParticipantProperties participantProperties;

    @Override
    public AgregarTerceroBO executeValidateParticipants(InputParticipantsDTO input) {
        LOGGER.info(" :: executeValidateAddParticipant :: START");
        LOGGER.info(" :: executeValidateAddParticipant :: Input DTO -> {} ::",input);

        try{
            QuotationCustomerDAO quotationInformation = getGeneralQuotationInformation(input.getQuotationId());
            LOGGER.info(" :: executeValidateAddParticipant :: productId -> {} ::", quotationInformation.getInsuranceProduct().getInsuranceProductType());

            ParticipantHandler participantHandler = FactoryProduct.getStrategyByProduct(rbvdR048, applicationConfigurationService,participantProperties,quotationInformation);

            AgregarTerceroBO responseCompany =  participantHandler.handleRequest(input, rbvdR048, applicationConfigurationService,participantProperties,quotationInformation);

            LOGGER.info(" :: executeValidateAddParticipant :: RimacValidationApiResponse -> {} ::",responseCompany);
            return responseCompany;
        }catch (BusinessException businessException){
            this.addAdviceWithDescription(businessException.getAdviceCode(),businessException.getMessage());
            return null;
        }
    }

    private QuotationCustomerDAO getGeneralQuotationInformation(String quotationId) {
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdR048);
        return consumerInternalService.getQuotationDB(quotationId);
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }
}
