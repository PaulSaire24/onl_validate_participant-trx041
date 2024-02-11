package com.bbva.rbvd.lib.r041.pattern.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.lib.r041.business.impl.ThirdDynamicLifeBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateVidaDinamico extends ValidateDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateVidaDinamico.class);
    public ValidateVidaDinamico(PreValidate preValidate, PostValidate postValidate) {
        super(preValidate, postValidate);
    }

    @Override
    public PayloadStore start(ValidateParticipantDTO input, String quotationId, ApplicationConfigurationService applicationConfigurationService) {

        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService);
        LOGGER.info("** start - PayloadConfig {} **",payloadConfig);
        payloadConfig.setQuotationId(quotationId);
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


        public ValidateVidaDinamico build() {
            return new ValidateVidaDinamico(preValidate, postValidate);
        }
    }
}
