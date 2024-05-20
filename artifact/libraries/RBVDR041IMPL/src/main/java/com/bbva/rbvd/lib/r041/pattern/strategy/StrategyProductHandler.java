package com.bbva.rbvd.lib.r041.pattern.strategy;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public interface StrategyProductHandler {

    AgregarTerceroBO prepareCompanyRequest(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData);

}
