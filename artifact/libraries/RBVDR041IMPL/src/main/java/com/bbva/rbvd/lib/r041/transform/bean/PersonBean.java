package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;
import com.bbva.rbvd.lib.r041.business.addThird.customer.CustomerContactBusiness;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.model.ContactBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

import static com.bbva.rbvd.lib.r041.util.ConvertUtil.toLocalDate;

public class PersonBean {

    public static PersonaBO buildPersonFromManager(PersonaBO personManager, ConstantsUtil.Rol contractor) {
        PersonaBO personContractor = new PersonaBO();
        personContractor.setNombres(personManager.getNombres());
        personContractor.setApePaterno(personManager.getApePaterno());
        personContractor.setApeMaterno(personManager.getApeMaterno());
        personContractor.setTipoDocumento(personManager.getTipoDocumento());
        personContractor.setNroDocumento(personManager.getNroDocumento());
        personContractor.setFechaNacimiento(personManager.getFechaNacimiento());
        personContractor.setSexo(personManager.getSexo());
        personContractor.setCorreoElectronico(personManager.getCorreoElectronico());
        personContractor.setRol(contractor.getValue());
        personContractor.setCelular(personManager.getCelular());

        personContractor.setTipoVia(personManager.getTipoVia());
        personContractor.setNombreVia(personManager.getNombreVia());
        personContractor.setNumeroVia(personManager.getNumeroVia());
        personContractor.setDistrito(personManager.getDistrito());
        personContractor.setProvincia(personManager.getProvincia());
        personContractor.setDepartamento(personManager.getDepartamento());
        personContractor.setDireccion(personManager.getDireccion());
        return personContractor;
    }

    public static PersonaBO buildPersonNonCustomerFromLifeDB(QuotationLifeDAO participant, PersonaBO personManager){
        PersonaBO personNonCustomer = new PersonaBO();
        String apellidos = participant.getClientLastName();
        String apPaterno="";
        String apMaterno="";

        if(StringUtils.isNotEmpty(apellidos)){
            int index = apellidos.indexOf(ConstantsUtil.Delimeter.VERTICAL_BAR);
            apPaterno = apellidos.substring(ConstantsUtil.Number.CERO,index);
            apMaterno = apellidos.substring(index+ConstantsUtil.Number.UNO);
        }
        String fechaNacimiento = participant.getCustomerBirthDate();
        if(StringUtils.isNotEmpty(fechaNacimiento)){
            fechaNacimiento = fechaNacimiento.substring(ConstantsUtil.Number.CERO,ConstantsUtil.Number.DIEZ);
        }

        personNonCustomer.setNombres(participant.getInsuredCustomerName());
        personNonCustomer.setApePaterno(apPaterno);
        personNonCustomer.setApeMaterno(apMaterno);

        personNonCustomer.setTipoDocumento(participant.getCustomerDocumentType());
        personNonCustomer.setNroDocumento(participant.getPersonalId());

        personNonCustomer.setFechaNacimiento(fechaNacimiento);
        personNonCustomer.setSexo(participant.getGenderId());
        personNonCustomer.setCorreoElectronico(participant.getUserEmailPersonalDesc());
        personNonCustomer.setRol(ConstantsUtil.Rol.INSURED.getValue());
        personNonCustomer.setCelular(participant.getPhoneId());

        personNonCustomer.setTipoVia(ValidationUtil.validateAllVia(personManager.getTipoVia()));
        personNonCustomer.setNombreVia(ValidationUtil.validateAllVia(personManager.getNombreVia()));
        personNonCustomer.setNumeroVia(ValidationUtil.validateAllVia(personManager.getNumeroVia()));

        personNonCustomer.setDistrito(personManager.getDistrito());
        personNonCustomer.setProvincia(personManager.getProvincia());
        personNonCustomer.setDepartamento(personManager.getDepartamento());
        personNonCustomer.setDireccion(personManager.getDireccion());

        return personNonCustomer;
    }

