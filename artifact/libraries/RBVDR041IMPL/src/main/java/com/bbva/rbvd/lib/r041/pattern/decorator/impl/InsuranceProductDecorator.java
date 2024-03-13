package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.lib.r041.pattern.decorator.PostParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.PreParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantValidations;

public abstract class ValidateDecorator implements ParticipantValidations {

    private PreParticipantValidations preValidate;
    private PostParticipantValidations postValidate;

    protected ValidateDecorator(PreParticipantValidations preValidate, PostParticipantValidations postValidate) {
        this.preValidate = preValidate;
        this.postValidate = postValidate;
    }

    public PreParticipantValidations getPreValidate() {
        return preValidate;
    }

    public PostParticipantValidations getPostValidate() {
        return postValidate;
    }
}
