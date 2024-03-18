package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;

import java.util.List;

public class CustomerBean {

    public static PersonaBO buildCustomer(Participant participant){
        PersonaBO personCustomer = new PersonaBO();
        personCustomer.setNombres(participant.getCustomer().getPemsalwu().getNombres());
        personCustomer.setApePaterno(participant.getCustomer().getPemsalwu().getApellip());
        personCustomer.setApeMaterno(participant.getCustomer().getPemsalwu().getApellim());
        personCustomer.setTipoDocumento(participant.getCustomer().getPemsalwu().getTdoi());
        personCustomer.setNroDocumento(participant.getCustomer().getPemsalwu().getNdoi());
        personCustomer.setFechaNacimiento(participant.getCustomer().getPemsalwu().getFechan());
        personCustomer.setSexo(participant.getCustomer().getPemsalwu().getSexo());
        personCustomer.setCorreoElectronico(participant.getCustomer().getPemsalwu().getContac3());
        personCustomer.setRol(ConstantsUtil.getValueByName(participant.getRolCode()));
        personCustomer.setCelular(participant.getCustomer().getPemsalwu().getContac2());

        personCustomer.setTipoVia(ValidationUtil.validateAllVia(participant.getCustomer().getPemsalwu().getIdendi1()));
        personCustomer.setNombreVia(ValidationUtil.validateAllVia(participant.getCustomer().getPemsalwu().getNombdi1()));
        personCustomer.setNumeroVia(ValidationUtil.validateAllVia(participant.getCustomer().getPemsalwu().getNroext1()));

        personCustomer.setDistrito(participant.getCustomer().getPemsalw4().getDesdist());
        personCustomer.setProvincia(participant.getCustomer().getPemsalw4().getDesprov());
        personCustomer.setDepartamento(participant.getCustomer().getPemsalw4().getDesdept());
        personCustomer.setDireccion(participant.getCustomer().getPemsalwu().getIdendi1().concat(" ").concat(participant.getCustomer().getPemsalwu().getNombdi1()));

        return personCustomer;
    }

    public static void enrichPerson(List<PersonaBO> personList){
        PersonaBO personManager = personList.get(0);
        PersonaBO personContractor = new PersonaBO();
        personContractor.setNombres(personManager.getNombres());
        personContractor.setApePaterno(personManager.getApePaterno());
        personContractor.setApeMaterno(personManager.getApeMaterno());
        personContractor.setTipoDocumento(personManager.getTipoDocumento());
        personContractor.setNroDocumento(personManager.getNroDocumento());
        personContractor.setFechaNacimiento(personManager.getFechaNacimiento());
        personContractor.setSexo(personManager.getSexo());
        personContractor.setCorreoElectronico(personManager.getCorreoElectronico());
        personContractor.setRol(ConstantsUtil.Rol.CONTRACTOR.getValue());
        personContractor.setCelular(personManager.getCelular());

        personContractor.setTipoVia(personManager.getTipoVia());
        personContractor.setNombreVia(personManager.getNombreVia());
        personContractor.setNumeroVia(personManager.getNumeroVia());
        personContractor.setDistrito(personManager.getDistrito());
        personContractor.setProvincia(personManager.getProvincia());
        personContractor.setDepartamento(personManager.getDepartamento());
        personContractor.setDireccion(personManager.getDireccion());
        if(personList.size()==1){
            PersonaBO personInsured = new PersonaBO();
            personInsured.setNombres(personManager.getNombres());
            personInsured.setApePaterno(personManager.getApePaterno());
            personInsured.setApeMaterno(personManager.getApeMaterno());
            personInsured.setTipoDocumento(personManager.getTipoDocumento());
            personInsured.setNroDocumento(personManager.getNroDocumento());
            personInsured.setFechaNacimiento(personManager.getFechaNacimiento());
            personInsured.setSexo(personManager.getSexo());
            personInsured.setCorreoElectronico(personManager.getCorreoElectronico());
            personInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
            personInsured.setCelular(personManager.getCelular());

            personInsured.setTipoVia(personManager.getTipoVia());
            personInsured.setNombreVia(personManager.getNombreVia());
            personInsured.setNumeroVia(personManager.getNumeroVia());
            personInsured.setDistrito(personManager.getDistrito());
            personInsured.setProvincia(personManager.getProvincia());
            personInsured.setDepartamento(personManager.getDepartamento());
            personInsured.setDireccion(personManager.getDireccion());

            personList.add(personContractor);
            personList.add(personInsured);

        } else if (personList.size()==2) {
            personList.add(personContractor);
        }

    }
}