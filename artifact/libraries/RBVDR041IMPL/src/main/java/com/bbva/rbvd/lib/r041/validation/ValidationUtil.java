package com.bbva.rbvd.lib.r041.validation;

import com.bbva.rbvd.dto.insrncsale.bo.emision.EntidadBO;
import com.bbva.rbvd.dto.insrncsale.utils.PersonTypeEnum;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ValidationUtil {

    private static final String RUC_ID = "R";

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Number.CLIENT_BANK_LENGHT);
    }
    public static Integer obtainExistingCompanyRole(ParticipantsDTO participantDTO, ParticipantProperties participantProperties, List<RolDAO> roles) {
        String roleCodeBank = participantProperties.obtainRoleCodeByEnum(participantDTO.getParticipantType().getId());
        return roles.stream()
                .filter(rolDTO -> roleCodeBank.equalsIgnoreCase(rolDTO.getParticipantRoleId().toString()))
                .map(RolDAO::getInsuranceCompanyRoleId)
                .map(Integer::parseInt)
                .findFirst()
                .orElse(null);
    }

    public static String validateAllVia(String via){
        return !StringUtils.isEmpty(via)?via:ConstantsUtil.RegularExpression.UNSPECIFIED;
    }

    public static PersonTypeEnum getPersonType(EntidadBO person) {
        if (RUC_ID.equalsIgnoreCase(person.getTipoDocumento())){
            if (StringUtils.startsWith(person.getNroDocumento(), "20")) return PersonTypeEnum.JURIDIC;
            else return PersonTypeEnum.NATURAL_WITH_BUSINESS;
        }
        return PersonTypeEnum.NATURAL;
    }

    private ValidationUtil() {
    }
}
