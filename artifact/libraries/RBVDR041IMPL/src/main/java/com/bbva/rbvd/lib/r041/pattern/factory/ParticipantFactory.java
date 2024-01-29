package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.lib.r041.pattern.IValidateParticipant;

public class ParticipantFactory {

    private final IValidateParticipant naturalParticipant;
    private final IValidateParticipant legalParticipant;

    public ParticipantFactory(IValidateParticipant naturalParticipant, IValidateParticipant legalParticipant) {
        this.naturalParticipant = naturalParticipant;
        this.legalParticipant = legalParticipant;
    }

    public IValidateParticipant buildParticipant(RBVDInternalConstants.TypeParticipant typeParticipant){
        switch (typeParticipant){
            case NATURAL :
                 return this.naturalParticipant;
            case LEGAL :
                 return this.legalParticipant;
            default :
                return null;
        }
    }



}
