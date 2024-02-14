package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

import java.util.List;
import java.util.Map;


public interface PreValidate {

    PayloadConfig getConfig(ValidateParticipantDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationJoinCustomerInformationDTO quotationInformation, String personType);
    QuotationJoinCustomerInformationDTO getCustomerBasicInformation(String quotationId);

}
