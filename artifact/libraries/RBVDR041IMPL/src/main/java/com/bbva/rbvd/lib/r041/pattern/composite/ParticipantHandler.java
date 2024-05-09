package com.bbva.rbvd.lib.r041.pattern.composite;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r048.RBVDR048;

public interface ParticipantHandler {

    AgregarTerceroBO handleRequest(InputParticipantsDTO input, RBVDR048 rbvdr048, ApplicationConfigurationService applicationConfigurationService, ParticipantProperties participantProperties, QuotationCustomerDAO quotationInformation);
}
