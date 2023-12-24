package com.bbva.rbvd.mock;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW5;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.*;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insurance.commons.*;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockHelper {
    public static PEWUResponse getPEWUResponseMock() {
        PEWUResponse out = new PEWUResponse();
        PEMSALWU pemsalwu = new PEMSALWU();
        PEMSALW4 pemsalw4 = new PEMSALW4();
        PEMSALW5 pemsalw5 = new PEMSALW5();
        pemsalwu.setTdoi("L");
        pemsalwu.setNdoi("99999999");
        pemsalwu.setNombres("Albert");
        pemsalwu.setApellip("Rodriguez");
        pemsalwu.setApellim("Mendoza");
        pemsalwu.setFechan("2019-08-21");
        pemsalwu.setSexo("M");
        pemsalwu.setCodigod("LIMA");
        pemsalwu.setCodigop("COVLAS");
        pemsalwu.setCodigod("MANZANAS");
        pemsalwu.setNroext1("15");
        pemsalwu.setNroint1("15");
        pemsalwu.setManzana("C");
        pemsalwu.setLote("3");
        pemsalwu.setIdendi1("ALM");
        pemsalwu.setIdendi2("AHH");
        pemsalwu.setIdencon("");
        pemsalwu.setTipocon("");
        pemsalwu.setContact("");
        pemsalwu.setVerific("");
        pemsalwu.setIdenco2("C001956783678");
        pemsalwu.setTipoco2("MV");
        pemsalwu.setContac2("956783678");
        pemsalwu.setVerifi2("false");
        pemsalwu.setIdenco3("EMAIL");
        pemsalwu.setTipoco3("MA");
        pemsalwu.setContac3("albert.rod@BBVA.COM");
        pemsalwu.setVerifi3("S");
        pemsalw4.setDesdept("LIMA");
        pemsalw4.setDesprov("COVLAS");
        pemsalw4.setDesdist("MANZANAS");
        pemsalw5.setDescmco("SIN TELEFONO FIJO");
        pemsalw5.setDescmc1("TELEFONO MOVIL");
        pemsalw5.setDescmc2("PERSONAL");
        out.setPemsalwu(pemsalwu);
        out.setPemsalw4(pemsalw4);
        out.setPemsalw5(pemsalw5);
        return out;
    }
    public static ParticipantsDTO getParticipantsDTOMock() {
        ParticipantsDTO participant =new ParticipantsDTO();
        List <ContactDetailsDTO> contactDetailsDTOList = new ArrayList<>();
        List <AddressesDTO> addressesDTOList = new ArrayList<>();
        List <AddressComponentsDTO> addressComponentsDTOList = new ArrayList<>();

        ContactDTO contact = new ContactDTO();
        contact.setContactDetailType("EMAIL");
        contact.setAddress("roger.espinoza@gmail.com");
        contactDetailsDTOList.add(new ContactDetailsDTO());
        contactDetailsDTOList.get(0).setContact(contact);

        ContactDTO contact2 = new ContactDTO();
        contact2.setContactDetailType("PHONE");
        contact2.setAddress("999888777");
        contactDetailsDTOList.add(new ContactDetailsDTO());
        contactDetailsDTOList.get(1).setContact(contact2);

        AddressComponentsDTO addressComponents = new AddressComponentsDTO();
        addressComponents.setCode("001");
        addressComponents.setName("lima");
        List <String> componetTypes = new ArrayList<>();
        componetTypes.add("01");
        addressComponents.setComponentTypes(componetTypes);
        addressComponentsDTOList.add(addressComponents);
        addressesDTOList.add(new AddressesDTO());
        addressesDTOList.get(0).setLocation(new LocationDTO());
        addressesDTOList.get(0).getLocation().setAddressComponents(addressComponentsDTOList);

        participant.setContactDetails(contactDetailsDTOList);
        participant.setAddresses(addressesDTOList);
        participant.setDocument(new DocumentDTO());
        participant.getDocument().setDocumentType(new DocumentTypeDTO());
        participant.getDocument().getDocumentType().setId("DNI");
        participant.getDocument().setDocumentNumber("110022681");
        participant.setBirthDate(new Date());
        participant.setGender(new GenderDTO());
        participant.getGender().setId("F");

        return participant;
    }
    public static BusinessASO getBusinessASOMock() {
        BusinessASO business = new BusinessASO();
        business.setBusinessDocuments(new ArrayList<>());
        BusinessDocumentASO businessDocumentASO = new BusinessDocumentASO();
        businessDocumentASO.setDocumentNumber("000111222333");
        business.getBusinessDocuments().add(businessDocumentASO);
        business.setLegalName("Banco Nacional");
        business.setFormation(new FormationASO());
        business.getFormation().setCountry(new CountryASO());
        business.getFormation().getCountry().setName("001");
        business.getFormation().setDate(new LocalDate());
        business.setAnnualSales(new SaleASO());
        business.getAnnualSales().setStartDate(new LocalDate());
        business.setBusinessGroup(new BusinessGroupASO());
        business.getBusinessGroup().setId("011");
        business.setEconomicActivity(new EconomicActivityASO());
        business.getEconomicActivity().setId("011");
        return business;
    }
}
