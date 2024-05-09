package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;

public interface Participant {

    PersonaBO createRequestParticipant(com.bbva.rbvd.lib.r041.transfer.Participant participant, QuotationCustomerDAO customerInformationDb, Integer roleId);

}
