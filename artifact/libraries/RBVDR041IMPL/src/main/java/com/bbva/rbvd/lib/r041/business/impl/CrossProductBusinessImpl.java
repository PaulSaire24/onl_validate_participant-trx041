package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants.ParticipantType;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.lib.r041.business.ICrossProductBusiness;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacLegalPerson;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CrossProductBusinessImpl implements ICrossProductBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossProductBusinessImpl.class);

    private RBVDR048 rbvdr048;

    public CrossProductBusinessImpl(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    @Override
    public AgregarTerceroBO createRequestByCompany(PayloadConfig payloadConfig) {
        AgregarTerceroBO requestCompany = new AgregarTerceroBO();
        PayloadAgregarTerceroBO addTerceroByCompany = new PayloadAgregarTerceroBO();

        List<Participant> participantList = payloadConfig.getParticipants();
        List<RolDAO> selectedRoles = payloadConfig.getRegisteredRolesDB();

        List<PersonaBO> personaList = new ArrayList<>();
        List<OrganizacionBO> organizacionList = new ArrayList<>();

        participantList.forEach(part-> {
            Integer roleId = ValidationUtil.obtainExistingCompanyRole(part.getInputParticipant(),
                    payloadConfig.getParticipantProperties(),selectedRoles);
            if(roleId != null){
                if(RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(part.getInputParticipant().getPerson().getPersonType())){
                    ParticipantType participantType = Objects.nonNull(part.getCustomer()) ? ParticipantType.CUSTOMER : ParticipantType.NON_CUSTOMER;
                    com.bbva.rbvd.lib.r041.pattern.factory.Participant participant = ParticipantFactory.buildParticipant(participantType);
                    personaList.add(participant.createRequestParticipant(part, payloadConfig.getQuotationInformation(),
                            roleId));

                    addTerceroByCompany.setPersona(personaList);
                }else{
                    PersonaBO personaBO = ValidateRimacNaturalPerson.mapCustomerRequestData(part, payloadConfig.getQuotationInformation(),
                            null);
                    OrganizacionBO organizacionBO = ValidateRimacLegalPerson.getDataOrganization(part.getLegalCustomer().getData().get(0), personaBO, payloadConfig.getQuotationInformation(), roleId,
                            part.getInputParticipant());
                    organizacionList.add(organizacionBO);

                    addTerceroByCompany.setOrganizacion(organizacionList);
                }
        }});

        addTerceroByCompany.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestCompany.setPayload(addTerceroByCompany);
        enrichPayloadByProduct(addTerceroByCompany,payloadConfig.getQuotationInformation());

        LOGGER.info("** createRequestByCompany - request Company -> {}",requestCompany);

        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);

        String quotationId = payloadConfig.getQuotationInformation().getQuotation().getInsuranceCompanyQuotaId();
        String productId = payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductType();
        String traceId = payloadConfig.getInput().getTraceId();
        String channel = payloadConfig.getInput().getChannelId();
        return consumerService.executeValidateParticipantRimacService(requestCompany,quotationId,productId,traceId,channel);
    }

    public static void enrichPayloadByProduct(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationCustomerDAO quotationInformation) {
        String insuranceProductType = quotationInformation.getInsuranceProduct().getInsuranceProductType();

        List<PersonaBO> listPeople = payloadAgregarTerceroBO.getPersona();
        if (listPeople != null) {
            enrichRimacPersonObject(payloadAgregarTerceroBO, listPeople, insuranceProductType);
        }

        List<OrganizacionBO> listOrganization = payloadAgregarTerceroBO.getOrganizacion();
        if (listOrganization != null) {
            enrichRimacOrganizationObject(payloadAgregarTerceroBO, listOrganization, insuranceProductType);
        }

        payloadAgregarTerceroBO.setCotizacion(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());

    }

    private static void enrichRimacOrganizationObject(PayloadAgregarTerceroBO payloadAgregarTerceroBO, List<OrganizacionBO> listOrganization, String insuranceProductType) {
        if ("830".equals(insuranceProductType)) {
            listOrganization = listOrganization.stream()
                    .map(organization -> {
                        organization.setProteccionDatosPersonales("S");
                        organization.setEnvioComunicacionesComerciales("N");
                        return organization;
                    }).collect(Collectors.toList());
            payloadAgregarTerceroBO.setOrganizacion(listOrganization);
        }
    }

    private static void enrichRimacPersonObject(PayloadAgregarTerceroBO payloadAgregarTerceroBO, List<PersonaBO> listPeople, String insuranceProductType) {
        if ("830".equals(insuranceProductType)) {
            listPeople = listPeople.stream()
                    .map(people -> {
                        people.setProteccionDatosPersonales("S");
                        people.setEnvioComunicacionesComerciales("N");
                        return people;
                    }).collect(Collectors.toList());
            payloadAgregarTerceroBO.setPersona(listPeople);
        }
    }
}
