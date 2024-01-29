package com.bbva.rbvd.lib.r041.pattern.factory.impl;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.insurance.group.ParticipantGroupDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.pattern.IValidateParticipant;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerPersonInternalService;
import com.bbva.rbvd.lib.r041.service.api.ConsumerValidatePersonExternalService;
import com.bbva.rbvd.lib.r041.transform.bean.NaturalPersonRimacBean;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class NaturalParticipantTypeImpl  implements IValidateParticipant {
    private ConsumerPersonInternalService consumerPersonInternalService;
    private ParticipantProperties participantProperties;
    private ConsumerValidatePersonExternalService consumerValidatePersonExternalService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NaturalPersonRimacBean.class);

    @Override
    public void validateParticipantData(ValidateParticipantDTO participant,List<RolDTO> roles,QuotationJoinCustomerInformationDTO quotationInformation) {
        List<ParticipantGroupDTO> groupList = FunctionUtils.groupByDocumentNumberAndDocumentType(participant);
        LOGGER.info("Participant Group List {}", groupList);
        AgregarTerceroBO addThirdPartiesRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
        List<PersonaBO> personaBOS = new ArrayList<>();
        groupList.forEach(participantGroup ->{
            LOGGER.info("Participant Group List iteration");
            PEWUResponse pewuResponse = this.consumerPersonInternalService.executeGetCustomerService(participantGroup.getDocumentType(), participantGroup.getDocumentNumber());
            participantGroup.getParticipantList().forEach(participantDTO ->{
                LOGGER.info("Participant ñ iteration");
                 Integer roleCompany = obtainExistingCompanyRole(participantDTO,participantProperties,roles);
                 personaBOS.add(obtainPersonInformation(pewuResponse, participantDTO, quotationInformation, roleCompany, participant.getChannelId()));
            });
        });
        payloadAgregarTerceroBO.setPersona(CollectionUtils.isEmpty(personaBOS) ? null : personaBOS);
        addThirdPartiesRimac.setPayload(payloadAgregarTerceroBO);
        String insuranceSimulationId = quotationInformation.getQuotation().getInsuranceCompanyQuotaId();
        String productId             = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        String traceId               = participant.getTraceId();
        this.consumerValidatePersonExternalService.executeValidateParticipantRimacService(addThirdPartiesRimac,insuranceSimulationId,productId,traceId);
    }

    public void setConsumerPersonInternalService(ConsumerPersonInternalService consumerPersonInternalService) {
        this.consumerPersonInternalService = consumerPersonInternalService;
    }

    public void setParticipantProperties(ParticipantProperties participantProperties) {
        this.participantProperties = participantProperties;
    }

    public void setConsumerValidatePersonExternalService(ConsumerValidatePersonExternalService consumerValidatePersonExternalService) {
        this.consumerValidatePersonExternalService = consumerValidatePersonExternalService;
    }

}
