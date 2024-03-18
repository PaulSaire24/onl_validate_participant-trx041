package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;


public interface PreParticipantValidations {

    PayloadConfig getConfig(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService);
    PayloadConfig getConfig(InputParticipantsDTO input,ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDTO quotationInformation, String personType);
    QuotationCustomerDTO getCustomerFromQuotation(String quotationId);

}
