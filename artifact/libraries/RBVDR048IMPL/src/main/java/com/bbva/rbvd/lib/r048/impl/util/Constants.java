package com.bbva.rbvd.lib.r048.impl.util;

public class Constants {

    public static final class Headers {
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String X_AMZ_DATE_HEADER = "X-Amz-Date";
        public static final String X_API_KEY_HEADER = "x-api-key";
        public static final String TRACE_ID_HEADER = "traceId";
    }

    public static final class ConfigurationValues {
        public static final String APP_NAME = "apx-pe";
        public static final String OAUTH_TOKEN = "";
        public static final String CRE_EXTRA_PARAMS = "user=KSMK;country=PE";
        public static final String INPUT_TEXT_SECURITY = "operation=DO;type=fpextff1;origin=ASO;endpoint=ASO;securityLevel=5";
        public static final String B64URL = "B64URL";
    }
    public static final String QUERY_GET_DATA_INSURED_BY_QUOTATION = "PISD.GET_INSURED_DATA_LIFE";
    public static final String QUERY_GET_PRODUCT_AND_MODALITY_TYPE_BY_QUOTATION = "PISD.GET_PRODUCT_AND_MODALITY_TYPE";
    public static final String POLICY_QUOTA_INTERNAL_ID = "POLICY_QUOTA_INTERNAL_ID";
    public static final String INSURANCE_PRODUCT_ID = "INSURANCE_PRODUCT_ID";
    public static final String INSURANCE_MODALITY_TYPE = "INSURANCE_MODALITY_TYPE";

    public enum Product {

        DYNAMIC_LIFE("VIDADINAMICO","841","841007");
        private final String name;
        private final String code;
        private final String codeFromDateBase;

        Product(String name, String code,String codeFromDateBase) {
            this.name = name;
            this.code = code;
            this.codeFromDateBase = codeFromDateBase;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public String getCodeFromDateBase() {
            return codeFromDateBase;
        }
    }

    public static String getCodeFromDBByCode(String code){
        Product[] val = Product.values();
        for (Product er: val) {
            if(er.getCode().equalsIgnoreCase(code)){
                return er.getCodeFromDateBase();
            }
        }
        return null;
    }
}
