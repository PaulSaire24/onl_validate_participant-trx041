package com.bbva.rbvd.lib.r041.pattern.strategy.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.impl.VehicleProductBusinessImpl;
import com.bbva.rbvd.lib.r041.pattern.strategy.StrategyProductHandler;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public class VehicleStrategy implements StrategyProductHandler {
    @Override
    public AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        VehicleProductBusinessImpl vehicleProductBusiness = new VehicleProductBusinessImpl();
        return vehicleProductBusiness.doVehicleProduct(payloadConfig, rimacRequestData);
    }
}
