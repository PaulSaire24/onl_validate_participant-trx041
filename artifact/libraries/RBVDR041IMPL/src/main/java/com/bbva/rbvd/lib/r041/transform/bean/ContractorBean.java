package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

import java.util.List;

public class ContractorBean {
    public static void builRolContractor(final List<PersonaBO> personaList){

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

           personaList.add(personContractor);

    }
}
