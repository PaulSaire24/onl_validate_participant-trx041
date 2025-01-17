package com.bbva.rbvd.util;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceBusinessDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceCompanyDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationModDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationDAO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantTypeDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;
import com.bbva.rbvd.dto.participant.request.GenderDTO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.dto.participant.request.DocumentTypeDTO;
import com.bbva.rbvd.dto.participant.request.AddressComponentsDTO;
import com.bbva.rbvd.dto.participant.request.AddressesDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.LocationDTO;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;

public class ParticipantsUtil {

    public static ParticipantsDTO buildParticipant(String typePerson, String typeDocument, String documentNumber, String personType, boolean isClient){
        ParticipantsDTO participant = new ParticipantsDTO();

        ParticipantTypeDTO participantType = new ParticipantTypeDTO();
        participantType.setId(typePerson);
        participant.setParticipantType(participantType);

        PersonDTO person = new PersonDTO();
        if(isClient){
            person.setCustomerId("97848900");
        }else {
            person.setCustomerId("PE0011000011700");
        }
        person.setPersonType(personType);
        person.setFirstName("firstName");
        person.setMiddleName("middleName");
        person.setLastName("lastName");
        person.setSecondLastName("secondLastName");
        person.setBirthDate(new Date());

        GenderDTO gender = new GenderDTO();
        gender.setId("MALE");

        person.setGender(gender);

        if(personType.equals("LEGAL")){
            person.setDescription("legal description");
            person.setLegalName("legalName");
            person.setCreationDate(new Date());
        }

        participant.setPerson(person);

        List<IdentityDocumentDTO> identityDocumentList = new ArrayList<>();
        IdentityDocumentDTO identityDocument = new IdentityDocumentDTO();
        identityDocument.setValue(documentNumber);
        DocumentTypeDTO documentType = new DocumentTypeDTO();
        documentType.setId(typeDocument);
        identityDocument.setDocumentType(documentType);
        identityDocumentList.add(identityDocument);
        participant.setIdentityDocuments(identityDocumentList);
            List<ContactDetailsDTO> contactDetailList = new ArrayList<>();
            ContactDetailsDTO contactDetail1 = new ContactDetailsDTO();
            contactDetail1.setContact("983949386");
            contactDetail1.setContactType("MOBILE_NUMBER");
            ContactDetailsDTO contactDetail2 = new ContactDetailsDTO();
            contactDetail2.setContact("example@bbva.com");
            contactDetail2.setContactType("EMAIL");
            ContactDetailsDTO contactDetail3 = new ContactDetailsDTO();
            contactDetail3.setContact("012243985");
            contactDetail3.setContactType("PHONE_NUMBER");
            contactDetailList.add(contactDetail1);
            contactDetailList.add(contactDetail2);
            contactDetailList.add(contactDetail3);
            participant.setContactDetails(contactDetailList);
        AddressesDTO addressesDTO = new AddressesDTO();
        List<AddressesDTO> addressesDTOList = new ArrayList<>();
        addressesDTO.setFormattedAddress("CAL CIRCUNVALACION BRENA 200, AHH LOS NARANJOS");
        LocationDTO locationDTO = new LocationDTO();
        AddressComponentsDTO addressComponentsDTO1 = new AddressComponentsDTO();
        addressComponentsDTO1.setComponentTypes(Collections.singletonList("STREET"));
        addressComponentsDTO1.setName("CIRCUNVALACION BRENA");
        AddressComponentsDTO addressComponentsDTO2 = new AddressComponentsDTO();
        addressComponentsDTO2.setComponentTypes(Collections.singletonList("AAHH"));
        addressComponentsDTO2.setName("LOS NARANJOS");
        AddressComponentsDTO addressComponentsDTO3 = new AddressComponentsDTO();
        addressComponentsDTO3.setComponentTypes(Collections.singletonList("DEPARTMENT"));
        addressComponentsDTO3.setName("LIMA");
        AddressComponentsDTO addressComponentsDTO4 = new AddressComponentsDTO();
        addressComponentsDTO4.setComponentTypes(Collections.singletonList("PROVINCE"));
        addressComponentsDTO4.setName("LIMA");
        AddressComponentsDTO addressComponentsDTO5 = new AddressComponentsDTO();
        addressComponentsDTO5.setComponentTypes(Collections.singletonList("DISTRICT"));
        addressComponentsDTO5.setName("CHORRILLOS");
        AddressComponentsDTO addressComponentsDTO6 = new AddressComponentsDTO();
        addressComponentsDTO6.setComponentTypes(Collections.singletonList("EXTERIOR_NUMBER"));
        addressComponentsDTO6.setName("200");
        AddressComponentsDTO addressComponentsDTO7 = new AddressComponentsDTO();
        addressComponentsDTO7.setComponentTypes(Collections.singletonList("UBIGEO"));
        addressComponentsDTO7.setName("0101009");

        List<AddressComponentsDTO> addressComponentsDTOList = new ArrayList<>();
        addressComponentsDTOList.add(addressComponentsDTO1);
        addressComponentsDTOList.add(addressComponentsDTO2);
        addressComponentsDTOList.add(addressComponentsDTO3);
        addressComponentsDTOList.add(addressComponentsDTO4);
        addressComponentsDTOList.add(addressComponentsDTO5);
        addressComponentsDTOList.add(addressComponentsDTO6);
        addressComponentsDTOList.add(addressComponentsDTO7);
        locationDTO.setAddressComponent(addressComponentsDTOList);
        addressesDTO.setLocation(locationDTO);
        addressesDTOList.add(addressesDTO);
        participant.setAddresses(addressesDTOList);
        return participant;

    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipants(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "78392872","NATURAL",true);
        ParticipantsDTO participant2 = buildParticipant("INSURED","DNI", "78392172","NATURAL",true);
        ParticipantsDTO participant3 = buildParticipant("CONTRACTOR","DNI", "78394872","NATURAL",true);
        participantsList.add(participant1);
        participantsList.add(participant2);
        participantsList.add(participant3);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipantsLifeCase1(){
InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "78394872","NATURAL",true);
        ParticipantsDTO participant2 = buildParticipant("INSURED","DNI", "78394872","NATURAL",true);
        participantsList.add(participant1);
        participantsList.add(participant2);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipantsLifeCase2(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "78394872","NATURAL",true);
        participantsList.add(participant1);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipantsTwoLifeCase(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "46716129","NATURAL", true);
        ParticipantsDTO participant3 = buildParticipant("INSURED","DNI", "00002023","NATURAL",false);
        participant3.getPerson().setFirstName(null);
        participant3.getPerson().setSecondLastName(null);
        participantsList.add(participant1);
        participantsList.add(participant3);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipantsOne(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "46716129","NATURAL", true);
        participantsList.add(participant1);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static QuotationCustomerDAO buildFindQuotationJoinByPolicyQuotaInternalId(String participantPersonalId, String businessName, String productType){
        QuotationCustomerDAO quotationJoinInformation = new QuotationCustomerDAO();

        QuotationDAO quotationEntity = new QuotationDAO();
        QuotationModDAO quotationModEntity = new QuotationModDAO();
        InsuranceProductDAO insuranceProductEntity = new InsuranceProductDAO();
        InsuranceBusinessDAO insuranceBusinessEntity = new InsuranceBusinessDAO();
        InsuranceCompanyDAO insuranceCompanyDAO = new InsuranceCompanyDAO();

        quotationEntity.setInsuredCustomerName("customer name");
        quotationEntity.setClientLasName("client last name");
        quotationEntity.setInsuranceCompanyQuotaId("b5add021-a825-4ba1-a455-95e11015cff7");
        quotationEntity.setParticipantPersonalId(participantPersonalId);
        quotationModEntity.setContactEmailDesc("example@bbva");
        quotationModEntity.setCustomerPhoneDesc("CUSTOMER_PHONE_DESC");
        quotationModEntity.setInsuranceProductId(new BigDecimal(1));
        quotationModEntity.setInsuranceModalityType("02");

        insuranceProductEntity.setInsuranceProductType(productType);
        insuranceProductEntity.setInsuranceProductId(new BigDecimal(1));
        insuranceProductEntity.setInsuranceProductDesc("VIDAINVERSION");
        insuranceBusinessEntity.setInsuranceBusinessName(businessName);

        insuranceCompanyDAO.setInsuranceCompanyId(new BigDecimal(1));

        quotationJoinInformation.setQuotation(quotationEntity);
        quotationJoinInformation.setQuotationMod(quotationModEntity);
        quotationJoinInformation.setInsuranceProduct(insuranceProductEntity);
        quotationJoinInformation.setInsuranceBusiness(insuranceBusinessEntity);
        quotationJoinInformation.setInsuranceCompanyDAO(insuranceCompanyDAO);
        return quotationJoinInformation;
    }

    public static PEWUResponse buildPersonHostDataResponse(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("AV.");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("AGR");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("150");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("D");
        pemsalwu.setLote("4");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase2(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("X");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("NA");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("NA");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("");
        pemsalwu.setNroint1("");
        pemsalwu.setManzana("D");
        pemsalwu.setLote("4");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase3(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("AV.");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("AGR");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("");
        pemsalwu.setNroint1("");
        pemsalwu.setManzana("D");
        pemsalwu.setLote("4");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase4() {
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("NA");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("150");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase5(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("NA");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("AGR");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("150");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase6(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("AV.");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("NA");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("123");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase7(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("AV.");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("NA");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase8(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("AGR");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("134");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static PEWUResponse buildPersonHostDataResponseCase9(){
        PEWUResponse pewuResponse = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("00932622");
        pemsalwu.setNroclie("77809762");
        pemsalwu.setFechav("2022-02-17");
        pemsalwu.setFechaal("2002-01-02");
        pemsalwu.setTipoper("F00");
        pemsalwu.setOficina("0199");
        pemsalwu.setTiponac("N");
        pemsalwu.setTipores("R");
        pemsalwu.setApellip("SALAS");
        pemsalwu.setApellim("FASABI");
        pemsalwu.setNombres("NATIVIDAD");
        pemsalwu.setTitulo("ARQ.");
        pemsalwu.setEstadoc("S");
        pemsalwu.setSexo("F");
        pemsalwu.setFechan("1968-02-05");
        pemsalwu.setPaisn("PER");
        pemsalwu.setPaisd1("paisd1");
        pemsalwu.setPaisre("PER");
        pemsalwu.setPaisna("PER");
        pemsalwu.setFechare("2010-12-0");
        pemsalwu.setCodact("");
        pemsalwu.setOcupaci("ASA");
        pemsalwu.setCentro("BBVA");
        pemsalwu.setSegment("80500");
        pemsalwu.setDescseg("PRIVADA");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("969100232");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
        pemsalwu.setTipodir("H");
        pemsalwu.setIdendi1("");
        pemsalwu.setNombdi1("LAS FLORES");
        pemsalwu.setIdendi2("AGR");
        pemsalwu.setNombdi2("TEST");
        pemsalwu.setDetalle("OCVALO");
        pemsalwu.setCodigod("01");
        pemsalwu.setCodigop("01");
        pemsalwu.setCodigdi("025");
        pemsalwu.setFedocac("2010-12-01");
        pemsalwu.setNroext1("");
        pemsalwu.setNroint1("201");
        pemsalwu.setManzana("");
        pemsalwu.setLote("");
        pemsalwu.setCuadran("");
        pewuResponse.setPemsalwu(pemsalwu);

        PEMSALW4 pemsalw4 = new PEMSALW4();
        pemsalw4.setDepetdo("HOGAR");
        pemsalw4.setDescvia("AVENIDA");
        pemsalw4.setDescurb("AGRUPACION");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("LIMA");
        pemsalw4.setDesdist("RIMAC");

        pewuResponse.setPemsalw4(pemsalw4);

        return pewuResponse;
    }

    public static List<RolDAO> buildRolByParticipantTypeResponse(){

        List<RolDAO> listResponseDb = new ArrayList<>();

        RolDAO line1 = new RolDAO();
        line1.setParticipantRoleId(new Integer(7));
        line1.setInsuranceCompanyRoleId("23");
        listResponseDb.add(line1);
        RolDAO line2 = new RolDAO();
        line2.setParticipantRoleId(new Integer(2));
        line2.setInsuranceCompanyRoleId("9");
        listResponseDb.add(line2);
        RolDAO line3 = new RolDAO();
        line3.setParticipantRoleId(new Integer(1));
        line3.setInsuranceCompanyRoleId("8");
        listResponseDb.add(line3);

        return listResponseDb;

    }

    public static InputParticipantsDTO getMockRequestBodyValidateLegalParticipants(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","RUC", "201928394221","LEGAL", true);
        ParticipantsDTO participant2 = buildParticipant("INSURED","RUC", "201928394221","LEGAL", true);
        ParticipantsDTO participant3 = buildParticipant("INSURED","RUC", "201928394221","NATURAL",true);
        participantsList.add(participant1);
        participantsList.add(participant2);
        participantsList.add(participant3);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }

    public static InputParticipantsDTO getMockRequestBodyValidateNaturalParticipantWithBusiness(){
        InputParticipantsDTO requestBody = new InputParticipantsDTO();
        requestBody.setQuotationId("0123489304");
        requestBody.setChannelId("PC");
        requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
        List<ParticipantsDTO> participantsList = new ArrayList<>();
        ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","RUC", "101928394221","NATURAL", true);
        ParticipantsDTO participant2 = buildParticipant("INSURED","RUC", "101928394221","NATURAL", true);
        ParticipantsDTO participant3 = buildParticipant("INSURED","RUC", "101928394221","NATURAL",true);
        participantsList.add(participant1);
        participantsList.add(participant2);
        participantsList.add(participant3);
        requestBody.setParticipants(participantsList);
        return requestBody;
    }
}
