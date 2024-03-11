package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;

public interface PostParticipantValidations {
    AgregarTerceroBO end(AgregarTerceroBO requestRimac, String quotationId, String productId, String traceId);


}
