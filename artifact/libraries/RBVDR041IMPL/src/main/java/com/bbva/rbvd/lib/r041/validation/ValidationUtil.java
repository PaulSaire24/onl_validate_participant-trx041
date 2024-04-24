package com.bbva.rbvd.lib.r041.validation;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ValidationUtil {

    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_LETTERS) && clientId.matches(ConstantsUtil.RegularExpression.CONTAIN_ONLY_NUMBERS) && clientId.length()>ConstantsUtil.Number.CLIENT_BANK_LENGHT);
    }
    public static Integer obtainExistingCompanyRole(ParticipantsDTO participantDTO, ParticipantProperties participantProperties, List<RolDAO> roles) {
        String roleCodeBank = participantProperties.obtainRoleCodeByEnum(participantDTO.getParticipantType().getId());
        return Integer.parseInt(roles.stream().filter(rolDTO -> roleCodeBank.equalsIgnoreCase(rolDTO.getParticipantRoleId().toString())).findFirst()
                .orElseThrow(() -> new BusinessException(
                        ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getAdviceCode(), false,
                        TypeErrorControllerEnum.ERROR_ROLE_NOT_REGISTRED_ON_APX_CONSOLE.getValue().
                                replace("{participantType}",participantDTO.getParticipantType().getId()))).
                getInsuranceCompanyRoleId());
    }

    public static String validateAllVia(String via){
        return !StringUtils.isEmpty(via)?via:ConstantsUtil.RegularExpression.UNSPECIFIED;
    }

}
