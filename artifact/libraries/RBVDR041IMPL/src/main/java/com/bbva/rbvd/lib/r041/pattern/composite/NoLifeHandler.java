package com.bbva.rbvd.lib.r041.pattern.composite;

import com.bbva.rbvd.lib.r041.pattern.strategy.product.DynamicLifeStrategy;
import com.bbva.rbvd.lib.r041.pattern.strategy.product.VehicleStrategy;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;

public class NoLifeHandler extends AbstractCompositeParticipantHandler{

    private final RBVDR048 rbvdr048;
    private final ParticipantProperties participantProperties;

    /**
     * Configurar los manejadores de productos para la categoría de seguro royal no vida
     * ej: addProductHandler(producType, ImplObject);
     * @param producType Tipo de producto de seguro (ej: 834, 832, etc.)
     * @param ImplObject Implementación de estrategia para construir request que viajará a entidad de seguros.
     */
    static {
        addProductHandler(ConstantsUtil.Product.VEHICLE.getCode(), new VehicleStrategy());
    }

    public NoLifeHandler(RBVDR048 rbvdr048, ParticipantProperties participantProperties){
        this.rbvdr048 = rbvdr048;
        this.participantProperties = participantProperties;
    }

}
