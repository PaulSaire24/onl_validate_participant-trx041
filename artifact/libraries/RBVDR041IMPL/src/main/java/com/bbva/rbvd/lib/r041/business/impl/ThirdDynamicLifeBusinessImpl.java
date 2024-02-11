package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimac;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

import java.util.List;

public class ThirdDynamicLifeBusinessImpl implements IThirdDynamicLifeBusiness {

    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  agregarTercero = new PayloadAgregarTerceroBO();
        List<PersonaBO> personaList =ValidateRimac.mapInRequestRimacDynamicLife(payloadConfig);
        agregarTercero.setPersona(personaList);
        agregarTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(agregarTercero);

        return requestRimac;

    }


}
