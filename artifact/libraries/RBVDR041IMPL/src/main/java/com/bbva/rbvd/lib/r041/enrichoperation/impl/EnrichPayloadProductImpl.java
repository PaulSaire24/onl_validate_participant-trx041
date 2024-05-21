package com.bbva.rbvd.lib.r041.enrichoperation.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.*;

import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r041.business.addThird.AddThirdBusiness;
import com.bbva.rbvd.lib.r041.enrichoperation.IEnrichPayloadProduct;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.dao.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;

import java.math.BigDecimal;
import java.util.List;

public class EnrichPayloadProductImpl implements IEnrichPayloadProduct {
    private ParticipantProperties participantProperties;
    private RBVDR048 rbvdr048;
    @Override
    public PayloadConfig enrichParticipantData(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation) {

        ParticipantsBusiness participantsBusiness = new ParticipantsBusiness(applicationConfigurationService,rbvdr048);
        List<Participant> participants = participantsBusiness.getParticipants(input,quotationInformation);

        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        payloadConfig.setParticipants(participants);
        payloadConfig.setInput(input);
        payloadConfig.setRegisteredRolesDB(this.getCompanyRoles(quotationInformation.getInsuranceCompanyDAO().getInsuranceCompanyId()));
        payloadConfig.setQuotationInformation(quotationInformation);
        payloadConfig.setParticipantProperties(participantProperties);
        return payloadConfig;
    }

    @Override
    public AgregarTerceroBO enrichRequestToCompany(PayloadConfig payloadConfig) {

        PayloadAgregarTerceroBO addTerceroByCompany = new AddThirdBusiness().getAgregarTerceroBO(payloadConfig);
        AgregarTerceroBO requestCompany = new AgregarTerceroBO();
        requestCompany.setPayload(addTerceroByCompany);

        return requestCompany;
    }

    private List<RolDAO> getCompanyRoles(BigDecimal insuranceCompanyId) {
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getRolesByCompanyDB(insuranceCompanyId);
    }

    public EnrichPayloadProductImpl(ParticipantProperties participantProperties, RBVDR048 rbvdr048) {
        this.participantProperties = participantProperties;
        this.rbvdr048 = rbvdr048;
    }
}
