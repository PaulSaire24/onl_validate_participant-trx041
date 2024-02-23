package com.bbva.rbvd.lib.r048;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
public interface RBVDR048 {
	AgregarTerceroBO executeAddParticipants(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId);
	PEWUResponse executeGetCustomerByDocType(String documentNumber, String documentType);
	String executeKsmkCryptography(String customerId);
	ListBusinessesASO executeListBusiness(String encryptedCustomerId);
}
