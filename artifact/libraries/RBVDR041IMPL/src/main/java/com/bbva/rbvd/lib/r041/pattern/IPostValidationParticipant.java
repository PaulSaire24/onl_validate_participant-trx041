package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;

public interface IPostValidationParticipant {
    void enrichPersonData(PayloadAgregarTerceroBO payloadAgregarTerceroBO, QuotationJoinCustomerInformationDTO quotationInformation);
}
