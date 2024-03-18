package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;

public class ParticipantNonCustomer implements Participant{

    @Override
    public PersonaBO createRequestParticipant(PEWUResponse personInput, ParticipantsDTO participants, QuotationCustomerDTO customerInformationDb, Integer roleId) {
        return ValidateRimacNaturalPerson.mapNonCustomerRequestData(participants, roleId);
    }
}
