package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.insurance.group.ParticipantGroupDTO;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.transform.bean.RolBean;
import com.bbva.rbvd.lib.r041.transform.map.ParticipantMap;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ValidationParameter implements PreValidate {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationParameter.class);
    private ParticipantProperties participantProperties;
    private PISDR601 pisdr601;
    private PISDR012 pisdr012;

    private RBVDR048 rbvdr048;


    public ValidationParameter(PISDR601 pisdr601, PISDR012 pisdr012, RBVDR048 rbvdr048, ParticipantProperties participantProperties) {
        this.pisdr601 = pisdr601;
        this.pisdr012 = pisdr012;
        this.rbvdr048 = rbvdr048;
        this.participantProperties = participantProperties;
    }

    @Override
    public PayloadConfig getConfig(ValidateParticipantDTO input,ApplicationConfigurationService applicationConfigurationService, QuotationJoinCustomerInformationDTO quotationInformation, String personType) {
        LOGGER.info(" ** GetConfig :: start");
        String insuranceProductId = quotationInformation.getQuotationMod().getInsuranceProductId().toPlainString();
        String modalityTypeProduct = quotationInformation.getQuotationMod().getInsuranceModalityType();
        List<RolDTO> roles = this.getRolesByCompany(ParticipantMap.productAndModalityTransforMap(insuranceProductId, modalityTypeProduct));
        PayloadConfig payloadConfig = new PayloadConfig();
        List<PayloadProperties> payloadPropertiesList = new ArrayList<>();
        List<ParticipantGroupDTO> groupParticipant = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info(" ** GetConfig :: groupParticipant -> {}",groupParticipant);

        groupParticipant.forEach(part -> {
                String documentTypeHost = applicationConfigurationService.getProperty(part.getDocumentType());
                part.getParticipantList().forEach(p -> p.getIdentityDocuments().get(0).getDocumentType().setId(documentTypeHost));
                if(ValidationUtil.isBBVAClient(part.getParticipantList().get(0).getPerson().getCustomerId())){
                    PayloadProperties payloadProperties = new PayloadProperties();
                    payloadProperties.setDocumetType(documentTypeHost);
                    payloadProperties.setCustomerId(part.getParticipantList().get(0).getPerson().getCustomerId());
                    payloadProperties.setDocumetNumber(part.getDocumentNumber());
                    PEWUResponse customer = executeGetCustomer(documentTypeHost,part.getDocumentNumber());
                    payloadProperties.setCustomer(customer);

                    if(StringUtils.startsWith(part.getDocumentNumber(),RBVDInternalConstants.Number.VEINTE)){
                        payloadProperties.setLegalCustomer(executeGetBusinessAgentASOInformation(part.getParticipantList().get(0).getPerson().getCustomerId()));
                    }

                    payloadPropertiesList.add(payloadProperties);
                }else{
                    PayloadProperties payloadPropertiesNonCustomer = new PayloadProperties();
                    payloadPropertiesNonCustomer.setDocumetType(documentTypeHost);
                    payloadPropertiesNonCustomer.setDocumetNumber(part.getDocumentNumber());
                    payloadPropertiesList.add(payloadPropertiesNonCustomer);
                }

        });

        payloadConfig.setProperties(payloadPropertiesList);
        payloadConfig.setInput(input);
        payloadConfig.setRegisteredRolesDB(roles);
        payloadConfig.setPersonType(personType);
        payloadConfig.setQuotationInformation(quotationInformation);
        payloadConfig.setParticipantProperties(participantProperties);
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
            throw new BusinessException(be.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().concat(TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue()));
        }
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

    public PEWUResponse executeGetCustomer(String documentNumber,String documentType){
         ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
         return consumerInternalService.executeGetCustomerServiceByDocType(documentNumber,documentType);
    }

    public ListBusinessesASO executeGetBusinessAgentASOInformation(String customerId){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        String encryptedCustomerId = consumerInternalService.executeKsmkCryptographyService(customerId);
        return consumerInternalService.executeListBusinessService(encryptedCustomerId);
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
        private PISDR012 pisdr012;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

    }
}
