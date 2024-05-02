package com.bbva.rbvd.lib.r048.impl.util;

public class Constants {

    public static final class Headers {
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String X_AMZ_DATE_HEADER = "X-Amz-Date";
        public static final String X_API_KEY_HEADER = "x-api-key";
        public static final String TRACE_ID_HEADER = "traceId";
    }

    public static final class OriginError {
        public static final String RIMAC = "RIMAC";
        public static final String APX = "APX";
        public static final String HOST = "HOST";
    }

    public static final class ConfigurationValues {
        public static final String APP_NAME = "apx-pe";
        public static final String OAUTH_TOKEN = "";
        public static final String CRE_EXTRA_PARAMS = "user=KSMK;country=PE";
        public static final String INPUT_TEXT_SECURITY = "operation=DO;type=fpextff1;origin=ASO;endpoint=ASO;securityLevel=5";
        public static final String B64URL = "B64URL";
    }
    public static final String ERROR_NOT_FOUND_IN_DATA_BASE_CODE = "BBVAER200137";
    public static final String ERROR_NOT_FOUND_IN_DATA_BASE_MESSAGE = "Error no encontrado en base de datos";


}
