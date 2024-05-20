package com.bbva.rbvd.lib.r041.model;

public class AddressBO {

    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;
    private String ubigeo;
    private String nombreVia;
    private String tipoVia;
    private String numeroVia;
    private String tipoPersona;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public String getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(String tipoVia) {
        this.tipoVia = tipoVia;
    }

    public String getNumeroVia() {
        return numeroVia;
    }

    public void setNumeroVia(String numeroVia) {
        this.numeroVia = numeroVia;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    @Override
    public String toString() {
        return "AddressBO{" +
                "direccion='" + direccion + '\'' +
                ", distrito='" + distrito + '\'' +
                ", provincia='" + provincia + '\'' +
                ", departamento='" + departamento + '\'' +
                ", ubigeo='" + ubigeo + '\'' +
                ", nombreVia='" + nombreVia + '\'' +
                ", tipoVia='" + tipoVia + '\'' +
                ", numeroVia='" + numeroVia + '\'' +
                ", tipoPersona='" + tipoPersona + '\'' +
                '}';
    }


    public static final class Builder {
        private String direccion;
        private String distrito;
        private String provincia;
        private String departamento;
        private String ubigeo;
        private String nombreVia;
        private String tipoVia;
        private String numeroVia;
        private String tipoPersona;

        private Builder() {
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Builder distrito(String distrito) {
            this.distrito = distrito;
            return this;
        }

        public Builder provincia(String provincia) {
            this.provincia = provincia;
            return this;
        }

        public Builder departamento(String departamento) {
            this.departamento = departamento;
            return this;
        }

        public Builder ubigeo(String ubigeo) {
            this.ubigeo = ubigeo;
            return this;
        }

        public Builder nombreVia(String nombreVia) {
            this.nombreVia = nombreVia;
            return this;
        }

        public Builder tipoVia(String tipoVia) {
            this.tipoVia = tipoVia;
            return this;
        }

        public Builder numeroVia(String numeroVia) {
            this.numeroVia = numeroVia;
            return this;
        }

        public Builder tipoPersona(String tipoPersona) {
            this.tipoPersona = tipoPersona;
            return this;
        }

        public AddressBO build() {
            AddressBO addressBO = new AddressBO();
            addressBO.setDireccion(direccion);
            addressBO.setDistrito(distrito);
            addressBO.setProvincia(provincia);
            addressBO.setDepartamento(departamento);
            addressBO.setUbigeo(ubigeo);
            addressBO.setNombreVia(nombreVia);
            addressBO.setTipoVia(tipoVia);
            addressBO.setNumeroVia(numeroVia);
            addressBO.setTipoPersona(tipoPersona);
            return addressBO;
        }
    }
}
