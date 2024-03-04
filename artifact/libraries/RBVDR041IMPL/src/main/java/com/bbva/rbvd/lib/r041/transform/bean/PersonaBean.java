package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;

public class PersonaBean {

    public static PersonaBO mapInRequestRimacDynamicLife(ParticipantsDTO participant,PayloadProperties partPewu){
        PersonaBO personaBO = new PersonaBO();
        personaBO.setNombres(partPewu.getCustomer().getPemsalwu().getNombres());
        personaBO.setApePaterno(partPewu.getCustomer().getPemsalwu().getApellip());
        personaBO.setApeMaterno(partPewu.getCustomer().getPemsalwu().getApellim());
        personaBO.setTipoDocumento(partPewu.getCustomer().getPemsalwu().getTdoi());
        personaBO.setNroDocumento(partPewu.getCustomer().getPemsalwu().getNdoi());
        personaBO.setFechaNacimiento(partPewu.getCustomer().getPemsalwu().getFechan());
        personaBO.setSexo(partPewu.getCustomer().getPemsalwu().getSexo());
        personaBO.setCorreoElectronico(partPewu.getCustomer().getPemsalwu().getContac3());
        personaBO.setRol(ConstantsUtil.getValueByName(participant.getParticipantType().getId()));
        personaBO.setCelular(partPewu.getCustomer().getPemsalwu().getContac2());

        personaBO.setTipoVia(ValidationUtil.validateAllVia(partPewu.getCustomer().getPemsalwu().getIdendi1()));
        personaBO.setNombreVia(ValidationUtil.validateAllVia(partPewu.getCustomer().getPemsalwu().getNombdi1()));
        personaBO.setNumeroVia(ValidationUtil.validateAllVia(partPewu.getCustomer().getPemsalwu().getNroext1()));

        personaBO.setDistrito(partPewu.getCustomer().getPemsalw4().getDesdist());
        personaBO.setProvincia(partPewu.getCustomer().getPemsalw4().getDesprov());
        personaBO.setDepartamento(partPewu.getCustomer().getPemsalw4().getDesdept());
        personaBO.setDireccion(partPewu.getCustomer().getPemsalwu().getIdendi1().concat(" ").concat(partPewu.getCustomer().getPemsalwu().getNombdi1()));

        return personaBO;
    }

}
