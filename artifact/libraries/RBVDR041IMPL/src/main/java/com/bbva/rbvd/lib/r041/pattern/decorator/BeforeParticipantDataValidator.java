package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;


public interface BeforeParticipantDataValidator {

    PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation);
    PayloadConfig before(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation, String personType);

}
