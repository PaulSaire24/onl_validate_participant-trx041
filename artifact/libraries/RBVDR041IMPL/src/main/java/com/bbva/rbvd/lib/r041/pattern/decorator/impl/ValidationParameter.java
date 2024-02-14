package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ValidationParameter implements PreValidate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationParameter.class);
    private PISDR601 pisdr601;
    private PBTQR002 pbtqr002;

    public ValidationParameter(PISDR601 pisdr601,PBTQR002 pbtqr002) {
        this.pisdr601 = pisdr601;
        this.pbtqr002 = pbtqr002;
    }

    public ValidationParameter(PBTQR002 pbtqr002) {
        this.pbtqr002 = pbtqr002;
    }

    @Override
    public PayloadConfig getConfig(ValidateParticipantDTO input,ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("** getConfig :: start");
        PayloadConfig payloadConfig = new PayloadConfig();
        List<PayloadProperties> payloadPropertiesList = new ArrayList<>();
        input.getParticipants().forEach(part -> {
            PayloadProperties payloadProperties = new PayloadProperties();
            String documentTypeHost = applicationConfigurationService.getProperty(part.getIdentityDocuments().get(0).getDocumentType().getId());
            part.getIdentityDocuments().get(0).getDocumentType().setId(documentTypeHost);
            if(ValidationUtil.isBBVAClient(part.getPerson().getCustomerId())){
                payloadProperties.setDocumetType(documentTypeHost);
                payloadProperties.setCustomerId(part.getPerson().getCustomerId());
                payloadProperties.setDocumetNumber(part.getIdentityDocuments().get(0).getValue());
                PEWUResponse customer = executeGetCustomer(part.getPerson().getCustomerId());
                payloadProperties.setCustomer(customer);
                payloadPropertiesList.add(payloadProperties);
            }

        });
        payloadConfig.setProperties(payloadPropertiesList);
        payloadConfig.setInput(input);
        return payloadConfig;
    }

    @Override
    public QuotationJoinCustomerInformationDTO getCustomerBasicInformation(String quotationId) {
        try{
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation START *****");
            QuotationJoinCustomerInformationDTO responseQueryCustomerProductInformation = pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(quotationId);
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation | responseQueryCustomerProductInformation {} *****",responseQueryCustomerProductInformation);
            return responseQueryCustomerProductInformation;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue());
        }
    }

    public PEWUResponse executeGetCustomer(String customerId){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(pbtqr002);
         return consumerInternalService.executeGetCustomerService(customerId);
    }


    public static final class Builder {
        private PISDR601 pisdr601;
        private PBTQR002 pbtqr002;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder pisdr601(PISDR601 pisdr601) {
            this.pisdr601 = pisdr601;
            return this;
        }

        public Builder pbtqr002(PBTQR002 pbtqr002) {
            this.pbtqr002 = pbtqr002;
            return this;
        }

        public ValidationParameter build() {
            return new ValidationParameter(pisdr601, pbtqr002);
        }

        public ValidationParameter buildOne() {
            return new ValidationParameter(pbtqr002);
        }
    }
}
