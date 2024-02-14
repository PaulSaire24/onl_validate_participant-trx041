package com.bbva.rbvd.lib.r041.pattern.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.lib.r041.business.impl.ThirdDynamicLifeBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.InsuranceProductDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsuranceProductVidaDinamico extends InsuranceProductDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceProductVidaDinamico.class);
    public InsuranceProductVidaDinamico(PreValidate preValidate, PostValidate postValidate) {
        super(preValidate, postValidate);
    }

    @Override
    public PayloadStore generateAddParticipants(ValidateParticipantDTO input, QuotationJoinCustomerInformationDTO quotationInformation, ApplicationConfigurationService applicationConfigurationService, String personType) {

        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService, quotationInformation, personType);
        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        LOGGER.info("** start - PayloadConfig {} **",payloadConfig);
        ThirdDynamicLifeBusinessImpl thirdDynamicLifeBusiness = new ThirdDynamicLifeBusinessImpl();
        AgregarTerceroBO requestRimac = thirdDynamicLifeBusiness.doDynamicLife(payloadConfig);
        LOGGER.info("** start - request Rimac {} **",requestRimac);

        //end
        AgregarTerceroBO responseRimac = this.getPostValidate().end(requestRimac,payloadConfig.getQuotationId(), ConstantsUtil.Product.DYNAMIC_LIFE.getCode(),payloadConfig.getInput().getTraceId());

        return PayloadStore.Builder.an()
                .responseRimac(responseRimac)
                .build();
    }

    public static final class Builder {
        private PreValidate preValidate;
        private PostValidate postValidate;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder preValidate(PreValidate preValidate) {
            this.preValidate = preValidate;
            return this;
        }

        public Builder postValidate(PostValidate postValidate) {
            this.postValidate = postValidate;
            return this;
        }


        public InsuranceProductVidaDinamico build() {
            return new InsuranceProductVidaDinamico(preValidate, postValidate);
        }
    }
}
