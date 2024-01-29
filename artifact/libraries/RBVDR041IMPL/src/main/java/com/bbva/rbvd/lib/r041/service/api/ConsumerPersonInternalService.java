package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ConsumerPersonInternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPersonInternalService.class);
    private PBTQR002 pbtqr002;

    public PEWUResponse executeGetCustomerService(String documentNumber, String documentType){
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService Start *****");
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService documentNumber {} - documentType {} *****", documentNumber, documentType);
        PEWUResponse result = pbtqr002.executeSearchInHostByDocument(documentNumber,documentType);
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService  ***** Response Host: {}", result);
        if( Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty()){
            return result;
        }

        LOGGER.info("***** RBVDR041Impl - executeGetListCustomer ***** with error: {}", result.getHostMessage());
        throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_PBTQ_CLIENT_INFORMATION_SERVICE.getValue());
    }

    public void setPbtqr002(PBTQR002 pbtqr002) {
        this.pbtqr002 = pbtqr002;
    }
}
