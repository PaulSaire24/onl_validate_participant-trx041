package com.bbva.rbvd.dto.validateparticipant.dto;

import java.io.Serializable;

public class ResponseLibrary <T> implements Serializable {

    private static final long serialVersionUID = 2931699728946643245L;
    private String statusProcess;
    private transient T body;

    private ResponseLibrary(String statusProcess, T body) {
        this.statusProcess = statusProcess;
        this.body = body;
    }

    public T getBody() {
        return this.body;
    }


    public String getStatusProcess() {
        return this.statusProcess;
    }


    public static final class ResponseServiceBuilder {
        private String status;
        private String adviceCode;
        private String message;

        public ResponseServiceBuilder() {
            //This method is an empty constructor
        }

        public static ResponseServiceBuilder an() {
            return new ResponseServiceBuilder();
        }

        public ResponseServiceBuilder statusIndicatorProcess(String statusIndicatorProcess, String adviceCode, String message) {
            this.status = statusIndicatorProcess;
            this.adviceCode = adviceCode;
            this.message = message;
            return this;
        }


        public <T> ResponseLibrary<T> body(T body) {
            return new ResponseLibrary<>(this.status, body);
        }

        public <T> ResponseLibrary<T> build() {
            return this.body(null);
        }
    }

}
