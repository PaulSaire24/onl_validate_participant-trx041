package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RolBean {
    public static List<RolDTO> rolByParticipantTransformBean(Map<String, Object> input){
        List<RolDTO> rolList = new ArrayList<>();
        List<Map<String, Object>> queryForListResponse = (List<Map<String, Object>>) input.get("dtoInsurance");
        queryForListResponse.forEach(list -> rolList.add(createRolObject(list)));
        return rolList;
    }

    private static RolDTO createRolObject(Map<String, Object> map){
        RolDTO rolDTO = new RolDTO();
        rolDTO.setParticipantRoleId(((BigDecimal) map.get("PARTICIPANT_ROLE_ID")).intValue());
        rolDTO.setInsuranceCompanyRoleId((String) map.get("INSURANCE_COMPANY_ROLE_ID"));
        return rolDTO;
    }
}
