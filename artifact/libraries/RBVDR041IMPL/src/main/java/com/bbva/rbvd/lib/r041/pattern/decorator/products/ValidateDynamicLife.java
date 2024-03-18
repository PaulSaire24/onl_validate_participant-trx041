package com.bbva.rbvd.lib.r041.pattern.decorator.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.impl.ThirdDynamicLifeBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.decorator.PreParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateDynamicLife extends ValidateDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateDynamicLife.class);
    public ValidateDynamicLife(PreParticipantValidations preValidate) {
        super(preValidate);
    }

    @Override
    public PayloadStore start(InputParticipantsDTO input, QuotationJoinCustomerInformationDTO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("** start - dinamic Life product");
        PayloadConfig payloadConfig = this.getPreValidate().getConfig(input,applicationConfigurationService);
        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        LOGGER.info("** start - PayloadConfig {} **",payloadConfig);
        ThirdDynamicLifeBusinessImpl thirdDynamicLifeBusiness = new ThirdDynamicLifeBusinessImpl(rbvdr048);
        AgregarTerceroBO responseRimac = thirdDynamicLifeBusiness.doDynamicLife(payloadConfig);
        LOGGER.info("** start - response Rimac {} **",responseRimac);


        return PayloadStore.Builder.an()
                .responseRimac(responseRimac)
                .build();
    }

    public static final class Builder {
        private PreParticipantValidations preValidate;

        private Builder() {
        }
        public static Builder an() {
            return new Builder();
        }

        public Builder preValidate(PreParticipantValidations preValidate) {
            this.preValidate = preValidate;
            return this;
        }
        public ValidateDynamicLife build() {
            return new ValidateDynamicLife(preValidate);
        }
    }
}
