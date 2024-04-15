package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantDataValidator;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ParticipantParameter;
import com.bbva.rbvd.lib.r041.pattern.decorator.products.InsuranceProductNonLifeProducts;
import com.bbva.rbvd.lib.r041.pattern.decorator.products.LifeProduct;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FactoryProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryProduct.class);
    public static ParticipantDataValidator getProductObject(String productId, String businessName, RBVDR048 rbvdr048, ParticipantParameter validationParameter,
                                                            ApplicationConfigurationService applicationConfigurationService){
        List<String> excludedLifeProductList = Arrays.asList(applicationConfigurationService.getProperty(
                ConstantsUtil.ENABLED_LIFE_PRODUCTS).split(","));
        List<String> excludedNonLifeProductList = Arrays.asList(applicationConfigurationService.getProperty(
                ConstantsUtil.ENABLED_NON_LIFE_PRODUCTS).split(","));

        if(ConstantsUtil.Product.BUSINESS_LIFE.getCode().equalsIgnoreCase(businessName) &&
                excludedLifeProductList.contains(productId)){
            LOGGER.info("**FactoryProductValidate: LIFE product **");
            return  LifeProduct.Builder.an()
                    .preValidate(ParticipantParameter.Builder.an().rbvdr048(rbvdr048).buildOne())
                    .build();
        } else if(excludedNonLifeProductList.contains(productId)){
            LOGGER.info("** FactoryProductValidate: NON LIFE product **");
            return InsuranceProductNonLifeProducts.Builder.an()
                    .preValidate(validationParameter)
                    .build();
        } else {
            throw new BusinessException(ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getMessage()
                            .concat(TypeErrorControllerEnum.ERROR_NOT_ENABLED_PRODUCT.getValue()));
        }
    }

    public static void enrichPayloadByProduct(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationCustomerDAO quotationInformation) {
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
