package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class AddPersonRimac {

    public static void  addPerson(final List<PersonaBO> personaList, List<ParticipantsDTO> participants){
        if(personaList.size()==1){
            PersonaBO personMnager = personaList.get(0);
            PersonaBO personContractor = new PersonaBO();
            personContractor.setNombres(personMnager.getNombres());
            personContractor.setApePaterno(personMnager.getApePaterno());
            personContractor.setApeMaterno(personMnager.getApeMaterno());
            personContractor.setTipoDocumento(personMnager.getTipoDocumento());
            personContractor.setNroDocumento(personMnager.getNroDocumento());
            personContractor.setFechaNacimiento(personMnager.getFechaNacimiento());
            personContractor.setSexo(personMnager.getSexo());
            personContractor.setCorreoElectronico(personMnager.getCorreoElectronico());
            personContractor.setRol(ConstantsUtil.Rol.CONTRACTOR.getValue());
            personContractor.setCelular(personMnager.getCelular());

            personContractor.setTipoVia(personMnager.getTipoVia());
            personContractor.setNombreVia(personMnager.getNombreVia());
            personContractor.setNumeroVia(personMnager.getNumeroVia());
            personContractor.setDistrito(personMnager.getDistrito());
            personContractor.setProvincia(personMnager.getProvincia());
            personContractor.setDepartamento(personMnager.getDepartamento());
            personContractor.setDireccion(personMnager.getDireccion());
            //set insured
            ParticipantsDTO participans = participants.get(0);
            PersonaBO personaInsured = new PersonaBO();
            String nomb = participans.getPerson().getFirstName().concat(StringUtils.isEmpty(participans.getPerson().getMiddleName())?"":" ".concat(participans.getPerson().getMiddleName()));
            Map<String,Object> contacDetails = getContactGroup(participans.getContactDetails());
            personaInsured.setNombres(nomb);
            personaInsured.setApePaterno(participans.getPerson().getLastName());
            personaInsured.setApeMaterno(participans.getPerson().getSecondLastName());
            personaInsured.setTipoDocumento(participans.getIdentityDocuments().get(0).getDocumentType().getId());
            personaInsured.setNroDocumento(participans.getIdentityDocuments().get(0).getValue());
            personaInsured.setFechaNacimiento(String.valueOf(participans.getPerson().getBirthDate()));
            personaInsured.setSexo(participans.getPerson().getGender().getId().equalsIgnoreCase("MALE")?"M":"L");
            personaInsured.setCorreoElectronico((String) contacDetails.get(ConstantsUtil.ContactDetails.EMAIL));
            personaInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
            personaInsured.setCelular((String) contacDetails.get(ConstantsUtil.ContactDetails.MOBILE_NUMBER));

            personaInsured.setTipoVia(personContractor.getTipoVia());
            personaInsured.setNombreVia(personContractor.getNombreVia());
            personaInsured.setDistrito(personContractor.getDistrito());
            personaInsured.setProvincia(personContractor.getProvincia());
            personaInsured.setDepartamento(personContractor.getDepartamento());
            personaInsured.setDireccion(personContractor.getDireccion());

            personaList.add(personContractor);
            personaList.add(personaInsured);


        } else if (personaList.size()==2) {
            ParticipantsDTO participans = participants.get(0);
            PersonaBO personaInsured = new PersonaBO();
            String nomb = participans.getPerson().getFirstName().concat(StringUtils.isEmpty(participans.getPerson().getMiddleName())?"":" ".concat(participans.getPerson().getMiddleName()));
            Map<String,Object> contacDetails = getContactGroup(participans.getContactDetails());
            personaInsured.setNombres(nomb);
            personaInsured.setApePaterno(participans.getPerson().getLastName());
            personaInsured.setApeMaterno(participans.getPerson().getSecondLastName());
            personaInsured.setTipoDocumento(participans.getIdentityDocuments().get(0).getDocumentType().getId());
            personaInsured.setNroDocumento(participans.getIdentityDocuments().get(0).getValue());
            personaInsured.setFechaNacimiento(String.valueOf(participans.getPerson().getBirthDate()));
            personaInsured.setSexo(participans.getPerson().getGender().getId().equalsIgnoreCase("MALE")?"M":"L");
            personaInsured.setCorreoElectronico((String) contacDetails.get(ConstantsUtil.ContactDetails.EMAIL));
            personaInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
            personaInsured.setCelular((String) contacDetails.get(ConstantsUtil.ContactDetails.MOBILE_NUMBER));

            personaInsured.setTipoVia(personaList.get(0).getTipoVia());
            personaInsured.setNombreVia(personaList.get(0).getNombreVia());
            personaInsured.setNumeroVia(personaList.get(0).getNumeroVia());
            personaInsured.setDistrito(personaList.get(0).getDistrito());
            personaInsured.setProvincia(personaList.get(0).getProvincia());
            personaInsured.setDepartamento(personaList.get(0).getDepartamento());
            personaInsured.setDireccion(personaList.get(0).getDireccion());
            personaList.add(personaInsured);
        }
    }

    private static Map<String,Object> getContactGroup(List<ContactDetailsDTO> contacts){
        Map<String,Object> mapContac = new HashMap<>();
        contacts.forEach(contact -> {
            if(ConstantsUtil.ContactDetails.EMAIL.equalsIgnoreCase(contact.getContactType())){
                mapContac.put(ConstantsUtil.ContactDetails.EMAIL,contact.getContact());
            } else if (ConstantsUtil.ContactDetails.MOBILE_NUMBER.equalsIgnoreCase(contact.getContactType())) {
                mapContac.put(ConstantsUtil.ContactDetails.MOBILE_NUMBER,contact.getContact());
            } else if (ConstantsUtil.ContactDetails.PHONE_NUMBER.equalsIgnoreCase(contact.getContactType())) {
                mapContac.put(ConstantsUtil.ContactDetails.PHONE_NUMBER,contact.getContact());
            }
        });
        return mapContac;
    }

}
