package com.bbva.rbvd.lib.r041.business;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.group.ParticipantGroupDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.LegalRepresentative;
import com.bbva.rbvd.lib.r041.transfer.InputNonCustomer;
import com.bbva.rbvd.lib.r041.transfer.NonCustomerFromDB;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.stream.IntStream;

public class ParticipantsBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsBusiness.class);
    private  ApplicationConfigurationService applicationConfigurationService;

    private ConsumerInternalService consumerInternalService;

    public ParticipantsBusiness(ApplicationConfigurationService applicationConfigurationService,RBVDR048 rbvdr048) {
        this.applicationConfigurationService = applicationConfigurationService;
        consumerInternalService = new ConsumerInternalService(rbvdr048);
    }

    public List<Participant> getParticipants(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation) {
        List<ParticipantGroupDTO> inputParticipantsGrouped = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info(" ** GetConfig :: groupParticipant -> {}",inputParticipantsGrouped);
        List<Participant> participants = new ArrayList<>();
        inputParticipantsGrouped.forEach(inputParticipant -> {
            Participant myParticipantByDocument = findParticipant(input, quotationInformation, inputParticipant);
            fillTotalParticipantsPerGroup(participants, inputParticipant.getParticipantList(), myParticipantByDocument);
        });
        return orderParticipantByType(participants);
    }

    private static boolean isCompanyCustomer(ParticipantGroupDTO part) {
        return StringUtils.startsWith(part.getDocumentNumber(), RBVDInternalConstants.Number.VEINTE);
    }

    public List<ParticipantGroupDTO> groupByDocumentNumberAndDocumentType(InputParticipantsDTO participant){
        List<ParticipantGroupDTO> groupParticipants = new ArrayList<>();
        IntStream.range(0,participant.getParticipants().size()).forEach(i ->{
            ParticipantsDTO participantPrimary = participant.getParticipants().get(i);
            List<ParticipantsDTO> participantMemory = new ArrayList<>();
            participantMemory.add(participantPrimary);
            IntStream.range(i+1,participant.getParticipants().size()).forEach(j ->{
                ParticipantsDTO participantSecond = participant.getParticipants().get(j);

                if(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId().equalsIgnoreCase(participantSecond.getIdentityDocuments().get(0).getDocumentType().getId())
                        && participantPrimary.getIdentityDocuments().get(0).getValue().equalsIgnoreCase(participantSecond.getIdentityDocuments().get(0).getValue())){
                    participantMemory.add(participantSecond);
                }
            });
            boolean isNotGrouped = groupParticipants.stream().noneMatch(groupParticipant -> groupParticipant.getDocumentType().equalsIgnoreCase(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId()) &&
                    groupParticipant.getDocumentNumber().equalsIgnoreCase(participantPrimary.getIdentityDocuments().get(0).getValue()));
            if(isNotGrouped){
                ParticipantGroupDTO participantGroupDTO = new ParticipantGroupDTO();
                participantGroupDTO.setDocumentNumber(participantPrimary.getIdentityDocuments().get(0).getValue());
                participantGroupDTO.setDocumentType(participantPrimary.getIdentityDocuments().get(0).getDocumentType().getId());
                participantGroupDTO.setParticipantList(participantMemory);
                groupParticipants.add(participantGroupDTO);
            }
        });
        LOGGER.info("groupByDocumentNumberAndDocumentType end ***** {}",groupParticipants);
        return groupParticipants;
    }

    private static List<Participant> orderParticipantByType(List<Participant> outputParticipants) {
        ConstantsUtil.Rol[] orderedRoles = ConstantsUtil.Rol.values();
        List<Participant> orderedParticipants = new ArrayList<>();
        for (int i = 0; i < orderedRoles.length; i++) {
            String orderRole = orderedRoles[i].getName();
            outputParticipants.forEach(participant -> {
                if (participant.getRolCode().equals(orderRole)) {
                    orderedParticipants.add(participant);
                }
            });
        }
        return orderedParticipants;
    }

    public void fillTotalParticipantsPerGroup(List<Participant> participantOutList, List<ParticipantsDTO> inputParticipant, Participant participant){
        Set<Participant> participantSetStructure = new HashSet<>();
        for (int i = 0; i < inputParticipant.size(); i++) {
            Participant participantOutput = participant.clone();
            inputParticipant.get(i).getIdentityDocuments().get(0).getDocumentType().setId(participantOutput.getDocumentType());
            ParticipantsDTO inputParticipantDTO = inputParticipant.get(i);
            participantOutput.setInputParticipant(inputParticipantDTO);
            participantOutput.setRolCode(inputParticipantDTO.getParticipantType().getId());
            participantSetStructure.add(participantOutput);
        }
        participantOutList.addAll(participantSetStructure);
    }

    private Participant findParticipant(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation, ParticipantGroupDTO inputParticipant) {
        String documentType = applicationConfigurationService.getProperty(inputParticipant.getDocumentType());
        String documentNumber = inputParticipant.getDocumentNumber();
        Optional<ParticipantsDTO> optInputPerson = inputParticipant.getParticipantList().stream().findFirst();
        Participant myParticipantByDocument = new Participant();
        if (optInputPerson.isPresent()) {
            PersonDTO inputPerson = optInputPerson.get().getPerson();
            if (ValidationUtil.isBBVAClient(inputPerson.getCustomerId())) {

                myParticipantByDocument.setDocumentType(documentType);
                myParticipantByDocument.setDocumentNumber(documentNumber);
                myParticipantByDocument.setCustomer(executeGetCustomer(documentNumber, documentType));

                if (isCompanyCustomer(inputParticipant)) {
                    myParticipantByDocument.setLegalCustomer(executeGetBusinessAgentASOInformation(inputPerson.getCustomerId()));
                }

                LegalRepresentative legalRepresentative = getLegalRepresentative(documentType, documentNumber, optInputPerson.get(), myParticipantByDocument.getCustomer());
                myParticipantByDocument.setLegalRepresentative(legalRepresentative);

            } else {
                myParticipantByDocument.setDocumentType(documentType);
                myParticipantByDocument.setDocumentNumber(documentNumber);

                if (inputPerson.getFirstName() != null && inputPerson.getLastName() != null) {
                    InputNonCustomer inputNonCustomer = InputNonCustomer.Builder.an()
                            .person(inputPerson)
                            .addresses(optInputPerson.get().getAddresses())
                            .contactDetails(optInputPerson.get().getContactDetails())
                            .identityDocuments(optInputPerson.get().getIdentityDocuments())
                            .build();

                    myParticipantByDocument.setInputNonCustomer(inputNonCustomer);
                    LegalRepresentative legalRepresentative = getLegalRepresentative(documentType, documentNumber, optInputPerson.get(), myParticipantByDocument.getCustomer());
                    myParticipantByDocument.setLegalRepresentative(legalRepresentative);

                } else {
                    NonCustomerFromDB nonCustomerFromDB = nonCustomerFromDB(input.getQuotationId(), quotationInformation, documentNumber,documentType);
                    myParticipantByDocument.setNonCustomerFromDB(nonCustomerFromDB);
                }

            }
        }
        return  myParticipantByDocument;
    }

    public NonCustomerFromDB nonCustomerFromDB(String internalQuotationId, QuotationCustomerDAO quotationInformation, String documentNumber, String documentType){
        NonCustomerFromDB nonCustomerFromDB = new NonCustomerFromDB();
        if(ConstantsUtil.Product.BUSINESS_LIFE.getCode().equals(quotationInformation.getInsuranceBusiness().getInsuranceBusinessName())){

            String productId = quotationInformation.getInsuranceProduct().getInsuranceProductId().toString();
            String planId = quotationInformation.getQuotationMod().getInsuranceModalityType();
            nonCustomerFromDB.setQuotationLife(getInsuredFromQuotation(internalQuotationId,productId,planId,documentNumber,documentType));
            LOGGER.info("** findParticipant nonCustomerFromQuotation -> {}",nonCustomerFromDB);
        }
        return nonCustomerFromDB;
    }

    public LegalRepresentative getLegalRepresentative(String documentType, String documentNumber, ParticipantsDTO participant, PEWUResponse customerInformation){
        if(participant.getParticipantType().getId().equals(ConstantsUtil.Rol.LEGAL_REPRESENTATIVE.getName())){
            LegalRepresentative legalRepresentative = new LegalRepresentative();
            if(customerInformation==null){
                legalRepresentative.setDocumentType(documentType);
                legalRepresentative.setDocumentNumber(documentNumber);
                legalRepresentative.setFirstName(participant.getPerson().getFirstName());
                legalRepresentative.setLastName(participant.getPerson().getLastName());
                legalRepresentative.setSecondLastName(participant.getPerson().getSecondLastName());
            }else{
                legalRepresentative.setDocumentType(customerInformation.getPemsalwu().getTdoi());
                legalRepresentative.setDocumentNumber(customerInformation.getPemsalwu().getNdoi());
                legalRepresentative.setFirstName(participant.getPerson().getFirstName());
                legalRepresentative.setLastName(participant.getPerson().getLastName());
                legalRepresentative.setSecondLastName(participant.getPerson().getSecondLastName());
            }

            return legalRepresentative;
        }
        return null;
    }

    private PEWUResponse executeGetCustomer(String documentNumber,String documentType){
        return consumerInternalService.executeGetCustomerServiceByDocType(documentNumber,documentType);
    }

    public ListBusinessesASO executeGetBusinessAgentASOInformation(String customerId){
        String encryptedCustomerId = consumerInternalService.executeKsmkCryptographyService(customerId);
        return consumerInternalService.executeListBusinessService(encryptedCustomerId);
    }

    private QuotationLifeDAO getInsuredFromQuotation(String quotationId, String productId, String planId,String documentNumber,String documentType){
        return consumerInternalService.getDataInsuredBD(quotationId,productId,planId,documentNumber,documentType);
    }


}
