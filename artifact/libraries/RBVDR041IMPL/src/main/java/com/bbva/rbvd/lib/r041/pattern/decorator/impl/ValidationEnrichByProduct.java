package com.bbva.rbvd.lib.r041.pattern.decorator.impl;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.lib.r041.pattern.IPostValidationParticipant;
import com.bbva.rbvd.lib.r041.transform.bean.NaturalPersonRimacBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ValidationEnrichByProduct implements IPostValidationParticipant {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationEnrichByProduct.class);

    @Override
    public void enrichPersonData(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationJoinCustomerInformationDTO quotationInformation) {
        String insuranceProductType = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        switch (insuranceProductType){
            case "830":
                LOGGER.info("*** ValidationEnrichByProduct *** enrich case {}", RBVDInternalConstants.TypeInsuranceProduct.VEHICLE.toString().concat(" ").concat("PRODUCT"));
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
                LOGGER.info("*** ValidationEnrichByProduct *** enrich case {}", "LIFE PRODUCT");
                if(Objects.nonNull(payloadAgregarTerceroBO)){
                    payloadAgregarTerceroBO.setProducto(quotationInformation.getInsuranceBusiness().getInsuranceBusinessName());
                }
            default:
                LOGGER.info("*** ValidationEnrichByProduct *** enrich case {}", "NON LIFE PRODUCT");
        }
    }


}
