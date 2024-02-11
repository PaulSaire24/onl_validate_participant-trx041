package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.pattern.Validate;

public abstract class ValidateDecorator implements Validate {

    private PreValidate preValidate;
    private PostValidate postValidate;

    protected ValidateDecorator(PreValidate preValidate, PostValidate postValidate) {
        this.preValidate = preValidate;
        this.postValidate = postValidate;
    }

    public PreValidate getPreValidate() {
        return preValidate;
    }

    public PostValidate getPostValidate() {
        return postValidate;
    }
}
