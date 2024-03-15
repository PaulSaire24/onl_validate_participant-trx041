package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.AddressComponentsDTO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;


import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;


import static com.bbva.rbvd.lib.r041.util.ConvertUtil.toLocalDate;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class ValidateRimacNaturalPerson {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateRimacNaturalPerson.class);
    private static final String RUC_ID = "R";
    private static final String EMAIL_VALUE = "EMAIL";
    private static final String MOBILE_VALUE = "MOBILE_NUMBER";
    private static final Integer MAX_CHARACTER = 1;
    private static final String SIN_ESPECIFICAR = "NA";
    private static final String NO_EXIST = "NotExist";

    private static final String INTERIOR_NUMBER_ID = "DPTO.";
    ValidateRimacNaturalPerson() {}

    public static PersonaBO mapInRequestRimacNonLife(PEWUResponse personInput, ParticipantsDTO participants, QuotationJoinCustomerInformationDTO customerInformationDb, Integer roleId){
        validatePewuResponsePersonData(personInput);

        PersonaBO persona = constructPerson(participants,personInput,customerInformationDb, roleId);

        StringBuilder stringAddress  = new StringBuilder();

        fillAddress(personInput, persona, stringAddress);

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

        persona.setTipoDocumento(RUC_ID.equalsIgnoreCase(customer.getPemsalwu().getTdoi())?customerInformationDb.getQuotation().getParticipantPersonalId():customer.getPemsalwu().getTdoi());
        persona.setNroDocumento(customer.getPemsalwu().getNdoi());
        persona.setApePaterno(customer.getPemsalwu().getApellip());

        persona.setApeMaterno(StringUtils.defaultString(customer.getPemsalwu().getApellim()).trim().length()  > MAX_CHARACTER ? customer.getPemsalwu().getApellim() : StringUtils.EMPTY);

        persona.setNombres(customer.getPemsalwu().getNombres());
        persona.setFechaNacimiento(customer.getPemsalwu().getFechan());
        if(!StringUtils.isEmpty(customer.getPemsalwu().getSexo())) persona.setSexo(customer.getPemsalwu().getSexo());

        persona.setCorreoElectronico(Objects.isNull(correoSelect) ? customerInformationDb.getQuotationMod().getContactEmailDesc() : correoSelect.getContact());

        persona.setCelular(Objects.isNull(celularSelect) ? customerInformationDb.getQuotationMod().getCustomerPhoneDesc() : celularSelect.getContact());
        persona.setRol(Objects.isNull(roleId)?null:roleId);
        return persona;
    }

    public static void fillAddress(PEWUResponse persona, PersonaBO rimacPersonObject, StringBuilder stringAddress) {

        fillAddressUbigeo(persona, rimacPersonObject);

        String addressViaList = fillAddressViaList(persona, rimacPersonObject);
        String addressGroupList = fillAddressGroupList(persona, addressViaList, rimacPersonObject);

        if(isNull(addressGroupList) && isNull(addressViaList)) {
            rimacPersonObject.setTipoVia(SIN_ESPECIFICAR);
            rimacPersonObject.setNombreVia(SIN_ESPECIFICAR);
            rimacPersonObject.setNumeroVia(SIN_ESPECIFICAR);
            rimacPersonObject.setDireccion(SIN_ESPECIFICAR);
        }

        String addressNumberVia = fillAddressNumberVia(persona, rimacPersonObject);

        String fullNameOther = fillAddressOther(persona, stringAddress);

        if (NO_EXIST.equals(addressNumberVia) || NO_EXIST.equals(fullNameOther)){
            fillAddressAditional(persona, stringAddress);
        }

        getFullDirectionFrom(addressViaList, addressGroupList, addressNumberVia, stringAddress, rimacPersonObject);

    }

    private static void fillAddressUbigeo(final PEWUResponse geographicGroups, final PersonaBO persona) {
        String ubigeo = StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigod()) +
                        StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigop()) +
                        StringUtils.defaultString(geographicGroups.getPemsalwu().getCodigdi());

        String department = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesdept())).orElse(StringUtils.EMPTY);
        String province = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesprov())).orElse(StringUtils.EMPTY);
        String district = Optional.ofNullable(geographicGroups.getPemsalw4()).map(pemsalw4 ->
                StringUtils.defaultString(pemsalw4.getDesdist())).orElse(StringUtils.EMPTY);

        persona.setDepartamento(department);
        persona.setProvincia(province);
        persona.setDistrito(district);
        persona.setUbigeo(ubigeo);
    }

    private static String fillAddressViaList(PEWUResponse geographicGroupsAddress, PersonaBO persona) {

        String nombreDir1 = null;
        String viaType = "";
        String viaName = "";

        if(!StringUtils.isEmpty(geographicGroupsAddress.getPemsalwu().getIdendi1())
                && !geographicGroupsAddress.getPemsalwu().getIdendi1().equals("NA")) {
            viaType = geographicGroupsAddress.getPemsalwu().getIdendi1();
            viaName = StringUtils.defaultString(geographicGroupsAddress.getPemsalwu().getNombdi1());

            persona.setTipoVia(viaType);
            persona.setNombreVia(viaName);

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
            groupName = StringUtils.defaultString(geographicGroupsAddress.getPemsalwu().getNombdi2());
            nombreDir2 = groupType.concat(" ").concat(groupName);
        }

        if(isNull(addressViaList)) {
            persona.setTipoVia(groupType);
            persona.setNombreVia(groupName);
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

    public static PersonaBO mapNonCustomerRequestData(ParticipantsDTO participantsDTO, Integer rolId){
        PersonaBO personaBO = new PersonaBO();
        personaBO.setTipoDocumento(participantsDTO.getIdentityDocuments().get(0).getDocumentType().getId());
        personaBO.setNroDocumento(participantsDTO.getIdentityDocuments().get(0).getValue());
        personaBO.setApePaterno(participantsDTO.getPerson().getLastName());
        personaBO.setApeMaterno(participantsDTO.getPerson().getSecondLastName());
        personaBO.setNombres(participantsDTO.getPerson().getFirstName().concat(" ").concat(participantsDTO.getPerson().getMiddleName()));
        personaBO.setFechaNacimiento(String.valueOf(toLocalDate(participantsDTO.getPerson().getBirthDate())));
        personaBO.setSexo(participantsDTO.getPerson().getGender().getId().equals("MALE") ? "M" : "F");
        personaBO.setCorreoElectronico(participantsDTO.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContactType().equals(EMAIL_VALUE)).map(ContactDetailsDTO::getContact).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setCelular(participantsDTO.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContactType().equals(MOBILE_VALUE)).map(ContactDetailsDTO::getContact).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setDireccion(participantsDTO.getAddresses().get(0).getFormattedAddress());
        personaBO.setRol(rolId);
        personaBO.setDistrito(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> component.getComponentTypes().get(0).equals("DISTRICT")).map(AddressComponentsDTO::getName).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setProvincia(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> component.getComponentTypes().get(0).equals("PROVINCE")).map(AddressComponentsDTO::getName).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setDepartamento(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> component.getComponentTypes().get(0).equals("DEPARTMENT")).map(AddressComponentsDTO::getName).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setUbigeo(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> component.getComponentTypes().get(0).equals("UBIGEO")).map(AddressComponentsDTO::getName).findFirst().orElse(StringUtils.EMPTY));
        personaBO.setTipoVia(filterViaTypeName(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent()));
        personaBO.setNombreVia(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> filterViaTypeCondition(component.getComponentTypes().get(0))).map(AddressComponentsDTO::getName)
                .findFirst().orElse(StringUtils.EMPTY));
        personaBO.setNumeroVia(participantsDTO.getAddresses().get(0).getLocation().getAddressComponent().stream().
                filter(component -> component.getComponentTypes().get(0).equals("EXTERIOR_NUMBER")).map(AddressComponentsDTO::getName).findFirst().orElse(StringUtils.EMPTY));
        return personaBO;
    }

    private static Map<String, String> tipeListDir() {

        Map<String, String> tipeListDirMap = new HashMap<>();

        tipeListDirMap.put("ALAMEDA", "ALM");
        tipeListDirMap.put("AVENUE", "AV.");
        tipeListDirMap.put("STREET", "CAL");
        tipeListDirMap.put("MALL", "CC.");
        tipeListDirMap.put("ROAD", "CRT");
        tipeListDirMap.put("SHOPPING_ARCADE", "GAL");
        tipeListDirMap.put("JIRON", "JR.");
        tipeListDirMap.put("JETTY", "MAL");
        tipeListDirMap.put("OVAL", "OVA");
        tipeListDirMap.put("PEDESTRIAN_WALK", "PAS");
        tipeListDirMap.put("SQUARE", "PLZ");
        tipeListDirMap.put("PARK", "PQE");
        tipeListDirMap.put("PROLONGATION", "PRL");
        tipeListDirMap.put("PASSAGE", "PSJ");
        tipeListDirMap.put("BRIDGE", "PTE");
        tipeListDirMap.put("DESCENT", "BAJ");
        tipeListDirMap.put("PORTAL", "POR");
        tipeListDirMap.put("GROUP", "AGR");
        tipeListDirMap.put("AAHH", "AHH");
        tipeListDirMap.put("HOUSING_COMPLEX", "CHB");
        tipeListDirMap.put("INDIGENOUS_COMMUNITY", "COM");
        tipeListDirMap.put("PEASANT_COMMUNITY", "CAM");
        tipeListDirMap.put("HOUSING_COOPERATIVE", "COV");
        tipeListDirMap.put("STAGE", "ETP");
        tipeListDirMap.put("SHANTYTOWN", "PJJ");
        tipeListDirMap.put("NEIGHBORHOOD", "SEC");
        tipeListDirMap.put("URBANIZATION", "URB");
        tipeListDirMap.put("NEIGHBORHOOD_UNIT", "UV.");
        tipeListDirMap.put("ZONE", "ZNA");
        tipeListDirMap.put("ASSOCIATION", "ASC");
        tipeListDirMap.put("FUNDO", "FUN");
        tipeListDirMap.put("MINING_CAMP", "MIN");
        tipeListDirMap.put("RESIDENTIAL", "RES");
        return tipeListDirMap;

    }

    private static boolean filterViaTypeCondition(final String geographicGroupTyeId) {
        Map<String, String> mapTypeListDir = tipeListDir();
        return mapTypeListDir.entrySet().stream().anyMatch(element -> element.getKey().equals(geographicGroupTyeId));
    }

    private static String filterViaTypeName(List<AddressComponentsDTO> addressComponents) {
        Map<String, String> mapTypeListDir = tipeListDir();
        String viaTypeName = StringUtils.EMPTY;
        for(AddressComponentsDTO address : addressComponents){
            if(mapTypeListDir.entrySet().stream().anyMatch(element -> element.getKey().equals(address.getComponentTypes().get(0)))){
                    viaTypeName = mapTypeListDir.get(address.getComponentTypes().get(0));
                }
        }

        return viaTypeName;
    }

    private static void validatePewuResponsePersonData(PEWUResponse pewuResponse){
        if (Objects.isNull(pewuResponse.getPemsalwu())){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage()
                            .concat(TypeErrorControllerEnum.ERROR_PBTQ_INCOMPLETE_CLIENT_INFORMATION.getValue()));
        }
    }

}
