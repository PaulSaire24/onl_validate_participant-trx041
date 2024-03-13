package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.PayloadCustomer;
import com.bbva.rbvd.lib.r041.transform.bean.ContractorBean;
import com.bbva.rbvd.lib.r041.transform.bean.InsuredBean;
import com.bbva.rbvd.lib.r041.transform.bean.PersonaBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThirdDynamicLifeBusinessImpl implements IThirdDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdDynamicLifeBusinessImpl.class);
    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  agregarTercero = new PayloadAgregarTerceroBO();
        List<ParticipantsDTO> participantsInputList = payloadConfig.getInput().getParticipants();
        List<PayloadCustomer> participantsPewuList = payloadConfig.getProperties();

        List<PersonaBO> personaList = new ArrayList<>();
        participantsInputList.forEach(partInput-> participantsPewuList.stream()
                .filter(parPewu -> parPewu.getDocumentType().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getDocumentType().getId())
                        && parPewu.getDocumentNumber().equalsIgnoreCase(partInput.getIdentityDocuments().get(0).getValue()))
                .findFirst()
                .ifPresent(parPewu -> personaList.add(PersonaBean.mapInRequestRimacDynamicLife(partInput, parPewu))));

        LOGGER.info("** doDynamicLife - personaList -> {}",personaList);
        if(personaList.size()==1 && Objects.nonNull(personaList.get(0))){
            ContractorBean.builRolContractor(personaList);
            InsuredBean.builRolInsured(personaList,payloadConfig.getInput().getParticipants(),payloadConfig.getDataInsuredBD());
        } else if (personaList.size()==2 && Objects.nonNull(personaList.get(0))) {
            InsuredBean.builRolInsured(personaList,payloadConfig.getInput().getParticipants(),payloadConfig.getDataInsuredBD());
        }

        agregarTercero.setPersona(personaList);
        agregarTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(agregarTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);
        return requestRimac;

    }
}
