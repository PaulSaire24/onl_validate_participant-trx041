package com.bbva.rbvd.lib.r048;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
public interface RBVDR048 {
	AgregarTerceroBO executeAddParticipantsDynamicLife(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId);

}
