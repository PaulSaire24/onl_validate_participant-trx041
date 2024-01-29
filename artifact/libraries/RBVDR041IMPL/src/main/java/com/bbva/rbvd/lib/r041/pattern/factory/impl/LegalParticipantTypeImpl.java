package com.bbva.rbvd.lib.r041.pattern.factory.impl;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.insurance.group.ParticipantGroupDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.pattern.IValidateParticipant;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerCryptographyService;
import com.bbva.rbvd.lib.r041.service.api.ConsumerLegalPersonInternalService;
import com.bbva.rbvd.lib.r041.service.api.ConsumerPersonInternalService;
import com.bbva.rbvd.lib.r041.service.api.ConsumerValidatePersonExternalService;
import com.bbva.rbvd.lib.r041.transform.bean.LegalPersonRimacBean;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class LegalParticipantTypeImpl implements IValidateParticipant {

    private ConsumerPersonInternalService consumerPersonInternalService;
    private ConsumerCryptographyService consumerCryptographyService;
    private ConsumerLegalPersonInternalService consumerLegalPersonInternalService;
    private ParticipantProperties participantProperties;
    private ConsumerValidatePersonExternalService consumerValidatePersonExternalService;

    @Override
    public void validateParticipantData(ValidateParticipantDTO participant,List<RolDTO> roles,QuotationJoinCustomerInformationDTO quotationInformation) {
        List<ParticipantGroupDTO> groupList = FunctionUtils.groupByDocumentNumberAndDocumentType(participant);
        AgregarTerceroBO addThirdPartiesRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
        List<OrganizacionBO> organizacionBOS = new ArrayList<>();
        groupList.forEach(participantGroup ->{
            ParticipantsDTO participantCommonInformation = participantGroup.getParticipantList().get(0);
            PEWUResponse pewuResponse = this.consumerPersonInternalService.executeGetCustomerService(participantGroup.getDocumentType(), participantGroup.getDocumentNumber());
            PersonaBO personaBO = obtainPersonInformation(pewuResponse, participantCommonInformation, quotationInformation, null, participant.getChannelId());
            String encryptedData = consumerCryptographyService.executeKsmkCryptographyService(participantCommonInformation.getPerson().getCustomerId());
            ListBusinessesASO listBusinessesASO = consumerLegalPersonInternalService.executeListBusinessService(encryptedData);
            participantGroup.getParticipantList().forEach(participantDTO ->{
                Integer roleCompany = obtainExistingCompanyRole(participantDTO,participantProperties,roles);
                OrganizacionBO organizacionBO = LegalPersonRimacBean.getDataOrganization(listBusinessesASO.getData().get(0), personaBO, quotationInformation, roleCompany, participantDTO);
                organizacionBOS.add(organizacionBO);
            });
        });
        payloadAgregarTerceroBO.setOrganizacion(CollectionUtils.isEmpty(organizacionBOS) ? null : organizacionBOS);
        addThirdPartiesRimac.setPayload(payloadAgregarTerceroBO);
        String insuranceSimulationId = quotationInformation.getQuotation().getInsuranceSimulationId();
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

    public void setConsumerCryptographyService(ConsumerCryptographyService consumerCryptographyService) {
        this.consumerCryptographyService = consumerCryptographyService;
    }

    public void setConsumerLegalPersonInternalService(ConsumerLegalPersonInternalService consumerLegalPersonInternalService) {
        this.consumerLegalPersonInternalService = consumerLegalPersonInternalService;
    }
}
