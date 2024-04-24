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
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.business.HandlerErrorBusiness;
import com.bbva.rbvd.lib.r048.impl.util.Constants;
import com.bbva.rbvd.lib.r048.impl.util.JsonHelper;
import com.bbva.rbvd.lib.r048.impl.util.RimacUrlForker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Base64;

import static java.util.Collections.singletonMap;

public class RBVDR048Impl extends RBVDR048Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR048Impl.class);

	@Override
	public AgregarTerceroBO executeAddParticipants(AgregarTerceroBO requestBody, String quotationId, String productId, String traceId,String channelId) {

		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService START *****");

		LOGGER.info("***** requestBody: {} :: quotationId: {}", requestBody, quotationId);
		LOGGER.info("***** productId: {} :: traceId: {}", productId, traceId);

		String jsonString = getRequestJson(requestBody);
		LOGGER.info("***** RBVDR048Impl - jsonString: {}", jsonString);

		AgregarTerceroBO output = null;

		RimacUrlForker rimacUrlForker = new RimacUrlForker(this.applicationConfigurationService);

        String httpMethodValue = this.applicationConfigurationService.getDefaultProperty("participant.api.rimac.method.".concat(productId),HttpMethod.PATCH.toString());
        SignatureAWS signature = this.pisdR014.executeSignatureConstruction(jsonString, httpMethodValue,
                rimacUrlForker.generateUriAddParticipants(quotationId,productId), null, traceId
        );
		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** signature: {}", signature);

		HttpEntity<String> entity = new HttpEntity<>(jsonString, createHttpHeadersAWS(signature));
		LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** entity: {}", entity);

		try {
			ResponseEntity<AgregarTerceroBO> response = this.externalApiConnector.exchange(rimacUrlForker.generateKeyAddParticipants(productId),HttpMethod.valueOf(httpMethodValue), entity,
					AgregarTerceroBO.class, singletonMap("cotizacion",quotationId));
			output = response.getBody();
			output.setErrorRimacBO(null);
			LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService ***** Response: {}", output.getPayload().getMensaje());
			LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService END *****");
		} catch (RestClientException ex) {
            LOGGER.info("***** RBVDR048Impl - executeAddParticipantsService catch {} *****", ex.getStackTrace());
            HandlerErrorBusiness handlerErrorBusiness = new HandlerErrorBusiness(this.pisdR403);
            handlerErrorBusiness.startHandlerError(channelId,ex);
        }catch (TimeoutException toex) {
            throw new BusinessException(ValidateParticipantErrors.TIMEOUT_ADD_PARTICIPANTS_RIMAC_ERROR.getAdviceCode(), false, ValidateParticipantErrors.TIMEOUT_ADD_PARTICIPANTS_RIMAC_ERROR.getMessage());
        }
        return output;
    }

    
    private String getRequestJson(Object o) {
		return JsonHelper.getInstance().serialization(o);
	}

    @Override
    public PEWUResponse executeGetCustomerByDocType(String documentNumber, String documentType) {
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService Start *****");
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService documentNumber {} - documentType {} *****", documentNumber, documentType);
        PEWUResponse result = pbtqR002.executeSearchInHostByDocument(documentType,documentNumber);
        LOGGER.info("***** RBVDR048Impl - executeGetCustomerService  ***** Response Host: {}", result);
        if( Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty() || Objects.isNull(result)){
            return result;
        }
        LOGGER.info("***** RBVDR041Impl - executeGetListCustomer ***** with error: {}", result.getHostMessage());
        throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage()
                        .concat(TypeErrorControllerEnum.ERROR_PBTQ_CLIENT_INFORMATION_SERVICE.getValue()));
    }

    @Override
    public QuotationLifeDAO executeGetDataInsuredBD(String quotationId, String productId, String planId, String documentNumber, String documentType) {
        LOGGER.info("***** RBVDR048Impl - getDataInsuredBD - START ****");
        try{
            QuotationLifeDAO dataInsured = this.pisdR040.executeGetInsuredQuotationLife(quotationId, productId, planId, documentNumber, documentType);
            LOGGER.info("***** RBVDR048Impl - getDataInsuredBD ***** result: {}", dataInsured);
            return dataInsured;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().
                    concat(TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue()));
        }
    }

    @Override
    public QuotationCustomerDAO executeGetCustomerInformationFromQuotation(String quotationId) {
        try{
            LOGGER.info("***** RBVDR048Impl - getCustomerBasicInformation START *****");
            QuotationCustomerDAO responseQueryCustomerProductInformation = pisdR040.executeFindQuotationJoinByPolicyQuotaInternalId(quotationId);
            LOGGER.info("***** RBVDR048Impl - getCustomerBasicInformation | responseQueryCustomerProductInformation {} *****",responseQueryCustomerProductInformation);
            return responseQueryCustomerProductInformation;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().
                    concat(TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue()));
        }
    }

    @Override
    public List<RolDAO> executeGetRolesByCompany(BigDecimal insuranceCompanyId) {
        try{
            LOGGER.info("***** RBVDR048Impl - executeGetRolesByCompany START *****");
            List<RolDAO> responseCompanyRoleList = pisdR040.executeListParticipantRolesByCompanyId(insuranceCompanyId);
            LOGGER.info("***** RBVDR048Impl - executeGetRolesByCompany | responseCompanyRoleList {} *****",responseCompanyRoleList);
            return responseCompanyRoleList;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false,  ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().
                    concat(TypeErrorControllerEnum.ERROR_OBTAIN_COMPANY_ROLES_FROM_DB.getValue()));
        }
    }

    @Override
    public String executeKsmkCryptography(String customerId) {
        LOGGER.info("***** RBVDR048Impl - executeKsmkCryptographyService Start *****");
        String b64CustomerId =  Base64.getUrlEncoder().withoutPadding().encodeToString(customerId.getBytes(StandardCharsets.UTF_8));
        List<OutputDTO> output = ksmkR002.executeKSMKR002(
                Collections.singletonList(new InputDTO(b64CustomerId, Constants.ConfigurationValues.B64URL)),
                Constants.ConfigurationValues.OAUTH_TOKEN, Constants.ConfigurationValues.INPUT_TEXT_SECURITY,
                new CredentialsDTO(Constants.ConfigurationValues.APP_NAME, Constants.ConfigurationValues.OAUTH_TOKEN,
                        Constants.ConfigurationValues.CRE_EXTRA_PARAMS));
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
		headers.set(Constants.Headers.AUTHORIZATION_HEADER, signature.getAuthorization());
		headers.set(Constants.Headers.X_AMZ_DATE_HEADER, signature.getxAmzDate());
		headers.set(Constants.Headers.X_API_KEY_HEADER, signature.getxApiKey());
		headers.set(Constants.Headers.TRACE_ID_HEADER, signature.getTraceId());

		LOGGER.info("createHttpHeadersAWS END *****");
		return headers;
	}
}
