package com.bbva.rbvd.lib.r041.business.addThird.customer;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.model.ContactBO;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class CustomerContactBusiness {
    public static ContactBO getContactToNonCustomerBO(List<ContactDetailsDTO> contactDetails) {
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


    public static ContactBO getContactToCustomerBO(ParticipantsDTO participant, PEWUResponse customer, QuotationCustomerDAO customerInformationDb) {
        ContactBO contactBO = new ContactBO();

        String mobile = getContactDetail(participant, ConstantsUtil.PERSONAL_DATA.MOBILE_VALUE,
                customerInformationDb.getQuotationMod().getCustomerPhoneDesc(),
                customer.getPemsalwu().getContac2());

        String email = getContactDetail(participant, ConstantsUtil.PERSONAL_DATA.EMAIL_VALUE,
                customerInformationDb.getQuotationMod().getContactEmailDesc(),
                customer.getPemsalwu().getContac3());

        contactBO.setMobile(mobile);
        contactBO.setEmail(email);

        return contactBO;
    }

    private static String getContactDetail(ParticipantsDTO participant, String contactType, String defaultContact, String finalContact) {
        return participant.getContactDetails() == null ? defaultContact :
                participant.getContactDetails().stream()
                        .filter(contactDetail -> contactType.equals(contactDetail.getContactType()))
                        .map(ContactDetailsDTO::getContact)
                        .findFirst()
                        .orElse(defaultContact == null ? finalContact : defaultContact);
    }


}
