package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.pattern.composite.LifeHandler;
import com.bbva.rbvd.lib.r041.pattern.composite.NoLifeHandler;
import com.bbva.rbvd.lib.r041.pattern.composite.ParticipantHandler;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class FactoryProduct {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryProduct.class);

    public static ParticipantHandler getStrategyByProduct(RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService, ParticipantProperties participantProperties, QuotationCustomerDAO quotationInformation){
        List<String> includedLifeProductList = Arrays.asList(applicationConfigurationService.getProperty(ConstantsUtil.ENABLED_LIFE_PRODUCTS).split(","));
        List<String> includedNonLifeProductList = Arrays.asList(applicationConfigurationService.getProperty(ConstantsUtil.ENABLED_NON_LIFE_PRODUCTS).split(","));

        String productId = quotationInformation.getInsuranceProduct().getInsuranceProductType();
        String businessName = quotationInformation.getInsuranceBusiness().getInsuranceBusinessName();

        if(ConstantsUtil.Product.BUSINESS_LIFE.getCode().equalsIgnoreCase(businessName) &&
                includedLifeProductList.contains(productId)){
            LOGGER.info("** FactoryProductValidate: LIFE product **");
            return new LifeHandler(rbvdr048,participantProperties);

        } else if(includedNonLifeProductList.contains(productId)){
            LOGGER.info("** FactoryProductValidate: NON LIFE product **");
            return new NoLifeHandler(rbvdr048,participantProperties);

        } else {
            throw new BusinessException(ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getMessage()
                            .concat(TypeErrorControllerEnum.ERROR_NOT_ENABLED_PRODUCT.getValue()));
        }
    }

    private FactoryProduct() {
    }
}