    public static PersonaBO getPersonFromPEWU(ParticipantsDTO participant, PEWUResponse customer, QuotationCustomerDAO customerInformationDb, Integer roleId, AddressBO addressBO)  {
        PersonaBO persona = new PersonaBO();

        ContactBO customerContact = CustomerContactBusiness.getContactToCustomerBO(participant, customer, customerInformationDb);
        persona.setCorreoElectronico(customerContact.getEmail());
        persona.setCelular(customerContact.getMobile());

        persona.setTipoDocumento(participant.getIdentityDocuments().get(0).getDocumentType().getId());
        persona.setNroDocumento((ConstantsUtil.PERSONAL_DATA.RUC_ID.equalsIgnoreCase(persona.getTipoDocumento())?participant.getIdentityDocuments().get(0).getValue():customer.getPemsalwu().getNdoi()));
        persona.setApePaterno(ValidationUtil.validateSN(customer.getPemsalwu().getApellip()));

        persona.setApeMaterno(StringUtils.defaultString(customer.getPemsalwu().getApellim()).trim().length()  > ConstantsUtil.PERSONAL_DATA.MAX_CHARACTER ? ValidationUtil.validateSN(customer.getPemsalwu().getApellim()) : StringUtils.EMPTY);

        persona.setNombres(ValidationUtil.validateSN(customer.getPemsalwu().getNombres()));
        persona.setFechaNacimiento(customer.getPemsalwu().getFechan());
        persona.setSexo(customer.getPemsalwu().getSexo());

        persona.setTipoPersona(ValidationUtil.getPersonType(persona).getCode());
        persona.setRol(Objects.isNull(roleId)?null:roleId);

        persona.setDireccion(addressBO.getDireccion());
        persona.setDistrito(addressBO.getDistrito());
        persona.setProvincia(addressBO.getProvincia());
        persona.setDepartamento(addressBO.getDepartamento());
        persona.setUbigeo(addressBO.getUbigeo());
        persona.setNombreVia(addressBO.getNombreVia());
        persona.setTipoVia(addressBO.getTipoVia());
        persona.setNumeroVia(addressBO.getNumeroVia());
        persona.setTipoPersona(addressBO.getTipoPersona());

        return persona;
    }

    public static PersonaBO getPersonWithInputData(Integer rolId, ContactBO contactBO, AddressBO addressBO, PersonDTO person, IdentityDocumentDTO identityDocumentDTO) {
        PersonaBO personaBO = new PersonaBO();
        personaBO.setTipoDocumento(identityDocumentDTO.getDocumentType().getId());
        personaBO.setNroDocumento(identityDocumentDTO.getValue());
        personaBO.setApePaterno(person.getLastName());
        personaBO.setApeMaterno(person.getSecondLastName());
        personaBO.setNombres(person.getFirstName().concat(
                Objects.nonNull(person.getMiddleName()) ?
                        " ".concat(person.getMiddleName()):StringUtils.EMPTY));
        personaBO.setFechaNacimiento(String.valueOf(toLocalDate(person.getBirthDate())));
        personaBO.setSexo(person.getGender().getId().equals("MALE") ? "M" : "F");
        personaBO.setTipoPersona(ValidationUtil.getPersonType(personaBO).getCode());

        personaBO.setRol(rolId);


        personaBO.setCorreoElectronico(contactBO.getEmail());
        personaBO.setCelular(contactBO.getMobile());
        personaBO.setDireccion(addressBO.getDireccion());
        personaBO.setDistrito(addressBO.getDistrito());
        personaBO.setProvincia(addressBO.getProvincia());
        personaBO.setDepartamento(addressBO.getDepartamento());
        personaBO.setUbigeo(addressBO.getUbigeo());
        personaBO.setTipoVia(addressBO.getTipoVia());
        personaBO.setNombreVia(addressBO.getNombreVia());
        personaBO.setNumeroVia(addressBO.getNumeroVia());
        return personaBO;
    }

    public static PersonaBO buildDirectionDataFromPerson(PersonaBO personToEnrich, PersonaBO person) {
        personToEnrich.setDireccion(person.getDireccion());
        personToEnrich.setDistrito(person.getDistrito());
        personToEnrich.setProvincia(person.getProvincia());
        personToEnrich.setDepartamento(person.getDepartamento());
        personToEnrich.setUbigeo(person.getUbigeo());
        personToEnrich.setTipoVia(person.getTipoVia());
        personToEnrich.setNombreVia(person.getNombreVia());
        personToEnrich.setNumeroVia(person.getNumeroVia());
        return person;
    }

    private PersonBean() {
    }
}
