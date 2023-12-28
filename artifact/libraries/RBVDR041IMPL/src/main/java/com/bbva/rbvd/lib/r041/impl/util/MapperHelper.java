package com.bbva.rbvd.lib.r041.impl.util;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.*;
import com.bbva.rbvd.dto.insurance.commons.AddressComponentsDTO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class MapperHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapperHelper.class);
    private static final String TAG_OTROS = "OTROS";
    private static final String FIELD_BLANK = "";

    private static final String EMAIL_VALUE = "EMAIL";
    private static final String PHONE_NUMBER_VALUE = "PHONE";

    public static PersonaBO convertLisCustomerToPerson(PEWUResponse listCustomer) {
        LOGGER.info("[RequestRimacBean] convertListPersons() :: Start");

        PersonaBO personaDTO = new PersonaBO();
        personaDTO.setTipoDocumento(listCustomer.getPemsalwu().getTdoi());
        personaDTO.setNroDocumento(listCustomer.getPemsalwu().getNdoi());
        personaDTO.setApePaterno(listCustomer.getPemsalwu().getApellip());
        personaDTO.setApeMaterno(listCustomer.getPemsalwu().getApellim());
        personaDTO.setNombres(listCustomer.getPemsalwu().getNombres());
        personaDTO.setFechaNacimiento(listCustomer.getPemsalwu().getFechan());
        personaDTO.setSexo(listCustomer.getPemsalwu().getSexo());
        personaDTO.setCorreoElectronico(listCustomer.getPemsalwu().getContact());
        personaDTO.setCelular(listCustomer.getPemsalwu().getContac2());

        personaDTO.setDistrito(listCustomer.getPemsalw4().getDesdist());
        personaDTO.setProvincia(listCustomer.getPemsalw4().getDesprov());
        personaDTO.setDepartamento(listCustomer.getPemsalw4().getDepetdo());
        personaDTO.setTipoVia(listCustomer.getPemsalw4().getDescvia());
        personaDTO.setNombreVia(listCustomer.getPemsalw4().getDescvia());
        personaDTO.setNumeroVia(listCustomer.getPemsalw4().getDescvia());
        personaDTO.setUbigeo(listCustomer.getPemsalw4().getCodpost()); //Concatenado desde host
        personaDTO.setDireccion(listCustomer.getPemsalw4().getIdestad()); //Concatenado pasarle lógica a Host

        personaDTO.setRol(8);


        LOGGER.info("[RequestRimacBean] convertListPersons() :: End");
        LOGGER.info("[RequestRimacBean] personData{}", personaDTO);
        return personaDTO;
    }
    public static OrganizacionBO convertBusinessToOrganization(final BusinessASO business, ParticipantsDTO participant) {

        ContactDetailsDTO correoSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(EMAIL_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        ContactDetailsDTO celularSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(PHONE_NUMBER_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        AddressComponentsDTO distrito = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO provincia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO departamento = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO ubigeo = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO nombreVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO tipoVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO numeroVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());

        OrganizacionBO organizacion = new OrganizacionBO();
        organizacion.setDireccion(participant.getAddresses().get(0).getLocation().getFormattedAddress());
        organizacion.setRol(0000);
        organizacion.setTipoDocumento("R");
        organizacion.setNroDocumento(business.getBusinessDocuments().get(0).getDocumentNumber());
        organizacion.setRazonSocial(business.getLegalName());
        organizacion.setNombreComercial(business.getLegalName());
        if(Objects.nonNull(business.getFormation())) {
            organizacion.setPaisOrigen(business.getFormation().getCountry().getName());
            organizacion.setFechaConstitucion(business.getFormation().getDate());
        } else {
            organizacion.setPaisOrigen("PERU");
            organizacion.setFechaConstitucion(null);
        }
        organizacion.setFechaInicioActividad(Objects.isNull(business.getAnnualSales()) ? null : business.getAnnualSales().getStartDate());
        organizacion.setTipoOrganizacion(Objects.isNull(business.getBusinessGroup()) ? FIELD_BLANK : business.getBusinessGroup().getId());
        organizacion.setGrupoEconomico(TAG_OTROS);
        organizacion.setCiiu(Objects.isNull(business.getEconomicActivity()) ? FIELD_BLANK : business.getEconomicActivity().getId());
//        organizacion.setTelefonoFijo(fijo);
        organizacion.setCelular(celularSelect.getContact().getNumber());
        organizacion.setCorreoElectronico(correoSelect.getContact().getAddress());
        organizacion.setDistrito(distrito.getName());
        organizacion.setProvincia(provincia.getName());
        organizacion.setDepartamento(departamento.getName());
        organizacion.setUbigeo(ubigeo.getName());
        organizacion.setNombreVia(nombreVia.getName());
        organizacion.setTipoVia(tipoVia.getName());
        organizacion.setNumeroVia(numeroVia.getName());
        organizacion.setTipoPersona("JURIDICO");
        return  organizacion;
    }

    public static PersonaBO convertParticipantToPerson(ParticipantsDTO participant) {
        LOGGER.info("[RequestRimacBean] convertListPersons() :: Start");
        ContactDetailsDTO correoSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(EMAIL_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        ContactDetailsDTO celularSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(PHONE_NUMBER_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        AddressComponentsDTO distrito = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO provincia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO departamento = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO ubigeo = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO nombreVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO tipoVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO numeroVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());

        PersonaBO personaDTO = new PersonaBO();
        personaDTO.setTipoDocumento(participant.getDocument().getDocumentType().getId());
        personaDTO.setNroDocumento(participant.getDocument().getDocumentNumber());
        personaDTO.setApePaterno(participant.getFirstName());
        personaDTO.setApeMaterno(participant.getMiddleName());
        personaDTO.setNombres(participant.getLastName());
        personaDTO.setFechaNacimiento(participant.getBirthDate().toString());
        personaDTO.setSexo(participant.getGender().getId());
        personaDTO.setCorreoElectronico(correoSelect.getContact().getAddress());
        personaDTO.setCelular(celularSelect.getContact().getNumber());

        personaDTO.setDistrito(distrito.getName());
        personaDTO.setProvincia(provincia.getName());
        personaDTO.setDepartamento(departamento.getName());
        personaDTO.setTipoVia(tipoVia.getName());
        personaDTO.setNombreVia(nombreVia.getName());
        personaDTO.setNumeroVia(numeroVia.getName());
        personaDTO.setUbigeo(ubigeo.getName()); //Concatenado desde host
        personaDTO.setDireccion(participant.getAddresses().get(0).getLocation().getFormattedAddress()); //Concatenado pasarle lógica a Host

        personaDTO.setRol(8);


        LOGGER.info("[RequestRimacBean] convertListPersons() :: End");

        return personaDTO;
    }
    public static OrganizacionBO convertParticipantToOrganization(ParticipantsDTO participant) {

        ContactDetailsDTO correoSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(EMAIL_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        ContactDetailsDTO celularSelect= participant.getContactDetails().stream().
                filter(contactDetail -> contactDetail.getContact().getContactDetailType().equals(PHONE_NUMBER_VALUE)).findFirst().orElse(new ContactDetailsDTO());

        AddressComponentsDTO distrito = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO provincia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO departamento = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO ubigeo = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO nombreVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO tipoVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());
        AddressComponentsDTO numeroVia = participant.getAddresses().get(0).getLocation().getAddressComponents().stream().
                filter(addressComponents -> addressComponents.getComponentTypes().get(0).equals("")).findFirst().orElse(new AddressComponentsDTO());

        OrganizacionBO organizacion = new OrganizacionBO();
        organizacion.setDireccion(participant.getAddresses().get(0).getLocation().getFormattedAddress());
        organizacion.setRol(0000);
        organizacion.setTipoDocumento("R");
        organizacion.setNroDocumento(participant.getDocument().getDocumentNumber());
        //organizacion.setRazonSocial();
        //organizacion.setNombreComercial(business.getLegalName());
//        if(false) {
//            organizacion.setPaisOrigen(business.getFormation().getCountry().getName());
//            organizacion.setFechaConstitucion(business.getFormation().getDate());
//        } else {
//            organizacion.setPaisOrigen("PERU");
//            organizacion.setFechaConstitucion(null);
//        }
//        organizacion.setFechaInicioActividad(Objects.isNull(business.getAnnualSales()) ? null : business.getAnnualSales().getStartDate());
//        organizacion.setTipoOrganizacion(Objects.isNull(business.getBusinessGroup()) ? FIELD_BLANK : business.getBusinessGroup().getId());
//        organizacion.setCiiu(Objects.isNull(business.getEconomicActivity()) ? FIELD_BLANK : business.getEconomicActivity().getId());
//        organizacion.setTelefonoFijo(fijo);
        organizacion.setGrupoEconomico(TAG_OTROS);
        organizacion.setCelular(celularSelect.getContact().getNumber());
        organizacion.setCorreoElectronico(correoSelect.getContact().getAddress());
        organizacion.setDistrito(distrito.getName());
        organizacion.setProvincia(provincia.getName());
        organizacion.setDepartamento(departamento.getName());
        organizacion.setUbigeo(ubigeo.getName());
        organizacion.setNombreVia(nombreVia.getName());
        organizacion.setTipoVia(tipoVia.getName());
        organizacion.setNumeroVia(numeroVia.getName());
        organizacion.setTipoPersona("JURIDICO");
        return  organizacion;
    }
    
}
