package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;

public class ParticipantFactory {

    public static Participant buildParticipant(RBVDInternalConstants.ParticipantType participantType){
        if(participantType.equals(RBVDInternalConstants.ParticipantType.CUSTOMER)){
            return new ParticipantCustomer();
        }else{
            return new ParticipantNonCustomer();
        }
    }

    private ParticipantFactory() {
    }
}
