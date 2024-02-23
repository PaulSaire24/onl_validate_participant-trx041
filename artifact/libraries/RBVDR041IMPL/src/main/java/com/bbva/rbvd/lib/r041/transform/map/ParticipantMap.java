package com.bbva.rbvd.lib.r041.transform.map;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ParticipantMap {
    public static Map<String,Object> productAndModalityTransforMap(String insuranceProductId, String insuranceModalityType){
        Map<String,Object> filterRoleByProductAndModality = new HashMap<>();
        filterRoleByProductAndModality.put("INSURANCE_PRODUCT_ID",insuranceProductId);
        filterRoleByProductAndModality.put("INSURANCE_MODALITY_TYPE",insuranceModalityType);
        filterRoleByProductAndModality.put("INSURANCE_COMPANY_ID",new BigDecimal(1));
        return filterRoleByProductAndModality;
    }
}
