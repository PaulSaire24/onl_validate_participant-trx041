package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;

public class PayloadStore {
    private AgregarTerceroBO responseRimac;

    public PayloadStore(AgregarTerceroBO responseRimac) {
        this.responseRimac = responseRimac;
    }

    public AgregarTerceroBO getResponseRimac() {
        return responseRimac;
    }

    public void setResponseRimac(AgregarTerceroBO responseRimac) {
        this.responseRimac = responseRimac;
    }

    @Override
    public String toString() {
        return "PayloadStore{" +
                "responseRimac=" + responseRimac +
                '}';
    }

    public static final class Builder {
        private AgregarTerceroBO responseRimac;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder responseRimac(AgregarTerceroBO responseRimac) {
            this.responseRimac = responseRimac;
            return this;
        }

        public PayloadStore build() {
            return new PayloadStore(responseRimac);
        }
    }
}
