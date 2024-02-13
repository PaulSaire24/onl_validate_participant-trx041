package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
                personaBO.setNumeroVia(parPewu.getCustomer().getPemsalwu().getNroext1());
                personaBO.setDistrito(parPewu.getCustomer().getPemsalw4().getDesdist());
                personaBO.setProvincia(parPewu.getCustomer().getPemsalw4().getDesdist());
                personaBO.setDepartamento(parPewu.getCustomer().getPemsalw4().getDesdept());
                personaBO.setDireccion(parPewu.getCustomer().getPemsalwu().getIdendi1().concat(" ").concat(parPewu.getCustomer().getPemsalwu().getNombdi1()));
                personaList.add(personaBO);
            }
        }));
        return personaList;
    }
}
