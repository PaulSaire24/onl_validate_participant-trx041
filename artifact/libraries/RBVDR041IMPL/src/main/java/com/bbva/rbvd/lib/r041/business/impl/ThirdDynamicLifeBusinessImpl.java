package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.business.ICreateThirdPartyBusiness;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacLegalPerson;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ThirdDynamicLifeBusinessImpl implements ICreateThirdPartyBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdDynamicLifeBusinessImpl.class);
    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  agregarTercero = new PayloadAgregarTerceroBO();
        List<PersonaBO> personaList = ValidateRimacNaturalPerson.mapInRequestRimacDynamicLife(payloadConfig);
        agregarTercero.setPersona(personaList);
        agregarTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(agregarTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);
        return requestRimac;

    }


}
