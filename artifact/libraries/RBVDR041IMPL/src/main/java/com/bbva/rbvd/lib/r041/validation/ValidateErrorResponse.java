package com.bbva.rbvd.lib.r041.validation;

import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.validateparticipant.dto.ResponseLibrary;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;

public class ValidateErrorResponse {
    private ValidateErrorResponse() {
    }

    public static ResponseLibrary<Void> validateValidateProcessErrors(String adviceCode, String typeError){
        switch (adviceCode){
            case "PISD00120000":
            case "PISD00120050":
            case "PISD00120051":
            case "PISD00120052":
                return buildResponseLibrary(ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getAdviceCode(), ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage().concat(typeError));
            default:
                return buildResponseLibrary("","");
        }


    }

    private static ResponseLibrary<Void> buildResponseLibrary(String adviceCode, String message){
        return ResponseLibrary.ResponseServiceBuilder
                .an().statusIndicatorProcess(RBVDInternalConstants.Status.FAILED, adviceCode,message)
                .build();

    }

}
