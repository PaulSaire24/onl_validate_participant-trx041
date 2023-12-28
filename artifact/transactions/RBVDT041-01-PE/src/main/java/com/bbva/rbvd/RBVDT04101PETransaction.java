package com.bbva.rbvd;

import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.RBVDR041;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * It validates if a one or more participants con or not get an insurance
 *
 */
public class RBVDT04101PETransaction extends AbstractRBVDT04101PETransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDT04101PETransaction.class);

	/**
	 * The execute method...
	 */
	@Override
	public void execute() {
		LOGGER.info("RBVDT04101PETransaction - START");
		RBVDR041 rbvdr041 = this.getServiceLibrary(RBVDR041.class);

		rbvdr041.addThird(this.getParticipants());
		// TODO - Implementation of business logic
	}

}
