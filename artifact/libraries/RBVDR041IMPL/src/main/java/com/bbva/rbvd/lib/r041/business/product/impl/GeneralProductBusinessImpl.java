package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;

import com.bbva.rbvd.dto.participant.dao.RolDAO;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants.ParticipantType;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;

import com.bbva.rbvd.lib.r041.business.product.IGeneralProductBusiness;

import com.bbva.rbvd.lib.r041.business.customer.CustomerBusiness;
import com.bbva.rbvd.lib.r041.business.organization.OrganizationBusiness;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;

import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;

import com.bbva.rbvd.lib.r041.validation.ValidationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneralProductBusinessImpl implements IGeneralProductBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralProductBusinessImpl.class);

    private ApplicationConfigurationService applicationConfigurationService;

    public GeneralProductBusinessImpl(ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
    }

    @Override
    public AgregarTerceroBO createRequestByCompany(PayloadConfig payloadConfig) {
        AgregarTerceroBO requestCompany = new AgregarTerceroBO();
        PayloadAgregarTerceroBO addTerceroByCompany = new PayloadAgregarTerceroBO();

        List<Participant> participantList = payloadConfig.getParticipants();
        List<RolDAO> selectedRoles = payloadConfig.getRegisteredRolesDB();

        List<PersonaBO> personaList = new ArrayList<>();
        List<OrganizacionBO> organizations = new ArrayList<>();

        participantList.forEach(part-> {
            Integer roleId = ValidationUtil.obtainExistingCompanyRole(part.getInputParticipant(),
                    payloadConfig.getParticipantProperties(),selectedRoles);
            if(roleId != null){
                if(RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(part.getInputParticipant().getPerson().getPersonType())){
                    ParticipantType participantType = Objects.nonNull(part.getCustomer()) ? ParticipantType.CUSTOMER : ParticipantType.NON_CUSTOMER;
                    com.bbva.rbvd.lib.r041.pattern.factory.Participant participant = ParticipantFactory.buildParticipant(participantType);
                    PersonaBO persona = participant.createRequestParticipant(part, payloadConfig.getQuotationInformation(),
                            roleId);
                    persona.setRolName(applicationConfigurationService.getProperty(part.getInputParticipant().getParticipantType().getId()));
                    personaList.add(persona);
                    addTerceroByCompany.setPersona(personaList);
                }else{
                    // debe ir por participant.?
                    PersonaBO personaBO = CustomerBusiness.mapCustomerRequestData(part, payloadConfig.getQuotationInformation(),
                            null);
                    OrganizacionBO organizacionBO = OrganizationBusiness.mapOrganizations(part.getLegalCustomer().getData().get(0), personaBO, payloadConfig.getQuotationInformation(), roleId,
                            part.getInputParticipant());
                    organizacionBO.setRolName(applicationConfigurationService.getProperty(part.getInputParticipant().getParticipantType().getId()));
                    organizations.add(organizacionBO);
                    addTerceroByCompany.setOrganizacion(organizations);
                }
        }});

        addTerceroByCompany.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestCompany.setPayload(addTerceroByCompany);

        LOGGER.info("** createRequestByCompany - request Company -> {}",requestCompany);

        return requestCompany;
    }
}
