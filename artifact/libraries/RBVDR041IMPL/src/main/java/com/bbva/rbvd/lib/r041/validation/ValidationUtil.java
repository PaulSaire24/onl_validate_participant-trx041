package com.bbva.rbvd.lib.r041.validation;

import com.bbva.rbvd.dto.insrncsale.bo.emision.EntidadBO;
import com.bbva.rbvd.dto.insrncsale.utils.PersonTypeEnum;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

public class ValidationUtil {

    private ValidationUtil() {
    }
    private static final String RUC_ID = "R";

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Number.CLIENT_BANK_LENGHT);
    }
    public static Integer obtainExistingCompanyRole(ParticipantsDTO participantDTO, ParticipantProperties participantProperties, List<RolDAO> roles) {
        String roleCodeBank = participantProperties.obtainPropertyFromConsole(participantDTO.getParticipantType().getId().concat(".bank.role"));
        return roles.stream()
                .filter(rolDTO -> roleCodeBank.equalsIgnoreCase(rolDTO.getParticipantRoleId().toString()))
                .map(RolDAO::getInsuranceCompanyRoleId)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(null);
    }

    public static PersonTypeEnum getPersonType(EntidadBO person) {
        if (RUC_ID.equalsIgnoreCase(person.getTipoDocumento())){
            if (StringUtils.startsWith(person.getNroDocumento(), "20")) return PersonTypeEnum.JURIDIC;
            else return PersonTypeEnum.NATURAL_WITH_BUSINESS;
        }
        return PersonTypeEnum.NATURAL;
    }


    public static String validateSN(String name) {
        if(Objects.isNull(name) || "null".equals(name) || " ".equals(name)){
            return "";
        }else{
            name = name.replace("#","Ñ");
            return name;
        }
    }


}
