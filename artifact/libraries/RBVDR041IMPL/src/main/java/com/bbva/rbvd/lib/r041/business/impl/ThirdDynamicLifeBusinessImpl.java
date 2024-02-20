package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r041.transform.bean.ContractorBean;
import com.bbva.rbvd.lib.r041.transform.bean.InsuredBean;
import com.bbva.rbvd.lib.r041.transform.bean.PersonaBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ThirdDynamicLifeBusinessImpl implements IThirdDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdDynamicLifeBusinessImpl.class);
    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  agregarTercero = new PayloadAgregarTerceroBO();
        List<ParticipantsDTO> participantsInputList = payloadConfig.getInput().getParticipants();
        List<PayloadProperties> participantsPewuList = payloadConfig.getProperties();

        List<PersonaBO> personaList = new ArrayList<>();
        participantsInputList.forEach(partInput->
                participantsPewuList.forEach(parPewu->{
                    if(parPewu.getDocumetType().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getDocumentType().getId())
                            && parPewu.getDocumetNumber().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getValue())){
                        personaList.add(PersonaBean.mapInRequestRimacDynamicLife(partInput,parPewu));
                    }
                }));
        if(personaList.size()==1){
            ContractorBean.builRolContractor(personaList);
            InsuredBean.builRolInsured(personaList,payloadConfig.getInput().getParticipants());
        } else if (personaList.size()==2) {
            InsuredBean.builRolInsured(personaList,payloadConfig.getInput().getParticipants());
        }

        agregarTercero.setPersona(personaList);
        agregarTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(agregarTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);
        return requestRimac;

    }
}
