package com.bbva.rbvd.lib.r041.pattern.decorator.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.impl.NonLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantDataValidatorDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceProductNonLifeProducts extends ParticipantDataValidatorDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceProductNonLifeProducts.class);

    public InsuranceProductNonLifeProducts(BeforeParticipantDataValidator preValidate) {
        super(preValidate);
    }

    @Override
    public PayloadStore start(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        PayloadConfig payloadConfig = this.getBeforeValidator().before(input,applicationConfigurationService, quotationInformation, input.getParticipants().get(0).getPerson().getPersonType());
        LOGGER.info(" :: PayloadConfig :: {} :: ",payloadConfig);
        NonLifeProductBusinessImpl nonLifeProductBusiness = new NonLifeProductBusinessImpl(rbvdr048);
        AgregarTerceroBO responseCompany = nonLifeProductBusiness.createRequestByCompany(payloadConfig);
        LOGGER.info(" :: Response Company :: {} :: ",responseCompany);


        return PayloadStore.Builder.an()
                .responseRimac(responseCompany)
                .build();
    }

    public static final class Builder {
        private BeforeParticipantDataValidator preValidate;

        private Builder() {
        }

        public static InsuranceProductNonLifeProducts.Builder an() {
            return new InsuranceProductNonLifeProducts.Builder();
        }

        public InsuranceProductNonLifeProducts.Builder preValidate(BeforeParticipantDataValidator preValidate) {
            this.preValidate = preValidate;
            return this;
        }

        public InsuranceProductNonLifeProducts build() {
            return new InsuranceProductNonLifeProducts(preValidate);
        }
    }
}
