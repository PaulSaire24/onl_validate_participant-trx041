package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.impl.GeneralProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class GeneralProductStrategy implements StrategyProductHandler {

    @Override
    public AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        GeneralProductBusinessImpl crossProductBusiness = new GeneralProductBusinessImpl();
        crossProductBusiness.createRequestByCompany(payloadConfig);
        return rimacRequestData;
    }
}
