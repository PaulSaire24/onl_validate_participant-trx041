package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.utils.LifeInsuranceInsuredData;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class InsuredBean {
    public static void builRolInsured(List<PersonaBO> personaList, List<ParticipantsDTO> participants,Map<String,Object> dataInsured){
        PersonaBO personManager = personaList.get(0);
        PersonaBO personaInsured = new PersonaBO();
        if(participants.size()==1){
            personaInsured.setNombres(personManager.getNombres());
            personaInsured.setApePaterno(personManager.getApePaterno());
            personaInsured.setApeMaterno(personManager.getApeMaterno());
            personaInsured.setTipoDocumento(personManager.getTipoDocumento());
            personaInsured.setNroDocumento(personManager.getNroDocumento());
            personaInsured.setFechaNacimiento(personManager.getFechaNacimiento());
            personaInsured.setSexo(personManager.getSexo());
            personaInsured.setCorreoElectronico(personManager.getCorreoElectronico());
            personaInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
            personaInsured.setCelular(personManager.getCelular());

            personaInsured.setTipoVia(personManager.getTipoVia());
            personaInsured.setNombreVia(personManager.getNombreVia());
            personaInsured.setNumeroVia(personManager.getNumeroVia());
            personaInsured.setDistrito(personManager.getDistrito());
            personaInsured.setProvincia(personManager.getProvincia());
            personaInsured.setDepartamento(personManager.getDepartamento());
            personaInsured.setDireccion(personManager.getDireccion());

        }else{
            String apellidos = (String) dataInsured.get(LifeInsuranceInsuredData.FIELD_CLIENT_LAST_NAME);
            String apPaterno="";
            String apMaterno="";

            if(StringUtils.isNotEmpty(apellidos)){
                int index = apellidos.indexOf(ConstantsUtil.Delimeter.VERTICAL_BAR);
                apPaterno = apellidos.substring(ConstantsUtil.Number.CERO,index);
                apMaterno = apellidos.substring(index+ConstantsUtil.Number.UNO);
            }
            String fechaNacimiento = String.valueOf(dataInsured.get(LifeInsuranceInsuredData.FIELD_CUSTOMER_BIRTH_DATE));
            if(StringUtils.isNotEmpty(fechaNacimiento)){
                fechaNacimiento = fechaNacimiento.substring(ConstantsUtil.Number.CERO,ConstantsUtil.Number.DIEZ);
            }

            personaInsured.setNombres((String) dataInsured.get(LifeInsuranceInsuredData.FIELD_INSURED_CUSTOMER_NAME));
            personaInsured.setApePaterno(apPaterno);
            personaInsured.setApeMaterno(apMaterno);

            personaInsured.setTipoDocumento((String) dataInsured.get("CUSTOMER_DOCUMENT_TYPE"));
            personaInsured.setNroDocumento((String) dataInsured.get("PERSONAL_ID"));

            personaInsured.setFechaNacimiento(fechaNacimiento);
            personaInsured.setSexo((String) dataInsured.get(LifeInsuranceInsuredData.FIELD_GENDER_ID));
            personaInsured.setCorreoElectronico((String) dataInsured.get(LifeInsuranceInsuredData.FIELD_USER_EMAIL_PERSONAL_DESC));
            personaInsured.setRol(ConstantsUtil.Rol.INSURED.getValue());
            personaInsured.setCelular((String) dataInsured.get(LifeInsuranceInsuredData.FIELD_PHONE_ID));

            personaInsured.setTipoVia(ValidationUtil.validateAllVia(personManager.getTipoVia()));
            personaInsured.setNombreVia(ValidationUtil.validateAllVia(personManager.getNombreVia()));
            personaInsured.setNumeroVia(ValidationUtil.validateAllVia(personManager.getNumeroVia()));

            personaInsured.setDistrito(personManager.getDistrito());
            personaInsured.setProvincia(personManager.getProvincia());
            personaInsured.setDepartamento(personManager.getDepartamento());
            personaInsured.setDireccion(personManager.getDireccion());
        }

        personaList.add(personaInsured);
    }
}
