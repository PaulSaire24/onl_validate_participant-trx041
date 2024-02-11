package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadStore;

public interface Validate {
    PayloadStore start(ValidateParticipantDTO input, String quoationId, ApplicationConfigurationService applicationConfigurationService);
}
