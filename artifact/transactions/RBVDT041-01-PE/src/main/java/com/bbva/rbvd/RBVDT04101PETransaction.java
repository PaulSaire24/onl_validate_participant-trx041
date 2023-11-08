package com.bbva.rbvd;

import com.bbva.rbvd.lib.r041.RBVDR041;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		RBVDR041 rbvdr041 = this.getServiceLibrary(RBVDR041.class);
		// TODO - Implementation of business logic
	}

}
