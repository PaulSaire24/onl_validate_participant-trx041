package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.lib.r041.pattern.decorator.BeforeParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantDataValidator;

public abstract class ParticipantDataValidatorDecorator implements ParticipantDataValidator {

    private BeforeParticipantDataValidator beforeValidator;

    protected ParticipantDataValidatorDecorator(BeforeParticipantDataValidator preValidate) {
        this.beforeValidator = preValidate;
    }

    public BeforeParticipantDataValidator getBeforeValidator() {
        return beforeValidator;
    }

}
