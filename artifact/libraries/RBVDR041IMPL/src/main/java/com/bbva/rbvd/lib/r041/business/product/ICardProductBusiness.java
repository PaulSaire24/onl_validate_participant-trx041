package com.bbva.rbvd.lib.r041.business.product;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public interface ICardProductBusiness {
    AgregarTerceroBO doCardProduct(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData);

}
