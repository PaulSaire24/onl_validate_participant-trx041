package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.ksmk.dto.caas.CredentialsDTO;
import com.bbva.ksmk.dto.caas.InputDTO;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r066.RBVDR066;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;


public class ConsumerLegalPersonInternalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerLegalPersonInternalService.class);

    private RBVDR066 rbvdr066;

        public ListBusinessesASO executeListBusinessService(String encryptedCustomerId){
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService Start *****");
        ListBusinessesASO listBusinesses = rbvdr066.executeGetListBusinesses(encryptedCustomerId, null);
        if(CollectionUtils.isEmpty(listBusinesses.getData())){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_LIST_BUSINESS_SERVICE.getValue());
        }

        return listBusinesses;
    }

    public void setRbvdr066(RBVDR066 rbvdr066) {
        this.rbvdr066 = rbvdr066;
    }
}
