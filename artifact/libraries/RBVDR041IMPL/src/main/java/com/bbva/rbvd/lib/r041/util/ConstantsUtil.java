package com.bbva.rbvd.lib.r041.util;

public class ConstantsUtil {

    public enum Product {

        DYNAMIC_LIFE("VIDADINAMICO","841"),
        EASY_YES("VIDAXXXXXX","840");
        private final String name;
        private final String code;

        Product(String name, String code) {
            this.name = name;
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }
    }

    public static final class Crypto {
        public static final String APP_NAME = "apx-pe";
        public static final String OAUTH_TOKEN = "";
        public static final String CRE_EXTRA_PARAMS = "user=KSMK;country=PE";
        public static final String INPUT_TEXT_SECURITY = "operation=DO;type=customerId;origin=ASO;endpoint=ASO;securityLevel=5";
        public static final String B64URL = "B64URL";

        private Crypto() {
        }
    }

    public static final class RegularExpression{
        private RegularExpression() {   }
        public static final String CONTAIN_ONLY_LETTERS=".*[a-zA-Z].*";
        public static final String CONTAIN_ONLY_NUMBERS=".*[0-9].*";
        public static final String DELIMITER = "|";
    }
    public static final class Numero{
        private Numero() {   }
        public static final Integer CERO = 0;
        public static final int CLIENT_BANK_LENGHT =8;
    }

    public enum Rol{
        PAYMENT_MANAGER("PAYMENT_MANAGER",23),
        CONTRACTOR("CONTRACTOR",8),
        INSURED("INSURED",9);
        private final String name;
        private final int value;

        Rol(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    public static int getValueByName(String name){
        Rol[] val = Rol.values();
        for (Rol er: val) {
            if(er.getName().equalsIgnoreCase(name)){
                return er.getValue();
            }
        }
        return 0;
    }
}