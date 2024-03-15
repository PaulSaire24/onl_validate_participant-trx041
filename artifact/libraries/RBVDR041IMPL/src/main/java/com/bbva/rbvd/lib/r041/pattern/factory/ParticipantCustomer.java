package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;

public class ParticipantCustomer implements Participant{

    @Override
    public PersonaBO createRequestParticipant(PEWUResponse personInput, ParticipantsDTO participants, QuotationJoinCustomerInformationDTO customerInformationDb, Integer roleId) {
        return ValidateRimacNaturalPerson.mapInRequestRimacNonLife(personInput,participants,customerInformationDb,roleId);
    }
}
