package com.bbva.rbvd.lib.r041.pattern.decorator.products;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.impl.LifeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantDataValidatorDecorator;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LifeProduct extends ParticipantDataValidatorDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifeProduct.class);
    public LifeProduct(BeforeParticipantDataValidator preValidate) {
        super(preValidate);
    }

    @Override
    public PayloadStore start(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("** start - dinamic Life product");
        PayloadConfig payloadConfig = this.getBeforeValidator().before(input,applicationConfigurationService,quotationInformation);
        LOGGER.info("** start - PayloadConfig {} **",payloadConfig);
        LifeProductBusinessImpl dynamicLifeParticipantBusiness = new LifeProductBusinessImpl(rbvdr048,applicationConfigurationService);
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
        public LifeProduct build() {
            return new LifeProduct(preValidate);
        }
    }
}
