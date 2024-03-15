package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;

public class ParticipantFactory {

    public static Participant buildParticipant(RBVDInternalConstants.ParticipantType participantType){
        switch (participantType){
            case CUSTOMER:
                return new ParticipantCustomer();
            default:
                return new ParticipantNonCustomer();
        }
    }

}
