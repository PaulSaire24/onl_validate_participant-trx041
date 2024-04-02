package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantParameter;
import com.bbva.rbvd.lib.r041.pattern.decorator.products.InsuranceProductNonLifeProducts;
import com.bbva.rbvd.lib.r041.pattern.decorator.products.DynamicLifeProduct;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FactoryProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryProduct.class);
    public static ParticipantDataValidator getProductObject(String productId, RBVDR048 rbvdr048, ParticipantParameter validationParameter){
        if(ConstantsUtil.Product.DYNAMIC_LIFE.getCode().equalsIgnoreCase(productId)){
            LOGGER.info("**FactoryProductValidate: Dynamic Life product **");
            return  DynamicLifeProduct.Builder.an()
                    .preValidate(ParticipantParameter.Builder.an().rbvdr048(rbvdr048).buildOne())
                    .build();
        } else {
            LOGGER.info("** FactoryProductValidate: NON LIFE product **");
            return InsuranceProductNonLifeProducts.Builder.an()
                    .preValidate(validationParameter)
                    .build();
        }
    }

    public static void enrichPayloadByProduct(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationCustomerDTO quotationInformation) {
        String insuranceProductType = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        switch (insuranceProductType) {
            case "830":
                LOGGER.info("*** ProductFactory *** enrich case {}", RBVDInternalConstants.TypeInsuranceProduct.VEHICLE.toString().concat(" ").concat("PRODUCT"));
                if(Objects.nonNull(payloadAgregarTerceroBO.getPersona())) {
                    List<PersonaBO> listPeople = payloadAgregarTerceroBO.getPersona().stream().map(people -> {
                        people.setProteccionDatosPersonales("S");
                        people.setEnvioComunicacionesComerciales("N");
                        return people;
                    }).collect(Collectors.toList());
                    payloadAgregarTerceroBO.setPersona(listPeople);
                }else{
                    List<OrganizacionBO> listOrganization = payloadAgregarTerceroBO.getOrganizacion().stream().map(organization -> {
                        organization.setProteccionDatosPersonales("S");
                        organization.setEnvioComunicacionesComerciales("N");
                        return organization;
                    }).collect(Collectors.toList());
                    payloadAgregarTerceroBO.setOrganizacion(listOrganization);
                }
                payloadAgregarTerceroBO.setCotizacion(quotationInformation.getQuotation().getInsuranceCompanyQuotaId());
                break;
            default:
                LOGGER.info("*** ProductFactory *** {}", "NON ENRICHMENT");
        }
    }

}
