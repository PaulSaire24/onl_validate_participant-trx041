package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.rbvd.lib.r041.pattern.Validate;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateStore;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.products.ValidateDynamicLife;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryProductValidate {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryProductValidate.class);
    public static Validate getProductType(String productId, RBVDR048 rbvdr048, PBTQR002 pbtqr002){
        if(ConstantsUtil.Product.DYNAMIC_LIFE.getCode().equalsIgnoreCase(productId)){
            LOGGER.info("**FactoryProductValidate: Dynamic Life product **");
            return  ValidateDynamicLife.Builder.an()
                    .preValidate(ValidationParameter.Builder.an().pbtqr002(pbtqr002).buildOne())
                    .postValidate(new ValidateStore(rbvdr048))
                    .build();
        } else {
            LOGGER.info("**FactoryProductValidate: another product");
            return null;
        }
    }

}
