package com.bbva.rbvd.lib.r041;

import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.ResponseLibrary;

import java.util.List;

/**
 * The  interface RBVDR041 class...
 */
public interface RBVDR041 {

	/**
	 * The execute method...
	 */
	ResponseLibrary<Void> executeValidateAddParticipant(ValidateParticipantDTO participant);

}
