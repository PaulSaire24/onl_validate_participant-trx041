package com.bbva.rbvd.lib.r041.business;


import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.validateparticipant.dto.ResponseLibrary;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.lib.r041.pattern.IValidateParticipant;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;
import com.bbva.rbvd.lib.r041.service.dao.ICustomerInformationDAO;
import com.bbva.rbvd.lib.r041.service.dao.IRolDAO;
import com.bbva.rbvd.lib.r041.transform.map.ParticipantMap;
import com.bbva.rbvd.lib.r041.validation.ValidateErrorResponse;
import com.bbva.rbvd.lib.r041.validation.ValidatePersonData;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class AddParticipantBusiness {

    private ICustomerInformationDAO iCustomerInformationDAO;
    private IRolDAO iRolDAO;

    private ParticipantFactory participantFactory;

    public ResponseLibrary<Void> executeAddParticipantAllProduct(ValidateParticipantDTO participant){

        try{
            QuotationJoinCustomerInformationDTO quotationInformation = this.iCustomerInformationDAO.getCustomerBasicInformation(participant.getQuotationId());
            String insuranceProductId = quotationInformation.getQuotationMod().getInsuranceProductId().toPlainString();
            String modalityTypeProduct = quotationInformation.getQuotationMod().getInsuranceModalityType();
            List<RolDTO> roles = this.iRolDAO.getRolesByCompany(ParticipantMap.productAndModalityTransforMap(insuranceProductId,modalityTypeProduct));

            if (!StringUtils.startsWith(quotationInformation.getQuotation().getParticipantPersonalId(),RBVDInternalConstants.Number.VEINTE)) {
                ValidatePersonData.validateAllParticipantsByIndicatedType(participant.getParticipants(), RBVDInternalConstants.TypeParticipant.NATURAL.toString());
                IValidateParticipant validateParticipant = participantFactory.buildParticipant(RBVDInternalConstants.TypeParticipant.NATURAL);
                validateParticipant.validateParticipantData(participant,roles,quotationInformation);
            } else {
                ValidatePersonData.validateAllParticipantsByIndicatedType(participant.getParticipants(), RBVDInternalConstants.TypeParticipant.LEGAL.toString());
                IValidateParticipant validateParticipant = participantFactory.buildParticipant(RBVDInternalConstants.TypeParticipant.LEGAL);
                validateParticipant.validateParticipantData(participant,roles,quotationInformation);
            }

            return ResponseLibrary.ResponseServiceBuilder
                    .an().statusIndicatorProcess(RBVDInternalConstants.Status.SUCCESSFUL, RBVDInternalConstants.Status.NONE,RBVDInternalConstants.Status.NONE)
                    .build();

        }catch(BusinessException be){
            return ValidateErrorResponse.validateValidateProcessErrors(be.getAdviceCode(), be.getMessage());
        }
    }

    public void setCustomerInformationDAO(ICustomerInformationDAO iCustomerInformationDAO) {
        this.iCustomerInformationDAO = iCustomerInformationDAO;
    }

    public void setRolDAO(IRolDAO iRolDAO) {
        this.iRolDAO = iRolDAO;
    }

    public void setParticipantFactory(ParticipantFactory participantFactory) {
        this.participantFactory = participantFactory;
    }
}
