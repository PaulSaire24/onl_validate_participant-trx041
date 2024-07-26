package com.bbva.rbvd.lib.r048.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insuranceroyal.error.DetailsErrorDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.transform.map.MessagesByRoleMap;
import com.bbva.rbvd.lib.r048.impl.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HandlerErrorBusiness {

    private PISDR403 pisdR403;
    private ApplicationConfigurationService applicationConfigurationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerErrorBusiness.class);

    public HandlerErrorBusiness(PISDR403 pisdR403, ApplicationConfigurationService applicationConfigurationService) {
        this.pisdR403 = pisdR403;
        this.applicationConfigurationService = applicationConfigurationService;
    }

    public void startHandlerError(PayloadAgregarTerceroBO payload, String channelId, RestClientException ex) throws BusinessException {

        ErrorRequestDTO err =  getErrorRequestFromException(ex,Constants.OriginError.RIMAC,channelId);
        LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
        if(!CollectionUtils.isEmpty(err.getDetails())){
            LOGGER.info("** RBVDR048Impl - with reference {} **",err);
            ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
            if(Objects.nonNull(responseErr) && !StringUtils.isEmpty(responseErr.getCode()) && !StringUtils.isEmpty(responseErr.getMessage())){
                LOGGER.info("** RBVDR048Impl - Error encontrado en base de datos");
                String[] messageList = responseErr.getMessage().split("\\|");
                String errorMessage = groupMessagesByRole(payload,messageList,err);
                LOGGER.info("** RBVDR048Impl - dto error message {} **",errorMessage);
                throw new BusinessException(responseErr.getCode(), false, errorMessage);
            }else{
                propagateError(applicationConfigurationService,err);
            }
        }
        throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
    }

    private void propagateError(ApplicationConfigurationService applicationConfigurationService, ErrorRequestDTO err) {
        if(applicationConfigurationService.getProperty(Constants.Properties.FLAG_NOT_FOUND_ERROR_ON_DB).equals("true")){
            StringBuilder message = new StringBuilder();
            for (DetailsErrorDTO detail : err.getDetails()) {
                LOGGER.info("** RBVDR048Impl - Error no encontrado en base de datos");
                message.append(Constants.Properties.SEPARATOR_SIGN).append(detail.getValue());
            }
            message.delete(0, 3);
            message.insert(0, applicationConfigurationService.getProperty(Constants.Properties.PREFIX_MESSAGE_ERROR_NOT_FOUND_ON_DB));
            throw new BusinessException("BBVA12345678", false, String.valueOf(message));
        }else{
            throw new BusinessException(Constants.ERROR_NOT_FOUND_IN_DATA_BASE_CODE, false, applicationConfigurationService.getProperty(Constants.Properties.ERROR_NOT_FOUND_MESSAGE));
        }
    }

    public ErrorRequestDTO getErrorRequestFromException(RestClientException exception, String scope, String channelId) {
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
                JsonArray jsonDetailsObject = jsonErrorObject.getAsJsonArray("details");
                String errorCode = jsonErrorObject.get("code").getAsString();
                if (Objects.nonNull(jsonDetailsObject) && !StringUtils.isEmpty(errorCode)){
                    List<String> mapDetails = new Gson().fromJson(jsonDetailsObject, ArrayList.class);
                    for (int i = 0; i<mapDetails.size(); i++){

                    }

                    error = buildErrorRequest(scope,mapDetails, errorCode,channelId);
                }
            }
            LOGGER.info("HttpClientErrorException - error -> {}", error);
            return error;
        }
        LOGGER.info("*** Non null or empty error responseBody with wrong structure ***");
        return error;
    }

    private ErrorRequestDTO buildErrorRequest(String scope, Map<String,String> mapDetails, String errorCode, String channelId) {
        ErrorRequestDTO errorRequest = new ErrorRequestDTO();
        LOGGER.info("HttpClientErrorException - Details arrays: {}", mapDetails);
        List<DetailsErrorDTO> detailsListr = new ArrayList<>();
        for (Map.Entry<String, String> entry : mapDetails.entrySet()) {
            DetailsErrorDTO errorDetail = new DetailsErrorDTO();
            String codeDetails = entry.getKey();
            String message = entry.getValue();
            errorDetail.setCode(codeDetails);
            errorDetail.setValue(message);
            detailsListr.add(errorDetail);
        }
        errorRequest.setChannel(channelId);
        errorRequest.setCode(errorCode);
        errorRequest.setDetails(detailsListr);
        errorRequest.setTypeErrorScope(scope);
        return errorRequest;
    }

    private String groupMessagesByRole(PayloadAgregarTerceroBO payload, String[] messageList, ErrorRequestDTO rimacError){
        Map<String, String> groupedMessages = new HashMap<>();
        Map<String,String> functionalErrCodes = null;
        if (rimacError.getCode().contains(applicationConfigurationService.getProperty(Constants.Properties.RIMAC_FUNCTIONAL_ERROR_CODE))){
            functionalErrCodes = Arrays.stream(applicationConfigurationService.getProperty
                                    (Constants.Properties.RIMAC_FUNCTIONAL_MAPPPING_VALUES).
                            split(",")).map(kv -> kv.split("\\|"))
                    .filter(kvArray -> kvArray.length == 2)
                    .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
        }

        MessagesByRoleMap messagesByRoleMap = new MessagesByRoleMap(rimacError, functionalErrCodes);

        if(payload.getPersona() != null){
            groupedMessages = messagesByRoleMap.mapMessagesToRolesOfPerson(payload.getPersona(),messageList);
        } else if (payload.getOrganizacion() != null){
            groupedMessages = messagesByRoleMap.mapMessagesToRolesOfCompany(payload.getOrganizacion(),messageList);
        }

        StringBuilder message = new StringBuilder();
        for (Map.Entry<String, String> entry : groupedMessages.entrySet()) {
            message.append(Constants.Properties.SEPARATOR_SIGN).append(entry.getValue());
        }

        message = message.delete(0, 3);
        return message.toString();
    }
}