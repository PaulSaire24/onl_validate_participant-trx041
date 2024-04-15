package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;

public interface Participant {

    PersonaBO createRequestParticipant(PEWUResponse personInput, ParticipantsDTO participants, QuotationCustomerDAO customerInformationDb, Integer roleId);

}
