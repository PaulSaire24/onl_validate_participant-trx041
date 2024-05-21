package com.bbva.rbvd.lib.r041.business.addThird;

import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.RepresentanteLegalBO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.addThird.customer.CustomerBusiness;
import com.bbva.rbvd.lib.r041.business.addThird.organization.OrganizationBusiness;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.RepresentanteLegalBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddThirdBusiness {

    public PayloadAgregarTerceroBO getAgregarTerceroBO(PayloadConfig payloadConfig) {
        List<Participant> participants = payloadConfig.getParticipants();

        List<PersonaBO> personas = new ArrayList<>();
        List<OrganizacionBO> organizations = new ArrayList<>();
        List<RepresentanteLegalBO> legalsRepresentatives = new ArrayList<>();

        participants.forEach(participant -> {
            Integer externalRoleId = this.getExternalCompanyRole(participant.getInputParticipant(), payloadConfig);
            if (externalRoleId != null) {
                if (isNaturalPerson(participant)) {
                    personas.add(createPersonaBO(participant, payloadConfig, externalRoleId));
                } else {
                    organizations.add(createOrganizacionBO(participant, payloadConfig, externalRoleId));
                }
            }
            if(participant.getLegalRepresentative()!=null){
                legalsRepresentatives.add(RepresentanteLegalBean.createRepresentanteLegal(participant.getLegalRepresentative()));
            }
        });


        //Enrich additional participants
        //Legal representative
        if (organizations.size()>0) {
            organizations.stream()
                    .filter(organization -> ConstantsUtil.Rol.CONTRACTOR.getValue()==organization.getRol())
                    .forEach(organization -> organization.setRepresentanteLegal(legalsRepresentatives));
        }

        //Beneficiaries
        //logic to enrich beneficiaries

        PayloadAgregarTerceroBO addTerceroByCompany = new PayloadAgregarTerceroBO();

        addTerceroByCompany.setPersona(personas);
        addTerceroByCompany.setOrganizacion(organizations);
        addTerceroByCompany.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        return addTerceroByCompany;
    }

    public static Integer getExternalCompanyRole(ParticipantsDTO participantDTO, PayloadConfig payloadConfig) {
        List<RolDAO> rolesFromDB = payloadConfig.getRegisteredRolesDB();

        String roleCodeBank = payloadConfig.getParticipantProperties().obtainPropertyFromConsole(participantDTO.getParticipantType().getId().concat(".bank.role"));
        return rolesFromDB.stream()
                .filter(rolDTO -> roleCodeBank.equalsIgnoreCase(rolDTO.getParticipantRoleId().toString()))
                .map(RolDAO::getInsuranceCompanyRoleId)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(null);
    }
    private static boolean isNaturalPerson(Participant participant) {
        return RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(participant.getInputParticipant().getPerson().getPersonType());
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
}
