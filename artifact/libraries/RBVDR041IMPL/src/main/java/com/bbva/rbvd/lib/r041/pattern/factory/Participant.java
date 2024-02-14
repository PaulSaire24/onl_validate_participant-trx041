package com.bbva.rbvd.lib.r041.pattern.factory;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;

public interface Participant {

    PersonaBO createRequestParticipant(PEWUResponse personInput, ParticipantsDTO participants, QuotationJoinCustomerInformationDTO customerInformationDb, Integer roleId);

}
