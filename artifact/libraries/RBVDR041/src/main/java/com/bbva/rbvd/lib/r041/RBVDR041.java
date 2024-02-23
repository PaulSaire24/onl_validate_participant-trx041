package com.bbva.rbvd.lib.r041;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;


public interface RBVDR041 {

	AgregarTerceroBO executeValidateAddParticipant(ValidateParticipantDTO participant);

}
