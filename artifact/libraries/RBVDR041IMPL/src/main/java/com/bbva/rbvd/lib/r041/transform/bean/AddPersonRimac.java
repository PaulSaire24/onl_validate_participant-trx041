package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AddPersonRimac {

    public static void  addPerson(final List<PersonaBO> personaList, List<ParticipantsDTO> participants){
        if(personaList.size()==1){
            Optional<PersonaBO> personMnager = Optional.of(personaList.stream().filter(per -> Objects.nonNull(per)).findFirst().get());
            PersonaBO personContractor = new PersonaBO();
            personContractor.setNombres(personMnager.get().getNombres());
            personContractor.setApePaterno(personMnager.get().getApePaterno());
            personContractor.setApeMaterno(personMnager.get().getApeMaterno());
            personContractor.setTipoDocumento(personMnager.get().getTipoDocumento());
            personContractor.setNroDocumento(personMnager.get().getNroDocumento());
            personContractor.setFechaNacimiento(personMnager.get().getFechaNacimiento());
            personContractor.setSexo(personMnager.get().getSexo());
            personContractor.setCorreoElectronico(personMnager.get().getCorreoElectronico());
            personContractor.setRol(ConstantsUtil.Rol.CONTRACTOR.getValue());
            personContractor.setCelular(personMnager.get().getCelular());

            personContractor.setTipoVia(personMnager.get().getTipoVia());
            personContractor.setNombreVia(personMnager.get().getNombreVia());
            personContractor.setNumeroVia(personMnager.get().getNumeroVia());
            personContractor.setDistrito(personMnager.get().getDistrito());
            personContractor.setProvincia(personMnager.get().getProvincia());
            personContractor.setDepartamento(personMnager.get().getDepartamento());
            personContractor.setDireccion(personMnager.get().getDireccion());
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
