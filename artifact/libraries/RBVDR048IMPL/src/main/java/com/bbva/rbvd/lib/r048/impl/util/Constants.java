package com.bbva.rbvd.lib.r048.impl.util;

public class Constants {
    public static final class Properties {
        public static final String FLAG_NOT_FOUND_ERROR_ON_DB = "flag.error.not.found.in.data.base";
        public static final String PREFIX_MESSAGE_ERROR_NOT_FOUND_ON_DB = "marca.message.error.not.found.in.data.base";
        public static final String ERROR_NOT_FOUND_MESSAGE = "error.not.found.in.data.base.message";
        public static final String RIMAC_FUNCTIONAL_ERROR_CODE = "rimac.functional.error.code";
        public static final String RIMAC_FUNCTIONAL_MAPPPING_VALUES = "functional.error.mapping.values";
        public static final String SEPARATOR_SIGN = " | ";

        public static final String GENERIC_MESSAGE_KEY = "generic.message";

        private Properties() {
        }
    }


    public static final class Headers {
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String X_AMZ_DATE_HEADER = "X-Amz-Date";
        public static final String X_API_KEY_HEADER = "x-api-key";
        public static final String TRACE_ID_HEADER = "traceId";

        private Headers() {
        }
    }

    public static final class Flag {

        public static final String ROL_NAME = "{0}";
        private Flag() {
        }
    }

    public static final class OriginError {
        public static final String RIMAC = "RIMAC";
        public static final String APX = "APX";
        public static final String HOST = "HOST";

        private OriginError() {
        }
    }

    public static final class ConfigurationValues {
        public static final String APP_NAME = "apx-pe";
        public static final String OAUTH_TOKEN = "";
        public static final String CRE_EXTRA_PARAMS = "user=KSMK;country=PE";
        public static final String INPUT_TEXT_SECURITY = "operation=DO;type=fpextff1;origin=ASO;endpoint=ASO;securityLevel=5";
        public static final String B64URL = "B64URL";

        private ConfigurationValues() {
        }
    }

    public static final String ERROR_NOT_FOUND_IN_DATA_BASE_CODE = "BBVAER200137";
    private Constants() {
    }
}
