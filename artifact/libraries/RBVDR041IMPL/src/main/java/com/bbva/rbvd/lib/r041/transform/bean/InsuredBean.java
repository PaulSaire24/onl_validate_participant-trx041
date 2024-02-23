package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bbva.rbvd.lib.r041.util.ConvertUtil.toLocalDate;

public class InsuredBean {
    public static void builRolInsured(List<PersonaBO> personaList, List<ParticipantsDTO> participants){
        PersonaBO personManager = personaList.get(0);
        ParticipantsDTO participans = participantArraySelection(participants);
        PersonaBO personaInsured = new PersonaBO();
        String nomb = participans.getPerson().getFirstName().concat(StringUtils.isEmpty(participans.getPerson().getMiddleName())?"":" ".concat(participans.getPerson().getMiddleName()));
        Map<String,Object> contacDetails = getContactGroup(participans.getContactDetails());
        personaInsured.setNombres(nomb);
        personaInsured.setApePaterno(participans.getPerson().getLastName());
        personaInsured.setApeMaterno(participans.getPerson().getSecondLastName());
        personaInsured.setTipoDocumento(participans.getIdentityDocuments().get(0).getDocumentType().getId());
        personaInsured.setNroDocumento(participans.getIdentityDocuments().get(0).getValue());
        personaInsured.setFechaNacimiento(String.valueOf(toLocalDate(participans.getPerson().getBirthDate())));
        personaInsured.setSexo(participans.getPerson().getGender().getId().equalsIgnoreCase("MALE")?"M":"L");
        personaInsured.setCorreoElectronico((String) contacDetails.get(ConstantsUtil.ContactDetails.EMAIL));
        personaInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
        personaInsured.setCelular((String) contacDetails.get(ConstantsUtil.ContactDetails.MOBILE_NUMBER));

        personaInsured.setTipoVia(personManager.getTipoVia());
        personaInsured.setNombreVia(personManager.getNombreVia());
        personaInsured.setDistrito(personManager.getDistrito());
        personaInsured.setProvincia(personManager.getProvincia());
        personaInsured.setNumeroVia(personManager.getNumeroVia());
        personaInsured.setDepartamento(personManager.getDepartamento());
        personaInsured.setDireccion(personManager.getDireccion());

        personaList.add(personaInsured);
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

    private static ParticipantsDTO participantArraySelection(List<ParticipantsDTO> participantsInput){
        ParticipantsDTO participants;
        if(participantsInput.size()==1){
            participants = participantsInput.get(0);
        } else {
            participants = participantsInput.size()==2?participantsInput.get(1):participantsInput.get(2);
        }
        return participants;
    }

    private InsuredBean() {
    }
}