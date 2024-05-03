package com.bbva.rbvd.lib.r041.business;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public interface ICrossProductBusiness {
    AgregarTerceroBO createRequestByCompany(PayloadConfig payloadConfig);
}
