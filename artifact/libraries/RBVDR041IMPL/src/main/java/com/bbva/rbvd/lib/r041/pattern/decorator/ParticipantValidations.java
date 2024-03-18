package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;

public interface ParticipantValidations {
    PayloadStore start(InputParticipantsDTO input, QuotationCustomerDTO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService);
}
