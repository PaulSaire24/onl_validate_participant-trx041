package com.bbva.rbvd.lib.r041.service.dao.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.service.dao.ICustomerInformationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.util.CollectionUtils.isEmpty;

public class CustomerInformationDAOImpl extends AbstractLibrary implements ICustomerInformationDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInformationDAOImpl.class);
    private PISDR601 pisdr601;
    @Override
    public QuotationJoinCustomerInformationDTO getCustomerBasicInformation(String quotationId) {
        try{
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation START *****");
            QuotationJoinCustomerInformationDTO responseQueryCustomerProductInformation = pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(quotationId);
            LOGGER.info("***** CustomerInformationDAOImpl - getCustomerBasicInformation | responseQueryCustomerProductInformation {} *****",responseQueryCustomerProductInformation);
                return responseQueryCustomerProductInformation;
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_OBTAIN_QUOTATION_FROM_DB.getValue());
        }
    }

    public void setPisdr601(PISDR601 pisdr601) {
        this.pisdr601 = pisdr601;
    }
}
