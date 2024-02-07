package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.pattern.IPostValidationParticipant;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidationEnrichByProduct implements IPostValidationParticipant {

    @Override
    public void enrichPersonData(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationJoinCustomerInformationDTO quotationInformation) {
        String insuranceProductType = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        switch (insuranceProductType){
            case "830":
                List<PersonaBO> listPeople = payloadAgregarTerceroBO.getPersona().stream().map(people -> {
                   people.setProteccionDatosPersonales("S");
                   people.setEnvioComunicacionesComerciales("N");
                   return people;
                }).collect(Collectors.toList());
                payloadAgregarTerceroBO.setPersona(listPeople);
                payloadAgregarTerceroBO.setCotizacion(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
            case "840":
            case "841":
            case "845":
                if(Objects.nonNull(payloadAgregarTerceroBO)){
                    payloadAgregarTerceroBO.setProducto(quotationInformation.getInsuranceBusiness().getInsuranceBusinessName());
                }
        }
    }


}
