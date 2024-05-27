package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.product.ICardProductBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.springframework.util.StringUtils;

import java.util.Optional;

public class CardProductBusinessImpl implements ICardProductBusiness {
    public AgregarTerceroBO doCardProduct(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {
        if(rimacRequestData.getPayload().getPersona() != null){
            rimacRequestData.getPayload().getPersona().forEach(person -> {
                validateInsuredWithNoData(person, rimacRequestData);
            });
        }
        return rimacRequestData;
    }

    private static void validateInsuredWithNoData(PersonaBO enrichedPerson, AgregarTerceroBO rimacRequestPerson){
        if (enrichedPerson.getRol() == ConstantsUtil.Rol.INSURED.getValue() && StringUtils.isEmpty(enrichedPerson.getDireccion())) {
            Optional<PersonaBO> paymentManager = rimacRequestPerson.getPayload().getPersona().stream().filter(requestPerson -> requestPerson.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue()).findFirst();
            if (paymentManager.isPresent()){
                PersonBean.buildDirectionDataFromPerson(enrichedPerson, paymentManager.get());
            }
        }
    }
}
