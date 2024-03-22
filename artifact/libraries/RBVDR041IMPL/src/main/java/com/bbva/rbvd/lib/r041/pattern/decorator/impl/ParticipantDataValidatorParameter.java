package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.mapper.RolDTO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.group.ParticipantGroupDTO;
import com.bbva.rbvd.lib.r041.transform.bean.RolBean;
import com.bbva.rbvd.lib.r041.transform.map.ParticipantMap;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ParticipantDataValidatorParameter implements BeforeParticipantDataValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantDataValidatorParameter.class);
    private ParticipantProperties participantProperties;
    private PISDR601 pisdr601;
    private RBVDR048 rbvdr048;
    private PISDR012 pisdr012;

    public ParticipantDataValidatorParameter(PISDR601 pisdr601, RBVDR048 rbvdr048) {
        this.pisdr601 = pisdr601;
        this.rbvdr048 = rbvdr048;
    }

    public ParticipantDataValidatorParameter(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public ParticipantDataValidatorParameter(PISDR601 pisdr601, PISDR012 pisdr012, RBVDR048 rbvdr048, ParticipantProperties participantProperties) {
        this.pisdr601 = pisdr601;
        this.pisdr012 = pisdr012;
        this.rbvdr048 = rbvdr048;
        this.participantProperties = participantProperties;
    }

    @Override
    public PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("** before dinamic life :: start");

        PayloadConfig payloadConfig = new PayloadConfig();

        Map<String,Object> result = getProducAndPlanByQuotation(input.getQuotationId());
        String productId = result.get(ConstantsUtil.INSURANCE_PRODUCT_ID).toString();
        String planId = (String) result.get(ConstantsUtil.INSURANCE_MODALITY_TYPE);

        ParticipantsBusiness participantsBusiness = new ParticipantsBusiness(applicationConfigurationService,this.rbvdr048);
        List<Participant> participants = participantsBusiness.getParticipants(input,productId, planId);
        LOGGER.info("** before :: participants.size()  {}",participants.size());

        payloadConfig.setParticipants(participants);
        payloadConfig.setInput(input);
        return payloadConfig;
    }


    @Override
    public PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDTO quotationInformation, String personType) {
        LOGGER.info(" ** GetConfig :: start");
        String insuranceProductId = quotationInformation.getQuotationMod().getInsuranceProductId().toPlainString();
        String modalityTypeProduct = quotationInformation.getQuotationMod().getInsuranceModalityType();
        List<RolDTO> roles = this.getRolesByCompany(ParticipantMap.productAndModalityTransforMap(insuranceProductId, modalityTypeProduct));
        PayloadConfig payloadConfig = new PayloadConfig();
        List<Participant> payloadPropertiesList = new ArrayList<>();
        List<ParticipantGroupDTO> groupParticipant = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info(" ** GetConfig :: groupParticipant -> {}",groupParticipant);

        groupParticipant.forEach(part -> {
            String documentTypeHost = applicationConfigurationService.getProperty(part.getDocumentType());
            part.getParticipantList().forEach(p -> p.getIdentityDocuments().get(0).getDocumentType().setId(documentTypeHost));
            if(ValidationUtil.isBBVAClient(part.getParticipantList().get(0).getPerson().getCustomerId())){
                Participant payloadProperties = new Participant();
                payloadProperties.setDocumentType(documentTypeHost);
                payloadProperties.setCustomerId(part.getParticipantList().get(0).getPerson().getCustomerId());
                payloadProperties.setDocumentNumber(part.getDocumentNumber());
                PEWUResponse customer = executeGetCustomer(part.getDocumentNumber(),documentTypeHost);
                payloadProperties.setCustomer(customer);

                if(StringUtils.startsWith(part.getDocumentNumber(), RBVDInternalConstants.Number.VEINTE)){
                    payloadProperties.setLegalCustomer(executeGetBusinessAgentASOInformation(part.getParticipantList().get(0).getPerson().getCustomerId()));
                }

                payloadPropertiesList.add(payloadProperties);
            }else{
                Participant payloadPropertiesNonCustomer = new Participant();
                payloadPropertiesNonCustomer.setDocumentType(documentTypeHost);
                payloadPropertiesNonCustomer.setDocumentNumber(part.getDocumentNumber());
                payloadPropertiesList.add(payloadPropertiesNonCustomer);
            }

        });

        payloadConfig.setParticipants(payloadPropertiesList);
        payloadConfig.setInput(input);
        payloadConfig.setRegisteredRolesDB(roles);
        payloadConfig.setPersonType(personType);
        payloadConfig.setQuotationInformation(quotationInformation);
        payloadConfig.setParticipantProperties(participantProperties);
        return payloadConfig;
    }


    @Override
    public QuotationCustomerDTO getCustomerFromQuotation(String quotationId) {
        try{
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation START *****");
            QuotationCustomerDTO responseQueryCustomerProductInformation = pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(quotationId);
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation | responseQueryCustomerProductInformation {} *****",responseQueryCustomerProductInformation);
            return responseQueryCustomerProductInformation;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue());
        }
    }

    private PEWUResponse executeGetCustomer(String documentNumber,String documentType){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.executeGetCustomerServiceByDocType(documentNumber,documentType);
    }

    private Map<String,Object> getProducAndPlanByQuotation(String quotationId){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getProducAndPlanByQuotation(quotationId);
    }


    public ListBusinessesASO executeGetBusinessAgentASOInformation(String customerId){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        String encryptedCustomerId = consumerInternalService.executeKsmkCryptographyService(customerId);
        return consumerInternalService.executeListBusinessService(encryptedCustomerId);
    }
    public  List<ParticipantGroupDTO> groupByDocumentNumberAndDocumentType(InputParticipantsDTO participant){
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

    public List<RolDTO> getRolesByCompany(Map<String, Object> arguments) {
        try{
            LOGGER.info("***** RolDAOImpl - getRolesByCompany START *****");
            Map<String, Object> responseRolesByCompany = pisdr012.executeGetParticipantRolesByCompany(arguments);
            LOGGER.info("***** RolDAOImpl - getRolesByCompany | responseRolesByCompany {} *****",responseRolesByCompany);
            return RolBean.rolByParticipantTransformBean(responseRolesByCompany);
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().concat(TypeErrorControllerEnum.ERROR_OBTAIN_PRODUCT_COMPANY_MODALITIES_FROM_DB.getValue()));
        }
    }

    public static final class Builder {
        private PISDR601 pisdr601;
        private RBVDR048 rbvdr048;
        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder pisdr601(PISDR601 pisdr601) {
            this.pisdr601 = pisdr601;
            return this;
        }

        public Builder rbvdr048(RBVDR048 rbvdr048) {
            this.rbvdr048 = rbvdr048;
            return this;
        }
        public ParticipantDataValidatorParameter build() {
            return new ParticipantDataValidatorParameter(pisdr601, rbvdr048);
        }

        public ParticipantDataValidatorParameter buildOne() {
            return new ParticipantDataValidatorParameter(rbvdr048);
        }
    }
}
