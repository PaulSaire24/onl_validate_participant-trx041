package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.utils.LifeInsuranceInsuredData;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


public class PersonBean {

    public static PersonaBO buildPersonFromCustomer(PEWUResponse customer, String rolCode) {
        PersonaBO personCustomer = new PersonaBO();
        personCustomer.setNombres(customer.getPemsalwu().getNombres());
        personCustomer.setApePaterno(customer.getPemsalwu().getApellip());
        personCustomer.setApeMaterno(customer.getPemsalwu().getApellim());
        personCustomer.setTipoDocumento(customer.getPemsalwu().getTdoi());
        personCustomer.setNroDocumento(customer.getPemsalwu().getNdoi());
        personCustomer.setFechaNacimiento(customer.getPemsalwu().getFechan());
        personCustomer.setSexo(customer.getPemsalwu().getSexo());
        personCustomer.setCorreoElectronico(customer.getPemsalwu().getContac3());
        personCustomer.setRol(ConstantsUtil.getValueByName(rolCode));
        personCustomer.setCelular(customer.getPemsalwu().getContac2());

        personCustomer.setTipoVia(ValidationUtil.validateAllVia(customer.getPemsalwu().getIdendi1()));
        personCustomer.setNombreVia(ValidationUtil.validateAllVia(customer.getPemsalwu().getNombdi1()));
        personCustomer.setNumeroVia(ValidationUtil.validateAllVia(customer.getPemsalwu().getNroext1()));

        personCustomer.setDistrito(customer.getPemsalw4().getDesdist());
        personCustomer.setProvincia(customer.getPemsalw4().getDesprov());
        personCustomer.setDepartamento(customer.getPemsalw4().getDesdept());
        personCustomer.setDireccion(customer.getPemsalwu().getIdendi1().concat(" ").concat(customer.getPemsalwu().getNombdi1()));

        return personCustomer;
    }

    public static PersonaBO buildPersonFromNonCustomer(Map mapParticipant, PersonaBO personManager){
        PersonaBO personNonCustomer = new PersonaBO();
        String apellidos = (String) mapParticipant.get(LifeInsuranceInsuredData.FIELD_CLIENT_LAST_NAME);
        String apPaterno="";
        String apMaterno="";

        if(StringUtils.isNotEmpty(apellidos)){
            int index = apellidos.indexOf(ConstantsUtil.Delimeter.VERTICAL_BAR);
            apPaterno = apellidos.substring(ConstantsUtil.Number.CERO,index);
            apMaterno = apellidos.substring(index+ConstantsUtil.Number.UNO);
        }
        String fechaNacimiento = String.valueOf(mapParticipant.get(LifeInsuranceInsuredData.FIELD_CUSTOMER_BIRTH_DATE));
        if(StringUtils.isNotEmpty(fechaNacimiento)){
            fechaNacimiento = fechaNacimiento.substring(ConstantsUtil.Number.CERO,ConstantsUtil.Number.DIEZ);
        }

        personNonCustomer.setNombres((String) mapParticipant.get(LifeInsuranceInsuredData.FIELD_INSURED_CUSTOMER_NAME));
        personNonCustomer.setApePaterno(apPaterno);
        personNonCustomer.setApeMaterno(apMaterno);

        personNonCustomer.setTipoDocumento((String) mapParticipant.get("CUSTOMER_DOCUMENT_TYPE"));
        personNonCustomer.setNroDocumento((String) mapParticipant.get("PERSONAL_ID"));

        personNonCustomer.setFechaNacimiento(fechaNacimiento);
        personNonCustomer.setSexo((String) mapParticipant.get(LifeInsuranceInsuredData.FIELD_GENDER_ID));
        personNonCustomer.setCorreoElectronico((String) mapParticipant.get(LifeInsuranceInsuredData.FIELD_USER_EMAIL_PERSONAL_DESC));
        personNonCustomer.setRol(ConstantsUtil.Rol.INSURED.getValue());
        personNonCustomer.setCelular((String) mapParticipant.get(LifeInsuranceInsuredData.FIELD_PHONE_ID));

        personNonCustomer.setTipoVia(ValidationUtil.validateAllVia(personManager.getTipoVia()));
        personNonCustomer.setNombreVia(ValidationUtil.validateAllVia(personManager.getNombreVia()));
        personNonCustomer.setNumeroVia(ValidationUtil.validateAllVia(personManager.getNumeroVia()));

        personNonCustomer.setDistrito(personManager.getDistrito());
        personNonCustomer.setProvincia(personManager.getProvincia());
        personNonCustomer.setDepartamento(personManager.getDepartamento());
        personNonCustomer.setDireccion(personManager.getDireccion());

        return personNonCustomer;
    }


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

}
