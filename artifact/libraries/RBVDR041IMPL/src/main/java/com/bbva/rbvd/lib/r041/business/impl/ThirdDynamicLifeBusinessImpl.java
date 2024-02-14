package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.AddPersonRimac;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimac;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ThirdDynamicLifeBusinessImpl implements IThirdDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdDynamicLifeBusinessImpl.class);
    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  agregarTercero = new PayloadAgregarTerceroBO();
        List<PersonaBO> personaList = ValidateRimac.mapInRequestRimacDynamicLife(payloadConfig);
        AddPersonRimac.addPerson(personaList,payloadConfig.getInput().getParticipants());
        System.out.println(personaList);
        agregarTercero.setPersona(personaList);
        agregarTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(agregarTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);
        return requestRimac;

    }


}
