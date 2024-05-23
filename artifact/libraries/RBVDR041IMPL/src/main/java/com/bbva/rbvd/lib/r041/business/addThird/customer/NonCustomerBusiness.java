package com.bbva.rbvd.lib.r041.business.addThird.customer;

import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.lib.r041.model.AddressBO;
import com.bbva.rbvd.lib.r041.model.ContactBO;
import com.bbva.rbvd.lib.r041.transfer.InputNonCustomer;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;

public class NonCustomerBusiness {

    public  static PersonaBO mapNonCustomerRequestData(InputNonCustomer participantNonCustomer, Integer rolId){
        PersonaBO personaBO = new PersonaBO();
        if(participantNonCustomer != null){
            ContactBO contactBO = CustomerContactBusiness.getContactToNonCustomerBO(participantNonCustomer.getContactDetails());
            AddressBO addressBO = new NonCustomerAddressBusiness().getAddressBO(participantNonCustomer.getAddresses());
            IdentityDocumentDTO identityDocumentDTO = participantNonCustomer.getIdentityDocuments().get(0);

            return PersonBean.getPersonWithInputData(rolId, contactBO, addressBO, participantNonCustomer.getPerson(), identityDocumentDTO);
        }
        personaBO.setRol(rolId);
        return personaBO;
    }
}
