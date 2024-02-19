package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ConsumerInternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerInternalService.class);

    private RBVDR048 rbvdr048;

    public ConsumerInternalService(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    public PEWUResponse executeGetCustomerService(String numDoc, String typeDoc){
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService Start *****");
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService numDoc {} *****", numDoc);
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService typeDoc {} *****", typeDoc);

        PEWUResponse result = rbvdr048.executeGetCustomerService(numDoc,typeDoc);
        LOGGER.info("***** RBVDR041Impl - executeGetCustomerService  ***** Response Host: {}", result);
        if( Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty()){
            return result;
        }
        LOGGER.info("***** RBVDR041Impl - executeGetListCustomer ***** with error: {}", result.getHostMessage());
        throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_PBTQ_CLIENT_INFORMATION_SERVICE.getValue());
    }
}
