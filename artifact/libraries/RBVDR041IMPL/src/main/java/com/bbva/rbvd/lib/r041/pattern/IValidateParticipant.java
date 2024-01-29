package com.bbva.rbvd.lib.r041.pattern;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.transform.bean.NaturalPersonRimacBean;

import java.util.List;

public interface IValidateParticipant {

    void validateParticipantData(ValidateParticipantDTO participant, List<RolDTO> roles, QuotationJoinCustomerInformationDTO quotationInformation);

    default PersonaBO obtainPersonInformation(PEWUResponse pewuResponse, ParticipantsDTO participantDTO, QuotationJoinCustomerInformationDTO quotationInformation, Integer roleCodeCompany, String saleChannelId) {
        return NaturalPersonRimacBean.mapRimacInputServiceNaturalPerson(pewuResponse, participantDTO, quotationInformation, roleCodeCompany, saleChannelId);
    }

    default Integer obtainExistingCompanyRole(ParticipantsDTO participantDTO, ParticipantProperties participantProperties, List<RolDTO> roles) {
    String roleCodeBank = participantProperties.obtainRoleCodeByEnum(participantDTO.getParticipantType().getId());
    return Integer.parseInt(roles.stream().filter(rolDTO -> roleCodeBank.equalsIgnoreCase(rolDTO.getParticipantRoleId().toString())).findFirst()
            .orElseThrow(() -> new BusinessException(
                    ValidateParticipantErrors.ERROR_BBVA_VALIDATION.getAdviceCode(), false,
                    TypeErrorControllerEnum.ERROR_ROLE_NOT_REGISTRED_ON_APX_CONSOLE.getValue().
                            replace("{participantType}",participantDTO.getParticipantType().getId()))).
            getInsuranceCompanyRoleId());
    }
}
