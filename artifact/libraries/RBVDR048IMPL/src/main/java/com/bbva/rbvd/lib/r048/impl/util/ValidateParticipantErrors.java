package com.bbva.rbvd.lib.r048.impl.util;

public enum ValidateParticipantErrors {
    ERROR_INTERNAL_SERVICE_INVOKATION("RBVD10094947", false, "Error en la invocación de servicio BBVA.");

    private final String adviceCode;
    private final boolean rollback;
    private final String message;

    public String getAdviceCode() { return adviceCode; }
    public boolean isRollback() { return rollback; }
    public String getMessage() { return message; }

    ValidateParticipantErrors(String adviceCode, boolean rollback, String message) {
        this.adviceCode = adviceCode;
        this.rollback = rollback;
        this.message = message;
    }
}
