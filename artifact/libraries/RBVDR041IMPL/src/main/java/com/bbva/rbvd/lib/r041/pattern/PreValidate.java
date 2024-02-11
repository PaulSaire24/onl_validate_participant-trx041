package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;


public interface PreValidate {

    PayloadConfig getConfig(ValidateParticipantDTO input, ApplicationConfigurationService applicationConfigurationService);
    QuotationJoinCustomerInformationDTO getCustomerBasicInformation(String quotationId);

}
