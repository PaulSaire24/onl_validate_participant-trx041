package com.bbva.rbvd.lib.r041.business.addThird.customer;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.model.ContactBO;
import com.bbva.rbvd.lib.r041.transfer.InputNonCustomer;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class NonCustomerBusiness {

    public  static PersonaBO mapNonCustomerRequestData(InputNonCustomer participantNonCustomer, Integer rolId){
        PersonaBO personaBO = new PersonaBO();
        if(participantNonCustomer != null){
            ContactBO contactBO = getContactBO(participantNonCustomer.getContactDetails());
            AddressBO addressBO = new NonCustomerAddressBusiness().getAddressBO(participantNonCustomer.getAddresses());
            IdentityDocumentDTO identityDocumentDTO = participantNonCustomer.getIdentityDocuments().get(0);

            return PersonBean.getPersonWithInputData(rolId, contactBO, addressBO, participantNonCustomer.getPerson(), identityDocumentDTO);
        }
        personaBO.setRol(rolId);
        return personaBO;
    }

    private static ContactBO getContactBO(List<ContactDetailsDTO> contactDetails) {
        ContactBO contactBO = new ContactBO();
        contactBO.setEmail(getContactDetail(contactDetails, ConstantsUtil.PERSONAL_DATA.EMAIL_VALUE));
        contactBO.setMobile(getContactDetail(contactDetails, ConstantsUtil.PERSONAL_DATA.MOBILE_VALUE));
        return contactBO;
    }
    private static String getContactDetail(List<ContactDetailsDTO> contactDetails, String contactType) {
        return contactDetails.stream()
                .filter(contactDetail -> contactDetail.getContactType().equals(contactType))
                .map(ContactDetailsDTO::getContact)
                .findFirst()
                .orElse(StringUtils.EMPTY);
    }


}
