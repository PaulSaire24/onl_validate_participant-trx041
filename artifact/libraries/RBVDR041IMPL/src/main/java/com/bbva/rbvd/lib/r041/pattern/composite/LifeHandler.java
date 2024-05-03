package com.bbva.rbvd.lib.r041.pattern.composite;

import com.bbva.rbvd.lib.r041.pattern.strategy.product.DynamicLifeStrategy;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;

public class LifeHandler extends AbstractCompositeParticipantHandler {
    private final RBVDR048 rbvdr048;
    private final ParticipantProperties participantProperties;

    static {
        addProductHandler(ConstantsUtil.Product.DYNAMIC_LIFE.getCode(), new DynamicLifeStrategy());
    }
    
    public LifeHandler(RBVDR048 rbvdr048,ParticipantProperties participantProperties){
      this.rbvdr048 = rbvdr048;
      this.participantProperties = participantProperties;
    }
    
}
