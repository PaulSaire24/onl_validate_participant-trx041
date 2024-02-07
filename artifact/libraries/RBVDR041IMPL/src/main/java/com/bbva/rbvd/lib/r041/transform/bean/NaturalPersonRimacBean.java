package com.bbva.rbvd.lib.r041.transform.bean;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class NaturalPersonRimacBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(NaturalPersonRimacBean.class);
    private static final String RUC_ID = "R";
    private static final String EMAIL_VALUE = "EMAIL";
    private static final String MOBILE_VALUE = "MOBILE_NUMBER";
    private static final Integer MAX_CHARACTER = 1;
    private static final String SIN_ESPECIFICAR = "N/A";
    private static final String NO_EXIST = "NotExist";

    private static final String INTERIOR_NUMBER_ID = "DPTO.";
    NaturalPersonRimacBean() {}

    public static PersonaBO mapRimacInputServiceNaturalPerson(PEWUResponse personInput, ParticipantsDTO participants, QuotationJoinCustomerInformationDTO customerInformationDb, Integer roleId, String saleChannelId){
        PersonaBO persona = constructPerson(participants,personInput,customerInformationDb, roleId);

        StringBuilder stringAddress  = new StringBuilder();

        fillAddress(personInput, persona, stringAddress, saleChannelId);

        LOGGER.info("[RequestRimacBean] convertListPersons() :: End");
        LOGGER.info("[RequestRimacBean] personData{}", persona);
        return persona;
    }

    private static PersonaBO constructPerson(ParticipantsDTO participant, PEWUResponse customer, QuotationJoinCustomerInformationDTO customerInformationDb, Integer roleId){
        PersonaBO persona = new PersonaBO();
        ContactDetailsDTO correoSelect = new ContactDetailsDTO();
        ContactDetailsDTO celularSelect = new ContactDetailsDTO();
        if(!CollectionUtils.isEmpty(participant.getContactDetails()) && Objects.nonNull(participant.getContactDetails())){
            correoSelect= participant.getContactDetails().stream().
                    filter(contactDetail -> contactDetail.getContactType().equals(EMAIL_VALUE)).findFirst().orElse(null);
            celularSelect= participant.getContactDetails().stream().
                    filter(contactDetail -> contactDetail.getContactType().equals(MOBILE_VALUE)).findFirst().orElse(null);
        }

        persona.setTipoDocumento(RUC_ID.equalsIgnoreCase(persona.getTipoDocumento())?customerInformationDb.getQuotation().getParticipantPersonalId():customer.getPemsalwu().getTdoi()); //primero bd luego listcustomer
        persona.setNroDocumento(customer.getPemsalwu().getNdoi()); //primero bd luego listcustomer
        persona.setApePaterno(customer.getPemsalwu().getApellip());

        persona.setApeMaterno( StringUtils.defaultString(customer.getPemsalwu().getApellim()).trim().length()  > MAX_CHARACTER ? customer.getPemsalwu().getApellim() : StringUtils.EMPTY);

        persona.setNombres(customer.getPemsalwu().getNombres());
        persona.setFechaNacimiento(customer.getPemsalwu().getFechan());
        if(!StringUtils.isEmpty(customer.getPemsalwu().getSexo())) persona.setSexo("MALE".equals(customer.getPemsalwu().getSexo()) ? "M" : "F");

        persona.setCorreoElectronico(Objects.isNull(correoSelect) ? customerInformationDb.getQuotationMod().getContactEmailDesc() : correoSelect.getContact());

        persona.setCelular(Objects.isNull(celularSelect) ? customerInformationDb.getQuotationMod().getCustomerPhoneDesc() : celularSelect.getContact());
        persona.setRol(Objects.isNull(roleId)?null:roleId);
        return persona;
    }

    public static String fillAddress(PEWUResponse persona, PersonaBO rimacPersonObject, StringBuilder stringAddress, String saleChannelId) {
        LOGGER.info("CHANNEL BBVA {}", saleChannelId);
        String controlChannel = " ";

        fillAddressUbigeo(persona, rimacPersonObject);

        String addressViaList = fillAddressViaList(persona, rimacPersonObject);
        String addressGroupList = fillAddressGroupList(persona, addressViaList, rimacPersonObject);

        if(isNull(addressGroupList) && isNull(addressViaList)) {
            rimacPersonObject.setTipoVia(SIN_ESPECIFICAR);
            rimacPersonObject.setNombreVia(SIN_ESPECIFICAR);
            rimacPersonObject.setNumeroVia(SIN_ESPECIFICAR);
            rimacPersonObject.setDireccion(SIN_ESPECIFICAR);
            return controlChannel;
        }

        String addressNumberVia = fillAddressNumberVia(persona, rimacPersonObject);

        String fullNameOther = fillAddressOther(persona, stringAddress);

        if (NO_EXIST.equals(addressNumberVia) || NO_EXIST.equals(fullNameOther)){
            fillAddressAditional(persona, stringAddress);
        }

        return getFullDirectionFrom(addressViaList, addressGroupList, addressNumberVia, stringAddress, rimacPersonObject);

    }

    private static void fillAddressUbigeo(final PEWUResponse geographicGroups, final PersonaBO persona) {

        String department = "";
        String province = "";
        String district = "";
        String ubigeo = "";

        ubigeo = geographicGroups.getPemsalwu().getCodigod() + geographicGroups.getPemsalwu().getCodigop() + geographicGroups.getPemsalwu().getCodigdi();
        department = geographicGroups.getPemsalw4().getDesdept();
        province = geographicGroups.getPemsalw4().getDesprov();
        district = geographicGroups.getPemsalw4().getDesdist();

        persona.setDepartamento(department);
        persona.setProvincia(province);
        persona.setDistrito(district);
        persona.setUbigeo(ubigeo);
    }

    private static String fillAddressViaList(PEWUResponse geographicGroupsAddress, PersonaBO persona) {

        String nombreDir1 = null;
        String viaType = "";
        String viaName = "";

        if(!StringUtils.isEmpty (geographicGroupsAddress.getPemsalwu().getIdendi1())
                && !geographicGroupsAddress.getPemsalwu().getIdendi1().equals("NA")) {
            viaType = geographicGroupsAddress.getPemsalwu().getIdendi1();
            viaName = geographicGroupsAddress.getPemsalwu().getNombdi1();

            persona.setTipoVia(geographicGroupsAddress.getPemsalwu().getIdendi1());
            persona.setNombreVia(geographicGroupsAddress.getPemsalwu().getNombdi1());

            nombreDir1 = viaType.concat(" ").concat(viaName);
        }

        return nombreDir1;

    }

    private static String fillAddressGroupList(PEWUResponse geographicGroupsAddress, String addressViaList, PersonaBO persona) {

        String nombreDir2 = null;
        String groupType = "";
        String groupName = "";

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getIdendi2())
                && !geographicGroupsAddress.getPemsalwu().getIdendi2().equals("NA")){
            groupType = geographicGroupsAddress.getPemsalwu().getIdendi2();
            groupName = geographicGroupsAddress.getPemsalwu().getNombdi2();

            if(nonNull(addressViaList)){
                nombreDir2 = groupType.concat(" ").concat(groupName);
            }else{
                persona.setTipoVia(groupType);
                persona.setNombreVia(groupName);
            }
        }

        return nombreDir2;

    }

    private static String fillAddressNumberVia(PEWUResponse geographicGroupsAddress, PersonaBO persona) {
        String numberVia = NO_EXIST;
            if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getNroext1())){
            persona.setNumeroVia(geographicGroupsAddress.getPemsalwu().getNroext1());
            numberVia = geographicGroupsAddress.getPemsalwu().getNroext1();
        }else{
            persona.setNumeroVia(SIN_ESPECIFICAR);
        }

        return numberVia;

    }

    public static String fillAddressOther(PEWUResponse geographicGroupsAddress, StringBuilder stringAddress) {

        String addressOther;
        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getNroint1())){
            addressOther = geographicGroupsAddress.getPemsalwu().getNroint1();
            stringAddress.append(INTERIOR_NUMBER_ID.concat(" ").concat(addressOther));
        }else{
            addressOther = NO_EXIST;
        }

        return addressOther;
    }

    public static void fillAddressAditional(PEWUResponse geographicGroupsAddress, StringBuilder stringAddress) {

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getManzana())){
            appendToAddress(stringAddress, geographicGroupsAddress.getPemsalwu().getManzana());
        }
        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getLote())){
            appendToAddress(stringAddress, geographicGroupsAddress.getPemsalwu().getLote());
        }

    }

    private static void appendToAddress(StringBuilder stringAddress, String toAppend) {
        if (stringAddress.length() > 0 && !stringAddress.toString().endsWith(" ")) {
            stringAddress.append(" ");
        }
        stringAddress.append(toAppend);
    }

    private static String getFullDirectionFrom(String addressViaList, String addressGroupList, String addressNumberVia, StringBuilder stringAddress, PersonaBO persona) {

        String directionForm = null;
        //Logica del primer Grupo : Ubicacion uno
        if(nonNull(addressViaList) && nonNull(addressGroupList) && !NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressViaList.concat(" ").concat(addressNumberVia).concat(", ").concat(addressGroupList)
                    .concat(" ").concat(stringAddress.toString());
        }

        if(nonNull(addressViaList) && nonNull(addressGroupList) && NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressViaList.concat(" ").concat(", ").concat(addressGroupList)
                    .concat(" ").concat(stringAddress.toString());
        }

        if(nonNull(addressViaList) && isNull(addressGroupList) && !NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressViaList.concat(" ").concat(addressNumberVia).concat(" ")
                    .concat(stringAddress.toString());
        }

        if(nonNull(addressViaList) && isNull(addressGroupList) && NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressViaList.concat(" ").concat(stringAddress.toString());
        }
        //Logica del segundo Grupo : Ubicacion dos
        if(isNull(addressViaList) && nonNull(addressGroupList) && !NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressGroupList.concat( " ").concat(addressNumberVia).concat(" ")
                    .concat(stringAddress.toString());
        }

        if(isNull(addressViaList) && nonNull(addressGroupList) && NO_EXIST.equals(addressNumberVia)) {
            directionForm = addressGroupList.concat( " ").concat(stringAddress.toString());
        }

        if(nonNull(directionForm)) {
            persona.setDireccion(directionForm);
        }

        return directionForm;

    }

}
