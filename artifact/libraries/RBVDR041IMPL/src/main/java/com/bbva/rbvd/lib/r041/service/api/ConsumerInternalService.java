package com.bbva.rbvd.lib.r041.service.api;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.ksmk.dto.caas.CredentialsDTO;
import com.bbva.ksmk.dto.caas.InputDTO;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.util.FunctionUtils;
import com.bbva.rbvd.lib.r066.RBVDR066;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ConsumerInternalService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerInternalService.class);

    private KSMKR002 ksmkr002;
    private RBVDR066 rbvdr066;
    private PBTQR002 pbtqr002;

    public ConsumerInternalService(KSMKR002 ksmkr002, RBVDR066 rbvdr066) {
        this.ksmkr002 = ksmkr002;
        this.rbvdr066 = rbvdr066;
    }

    public ConsumerInternalService(PBTQR002 pbtqr002) {
        this.pbtqr002 = pbtqr002;
    }

    public String executeKsmkCryptographyService(String customerId){
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService Start *****");
        String b64CustomerId =  FunctionUtils.encodeB64(customerId);
        List<OutputDTO> output = ksmkr002.executeKSMKR002(Collections.singletonList(new InputDTO(b64CustomerId, ConstantsUtil.Crypto.B64URL)), "", ConstantsUtil.Crypto.INPUT_TEXT_SECURITY, new CredentialsDTO(ConstantsUtil.Crypto.APP_NAME, "", ConstantsUtil.Crypto.CRE_EXTRA_PARAMS));
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService  ***** Response: {}", output);
        if (CollectionUtils.isEmpty(output)){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_KSMK_ENCRYPT_SERVICE.getValue());
        }

        return output.get(0).getData();
    }

    public ListBusinessesASO executeListBusinessService(String encryptedCustomerId){
        LOGGER.info("***** RBVDR041Impl - executeKsmkCryptographyService Start *****");
        ListBusinessesASO listBusinesses = rbvdr066.executeGetListBusinesses(encryptedCustomerId, null);
        if(CollectionUtils.isEmpty(listBusinesses.getData())){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_LIST_BUSINESS_SERVICE.getValue());
        }

        return listBusinesses;
    }

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
}
