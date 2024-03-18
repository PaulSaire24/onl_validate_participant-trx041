package com.bbva.rbvd.lib.r041.pattern.decorator.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.impl.NonLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.decorator.PreParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceProductNonLifeProducts extends ValidateDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceProductNonLifeProducts.class);

    public InsuranceProductNonLifeProducts(PreParticipantValidations preValidate) {
        super(preValidate);
    }

    @Override
    public PayloadStore start(InputParticipantsDTO input, QuotationJoinCustomerInformationDTO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService, quotationInformation, input.getParticipants().get(0).getPerson().getPersonType());
        LOGGER.info(" :: PayloadConfig :: {} :: ",payloadConfig);
        NonLifeProductBusinessImpl nonLifeProductBusiness = new NonLifeProductBusinessImpl(rbvdr048);
        AgregarTerceroBO responseCompany = nonLifeProductBusiness.createRequestByCompany(payloadConfig);
        LOGGER.info(" :: Response Company :: {} :: ",responseCompany);


        return PayloadStore.Builder.an()
                .responseRimac(responseCompany)
                .build();
    }

    public static final class Builder {
        private PreParticipantValidations preValidate;

        private Builder() {
        }

        public static InsuranceProductNonLifeProducts.Builder an() {
            return new InsuranceProductNonLifeProducts.Builder();
        }

        public InsuranceProductNonLifeProducts.Builder preValidate(PreParticipantValidations preValidate) {
            this.preValidate = preValidate;
            return this;
        }

        public InsuranceProductNonLifeProducts build() {
            return new InsuranceProductNonLifeProducts(preValidate);
        }
    }
}
