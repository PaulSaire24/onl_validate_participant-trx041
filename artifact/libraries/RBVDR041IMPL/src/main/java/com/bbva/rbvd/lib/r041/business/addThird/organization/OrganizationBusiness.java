package com.bbva.rbvd.lib.r041.business.addThird.organization;

import com.bbva.rbvd.dto.insrncsale.aso.CommonFieldsASO;
import com.bbva.rbvd.dto.insrncsale.aso.FormationASO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transform.bean.OrganizationBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrganizationBusiness {

    public static OrganizacionBO mapOrganizations(final BusinessASO business, PersonaBO persona, QuotationCustomerDAO quotation, Integer rolId, ParticipantsDTO requestBody) {


        String numDoc = FunctionUtils.isNotEmptyList(requestBody.getIdentityDocuments()).map(identityDocument -> identityDocument.get(0).getValue()).orElse(StringUtils.EMPTY);
        String legalName = validateSN(business.getLegalName());
        String paisOrigen = null;
        LocalDate fechaConstitucion = null;

        if(Objects.nonNull(business.getFormation())) {
            paisOrigen = Optional.ofNullable(business.getFormation()).map(OrganizationBusiness::mapPaisOrigen).orElse(StringUtils.EMPTY);
            fechaConstitucion = Optional.ofNullable(business.getFormation()).map(FormationASO::getDate).orElse(null);
        } else {
            paisOrigen = ConstantsUtil.Organization.COUNTRY_REQUIRED;
        }

        LocalDate fechaInicioActividad = Objects.isNull(business.getAnnualSales()) ? null : business.getAnnualSales().getStartDate();
        String tipoOrganizacion = Optional.ofNullable(business.getBusinessGroup()).map(CommonFieldsASO::getId).orElse(StringUtils.EMPTY);
        String ciiu = Optional.ofNullable(business.getEconomicActivity()).map(CommonFieldsASO::getId).orElse(StringUtils.EMPTY);

        List<ContactDetailsDTO> contactDetail = requestBody.getContactDetails().stream().filter(Objects::nonNull).collect(Collectors.toList());

        String fijo = getContactDetail(contactDetail, ConstantsUtil.ContactType.PHONE_NUMBER);
        String celular = getContactDetail(contactDetail, ConstantsUtil.ContactType.MOBILE_NUMBER);
        String correo = getContactDetail(contactDetail, ConstantsUtil.ContactType.EMAIL);

        correo =  StringUtils.defaultString(quotation.getQuotationMod().getContactEmailDesc(),correo) ;
        celular = StringUtils.defaultString(quotation.getQuotationMod().getCustomerPhoneDesc(),celular);


        OrganizacionBO organization = OrganizationBean.getOrganizationBO(persona, rolId,
                numDoc, fijo, celular, correo, legalName,
                paisOrigen, fechaConstitucion, fechaInicioActividad, tipoOrganizacion, ciiu);

        return organization;
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
    private static String getContactDetail(List<ContactDetailsDTO> contactDetails, String contactType) {
        return contactDetails.stream()
                .filter(contact -> contactType.equals(contact.getContactType()))
                .map(ContactDetailsDTO::getContact)
                .findFirst()
                .orElse(StringUtils.EMPTY);
    }
}
