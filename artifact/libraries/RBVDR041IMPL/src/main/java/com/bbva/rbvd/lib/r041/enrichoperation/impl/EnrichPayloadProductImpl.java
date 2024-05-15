package com.bbva.rbvd.lib.r041.enrichoperation.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.RepresentanteLegalBO;

import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r041.enrichoperation.IEnrichPayloadProduct;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.LegalRepresentative;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public AgregarTerceroBO enrichRimacPayloadByProductAndParticipantType(AgregarTerceroBO rimacRequest, QuotationCustomerDAO quotationInformation,List<Participant> participants) {

            List<PersonaBO> listPeople = rimacRequest.getPayload().getPersona();
            if (listPeople != null) {
                listPeople.forEach(person ->{
                    enrichPersonByProduct(person, quotationInformation);
                });
            }

            List<OrganizacionBO> listOrganization = rimacRequest.getPayload().getOrganizacion();
            if (listOrganization != null) {
                listOrganization.forEach(organization ->{
                    enrichOrganizationByProduct(organization, quotationInformation);

                    if(ConstantsUtil.Rol.CONTRACTOR.getValue()==organization.getRol()){
                        enrichOrgnizationByParticipantType(organization,participants);
                    }
                });
            }

            rimacRequest.getPayload().setCotizacion(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());

        return rimacRequest;
    }

    public List<RolDAO> getCompanyRoles(BigDecimal insuranceCompanyId) {
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getRolesByCompanyDB(insuranceCompanyId);
    }

    private void enrichOrganizationByProduct(OrganizacionBO organizacionBO, QuotationCustomerDAO quotationInformation) {
        if("830".equals(quotationInformation.getInsuranceProduct().getInsuranceProductType())){
            organizacionBO.setProteccionDatosPersonales("S");
            organizacionBO.setEnvioComunicacionesComerciales("N");
        }
    }

    private void enrichPersonByProduct(PersonaBO personaBO, QuotationCustomerDAO quotationInformation) {
        if("830".equals(quotationInformation.getInsuranceProduct().getInsuranceProductType())){
            personaBO.setProteccionDatosPersonales("S");
            personaBO.setEnvioComunicacionesComerciales("N");
        }
    }

    private void enrichOrgnizationByParticipantType(OrganizacionBO organizacionBO, List<Participant> participantList){
        List<RepresentanteLegalBO> representanteLegalBOList = new ArrayList<>();
        participantList.forEach(participant -> {
            if(participant.getLegalRepresentative()!=null){
                representanteLegalBOList.add(this.createRepresentsanteLegal(participant.getLegalRepresentative()));
            }
        });

        if(!CollectionUtils.isEmpty(representanteLegalBOList)){
            organizacionBO.setRepresentanteLegal(representanteLegalBOList);
        }
    }

    private RepresentanteLegalBO createRepresentsanteLegal(final LegalRepresentative legalRepresentative) {
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
