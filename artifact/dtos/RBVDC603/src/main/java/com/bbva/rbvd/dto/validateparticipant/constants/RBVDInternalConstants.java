package com.bbva.rbvd.dto.validateparticipant.constants;

public class RBVDInternalConstants {

    public static final class Status {
        public static final String SUCCESSFUL = "SUCCESSFUL";
        public static final String FAILED = "FAILED";
        public static final String NONE = "NONE";

        private Status() {
        }
    }

    public enum Role {

        CONTRACTOR("1", "8","CONTRACTOR"),
        PAYMENT_MANAGER("7", "23","PAYMENT_MANAGER"),
        INSURED("2", "9","INSURED");
        private final String roleBank;
        private final String roleCompany;
        private final String description;

        Role(String roleBank, String roleCompany,String description) {
            this.roleBank = roleBank;
            this.roleCompany = roleCompany;
            this.description = description;
        }

        public String getRoleBank() { return roleBank; }
        public String getRoleCompany() { return roleCompany; }
        public String getDescription() { return description; }
    }

    public static final class Number{
        public static final String VEINTE = "20";
    }

    public enum TypeParticipant{
        NATURAL,
        LEGAL
    }

    public enum TypeInsuranceProduct{
        VEHICLE(""),
        HOME(""),
        PYME(""),
        CARD(""),
        UNEMPLOYEEMENT(""),
        EASY_YES(""),
        DINAMIC_LIFE(""),
        INVESTMENT_LIFE("");

        private final String productId;

        TypeInsuranceProduct(String productId) {
            this.productId = productId;
        }
    }
}
