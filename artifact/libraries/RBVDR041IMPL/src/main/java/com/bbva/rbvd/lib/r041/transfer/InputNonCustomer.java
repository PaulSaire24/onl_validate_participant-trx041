package com.bbva.rbvd.lib.r041.transfer;

import com.bbva.rbvd.dto.participant.request.AddressesDTO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;

import java.util.List;

public class InputNonCustomer {
    private PersonDTO person;
    private List<IdentityDocumentDTO> identityDocuments;
    private List<ContactDetailsDTO> contactDetails;
    private List<AddressesDTO> addresses;

    public PersonDTO getPerson() {
        return person;
    }

    public void setPerson(PersonDTO person) {
        this.person = person;
    }

    public List<IdentityDocumentDTO> getIdentityDocuments() {
        return identityDocuments;
    }

    public void setIdentityDocuments(List<IdentityDocumentDTO> identityDocuments) {
        this.identityDocuments = identityDocuments;
    }

    public List<ContactDetailsDTO> getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(List<ContactDetailsDTO> contactDetails) {
        this.contactDetails = contactDetails;
    }

    public List<AddressesDTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesDTO> addresses) {
        this.addresses = addresses;
    }


    public static final class Builder {
        private InputNonCustomer inputNonCustomer;

        private Builder() {
            inputNonCustomer = new InputNonCustomer();
        }

        public static Builder an() {
            return new Builder();
        }

        public Builder person(PersonDTO person) {
            inputNonCustomer.setPerson(person);
            return this;
        }

        public Builder identityDocuments(List<IdentityDocumentDTO> identityDocuments) {
            inputNonCustomer.setIdentityDocuments(identityDocuments);
            return this;
        }

        public Builder contactDetails(List<ContactDetailsDTO> contactDetails) {
            inputNonCustomer.setContactDetails(contactDetails);
            return this;
        }

        public Builder addresses(List<AddressesDTO> addresses) {
            inputNonCustomer.setAddresses(addresses);
            return this;
        }

        public InputNonCustomer build() {
            return inputNonCustomer;
        }
    }
}
