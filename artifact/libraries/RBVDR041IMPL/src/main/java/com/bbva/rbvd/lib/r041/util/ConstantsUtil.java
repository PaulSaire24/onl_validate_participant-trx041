package com.bbva.rbvd.lib.r041.util;

import java.time.ZoneId;

public class ConstantsUtil {
    public static final class Zone {
        public static final ZoneId ZONE_ID_GTM = ZoneId.of("GMT");
        private Zone() {
        }
    }
    public enum Product {

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


    public static final class RegularExpression{
        private RegularExpression() {   }
        public static final String CONTAIN_ONLY_LETTERS=".*[a-zA-Z].*";
        public static final String CONTAIN_ONLY_NUMBERS=".*[0-9].*";
        public static final String UNSPECIFIED = "N/A";
    }

    public static final class Delimeter{
        public static final String VERTICAL_BAR = "|";

    }
    public final class Number{
        public static final int UNO = 1;
        public static final int CERO = 0;
        public static final int DIEZ = 10;

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
