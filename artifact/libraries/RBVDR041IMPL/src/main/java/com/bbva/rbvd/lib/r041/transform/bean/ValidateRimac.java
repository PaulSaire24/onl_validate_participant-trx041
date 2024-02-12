package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidateRimac {

    public static List<PersonaBO> mapInRequestRimacDynamicLife(PayloadConfig payloadConfig){

        List<PersonaBO> personaList = new ArrayList<>();

        List<ParticipantsDTO> participantsInputList = payloadConfig.getInput().getParticipants();
        List<PayloadProperties> participantsPewuList = payloadConfig.getProperties();
        participantsInputList.forEach(partInput->
                participantsPewuList.forEach(parPewu->{
            if(parPewu.getDocumetType().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getDocumentType().getId())
                && parPewu.getDocumetNumber().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getValue())){
                PersonaBO personaBO = new PersonaBO();
                personaBO.setNombres(parPewu.getCustomer().getPemsalwu().getNombres());
                personaBO.setApePaterno(parPewu.getCustomer().getPemsalwu().getApellip());
                personaBO.setApeMaterno(parPewu.getCustomer().getPemsalwu().getApellim());
                personaBO.setTipoDocumento(parPewu.getCustomer().getPemsalwu().getTdoi());
                personaBO.setNroDocumento(parPewu.getCustomer().getPemsalwu().getNdoi());
                personaBO.setFechaNacimiento(parPewu.getCustomer().getPemsalwu().getFechan());
                personaBO.setSexo(parPewu.getCustomer().getPemsalwu().getSexo());
                personaBO.setCorreoElectronico(parPewu.getCustomer().getPemsalwu().getContac3());
                personaBO.setRol(ConstantsUtil.getValueByName(partInput.getParticipantType().getId()));
                personaBO.setCelular(parPewu.getCustomer().getPemsalwu().getNroclie());
                personaBO.setTipoVia(parPewu.getCustomer().getPemsalwu().getIdendi1());
                personaBO.setNombreVia(parPewu.getCustomer().getPemsalwu().getNombdi1());
                personaBO.setDistrito(parPewu.getCustomer().getPemsalw4().getDesdist());
                personaBO.setProvincia(parPewu.getCustomer().getPemsalw4().getDesdist());
                personaBO.setDepartamento(parPewu.getCustomer().getPemsalw4().getDesdept());
                personaBO.setDireccion(parPewu.getCustomer().getPemsalwu().getIdendi1().concat(" ").concat(parPewu.getCustomer().getPemsalwu().getNombdi1()));
                personaList.add(personaBO);
            }else{
                PersonaBO personaBO = new PersonaBO();
                String nomb = partInput.getPerson().getFirstName().concat(StringUtils.isEmpty(partInput.getPerson().getMiddleName())?"":" ".concat(partInput.getPerson().getMiddleName()));
                Map<String,Object> contacDetails = getContactGroup(partInput.getContactDetails());
                personaBO.setNombres(nomb);
                personaBO.setApePaterno(partInput.getPerson().getLastName());
                personaBO.setApeMaterno(partInput.getPerson().getSecondLastName());
                personaBO.setTipoDocumento(partInput.getIdentityDocuments().get(0).getDocumentType().getId());
                personaBO.setNroDocumento(partInput.getIdentityDocuments().get(0).getValue());
                personaBO.setFechaNacimiento(String.valueOf(partInput.getPerson().getBirthDate()));
                personaBO.setSexo(partInput.getPerson().getGender().getId().equalsIgnoreCase("MALE")?"M":"L");
                personaBO.setCorreoElectronico((String) contacDetails.get(ConstantsUtil.ContactDetails.EMAIL));
                personaBO.setRol(ConstantsUtil.getValueByName(partInput.getParticipantType().getId()));
                personaBO.setCelular((String) contacDetails.get(ConstantsUtil.ContactDetails.MOBILE_NUMBER));

                PersonaBO personaPayment = personaList.get(0);

                personaBO.setTipoVia(personaPayment.getTipoVia());
                personaBO.setNombreVia(personaPayment.getNombreVia());
                personaBO.setDistrito(personaPayment.getDistrito());
                personaBO.setProvincia(personaPayment.getProvincia());
                personaBO.setDepartamento(personaPayment.getDepartamento());
                personaBO.setDireccion(personaPayment.getDireccion());
                personaList.add(personaBO);
            }
        }));

        return personaList;
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
