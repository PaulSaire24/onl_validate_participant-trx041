package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;

public interface ParticipantValidations {
    PayloadStore start(InputParticipantsDTO input, QuotationJoinCustomerInformationDTO quotationInformation, ApplicationConfigurationService applicationConfigurationService);
}
