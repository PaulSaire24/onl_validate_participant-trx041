package com.bbva.rbvd.lib.r041.enrichoperation;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

public interface IEnrichPayloadProduct {
    PayloadConfig enrichParticipantData(InputParticipantsDTO input, ApplicationConfigurationService applicationConfigurationService, QuotationCustomerDAO quotationInformation);
     AgregarTerceroBO enrichRequestToCompany(PayloadConfig payloadConfig) ;

    }
