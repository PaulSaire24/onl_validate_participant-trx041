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
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

public class ValidateVidaDinamico extends ValidateDecorator {
     private PISDR352 pisdr352;

    public ValidateVidaDinamico(PreValidate preValidate, PostValidate postValidate, PISDR352 pisdr352) {
        super(preValidate, postValidate);
        this.pisdr352 = pisdr352;
    }

    @Override
    public AgregarTerceroBO start(ValidateParticipantDTO input, String quotationId, ApplicationConfigurationService applicationConfigurationService) {

        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService);
        payloadConfig.setQuotationId(quotationId);
        ThirdDynamicLifeBusinessImpl thirdDynamicLifeBusiness = new ThirdDynamicLifeBusinessImpl();
        AgregarTerceroBO requestRimac = thirdDynamicLifeBusiness.doDynamicLife(payloadConfig);

        return this.getPostValidate().end(requestRimac,payloadConfig.getQuotationId(), ConstantsUtil.Product.DYNAMIC_LIFE.getCode(),payloadConfig.getInput().getTraceId());
    }

    public static final class Builder {
        private PreValidate preValidate;
        private PostValidate postValidate;
        private PISDR352 pisdr352;

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

        public Builder pisdr352(PISDR352 pisdr352) {
            this.pisdr352 = pisdr352;
            return this;
        }

        public ValidateVidaDinamico build() {
            return new ValidateVidaDinamico(preValidate, postValidate, pisdr352);
        }
    }
}
