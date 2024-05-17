package com.bbva.rbvd.lib.r048.impl.transform.map;

import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insuranceroyal.error.DetailsErrorDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.lib.r048.impl.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MessagesByRoleMap {
    private final Map<String, String> functionalErrCodes;
    private final ErrorRequestDTO rimacError;

    public MessagesByRoleMap(ErrorRequestDTO rimacError, Map<String, String> functionalErrCodes) {
        this.functionalErrCodes = functionalErrCodes;
        this.rimacError = rimacError;
    }

    public Map<String, String> mapMessagesToRolesOfPerson(List<PersonaBO> personas, String[] messageList) {
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

    public Map<String, String> mapMessagesToRolesOfCompany(List<OrganizacionBO> organizacionBO, String[] messageList) {
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
        }else if (comparerMessageLowCase.contains(Constants.Flag.ROL_NAME) && functionalErrCodes != null) {
            // Si se encuentra, devolver el mensaje correspondiente
            // Condición de igualdad con el mensaje funcional respectivo
            Optional<String> errorCodeKey = functionalErrCodes.keySet().stream().filter(comparerMessageLowCase::contains).findFirst();
            Optional<DetailsErrorDTO> errorObject = errorCodeKey.map(codeKey -> rimacError.getDetails().stream().filter(rErr ->
                    rErr.getCode().equals(functionalErrCodes.get(codeKey))).findFirst()).orElse(Optional.empty());

            if(errorObject.isPresent() && errorObject.get().getValue().contains(nroDocumento)){
                messageMapped = messageMapped.replace(Constants.Flag.ROL_NAME, rolName.equalsIgnoreCase("RESPONSABLE") ? rolName.concat(" de pago") : rolName);
                message = messageByPersonDoc != null ? messageByPersonDoc + Constants.Properties.SEPARATOR_SIGN + messageMapped.trim() : messageMapped.trim();
                rimacError.getDetails().remove(errorObject.get());
            }
        }
        return message;
    }
}