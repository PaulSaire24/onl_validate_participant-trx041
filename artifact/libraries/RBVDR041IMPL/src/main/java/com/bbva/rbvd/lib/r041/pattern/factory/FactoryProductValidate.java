package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.lib.r041.pattern.decorator.ParticipantValidations;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateStore;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.decorator.products.ValidateDynamicLife;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryProductValidate {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryProductValidate.class);
    public static ParticipantValidations getProductType(String productId, RBVDR048 rbvdr048){
        if(ConstantsUtil.Product.DYNAMIC_LIFE.getCode().equalsIgnoreCase(productId)){
            LOGGER.info("**FactoryProductValidate: Dynamic Life product **");
            return  ValidateDynamicLife.Builder.an()
                    .preValidate(ValidationParameter.Builder.an().rbvdr048(rbvdr048).buildOne())
                    .postValidate(new ValidateStore(rbvdr048))
                    .build();
        } else {
            LOGGER.info("**FactoryProductValidate: another product");
            return null;
        }
    }

}
