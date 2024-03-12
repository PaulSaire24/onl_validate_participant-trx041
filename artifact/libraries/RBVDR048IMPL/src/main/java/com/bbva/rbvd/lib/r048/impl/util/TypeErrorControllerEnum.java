package com.bbva.rbvd.lib.r048.impl.util;

public enum TypeErrorControllerEnum {
    ERROR_PBTQ_CLIENT_INFORMATION_SERVICE("En la invocación a la Api de clientes"),
    ERROR_OBTAIN_QUOTATION_FROM_DB("En el proceso de obtener la cotización de base de datos");
    private final String value;
    TypeErrorControllerEnum(String value) { this.value = value; }
    public String getValue() { return value; }
}
