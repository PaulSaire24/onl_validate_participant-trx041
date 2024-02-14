package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r352.PISDR352;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.lib.r041.pattern.InsuranceProduct;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateStore;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.products.InsuranceProductNonLifeProducts;
import com.bbva.rbvd.lib.r041.pattern.products.InsuranceProductVidaDinamico;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProductFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductFactory.class);
    private static final String MESSAGE_CONSTANT = "*** ProductFactory *** enrich case {}";


    public static InsuranceProduct buildProduct(String productId, PISDR352 pisdr352, PBTQR002 pbtqr002, ValidationParameter validationParameter){
        if(ConstantsUtil.Product.DYNAMIC_LIFE.getCode().equalsIgnoreCase(productId)){
            LOGGER.info("**FactoryProductValidate: Dynamic Life product **");
            return  InsuranceProductVidaDinamico.Builder.an()
                    .preValidate(validationParameter)
                    .postValidate(new ValidateStore(pisdr352))
                    .build();
        } else {
            LOGGER.info("** FactoryProductValidate: NON LIFE product **");
            return InsuranceProductNonLifeProducts.Builder.an()
                    .preValidate(validationParameter)
                    .postValidate(new ValidateStore(pisdr352))
                    .build();
        }
    }

    public static void enrichPayloadByProduct(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationJoinCustomerInformationDTO quotationInformation) {
        String insuranceProductType = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        switch (insuranceProductType) {
            case "830":
                LOGGER.info(MESSAGE_CONSTANT, RBVDInternalConstants.TypeInsuranceProduct.VEHICLE.toString().concat(" ").concat("PRODUCT"));
                List<PersonaBO> listPeople = payloadAgregarTerceroBO.getPersona().stream().map(people -> {
                    people.setProteccionDatosPersonales("S");
                    people.setEnvioComunicacionesComerciales("N");
                    return people;
                }).collect(Collectors.toList());
                payloadAgregarTerceroBO.setPersona(listPeople);
                payloadAgregarTerceroBO.setCotizacion(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
            default:
                LOGGER.info(MESSAGE_CONSTANT, "NON LIFE PRODUCT");
        }
    }

}
