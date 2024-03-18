package com.bbva.rbvd.lib.r041;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;


public interface RBVDR041 {

	AgregarTerceroBO executeValidateAddParticipant(InputParticipantsDTO participant);

}
