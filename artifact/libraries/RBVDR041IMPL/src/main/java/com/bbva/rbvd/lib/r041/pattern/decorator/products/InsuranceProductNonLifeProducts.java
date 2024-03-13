package com.bbva.rbvd.lib.r041.pattern.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.lib.r041.business.impl.NonLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.InsuranceProductDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceProductNonLifeProducts extends InsuranceProductDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceProductNonLifeProducts.class);

    public InsuranceProductNonLifeProducts(PreValidate preValidate, PostValidate postValidate) {
        super(preValidate, postValidate);
    }

    @Override
    public PayloadStore generateAddParticipants(ValidateParticipantDTO input, QuotationJoinCustomerInformationDTO quotationInformation, ApplicationConfigurationService applicationConfigurationService, String personType) {
        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService, quotationInformation, personType);
        LOGGER.info(" :: PayloadConfig :: {} :: ",payloadConfig);
        NonLifeProductBusinessImpl nonLifeProductBusiness = new NonLifeProductBusinessImpl();
        AgregarTerceroBO requestCompany = nonLifeProductBusiness.createRequestByCompany(payloadConfig);
        LOGGER.info(" :: Request Company :: {} :: ",requestCompany);
        AgregarTerceroBO responseCompany = this.getPostValidate().end(requestCompany,payloadConfig.getQuotationInformation().getQuotation().getInsuranceCompanyQuotaId(), payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductType(),payloadConfig.getInput().getTraceId());
        return PayloadStore.Builder.an()
                .responseRimac(responseCompany)
                .build();
    }

    public static final class Builder {
        private PreValidate preValidate;
        private PostValidate postValidate;

        private Builder() {
        }

        public static InsuranceProductNonLifeProducts.Builder an() {
            return new InsuranceProductNonLifeProducts.Builder();
        }

        public InsuranceProductNonLifeProducts.Builder preValidate(PreValidate preValidate) {
            this.preValidate = preValidate;
            return this;
        }

        public InsuranceProductNonLifeProducts.Builder postValidate(PostValidate postValidate) {
            this.postValidate = postValidate;
            return this;
        }


        public InsuranceProductNonLifeProducts build() {
            return new InsuranceProductNonLifeProducts(preValidate, postValidate);
        }
    }
}
