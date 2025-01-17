package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;

public class ParticipantCustomer implements Participant{

    @Override
    public PersonaBO createRequestParticipant(com.bbva.rbvd.lib.r041.transfer.Participant participants, QuotationCustomerDAO customerInformationDb, Integer roleId) {
        return ValidateRimacNaturalPerson.mapCustomerRequestData(participants,customerInformationDb,roleId);
    }
}
