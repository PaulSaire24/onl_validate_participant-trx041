package com.bbva.rbvd.lib.r048.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insuranceroyal.error.DetailsErrorDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.*;

public class HandlerErrorBusiness {

    private PISDR403 pisdR403;
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerErrorBusiness.class);

    public HandlerErrorBusiness(PISDR403 pisdR403) {
        this.pisdR403 = pisdR403;
    }

    public void startHandlerError(String productId, RestClientException ex) throws BusinessException{

        ErrorRequestDTO err =  getErrorRequestFromException(ex,Constants.OriginError.RIMAC,Constants.getCodeFromDBByCode(productId));
        LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
        if(!CollectionUtils.isEmpty(err.getDetails())){
            LOGGER.info("** RBVDR048Impl - with reference (product) {} **",err);
            ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
            if(Objects.nonNull(responseErr) && !StringUtils.isEmpty(responseErr.getCode()) && !StringUtils.isEmpty(responseErr.getMessage())){
                LOGGER.info("** RBVDR048Impl - Error encontrado en base de datos");
                throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
            }
            throw new BusinessException(Constants.ERROR_NOT_FOUND_IN_DATA_BASE_CODE, false, Constants.ERROR_NOT_FOUND_IN_DATA_BASE_MESSAGE);
        }
        throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
    }

    public ErrorRequestDTO getErrorRequestFromException(RestClientException exception, String scope, String productCode) {
        ErrorRequestDTO error = new ErrorRequestDTO();
        if(exception instanceof HttpClientErrorException) {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) exception;
            String requestBody = httpClientErrorException.getResponseBodyAsString();
            LOGGER.info("HttpClientErrorException - Response body: {}", requestBody);

            if(org.apache.commons.lang3.StringUtils.isEmpty(requestBody) || requestBody.contains("html")) {
                LOGGER.info("*** Null or empty error responseBody ***");
                return error;
            }

            JsonObject jsonResponseObject = new JsonParser().parse(requestBody).getAsJsonObject();
            JsonObject jsonErrorObject = jsonResponseObject.getAsJsonObject("error");
            if (Objects.nonNull(jsonErrorObject)){
                JsonObject jsonDetailsObject = jsonErrorObject.getAsJsonObject("details");
                if (Objects.nonNull(jsonDetailsObject)){
                    Map<String, String> mapDetails = new Gson().fromJson(jsonDetailsObject, HashMap.class);
                    error = buildErrorRequest(scope, productCode, mapDetails);
                }
            }
            LOGGER.info("HttpClientErrorException - error -> {}", error);
            return error;
        }
        LOGGER.info("*** Non null or empty error responseBody with wrong structure ***");
        return error;
    }

    private ErrorRequestDTO buildErrorRequest(String scope, String productCode, Map<String,String> mapDetails) {
        ErrorRequestDTO errorRequest = new ErrorRequestDTO();
        LOGGER.info("HttpClientErrorException - Details arrays: {}", mapDetails);
        List<DetailsErrorDTO> detailsListr = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapDetails.entrySet()) {
            DetailsErrorDTO errorDetail = new DetailsErrorDTO();
            String code = entry.getKey();
            String message = entry.getValue();
            errorDetail.setCode(code);
            errorDetail.setValue(message);
            detailsListr.add(errorDetail);
        }
        errorRequest.setDetails(detailsListr);
        errorRequest.setTypeErrorScope(scope);
        errorRequest.setReference(productCode);
        return errorRequest;
    }
}
