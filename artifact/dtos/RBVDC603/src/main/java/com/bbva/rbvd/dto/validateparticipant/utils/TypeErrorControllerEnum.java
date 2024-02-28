package com.bbva.rbvd.dto.validateparticipant.utils;

public enum TypeErrorControllerEnum {
    ERROR_OBTAIN_QUOTATION_FROM_DB("En el proceso de obtener la cotización de base de datos"),
    ERROR_OBTAIN_PRODUCT_COMPANY_MODALITIES_FROM_DB("En el proceso de obtener los roles en base de datos asociados a la compañía aseguradora"),
    ERROR_PARTICIPANTS_OF_DIFFERENT_TYPE("Los participantes no son del mismo tipo"),
    ERROR_PBTQ_CLIENT_INFORMATION_SERVICE("En la invocación a la Api de clientes"),
    ERROR_PBTQ_INCOMPLETE_CLIENT_INFORMATION("Data de cliente incompleta"),
    ERROR_ROLE_NOT_REGISTRED_ON_APX_CONSOLE("No existe el rol {participantType} registrado para el cliente"),
    ERROR_KSMK_ENCRYPT_SERVICE("En la invocación a la Api de encriptado"),
    ERROR_LIST_BUSINESS_SERVICE("En la invocación a la Api de orgnización");
    private final String value;
    TypeErrorControllerEnum(String value) { this.value = value; }
    public String getValue() { return value; }
}
