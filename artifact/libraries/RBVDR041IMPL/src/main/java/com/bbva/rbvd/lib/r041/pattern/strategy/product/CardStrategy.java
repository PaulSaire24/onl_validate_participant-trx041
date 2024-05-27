package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.impl.CardProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class CardStrategy implements StrategyProductHandler {
    @Override
    public AgregarTerceroBO customRequestByProduct(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        CardProductBusinessImpl cardProductBusiness = new CardProductBusinessImpl();
        return cardProductBusiness.doCardProduct(payloadConfig, rimacRequestData);
    }
}
