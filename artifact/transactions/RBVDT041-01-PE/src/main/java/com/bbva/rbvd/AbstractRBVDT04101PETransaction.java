package com.bbva.rbvd;

import com.bbva.elara.transaction.AbstractTransaction;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import java.util.List;

/**
 * In this class, the input and output data is defined automatically through the setters and getters.
 */
public abstract class AbstractRBVDT04101PETransaction extends AbstractTransaction {

	public AbstractRBVDT04101PETransaction(){
	}


	/**
	 * Return value for input parameter participants
	 */
	protected List<ParticipantsDTO> getParticipants(){
		return (List<ParticipantsDTO>)this.getParameter("participants");
	}

	/**
	 * Set value for List<List<ParticipantsDTO>> output parameter participants
	 */
	protected void setParticipants(final List<List<ParticipantsDTO>> field){
		this.addParameter("participants", field);
	}
}
