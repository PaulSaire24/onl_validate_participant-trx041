package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.joda.time.LocalDate;

public class OrganizationBean {

    public static OrganizacionBO getOrganizationBO(PersonaBO persona, Integer rolId,
                                                    String numDoc, String fijo, String celular,
                                                    String correo, String legalName,
                                                    String paisOrigen,
                                                    LocalDate fechaConstitucion, LocalDate fechaInicioActividad,
                                                    String tipoOrganizacion, String ciiu) {
        OrganizacionBO organization = new OrganizacionBO();
        organization.setDireccion(persona.getDireccion());
        organization.setRol(rolId);
        organization.setTipoDocumento(ConstantsUtil.Organization.RUC_ID);
        organization.setNroDocumento(numDoc);
        organization.setRazonSocial(legalName);
        organization.setNombreComercial(legalName);
        organization.setPaisOrigen(paisOrigen);
        organization.setFechaConstitucion(fechaConstitucion);
        organization.setFechaInicioActividad(fechaInicioActividad);
        organization.setTipoOrganizacion(tipoOrganizacion);
        organization.setGrupoEconomico(ConstantsUtil.Organization.TAG_OTROS);
        organization.setCiiu(ciiu);
        organization.setTelefonoFijo(fijo);
        organization.setCelular(celular);
        organization.setCorreoElectronico(correo);
        organization.setDistrito(persona.getDistrito());
        organization.setProvincia(persona.getProvincia());
        organization.setDepartamento(persona.getDepartamento());
        organization.setUbigeo(persona.getUbigeo());
        organization.setNombreVia(persona.getNombreVia());
        organization.setTipoVia(persona.getTipoVia());
        organization.setNumeroVia(persona.getNumeroVia());
        organization.setTipoPersona(ValidationUtil.getPersonType(organization).getCode());
        return organization;
    }
}
