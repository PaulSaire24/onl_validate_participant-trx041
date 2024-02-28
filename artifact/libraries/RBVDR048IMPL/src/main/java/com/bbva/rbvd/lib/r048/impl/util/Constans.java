package com.bbva.rbvd.lib.r048.impl.util;

public class Constans {

    public static final class Headers {
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String X_AMZ_DATE_HEADER = "X-Amz-Date";
        public static final String X_API_KEY_HEADER = "x-api-key";
        public static final String TRACE_ID_HEADER = "traceId";

        private Headers() {
        }
    }

    public static final String NON_EXISTENT_MESSAGE = "No se encontró mensaje del servidor";
    public static final String TIMEOUT_ERROR_MESSAGE = "Actualmente, el servicio validar participantes de Rimac no está disponible debido a un tiempo de espera en la conexión. Te recomendamos intentar acceder a este servicio en unos minutos, gracias.";
    public static final String COD_ERROR_NOT_FOUND = "RBVD00000136";
    public static final String TIMEOUT_ERROR_CODE = "RBVD01020044";

    private Constans() {
    }
}
