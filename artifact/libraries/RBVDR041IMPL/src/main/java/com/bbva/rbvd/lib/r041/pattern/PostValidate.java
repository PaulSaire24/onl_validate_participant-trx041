package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;

public interface PostValidate {
    AgregarTerceroBO end(AgregarTerceroBO requestRimac,String quotationId,String productId,String traceId);


}
