package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPersonRimac {

    public static void  addPerson(List<PersonaBO> personaList, List<ParticipantsDTO> participants){
        if(personaList.size()==1){
            PersonaBO personaContractor = personaList.get(0);
            personaContractor.setRol(ConstantsUtil.Rol.CONTRACTOR.getValue());
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

            personaInsured.setTipoVia(personaContractor.getTipoVia());
            personaInsured.setNombreVia(personaContractor.getNombreVia());
            personaInsured.setDistrito(personaContractor.getDistrito());
            personaInsured.setProvincia(personaContractor.getProvincia());
            personaInsured.setDepartamento(personaContractor.getDepartamento());
            personaInsured.setDireccion(personaContractor.getDireccion());

            personaList.add(personaContractor);
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
