package com.bbva.rbvd.lib.r048;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;

import java.util.Map;

public interface RBVDR048 {
	AgregarTerceroBO executeAddParticipantsDynamicLife(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId);
	PEWUResponse executeGetCustomerService(String numDoc, String typeDoc);
	Map<String,Object> getDataInsuredBD(String quotationId,String productId,String planId);
	Map<String,Object>getProducAndPlanByQuotation(String quotationId);
}
