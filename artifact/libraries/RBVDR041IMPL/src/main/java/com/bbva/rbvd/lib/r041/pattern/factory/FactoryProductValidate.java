package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.lib.r041.pattern.PostValidate;
import com.bbva.rbvd.lib.r041.pattern.PreValidate;
import com.bbva.rbvd.lib.r041.pattern.Validate;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidateStore;
import com.bbva.rbvd.lib.r041.pattern.decorator.impl.ValidationParameter;
import com.bbva.rbvd.lib.r041.pattern.products.ValidateVidaDinamico;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;

public class FactoryProductValidate {
    public static Validate getProductType(String productId, PISDR352 pisdr352, PBTQR002 pbtqr002){
        if(ConstantsUtil.Product.DYNAMIC_LIFE.getCode().equalsIgnoreCase(productId)){
            return  ValidateVidaDinamico.Builder.an()
                    .preValidate(ValidationParameter.Builder.an().pbtqr002(pbtqr002).buildOne())
                    .postValidate(new ValidateStore(pisdr352))
                    .pisdr352(pisdr352)
                    .build();
        } else {
            return null;
        }
    }

}
