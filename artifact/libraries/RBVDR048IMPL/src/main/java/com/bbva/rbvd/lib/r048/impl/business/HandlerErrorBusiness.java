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
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HandlerErrorBusiness {

    private PISDR403 pisdR403;
    private ApplicationConfigurationService applicationConfigurationService;
    private Map<String,String> functionalErrCodes;
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

    private String groupMessagesByRole(PayloadAgregarTerceroBO payload, String[] messageList, ErrorRequestDTO rimacError){
        Map<String, String> groupedMessages = new HashMap<>();

        if(payload.getPersona() != null){
            groupedMessages = mapMessagesToRolesOfPerson(payload.getPersona(),messageList, rimacError);
        } else if (payload.getOrganizacion() != null){
            groupedMessages = mapMessagesToRolesOfCompany(payload.getOrganizacion(),messageList, rimacError);
        }

        StringBuilder message = new StringBuilder();
            for (Map.Entry<String, String> entry : groupedMessages.entrySet()) {
                message.append(Constants.Properties.SEPARATOR_SIGN).append(entry.getValue());
            }

            message = message.delete(0, 3);
            return message.toString();
    }

    private Map<String, String> mapMessagesToRolesOfPerson(List<PersonaBO> personas, String[] messageList, ErrorRequestDTO rimacError) {
        Map<String, String> groupedMessages = new HashMap<>();
        for (PersonaBO persona : personas) {
            // Obtener el número de documento de la persona
            String nroDocumento = persona.getNroDocumento();
            // Obtener el rol de la persona
            String rolName = persona.getRolName();

            // Verificar si tiene el numero de documento //
            if(!groupedMessages.containsKey(nroDocumento)){
                // Verificar si el mensaje contiene el rol actual
                for (String messageMapped : messageList) {
                    // Verificar si la parte actual contiene el rol buscado
                    String outputMessage = assignMessageForRole(messageMapped, rolName, groupedMessages.get(nroDocumento), nroDocumento, rimacError);
                    if(!outputMessage.isEmpty()){
                        groupedMessages.put(nroDocumento, outputMessage);
                    }
                }
            }
        }
        return groupedMessages;
    }

    private Map<String, String> mapMessagesToRolesOfCompany(List<OrganizacionBO> organizacionBO, String[] messageList, ErrorRequestDTO rimacError) {
        Map<String, String> groupedMessages = new HashMap<>();
        for (OrganizacionBO organizacion : organizacionBO) {
            // Obtener el número de documento de la persona
            String nroDocumento = organizacion.getNroDocumento();
            // Obtener el rol de la persona
            String rolName = organizacion.getRolName();

            // Verificar si tiene el numero de documento
            if(!groupedMessages.containsKey(nroDocumento)){
                // Verificar si el mensaje contiene el rol actual
                for (String messageMapped : messageList) {
                    // Verificar si la parte actual contiene el rol buscado
                    String outputMessage = assignMessageForRole(messageMapped, rolName, groupedMessages.get(nroDocumento), nroDocumento, rimacError);
                    if(!outputMessage.isEmpty()){
                            groupedMessages.put(nroDocumento, outputMessage);
                    }
                }
            }
        }
        return groupedMessages;
    }

    private String assignMessageForRole(String messageMapped, String rolName, String messageByPersonDoc, String nroDocumento, ErrorRequestDTO rimacError) {
        String message = "";
        String comparerMessageLowCase = messageMapped.toLowerCase();
        if (comparerMessageLowCase.contains(rolName)) {
            // Si se encuentra, devolver el mensaje correspondiente
            message = messageByPersonDoc!=null? messageByPersonDoc + Constants.Properties.SEPARATOR_SIGN + messageMapped.trim() : messageMapped.trim();
        }else if (comparerMessageLowCase.contains(Constants.Flag.ROL_NAME)){
            // Si se encuentra, devolver el mensaje correspondiente
            // Condición de igualdad con el mensaje funcional respectivo
            if(functionalErrCodes == null) {
                functionalErrCodes = Arrays.stream(applicationConfigurationService.getProperty
                                        (Constants.Properties.RIMAC_FUNCTIONAL_MAPPPING_VALUES).
                                split(",")).map(kv -> kv.split("\\|"))
                        .filter(kvArray -> kvArray.length == 2)
                        .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
            }
            Optional<String> errorCodeKey = functionalErrCodes.keySet().stream().filter(comparerMessageLowCase::contains).findFirst();
            Optional<DetailsErrorDTO> errorObject = errorCodeKey.isPresent() ? rimacError.getDetails().stream().filter(rErr -> rErr.getCode().equals(functionalErrCodes.get(errorCodeKey.get()))).findFirst() : Optional.empty();

            if(errorObject.isPresent()){
                if (errorObject.get().getValue().contains(nroDocumento)){
                    messageMapped = messageMapped.replace(Constants.Flag.ROL_NAME, rolName.equalsIgnoreCase("RESPONSABLE") ? rolName.concat(" de pago") : rolName);
                    message = messageByPersonDoc != null ? messageByPersonDoc + Constants.Properties.SEPARATOR_SIGN + messageMapped.trim() : messageMapped.trim();
                    rimacError.getDetails().remove(errorObject.get());
                }
            }
        }
        return message;
    }
}
