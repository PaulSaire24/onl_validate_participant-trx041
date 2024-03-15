package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.aso.CommonFieldsASO;
import com.bbva.rbvd.dto.insrncsale.aso.FormationASO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ValidateRimacLegalPerson {
    private static final String RUC_ID = "R";
    private static final String TAG_OTROS = "OTROS";
    private static final String COUNTRY_REQUIRED = "PERU";

    public static OrganizacionBO getDataOrganization(final BusinessASO businesses, PersonaBO persona, QuotationJoinCustomerInformationDTO registerInsuranceQuotationDAO, Integer rolId, ParticipantsDTO requestBody) {
        return mapOrganizations(businesses, persona, registerInsuranceQuotationDAO, rolId, requestBody);

    }

    private static OrganizacionBO mapOrganizations(final BusinessASO business, PersonaBO persona, QuotationJoinCustomerInformationDTO quotation, Integer rolId, ParticipantsDTO requestBody) {

        List<ContactDetailsDTO> contactDetail = requestBody.getContactDetails().stream().filter(Objects::nonNull).collect(Collectors.toList());

        String fijo = contactDetail.stream().filter(contact -> "PHONE_NUMBER".equals(contact.getContactType())).findFirst().orElse(new ContactDetailsDTO()).getContact();
        String celular = contactDetail.stream().filter(contact -> "MOBILE_NUMBER".equals(contact.getContactType())).findFirst().orElse(new ContactDetailsDTO()).getContact();
        String correo = contactDetail.stream().filter(contact -> "EMAIL".equals(contact.getContactType())).findFirst().orElse(new ContactDetailsDTO()).getContact();

        correo =  StringUtils.defaultString(quotation.getQuotationMod().getContactEmailDesc(),correo) ;
        celular = StringUtils.defaultString(quotation.getQuotationMod().getCustomerPhoneDesc(),celular);

        OrganizacionBO organizacion = new OrganizacionBO();
        organizacion.setDireccion(validateSN(persona.getDireccion()));
        organizacion.setRol(rolId);
        organizacion.setTipoDocumento(RUC_ID);
        organizacion.setNroDocumento(FunctionUtils.isNotEmptyList(business.getBusinessDocuments()).map(businessDocument -> businessDocument.get(0).getDocumentNumber()).orElse(StringUtils.EMPTY));
        organizacion.setRazonSocial(business.getLegalName());
        organizacion.setNombreComercial(business.getLegalName());

        if(Objects.nonNull(business.getFormation())) {
            organizacion.setPaisOrigen(Optional.ofNullable(business.getFormation()).map(ValidateRimacLegalPerson::mapPaisOrigen).orElse(StringUtils.EMPTY));
            organizacion.setFechaConstitucion(Optional.ofNullable(business.getFormation()).map(FormationASO::getDate).orElse(null));
        } else {
            organizacion.setPaisOrigen(COUNTRY_REQUIRED);
            organizacion.setFechaConstitucion(null);
        }

        organizacion.setFechaInicioActividad(Objects.isNull(business.getAnnualSales()) ? null : business.getAnnualSales().getStartDate());
        organizacion.setTipoOrganizacion(Optional.ofNullable(business.getBusinessGroup()).map(CommonFieldsASO::getId).orElse(StringUtils.EMPTY));
        organizacion.setGrupoEconomico(TAG_OTROS);
        organizacion.setCiiu(Optional.ofNullable(business.getEconomicActivity()).map(CommonFieldsASO::getId).orElse(StringUtils.EMPTY));
        organizacion.setTelefonoFijo(fijo);
        organizacion.setCelular(celular);
        organizacion.setCorreoElectronico(correo);
        organizacion.setDistrito(persona.getDistrito());
        organizacion.setProvincia(persona.getProvincia());
        organizacion.setDepartamento(persona.getDepartamento());
        organizacion.setUbigeo(persona.getUbigeo());
        organizacion.setNombreVia(persona.getNombreVia());
        organizacion.setTipoVia(persona.getTipoVia());
        organizacion.setNumeroVia(persona.getNumeroVia());

        return organizacion;
    }

    private static String validateSN(String name) {
        if(Objects.isNull(name) || "null".equals(name) || " ".equals(name)){
            return "";
        }else{
            name = name.replace("#","Ñ");
            return name;
        }
    }

    private static String mapPaisOrigen(FormationASO formationASO) {
        if(Objects.isNull(formationASO.getCountry())){
            return StringUtils.EMPTY;
        }
        return formationASO.getCountry().getName();
    }
}
