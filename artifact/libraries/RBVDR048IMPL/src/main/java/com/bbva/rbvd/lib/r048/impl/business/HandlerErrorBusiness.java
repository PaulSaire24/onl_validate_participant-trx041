package com.bbva.rbvd.lib.r048.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
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

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class HandlerErrorBusiness {

    private PISDR403 pisdR403;
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerErrorBusiness.class);

    public HandlerErrorBusiness(PISDR403 pisdR403) {
        this.pisdR403 = pisdR403;
    }

    public void startHandlerError(PayloadAgregarTerceroBO payload, String channelId, RestClientException ex, ApplicationConfigurationService applicationConfigurationService) throws BusinessException {

        ErrorRequestDTO err =  getErrorRequestFromException(ex,Constants.OriginError.RIMAC,channelId);
        LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
        if(!CollectionUtils.isEmpty(err.getDetails())){
            LOGGER.info("** RBVDR048Impl - with reference {} **",err);
            ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
                if(Objects.nonNull(responseErr) && !StringUtils.isEmpty(responseErr.getCode()) && !StringUtils.isEmpty(responseErr.getMessage())){
                LOGGER.info("** RBVDR048Impl - Error encontrado en base de datos");
                groupMessagesByRole(payload,responseErr);
                    System.out.println(responseErr.getMessage());
                throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
            }else{
                    propagateError(applicationConfigurationService,err);
                }
        }
        throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
    }

    private void propagateError(ApplicationConfigurationService applicationConfigurationService, ErrorRequestDTO err) {
        if(applicationConfigurationService.getProperty("flag.error.not.found.in.data.base").equals("true")){
            StringBuilder message = new StringBuilder();
            for (DetailsErrorDTO detail : err.getDetails()) {
                LOGGER.info("** RBVDR048Impl - Error no encontrado en base de datos");
                message.append(" | ").append(detail.getValue());
            }
            message.delete(0, 3);
            message.insert(0, applicationConfigurationService.getProperty("marca.message.error.not.found.in.data.base"));
            throw new BusinessException("BBVA12345678", false, String.valueOf(message));
        }else{
            throw new BusinessException(Constants.ERROR_NOT_FOUND_IN_DATA_BASE_CODE, false, applicationConfigurationService.getProperty("error.not.found.in.data.base.message"));
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

    private void groupMessagesByRole(PayloadAgregarTerceroBO payload,ErrorResponseDTO errorResponse) {
        Map<String, String> groupedMessages = new HashMap<>();
        String[] messageList = errorResponse.getMessage().toLowerCase().split("\\|");
        if(messageList.length <= 1){
            return;
        }
        if(payload.getPersona() != null){
            mapMessagesToRolesOfperson(payload.getPersona(), groupedMessages, messageList);
        } else if (payload.getOrganizacion() != null){
            mapMessagesToRolesOfCompany(payload.getOrganizacion(), groupedMessages, messageList);
        }

        StringBuilder message = new StringBuilder();
            for (Map.Entry<String, String> entry : groupedMessages.entrySet()) {
                message.append(" | ").append(entry.getValue());
            }
            message.delete(0, 3);
            errorResponse.setMessage(message.toString());
    }

    private void mapMessagesToRolesOfperson(List<PersonaBO> personas, Map<String, String> groupedMessages, String[] messageList) {
        for (PersonaBO persona : personas) {
            // Obtener el número de documento de la persona
            String nroDocumento = persona.getNroDocumento();
            // Obtener el rol de la persona
            String rolName = persona.getRolName();

            // Verificar si tiene el numero de documento
            if(!groupedMessages.containsKey(nroDocumento)){
                // Verificar si el mensaje contiene el rol actual
                for (String part : messageList) {
                    // Verificar si la parte actual contiene el rol buscado
                    assignMessageForRole(part, rolName, groupedMessages, nroDocumento);
                }
            }
        }
    }

    private void mapMessagesToRolesOfCompany(List<OrganizacionBO> organizacionBO, Map<String, String> groupedMessages, String[] messageList) {
        for (OrganizacionBO organizacion : organizacionBO) {
            // Obtener el número de documento de la persona
            String nroDocumento = organizacion.getNroDocumento();
            // Obtener el rol de la persona
            String rolName = organizacion.getRolName();

            // Verificar si tiene el numero de documento
            if(!groupedMessages.containsKey(nroDocumento)){
                // Verificar si el mensaje contiene el rol actual
                for (String part : messageList) {
                    // Verificar si la parte actual contiene el rol buscado
                    assignMessageForRole(part, rolName, groupedMessages, nroDocumento);
                }
            }
        }
    }

    private void assignMessageForRole(String part, String rolName, Map<String, String> groupedMessages, String nroDocumento) {
        if (part.contains(rolName)) {
            // Si se encuentra, devolver el mensaje correspondiente
            String message = groupedMessages.get(nroDocumento)!=null? groupedMessages.get(nroDocumento) + " | " + part.trim() : part.trim();
            groupedMessages.put(nroDocumento, message);
        }
    }
}
