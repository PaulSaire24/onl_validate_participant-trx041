package com.bbva.rbvd.lib.r048.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insuranceroyal.error.DetailsErrorDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.util.Constants;
import com.google.gson.Gson;
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

    public void startHandlerError(List<PersonaBO> personas,String channelId, RestClientException ex) throws BusinessException{

        ErrorRequestDTO err =  getErrorRequestFromException(ex,Constants.OriginError.RIMAC,channelId);
        LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
        if(!CollectionUtils.isEmpty(err.getDetails())){
            LOGGER.info("** RBVDR048Impl - with reference {} **",err);
            ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
            //groupMessagesByRole(personas,responseErr);
            if(Objects.nonNull(responseErr) && !StringUtils.isEmpty(responseErr.getCode()) && !StringUtils.isEmpty(responseErr.getMessage())){
                LOGGER.info("** RBVDR048Impl - Error encontrado en base de datos");
                throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
            }
            throw new BusinessException(Constants.ERROR_NOT_FOUND_IN_DATA_BASE_CODE, false, Constants.ERROR_NOT_FOUND_IN_DATA_BASE_MESSAGE);
        }
        throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
    }

    private ErrorRequestDTO getErrorRequestFromException(RestClientException exception, String scope, String channelId) {
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
                String errorCode = jsonErrorObject.get("code").getAsString();
                if (Objects.nonNull(jsonDetailsObject) && !StringUtils.isEmpty(errorCode)){
                    Map<String, String> mapDetails = new Gson().fromJson(jsonDetailsObject, HashMap.class);
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

    private void groupMessagesByRole(List<PersonaBO> personas,ErrorResponseDTO errorResponse) {
        List<String> listMessage = Arrays.asList(errorResponse.getMessage().toLowerCase().split("\\|"));
        List<String> messagesPayment = new ArrayList<>();
        List<String> messagesContractor = new ArrayList<>();
        List<String> messagesInsured = new ArrayList<>();

        for (String message : listMessage) {
            if (message.contains("responsable")) {
                messagesPayment.add(message.trim());
            } else if (message.contains("contratante")) {
                messagesContractor.add(message.trim());
            } else if (message.contains("asegurado")) {
                messagesInsured.add(message.trim());
            }
        }
        PersonaBO personManager = getPersonaBO(personas, 23);
        PersonaBO personContractor = getPersonaBO(personas, 8);
        PersonaBO personInsured = getPersonaBO(personas, 9);
        StringBuilder messageGeneral = new StringBuilder();
        if(personManager.getNroDocumento().equals(personContractor.getNroDocumento()) && personManager.getNroDocumento().equals(personInsured.getNroDocumento())){
            getMessageByRol(messagesPayment, messageGeneral);
            prepareMessageGeneral(errorResponse, messageGeneral);

        } else if (personManager.getNroDocumento().equals(personContractor.getNroDocumento()) && !personManager.getNroDocumento().equals(personInsured.getNroDocumento())) {
            getMessageByRol(messagesPayment, messageGeneral);
            getMessageByRol(messagesInsured, messageGeneral);
            prepareMessageGeneral(errorResponse, messageGeneral);
        } else if (personContractor.getNroDocumento().equals(personInsured.getNroDocumento()) && !personContractor.getNroDocumento().equals(personManager.getNroDocumento())) {
            getMessageByRol(messagesPayment, messageGeneral);
            getMessageByRol(messagesContractor, messageGeneral);
            prepareMessageGeneral(errorResponse, messageGeneral);
        }

    }

    private void prepareMessageGeneral(ErrorResponseDTO errorResponse, StringBuilder messageGeneral) {
        if(messageGeneral.length() > 0){
            messageGeneral.delete(0, 3);
            errorResponse.setMessage(messageGeneral.toString());
        }
    }
    private void getMessageByRol(List<String> messages, StringBuilder messageGeneral) {
        if(!CollectionUtils.isEmpty(messages)){
            for (String message : messages) {
                messageGeneral.append(" | ").append(message);
            }
        }
    }
    private PersonaBO getPersonaBO(List<PersonaBO> personas, int x) {
        return personas.stream().filter(persona -> persona.getRol() == x).findFirst().orElse(null);
    }

}
