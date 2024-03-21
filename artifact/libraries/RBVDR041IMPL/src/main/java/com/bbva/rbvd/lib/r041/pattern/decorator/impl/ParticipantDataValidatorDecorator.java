package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantDataValidator;

public abstract class ParticipantDataValidatorDecorator implements ParticipantDataValidator {

    private BeforeParticipantDataValidator preValidate;

    protected ParticipantDataValidatorDecorator(BeforeParticipantDataValidator preValidate) {
        this.preValidate = preValidate;
    }

    public BeforeParticipantDataValidator getPreValidate() {
        return preValidate;
    }

}
