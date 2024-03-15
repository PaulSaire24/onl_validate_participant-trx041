package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;

public interface InsuranceProduct {
    PayloadStore generateAddParticipants(InputParticipantsDTO input, QuotationJoinCustomerInformationDTO quotationInformation, ApplicationConfigurationService applicationConfigurationService, String productType);
}
