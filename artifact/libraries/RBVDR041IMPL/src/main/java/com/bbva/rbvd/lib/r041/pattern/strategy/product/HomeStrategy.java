package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.impl.HomeProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class HomeStrategy implements StrategyProductHandler {
    @Override
    public AgregarTerceroBO customRequestByProduct(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        HomeProductBusinessImpl homeProductBusiness = new HomeProductBusinessImpl();
        return homeProductBusiness.doHomeProduct(payloadConfig, rimacRequestData);
    }
}
