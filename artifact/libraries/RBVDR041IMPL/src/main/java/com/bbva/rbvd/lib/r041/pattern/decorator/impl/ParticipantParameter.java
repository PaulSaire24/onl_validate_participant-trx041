package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.lib.r041.business.ParticipantsBusiness;
import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ParticipantParameter implements BeforeParticipantDataValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantParameter.class);
    private ParticipantProperties participantProperties;
    private RBVDR048 rbvdr048;

    public ParticipantParameter(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public ParticipantParameter(RBVDR048 rbvdr048, ParticipantProperties participantProperties) {
        this.rbvdr048 = rbvdr048;
        this.participantProperties = participantProperties;
    }

    @Override
    public PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation) {
        LOGGER.info("** before dinamic life :: start **");

        PayloadConfig payloadConfig = new PayloadConfig();

        String productId = quotationInformation.getInsuranceProduct().getInsuranceProductId().toString();
        String planId = quotationInformation.getQuotationMod().getInsuranceModalityType();

        ParticipantsBusiness participantsBusiness = new ParticipantsBusiness(applicationConfigurationService,this.rbvdr048);
        List<Participant> participants = participantsBusiness.getParticipants(input,productId, planId);
        LOGGER.info("** before :: participants.size()  {} **",participants.size());

        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        payloadConfig.setParticipants(participants);
        payloadConfig.setInput(input);
        return payloadConfig;
    }


    @Override
    public PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation, String personType) {
        LOGGER.info("** before non life :: start **");

        PayloadConfig payloadConfig = new PayloadConfig();

        ParticipantsBusiness participantsBusiness = new ParticipantsBusiness(applicationConfigurationService,this.rbvdr048);
        List<Participant> participants = participantsBusiness.getParticipants(input);
        LOGGER.info("** before :: participants.size()  {}",participants.size());

        payloadConfig.setParticipants(participants);
        payloadConfig.setInput(input);
        payloadConfig.setRegisteredRolesDB(this.getCompanyRoles(quotationInformation.getInsuranceCompanyDAO().getInsuranceCompanyId()));
        payloadConfig.setPersonType(personType);
        payloadConfig.setQuotationInformation(quotationInformation);
        payloadConfig.setParticipantProperties(participantProperties);
        return payloadConfig;
    }

    public List<RolDAO> getCompanyRoles(BigDecimal insuranceCompanyId) {
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getRolesByCompanyDB(insuranceCompanyId);
    }

    public static final class Builder {
        private RBVDR048 rbvdr048;
        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder rbvdr048(RBVDR048 rbvdr048) {
            this.rbvdr048 = rbvdr048;
            return this;
        }
        public ParticipantParameter buildOne() {
            return new ParticipantParameter(rbvdr048);
        }
    }
}
