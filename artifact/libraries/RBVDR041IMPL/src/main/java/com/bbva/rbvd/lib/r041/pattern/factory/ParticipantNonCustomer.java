package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.lib.r041.business.customer.NonCustomerBusiness;

public class ParticipantNonCustomer implements Participant{

    @Override
    public PersonaBO createRequestParticipant(com.bbva.rbvd.lib.r041.transfer.Participant participant, QuotationCustomerDAO customerInformationDb, Integer roleId) {
        return NonCustomerBusiness.mapNonCustomerRequestData(participant.getInputNonCustomer(), roleId);
    }
}
