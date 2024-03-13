package com.bbva.rbvd;

import com.bbva.elara.domain.transaction.RequestHeaderParamsName;

import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.response.HttpResponseCode;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;

import com.bbva.rbvd.lib.r041.RBVDR041;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class RBVDT04101PETransaction extends AbstractRBVDT04101PETransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDT04101PETransaction.class);

	@Override
	public void execute() {
		RBVDR041 rbvdR041 = this.getServiceLibrary(RBVDR041.class);
		LOGGER.info(" :: executeValidateAddParticipant :: [ START ] ");
		LOGGER.info(" :: executeValidateAddParticipant :: Request [ QuotationId :: {} , Participants :: {} ] ",this.getQuotationId(),this.getParticipants());
		InputParticipantsDTO validateParticipant = new InputParticipantsDTO();
		validateParticipant.setParticipants(this.getParticipants());
		validateParticipant.setQuotationId(this.getQuotationId());
		String traceId = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.REQUESTID);
		String channelCode = (String) this.getRequestHeader().getHeaderParameter(RequestHeaderParamsName.CHANNELCODE);
		validateParticipant.setTraceId(traceId);
		validateParticipant.setChannelId(channelCode);
		AgregarTerceroBO result = rbvdR041.executeValidateAddParticipant(validateParticipant);
		LOGGER.info(" :: executeValidateAddParticipant :: response rimac -> {}", result);
		if(Objects.nonNull(result) && CollectionUtils.isEmpty(this.getAdviceList())){
			LOGGER.info(" :: execute trx -> OK");
			this.setHttpResponseCode(HttpResponseCode.HTTP_CODE_200, Severity.OK);
		}else{
			LOGGER.info(" :: execute trx with errors -> :_(");
			this.setSeverity(Severity.ENR);
		}
	}

}
