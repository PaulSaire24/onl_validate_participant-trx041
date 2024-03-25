package com.bbva.rbvd.lib.r041.pattern.decorator.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.impl.DynamicLifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantDataValidatorDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicLifeProduct extends ParticipantDataValidatorDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicLifeProduct.class);
    public DynamicLifeProduct(BeforeParticipantDataValidator preValidate) {
        super(preValidate);
    }

    @Override
    public PayloadStore start(InputParticipantsDTO input, QuotationCustomerDTO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("** start - dinamic Life product");
        PayloadConfig payloadConfig = this.getPreValidate().before(input,applicationConfigurationService);
        payloadConfig.setQuotationId(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
        LOGGER.info("** start - PayloadConfig {} **",payloadConfig);
        DynamicLifeProductBusinessImpl dynamicLifeParticipantBusiness = new DynamicLifeProductBusinessImpl(rbvdr048);
        AgregarTerceroBO responseRimac = dynamicLifeParticipantBusiness.doDynamicLife(payloadConfig);
        LOGGER.info("** start - response Rimac {} **",responseRimac);


        return PayloadStore.Builder.an()
                .responseRimac(responseRimac)
                .build();
    }

    public static final class Builder {
        private BeforeParticipantDataValidator preValidate;

        private Builder() {
        }
        public static Builder an() {
            return new Builder();
        }

        public Builder preValidate(BeforeParticipantDataValidator preValidate) {
            this.preValidate = preValidate;
            return this;
        }
        public DynamicLifeProduct build() {
            return new DynamicLifeProduct(preValidate);
        }
    }
}