package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.lib.r041.pattern.decorator.PreParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantValidations;

public abstract class ValidateDecorator implements ParticipantValidations {

    private PreParticipantValidations preValidate;

    protected ValidateDecorator(PreParticipantValidations preValidate) {
        this.preValidate = preValidate;
    }

    public PreParticipantValidations getPreValidate() {
        return preValidate;
    }

}
