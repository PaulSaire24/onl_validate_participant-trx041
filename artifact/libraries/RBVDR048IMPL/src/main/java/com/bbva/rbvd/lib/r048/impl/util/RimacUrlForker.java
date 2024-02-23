package com.bbva.rbvd.lib.r048.impl.util;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.utils.RBVDProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RimacUrlForker {
    private static final Logger LOGGER = LoggerFactory.getLogger(RimacUrlForker.class);
    private ApplicationConfigurationService applicationConfigurationService;
    public RimacUrlForker(ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
    }

    public String generateUriAddParticipants(String quotationId,String productId){
        String urifromConsole = this.applicationConfigurationService.getProperty(RBVDProperties.URI_ADD_PARTICIPANTS.getValue().replace("{idProd}",productId));
        int helper = urifromConsole.indexOf(".com");
        String uri = urifromConsole.substring(helper+4).replace("{cotizacion}", quotationId);

        LOGGER.info("***** RimacUrlForker - generateUriAddParticipants ***** uri: {}", uri);

        return uri;
    }

    public String generateKeyAddParticipants(String productId){
        String key = RBVDProperties.ID_ADD_PARTICIPANTS.getValue().concat(".").concat(productId);

        LOGGER.info("**** RimacUrlForker - generateKeyAddParticipants - property key : {} ****",key);

        return key;
    }
}
