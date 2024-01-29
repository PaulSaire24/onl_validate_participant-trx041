package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.ksmk.dto.caas.CredentialsDTO;
import com.bbva.ksmk.dto.caas.InputDTO;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class ConsumerCryptographyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerCryptographyService.class);
    private static final String BASE64_URL = "B64URL";
    private static final String APP_NAME = "apx-pe";
    private static final String INPUT_CONTEXT_CRYPTO_CUSTOMER_ID = "operation=DO;type=customerId;origin=ASO;endpoint=ASO;securityLevel=5";
    private static final String CRED_EXTRA_PARAMS = "user=KSMK;country=PE";
    private KSMKR002 ksmkr002;

    public String executeKsmkCryptographyService(String customerId){
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService Start *****");
        String b64CustomerId =  FunctionUtils.encodeB64(customerId);
        List<OutputDTO> output = ksmkr002.executeKSMKR002(Collections.singletonList(new InputDTO(b64CustomerId, BASE64_URL)), "", INPUT_CONTEXT_CRYPTO_CUSTOMER_ID, new CredentialsDTO(APP_NAME, "", CRED_EXTRA_PARAMS));
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService  ***** Response: {}", output);
        if (CollectionUtils.isEmpty(output)){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_KSMK_ENCRYPT_SERVICE.getValue());
        }

        return output.get(0).getData();
    }

    public void setKsmkr002(KSMKR002 ksmkr002) {
        this.ksmkr002 = ksmkr002;
    }
}
