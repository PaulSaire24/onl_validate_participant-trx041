package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;

public interface Validate {
    AgregarTerceroBO start(ValidateParticipantDTO input, String quoationId, ApplicationConfigurationService applicationConfigurationService);
}
