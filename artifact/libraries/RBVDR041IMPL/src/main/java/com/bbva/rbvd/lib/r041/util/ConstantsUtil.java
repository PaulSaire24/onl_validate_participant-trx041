package com.bbva.rbvd.lib.r041.util;

import java.time.ZoneId;

public class ConstantsUtil {

    public static final String ENABLED_LIFE_PRODUCTS = "life.prod.validate.participants";
    public static final String ENABLED_NON_LIFE_PRODUCTS = "non.life.prod.validate.participants";
    public static final class Zone {
        public static final ZoneId ZONE_ID_GTM = ZoneId.of("GMT");
        private Zone() {
        }
    }
    public enum Product {

        BUSINESS_LIFE("BUSINESS_LIFE","VIDA"),
        DYNAMIC_LIFE("VIDADINAMICO","841");
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



    public static final class PERSONAL_ADDRESS {
        private PERSONAL_ADDRESS() {
        }

        public static final String SIN_ESPECIFICAR = "SN";
        public static final String NO_EXIST = "NotExist";

        public static final String INTERIOR_NUMBER_ID = "DPTO.";
    }
    public static final class PERSONAL_DATA {
        private PERSONAL_DATA() {
        }

        public static final String RUC_ID = "R";
        public static final String EMAIL_VALUE = "EMAIL";
        public static final String MOBILE_VALUE = "MOBILE_NUMBER";
        public static final int MAX_CHARACTER = 1;
    }
    public static final class ADDRESS_LABEL {
        private ADDRESS_LABEL() {
        }

        public static final String DISTRICT = "DISTRICT";
        public static final String PROVINCE = "PROVINCE";
        public static final String DEPARTMENT = "DEPARTMENT";
        public static final String UBIGEO = "UBIGEO";
        public static final String EXTERIOR_NUMBER = "EXTERIOR_NUMBER";
        public static final String STREET = "STREET";
    }

    public static final class Organization {
        private Organization() {
        }
        public static final String RUC_ID = "R";
        public static final String TAG_OTROS = "OTROS";
        public static final String COUNTRY_REQUIRED = "PERU";
    }

    public static final class ContactType {
        private ContactType() {
        }
        public static final String PHONE_NUMBER = "PHONE_NUMBER";
        public static final String MOBILE_NUMBER = "MOBILE_NUMBER";
        public static final String EMAIL = "EMAIL";

    }

    public static final class RegularExpression{
        private RegularExpression() {   }
        public static final String CONTAIN_ONLY_LETTERS=".*[a-zA-Z].*";
        public static final String CONTAIN_ONLY_NUMBERS=".*[0-9].*";
        public static final String UNSPECIFIED = "SN";
    }

    public static final class Delimeter{
        public static final String VERTICAL_BAR = "|";

        private Delimeter() {
        }
    }
    public final class Number{
        public static final int UNO = 1;
        public static final int CERO = 0;
        public static final int DIEZ = 10;

        public static final int CLIENT_BANK_LENGHT =8;

        private Number() {
        }
    }

    public enum Rol{
        PAYMENT_MANAGER("PAYMENT_MANAGER",23),
        CONTRACTOR("CONTRACTOR",8),
        INSURED("INSURED",9),
        LEGAL_REPRESENTATIVE("LEGAL_REPRESENTATIVE",8),
        BENEFICIARY("BENEFICIARY",9);
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
