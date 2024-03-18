package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insrncsale.utils.LifeInsuranceInsuredData;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class NonCustomerBean {
    public static PersonaBO buildNonCustomer(Participant participant, List<PersonaBO> persons){
       PersonaBO personManager = persons.get(0);
        PersonaBO personNonCustomer = new PersonaBO();
        String apellidos = (String) participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_CLIENT_LAST_NAME);
        String apPaterno="";
        String apMaterno="";

        if(StringUtils.isNotEmpty(apellidos)){
            int index = apellidos.indexOf(ConstantsUtil.Delimeter.VERTICAL_BAR);
            apPaterno = apellidos.substring(ConstantsUtil.Number.CERO,index);
            apMaterno = apellidos.substring(index+ConstantsUtil.Number.UNO);
        }
        String fechaNacimiento = String.valueOf(participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_CUSTOMER_BIRTH_DATE));
        if(StringUtils.isNotEmpty(fechaNacimiento)){
            fechaNacimiento = fechaNacimiento.substring(ConstantsUtil.Number.CERO,ConstantsUtil.Number.DIEZ);
        }

        personNonCustomer.setNombres((String) participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_INSURED_CUSTOMER_NAME));
        personNonCustomer.setApePaterno(apPaterno);
        personNonCustomer.setApeMaterno(apMaterno);

        personNonCustomer.setTipoDocumento((String) participant.getNonCustomer().get("CUSTOMER_DOCUMENT_TYPE"));
        personNonCustomer.setNroDocumento((String) participant.getNonCustomer().get("PERSONAL_ID"));

        personNonCustomer.setFechaNacimiento(fechaNacimiento);
        personNonCustomer.setSexo((String) participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_GENDER_ID));
        personNonCustomer.setCorreoElectronico((String) participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_USER_EMAIL_PERSONAL_DESC));
        personNonCustomer.setRol(ConstantsUtil.Rol.INSURED.getValue());
        personNonCustomer.setCelular((String) participant.getNonCustomer().get(LifeInsuranceInsuredData.FIELD_PHONE_ID));

        personNonCustomer.setTipoVia(ValidationUtil.validateAllVia(personManager.getTipoVia()));
        personNonCustomer.setNombreVia(ValidationUtil.validateAllVia(personManager.getNombreVia()));
        personNonCustomer.setNumeroVia(ValidationUtil.validateAllVia(personManager.getNumeroVia()));

        personNonCustomer.setDistrito(personManager.getDistrito());
        personNonCustomer.setProvincia(personManager.getProvincia());
        personNonCustomer.setDepartamento(personManager.getDepartamento());
        personNonCustomer.setDireccion(personManager.getDireccion());

        return personNonCustomer;
    }
}
