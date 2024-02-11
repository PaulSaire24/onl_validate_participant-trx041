package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.insurance.group.ParticipantGroupDTO;
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
import java.util.stream.IntStream;

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
        PayloadProperties payloadProperties = new PayloadProperties();
        List<PayloadProperties> payloadPropertiesList = new ArrayList<>();
        List<ParticipantGroupDTO> groupParticipant = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info("** getConfig :: groupParticipant -> {}",groupParticipant);
        groupParticipant.forEach(part -> {
                if(ValidationUtil.isBBVAClient(part.getParticipantList().get(0).getPerson().getCustomerId())){
                    String documentTypeHost = applicationConfigurationService.getProperty(part.getDocumentType());
                    PEWUResponse customer = executeGetCustomer(documentTypeHost,part.getDocumentNumber());
                    payloadProperties.setDocumetType(part.getDocumentType());
                    payloadProperties.setDocumetNumber(part.getDocumentNumber());
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

    public PEWUResponse executeGetCustomer(String documentNumber,String documentType){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(pbtqr002);
         return consumerInternalService.executeGetCustomerService(documentNumber,documentType);
    }
    public  List<ParticipantGroupDTO> groupByDocumentNumberAndDocumentType(ValidateParticipantDTO participant){
        List<ParticipantGroupDTO> groupParticipants = new ArrayList<>();
        IntStream.range(0,participant.getParticipants().size()).forEach(i ->{
            ParticipantsDTO participantPrimary = participant.getParticipants().get(i);
            List<ParticipantsDTO> participantMemory = new ArrayList<>();
            participantMemory.add(participantPrimary);
            IntStream.range(i+1,participant.getParticipants().size()).forEach(j ->{
                ParticipantsDTO participantSecond = participant.getParticipants().get(j);

                if(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId().equalsIgnoreCase(participantSecond.getIdentityDocuments().get(0).getDocumentType().getId())
                        && participantPrimary.getIdentityDocuments().get(0).getValue().equalsIgnoreCase(participantSecond.getIdentityDocuments().get(0).getValue())){
                    participantMemory.add(participantSecond);
                }
            });
            boolean isNotGrouped = groupParticipants.stream().noneMatch(groupParticipant -> groupParticipant.getDocumentType().equalsIgnoreCase(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId()) &&
                    groupParticipant.getDocumentNumber().equalsIgnoreCase(participantPrimary.getIdentityDocuments().get(0).getValue()));
            if(isNotGrouped){
                ParticipantGroupDTO participantGroupDTO = new ParticipantGroupDTO();
                participantGroupDTO.setDocumentNumber(participantPrimary.getIdentityDocuments().get(0).getValue());
                participantGroupDTO.setDocumentType(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId());
                participantGroupDTO.setParticipantList(participantMemory);
                groupParticipants.add(participantGroupDTO);
            }
        });
        LOGGER.info("groupByDocumentNumberAndDocumentType end ***** {}",groupParticipants);
        return groupParticipants;
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
