package com.bbva.rbvd.lib.r048.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.ksmk.dto.caas.CredentialsDTO;
import com.bbva.ksmk.dto.caas.InputDTO;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.bbva.rbvd.lib.r048.impl.util.ErrorUtil.prepareRequestToHandlerError;
import static java.util.Collections.singletonMap;

public class RBVDR048Impl extends RBVDR048Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR048Impl.class);
    private static final String APP_NAME = "apx-pe";
    private static final String OAUTH_TOKEN = "";
    private static final String CRE_EXTRA_PARAMS = "user=KSMK;country=PE";
    private static final String INPUT_TEXT_SECURITY = "operation=DO;type=fpextff1;origin=ASO;endpoint=ASO;securityLevel=5";
    private static final String B64URL = "B64URL";
	@Override
	public AgregarTerceroBO executeAddParticipants(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId) {

		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService START *****");

		LOGGER.info("***** requestBody: {} :: quotationId: {}", requestBody, quotationId);
		LOGGER.info("***** productId: {} :: traceId: {}", productId, traceId);

		String jsonString = getRequestJson(requestBody);
		LOGGER.info("***** RBVDR048Impl - jsonString: {}", jsonString);

		AgregarTerceroBO output = new AgregarTerceroBO();

		RimacUrlForker rimacUrlForker = new RimacUrlForker(this.applicationConfigurationService);

        String httpMethodValue = this.applicationConfigurationService.getDefaultProperty("participant.api.rimac.method.".concat(productId),HttpMethod.PATCH.toString());
        SignatureAWS signature = this.pisdR014.executeSignatureConstruction(jsonString, httpMethodValue,
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
			ErrorRequestDTO err =  prepareRequestToHandlerError(ex);
            LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
			if(Objects.nonNull(err.getHttpCode()) && !CollectionUtils.isEmpty(err.getDetails())){
				err.setTypeErrorScope("RIMAC");
				ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
				throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
			}
            throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
		}catch (TimeoutException toex) {
            throw new BusinessException(ValidateParticipantErrors.TIMEOUT_ADD_PARTICIPANTS_RIMAC_ERROR.getAdviceCode(), false, ValidateParticipantErrors.TIMEOUT_ADD_PARTICIPANTS_RIMAC_ERROR.getMessage());
        }
	}

	private String getRequestJson(Object o) {
		return JsonHelper.getInstance().serialization(o);
	}

    @Override
    public PEWUResponse executeGetCustomerByDocType(String documentNumber, String documentType) {
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService Start *****");
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService documentNumber {} - documentType {} *****", documentNumber, documentType);
        PEWUResponse result = pbtqR002.executeSearchInHostByDocument(documentNumber,documentType);
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService  ***** Response Host: {}", result);
        if( Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty()){
            return result;
        }
        LOGGER.info("***** RBVDR041Impl - executeGetListCustomer ***** with error: {}", result.getHostMessage());
        throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage()
                        .concat(TypeErrorControllerEnum.ERROR_PBTQ_CLIENT_INFORMATION_SERVICE.getValue()));
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

    @Override
    public String executeKsmkCryptography(String customerId) {
        LOGGER.info("***** RBVDR048Impl - executeKsmkCryptographyService Start *****");
        String b64CustomerId =  Base64.getUrlEncoder().withoutPadding().encodeToString(customerId.getBytes(StandardCharsets.UTF_8));
        List<OutputDTO> output = ksmkR002.executeKSMKR002(Collections.singletonList(new InputDTO(b64CustomerId, B64URL)), OAUTH_TOKEN, INPUT_TEXT_SECURITY, new CredentialsDTO(APP_NAME, OAUTH_TOKEN, CRE_EXTRA_PARAMS));
        LOGGER.info("***** RBVDR048Impl - executeKsmkCryptographyService  ***** Response: {}", output);
        if (CollectionUtils.isEmpty(output)){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage()
                            .concat(TypeErrorControllerEnum.ERROR_KSMK_ENCRYPT_SERVICE.getValue()));
        }

        return output.get(0).getData();
    }

    @Override
    public ListBusinessesASO executeListBusiness(String encryptedCustomerId) {
        LOGGER.info("***** RBVDR048Impl - executeListBusinessService Start *****");
        ListBusinessesASO listBusinesses = rbvdR066.executeGetListBusinesses(encryptedCustomerId, null);
        if(CollectionUtils.isEmpty(listBusinesses.getData())){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage().
                            concat(TypeErrorControllerEnum.ERROR_LIST_BUSINESS_SERVICE.getValue()));
        }

        return listBusinesses;
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
