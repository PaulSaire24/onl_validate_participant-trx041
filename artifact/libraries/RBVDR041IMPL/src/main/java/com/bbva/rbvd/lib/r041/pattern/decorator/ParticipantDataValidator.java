package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;
import com.bbva.rbvd.lib.r048.RBVDR048;

public interface ParticipantDataValidator {
    PayloadStore start(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService);
}
