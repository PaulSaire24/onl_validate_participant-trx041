package com.bbva.rbvd.lib.r041.validation;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Numero.CLIENT_BANK_LENGHT);
    }

   /* public void addressValidation(PEWUResponse pewu, ParticipantsDTO participants, PersonaBO personaBO){
        if(StringUtils.isNotEmpty(pewu.getPemsalwu().getIdendi1())){
            personaBO.setTipoVia(pewu.getPemsalwu().getIdendi1());
        } else if (StringUtils.isNotEmpty(participants.getAddresses().get(0).getLocation().getAddressComponent().get(0).getComponentTypes().get(0))) {
            personaBO.setTipoVia(participants.getAddresses().get(0).getLocation().getAddressComponent().get(0).getComponentTypes().get(0));
        }else{
            personaBO.setTipoVia(ConstantsUtil.RegularExpression.UNSPECIFIED);
        }
    }*/
}
