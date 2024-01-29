package com.bbva.rbvd.lib.r041.properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticipantProperties extends Properties{

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantProperties.class);

    public String obtainRoleCodeByEnum(String roleEnum){
        String codeRole = this.getProperty(roleEnum, StringUtils.EMPTY);
        LOGGER.info(" :: ParticipantProperties[ obtainRoleCodeByEnum :: {} ]",codeRole);
        return codeRole;
    }

}
