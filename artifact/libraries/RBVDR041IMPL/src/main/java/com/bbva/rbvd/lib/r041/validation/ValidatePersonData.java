package com.bbva.rbvd.lib.r041.validation;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;

import java.util.List;

public class ValidatePersonData {
    private ValidatePersonData () {}

    public static void validateAllParticipantsByIndicatedType(List<ParticipantsDTO> participants, String personType){
        if(!participants.stream().allMatch(participant -> personType.equalsIgnoreCase(participant.getPerson().getPersonType()))){
            throw new BusinessException(ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_PARTICIPANTS_OF_DIFFERENT_TYPE.getValue());
        }
    }
}
