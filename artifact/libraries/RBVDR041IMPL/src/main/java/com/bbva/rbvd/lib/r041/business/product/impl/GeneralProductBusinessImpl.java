package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.business.product.IGeneralProductBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralProductBusinessImpl implements IGeneralProductBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralProductBusinessImpl.class);
    public GeneralProductBusinessImpl() {

    }

    @Override
    public AgregarTerceroBO createRequestByCompany(PayloadConfig payloadConfig) {
        //Not implemented
        return null;
    }
}
