package com.bbva.rbvd.lib.r041.pattern.composite;

import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r048.RBVDR048;

public class NoLifeHandler extends AbstractCompositeParticipantHandler{

    private final RBVDR048 rbvdr048;
    private final ParticipantProperties participantProperties;

    static {
        // Not implemented
    }

    public NoLifeHandler(RBVDR048 rbvdr048, ParticipantProperties participantProperties){
        this.rbvdr048 = rbvdr048;
        this.participantProperties = participantProperties;
    }

}
