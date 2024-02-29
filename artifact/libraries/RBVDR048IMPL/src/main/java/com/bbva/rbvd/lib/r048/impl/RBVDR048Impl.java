package com.bbva.rbvd.lib.r048.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.util.Constans;
import com.bbva.rbvd.lib.r048.impl.util.JsonHelper;
import com.bbva.rbvd.lib.r048.impl.util.RimacUrlForker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.bbva.rbvd.lib.r048.impl.util.ErrorUtil.getErrorCode;
import static java.util.Collections.singletonMap;

public class RBVDR048Impl extends RBVDR048Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR048Impl.class);

	@Override
	public AgregarTerceroBO executeAddParticipantsDynamicLife(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId) {

		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService START *****");

		LOGGER.info("***** requestBody: {} :: quotationId: {}", requestBody, quotationId);
		LOGGER.info("***** productId: {} :: traceId: {}", productId, traceId);

		String jsonString = getRequestJson(requestBody);
		LOGGER.info("***** RBVDR048Impl - jsonString: {}", jsonString);

		AgregarTerceroBO output = new AgregarTerceroBO();

		RimacUrlForker rimacUrlForker = new RimacUrlForker(this.applicationConfigurationService);

		SignatureAWS signature = this.pisdR014.executeSignatureConstruction(jsonString, HttpMethod.PATCH.toString(),
				rimacUrlForker.generateUriAddParticipants(quotationId,productId), null, traceId
		);
		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** signature: {}", signature);

		HttpEntity<String> entity = new HttpEntity<>(jsonString, createHttpHeadersAWS(signature));
		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** entity: {}", entity);

		try {
			ResponseEntity<AgregarTerceroBO> response = this.externalApiConnector.exchange(rimacUrlForker.generateKeyAddParticipants(productId),HttpMethod.PATCH, entity,
					AgregarTerceroBO.class, singletonMap("cotizacion",quotationId));
			output = response.getBody();
			output.setErrorRimacBO(null);
			LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** Response: {}", output.getPayload().getMensaje());
			LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService END *****");
			return output;
		} catch (RestClientException ex) {
			ErrorRequestDTO err =  getErrorCode(ex);
			if(Objects.nonNull(err.getHttpCode()) && !CollectionUtils.isEmpty(err.getDetails())){
				err.setTypeErrorScope("RIMAC");
				ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
				throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
			}
			throw new BusinessException(Constans.COD_ERROR_NOT_FOUND, false, Constans.NON_EXISTENT_MESSAGE);
		}
	}

	@Override
	public PEWUResponse executeGetCustomerService(String numDoc, String typeDoc) {
		LOGGER.info("***** RBVDR048Impl - executeGetCustomerService Start *****");
		LOGGER.info("***** RBVDR048Impl - executeGetCustomerService numDoc {} *****", numDoc);
		LOGGER.info("***** RBVDR048Impl - executeGetCustomerService typeDoc {} *****", typeDoc);
		PEWUResponse result = this.pbtqR002.executeSearchInHostByDocument(typeDoc,numDoc);
		LOGGER.info("***** RBVDR048Impl - executeGetCustomerService  ***** Response Host: {}", result);
		if( Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty()){
			return result;
		}
		LOGGER.info("***** RBVDR048Impl - executeGetListCustomer ***** with error: {}", result.getHostMessage());
		throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_PBTQ_CLIENT_INFORMATION_SERVICE.getValue());
	}

	@Override
	public Map<String, Object> executeGetDataInsuredBD(String quotationId, String productId, String planId) {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put(Constans.POLICY_QUOTA_INTERNAL_ID,quotationId);
		arguments.put(Constans.INSURANCE_PRODUCT_ID,productId);
		arguments.put(Constans.INSURANCE_MODALITY_TYPE,planId);
		LOGGER.info("***** RBVDR048Impl - getDataInsuredBD ***** arguments: {}", arguments);
		Map<String, Object> dataInsured = this.pisdR350.executeGetASingleRow(Constans.QUERY_GET_DATA_INSURED_BY_QUOTATION,arguments);
		LOGGER.info("***** RBVDR048Impl - getDataInsuredBD ***** result: {}", dataInsured);
		return dataInsured;
	}

	@Override
	public Map<String, Object> executeGetProducAndPlanByQuotation(String quotationId) {
		Map<String, Object> arguments = new HashMap<>();
		arguments.put(Constans.POLICY_QUOTA_INTERNAL_ID,quotationId);
		LOGGER.info("***** RBVDR048Impl - getProducAndPlanByQuotation ***** arguments: {}", arguments);
		Map<String, Object> result = this.pisdR350.executeGetASingleRow(Constans.QUERY_GET_PRODUCT_AND_MODALITY_TYPE_BY_QUOTATION,arguments);
		LOGGER.info("***** RBVDR048Impl - getDataInsuredBD ***** result: {}", result);
		return result;
	}

	private String getRequestJson(Object o) {
		return JsonHelper.getInstance().serialization(o);
	}


	private HttpHeaders createHttpHeadersAWS(SignatureAWS signature) {
		LOGGER.info("createHttpHeadersAWS START {} *****", signature);
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application","json", StandardCharsets.UTF_8);
		headers.setContentType(mediaType);
		headers.set(Constans.Headers.AUTHORIZATION_HEADER, signature.getAuthorization());
		headers.set(Constans.Headers.X_AMZ_DATE_HEADER, signature.getxAmzDate());
		headers.set(Constans.Headers.X_API_KEY_HEADER, signature.getxApiKey());
		headers.set(Constans.Headers.TRACE_ID_HEADER, signature.getTraceId());

		LOGGER.info("createHttpHeadersAWS END *****");
		return headers;
	}
}
