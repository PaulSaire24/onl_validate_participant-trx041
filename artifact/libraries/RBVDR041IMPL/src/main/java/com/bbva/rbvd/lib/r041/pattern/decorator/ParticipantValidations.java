package com.bbva.rbvd.lib.r041.pattern.decorator;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;

public interface ParticipantValidations {
    PayloadStore start(InputParticipantsDTO input, String quoationId, ApplicationConfigurationService applicationConfigurationService);
}
