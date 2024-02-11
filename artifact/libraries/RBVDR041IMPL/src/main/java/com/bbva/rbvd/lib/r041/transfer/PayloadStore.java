package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;

public class PayloadStore {
    private String creationUser;
    private String userAudit;
    private String documentTypeId;
    private PEWUResponse customer;

    public PayloadStore(String creationUser, String userAudit, String documentTypeId, PEWUResponse customer) {
        this.creationUser = creationUser;
        this.userAudit = userAudit;
        this.documentTypeId = documentTypeId;
        this.customer = customer;
    }

    public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

    public String getUserAudit() {
        return userAudit;
    }

    public void setUserAudit(String userAudit) {
        this.userAudit = userAudit;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public PEWUResponse getCustomer() {
        return customer;
    }

    public void setCustomer(PEWUResponse customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "PayloadStore{" +
                "creationUser='" + creationUser + '\'' +
                ", userAudit='" + userAudit + '\'' +
                ", documentTypeId='" + documentTypeId + '\'' +
                ", customer=" + customer +
                '}';
    }


    public static final class Builder {
        private String creationUser;
        private String userAudit;
        private String documentTypeId;
        private PEWUResponse customer;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder creationUser(String creationUser) {
            this.creationUser = creationUser;
            return this;
        }

        public Builder userAudit(String userAudit) {
            this.userAudit = userAudit;
            return this;
        }

        public Builder documentTypeId(String documentTypeId) {
            this.documentTypeId = documentTypeId;
            return this;
        }

        public Builder customer(PEWUResponse customer) {
            this.customer = customer;
            return this;
        }

        public PayloadStore build() {
            return new PayloadStore(creationUser, userAudit, documentTypeId, customer);
        }
    }
}
