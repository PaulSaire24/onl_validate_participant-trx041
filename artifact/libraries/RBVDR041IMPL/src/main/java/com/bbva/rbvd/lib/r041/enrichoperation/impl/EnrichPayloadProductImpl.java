package com.bbva.rbvd.lib.r041.enrichoperation.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.*;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r041.business.customer.CustomerBusiness;
import com.bbva.rbvd.lib.r041.business.organization.OrganizationBusiness;
import com.bbva.rbvd.lib.r041.enrichoperation.IEnrichPayloadProduct;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.LegalRepresentative;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnrichPayloadProductImpl implements IEnrichPayloadProduct {
    private ParticipantProperties participantProperties;
    private RBVDR048 rbvdr048;
    @Override
    public PayloadConfig enrichParticipantData(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation) {
        PayloadConfig payloadConfig = new PayloadConfig();
        ParticipantsBusiness participantsBusiness = new ParticipantsBusiness(applicationConfigurationService,rbvdr048);
        List<Participant> participants = participantsBusiness.getParticipants(input,quotationInformation);
        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        payloadConfig.setParticipants(participants);
        payloadConfig.setInput(input);
        payloadConfig.setRegisteredRolesDB(this.getCompanyRoles(quotationInformation.getInsuranceCompanyDAO().getInsuranceCompanyId()));
        payloadConfig.setQuotationInformation(quotationInformation);
        payloadConfig.setParticipantProperties(participantProperties);
        return payloadConfig;
    }

    public AgregarTerceroBO enrichToSendRimac(PayloadConfig payloadConfig) {
        AgregarTerceroBO requestCompany = new AgregarTerceroBO();
        PayloadAgregarTerceroBO addTerceroByCompany = new PayloadAgregarTerceroBO();

        List<Participant> participantList = payloadConfig.getParticipants();
        List<RolDAO> selectedRoles = payloadConfig.getRegisteredRolesDB();

        List<PersonaBO> personaList = new ArrayList<>();
        List<OrganizacionBO> organizations = new ArrayList<>();

        participantList.forEach(part -> {
            Integer roleId = ValidationUtil.obtainExistingCompanyRole(part.getInputParticipant(), payloadConfig.getParticipantProperties(), selectedRoles);
            if (roleId != null) {
                if (RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(part.getInputParticipant().getPerson().getPersonType())) {
                    PersonaBO persona = createPersonaBO(part, payloadConfig, roleId);
                    personaList.add(persona);
                    addTerceroByCompany.setPersona(personaList);
                } else {
                    OrganizacionBO organizacionBO = createOrganizacionBO(part, payloadConfig, roleId);
                    organizations.add(organizacionBO);
                    addTerceroByCompany.setOrganizacion(organizations);
                }
            }
        });

        //Enrich additional participants
        //Legal representative
        if (addTerceroByCompany.getOrganizacion() != null) {
            organizations.stream()
                    .filter(organization -> ConstantsUtil.Rol.CONTRACTOR.getValue()==organization.getRol())
                    .forEach(organization -> enrichOrganizationByParticipantType(organization, participantList));
        }

        //Beneficiaries
        //logic to enrich beneficiaries

        addTerceroByCompany.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestCompany.setPayload(addTerceroByCompany);

        return requestCompany;
    }

    private PersonaBO createPersonaBO(Participant part, PayloadConfig payloadConfig, Integer roleId) {
        RBVDInternalConstants.ParticipantType participantType = Objects.nonNull(part.getCustomer()) ? RBVDInternalConstants.ParticipantType.CUSTOMER : RBVDInternalConstants.ParticipantType.NON_CUSTOMER;
        com.bbva.rbvd.lib.r041.pattern.factory.Participant participant = ParticipantFactory.buildParticipant(participantType);
        PersonaBO persona = participant.createRequestParticipant(part, payloadConfig.getQuotationInformation(), roleId);
        persona.setRolName(payloadConfig.getParticipantProperties().obtainPropertyFromConsole(part.getInputParticipant().getParticipantType().getId()));
        return persona;
    }

    private OrganizacionBO createOrganizacionBO(Participant part, PayloadConfig payloadConfig, Integer roleId) {
        PersonaBO personaBO = CustomerBusiness.mapCustomerRequestData(part, payloadConfig.getQuotationInformation(), null);
        OrganizacionBO organizacionBO = OrganizationBusiness.mapOrganizations(part.getLegalCustomer().getData().get(0), personaBO, payloadConfig.getQuotationInformation(), roleId, part.getInputParticipant());
        organizacionBO.setRolName(payloadConfig.getParticipantProperties().obtainPropertyFromConsole(part.getInputParticipant().getParticipantType().getId()));
        return organizacionBO;
    }

    public List<RolDAO> getCompanyRoles(BigDecimal insuranceCompanyId) {
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getRolesByCompanyDB(insuranceCompanyId);
    }

    private void enrichOrganizationByParticipantType(OrganizacionBO organizacionBO, List<Participant> participantList){
        List<RepresentanteLegalBO> representanteLegalBOList = new ArrayList<>();
        participantList.forEach(participant -> {
            if(participant.getLegalRepresentative()!=null){
                representanteLegalBOList.add(this.createRepresentanteLegal(participant.getLegalRepresentative()));
            }
        });

        if(!CollectionUtils.isEmpty(representanteLegalBOList)){
            organizacionBO.setRepresentanteLegal(representanteLegalBOList);
        }
    }

    private RepresentanteLegalBO createRepresentanteLegal(final LegalRepresentative legalRepresentative) {
        RepresentanteLegalBO representanteLegalBO = new RepresentanteLegalBO();
        representanteLegalBO.setTipoDocumento(legalRepresentative.getDocumentType());
        representanteLegalBO.setNroDocumento(legalRepresentative.getDocumentNumber());
        representanteLegalBO.setApePaterno(legalRepresentative.getLastName());
        representanteLegalBO.setApeMaterno(legalRepresentative.getSecondLastName());
        representanteLegalBO.setNombres(legalRepresentative.getFirstName());
        return representanteLegalBO;
    }

    public EnrichPayloadProductImpl(ParticipantProperties participantProperties, RBVDR048 rbvdr048) {
        this.participantProperties = participantProperties;
        this.rbvdr048 = rbvdr048;
    }
}
