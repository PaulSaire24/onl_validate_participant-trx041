package com.bbva.rbvd.lib.r048.impl.business;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorRequestDTO;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.util.Constants;
import com.bbva.rbvd.lib.r048.impl.util.ErrorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.util.Objects;

public class HandlerErrorBusiness {

    private PISDR403 pisdR403;
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerErrorBusiness.class);

    public HandlerErrorBusiness(PISDR403 pisdR403) {
        this.pisdR403 = pisdR403;
    }

    public void startHandlerError(String productId, RestClientException ex) throws BusinessException{
        ErrorRequestDTO err =  ErrorUtil.prepareRequestToHandlerError(ex);
        LOGGER.info("** RBVDR048Impl - executeAddParticipantsService catch {} **",err);
        if(Objects.nonNull(err) && !CollectionUtils.isEmpty(err.getDetails())){
            err.setTypeErrorScope("RIMAC");
            err.setReference(Constants.getCodeFromDBByCode(productId));
            LOGGER.info("** RBVDR048Impl - with reference (product) {} **",err);
            ErrorResponseDTO responseErr = this.pisdR403.executeFindError(err);
            if(Objects.nonNull(responseErr) && !StringUtils.isEmpty(responseErr.getCode()) && !StringUtils.isEmpty(responseErr.getMessage())){
                LOGGER.info("** RBVDR048Impl - Error encontrado en base de datos");
                throw new BusinessException(responseErr.getCode(), false, responseErr.getMessage());
            }
            throw new BusinessException(Constants.ERROR_NOT_FOUND_IN_DATA_BASE_CODE, false, Constants.ERROR_NOT_FOUND_IN_DATA_BASE_MESSAGE);
        }
        throw new BusinessException(ValidateParticipantErrors.ERROR_NOT_FOUND.getAdviceCode(), false, ValidateParticipantErrors.ERROR_NOT_FOUND.getMessage());
    }
}
