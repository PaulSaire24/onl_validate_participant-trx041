package com.bbva.rbvd.lib.r041.business.addThird.customer;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;


public class CustomerBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerBusiness.class);

    public static PersonaBO mapCustomerRequestData(Participant participant, QuotationCustomerDAO customerInformationDb, Integer roleId){
        PEWUResponse customerFromPewu = participant.getCustomer();
        validatePewuResponsePersonData(customerFromPewu);

        StringBuilder stringAddress  = new StringBuilder();

        AddressBO addressBO = new CustomerAddressBusiness().fillAddress(customerFromPewu, stringAddress);
        PersonaBO persona = PersonBean.getPersonFromPEWU(participant.getInputParticipant(),customerFromPewu,customerInformationDb, roleId, addressBO);


        LOGGER.info("[RequestRimacBean] convertListPersons() :: End");
        LOGGER.info("[RequestRimacBean] personData{}", persona);
        return persona;
    }

    private static void validatePewuResponsePersonData(PEWUResponse pewuResponse){
        if (Objects.isNull(pewuResponse.getPemsalwu())){
            throw new BusinessException(ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getAdviceCode(), false,
                    ValidateParticipantErrors.ERROR_INTERNAL_SERVICE_INVOKATION.getMessage()
                            .concat(TypeErrorControllerEnum.ERROR_PBTQ_INCOMPLETE_CLIENT_INFORMATION.getValue()));
        }
    }


}
