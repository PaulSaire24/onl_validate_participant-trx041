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
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ParticipantsBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsBusiness.class);
    private  ApplicationConfigurationService applicationConfigurationService;
    private RBVDR048 rbvdr048;

    private ConsumerInternalService consumerInternalService;

    public ParticipantsBusiness(ApplicationConfigurationService applicationConfigurationService,RBVDR048 rbvdr048) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.rbvdr048 = rbvdr048;
        consumerInternalService = new ConsumerInternalService(rbvdr048);
    }

    public List<Participant> getParticipants(InputParticipantsDTO input,String productId, String planId) {
        List<Participant> participants = new ArrayList<>();
        input.getParticipants().forEach(part -> {
            Participant participant = new Participant();
            String documentType = applicationConfigurationService.getProperty(part.getIdentityDocuments().get(0).getDocumentType().getId());
            part.getIdentityDocuments().get(0).getDocumentType().setId(documentType);
            participant.setDocumentType(documentType);
            participant.setCustomerId(part.getPerson().getCustomerId());
            participant.setDocumentNumber(part.getIdentityDocuments().get(0).getValue());
            participant.setRolCode(part.getParticipantType().getId());
            if(ValidationUtil.isBBVAClient(part.getPerson().getCustomerId())){
                PEWUResponse customer = executeGetCustomer(part.getIdentityDocuments().get(0).getValue(),documentType);
                participant.setCustomer(customer);
                LOGGER.info("** getConfig participant -> {}",participant);
            }else{
                // en el scrip ir por doc indentidad
                QuotationLifeDAO insuredInformation = getInsuredFromQuotation(input.getQuotationId(),productId,planId,part.getIdentityDocuments().get(0).getValue(),documentType);
                LOGGER.info("** getConfig dataInsured -> {}",insuredInformation);
                participant.setNonCustomerLife(insuredInformation);

            }
            participants.add(participant);
        });
        return participants;
    }

    public List<Participant> getParticipants(InputParticipantsDTO input, QuotationCustomerDAO quotationInformation) {
        List<ParticipantGroupDTO> inputParticipantsGrouped = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info(" ** GetConfig :: groupParticipant -> {}",inputParticipantsGrouped);
        List<Participant> participants = new ArrayList<>();
        inputParticipantsGrouped.forEach(inputParticipant -> {
            String documentType = applicationConfigurationService.getProperty(inputParticipant.getDocumentType());
            Optional<ParticipantsDTO> optInputPerson = inputParticipant.getParticipantList().stream().findFirst();
            Participant myParticipant = new Participant();
            if(optInputPerson.isPresent()){
                PersonDTO inputPerson = optInputPerson.get().getPerson();
                if(ValidationUtil.isBBVAClient(inputPerson.getCustomerId())){

                    myParticipant.setDocumentType(documentType);
                    myParticipant.setCustomerId(inputPerson.getCustomerId());
                    myParticipant.setDocumentNumber(inputParticipant.getDocumentNumber());

                    if(isCompanyCustomer(inputParticipant)){
                        myParticipant.setLegalCustomer(executeGetBusinessAgentASOInformation(inputPerson.getCustomerId()));
                    }else {
                        myParticipant.setCustomer(executeGetCustomer(inputParticipant.getDocumentNumber(),documentType));
                    }

                }else{
                    myParticipant.setDocumentType(documentType);
                    myParticipant.setDocumentNumber(inputParticipant.getDocumentNumber());

                    //validar si los atributos(firstName, LastName) de PersonaDTO tiene valores, si es asi llenar el objeto myParticipant con todos los atributos de PersonaDTO
                    if(inputPerson.getFirstName() != null && inputPerson.getLastName() != null) {
                       // corregir en la forma correcta ,( pienseo que el attributo de nonCustomer de
                        // MyParticipant debe ser de otra clase que contenga los atributos de PersonaDTO y no hacer referencia al Quotation.
                        myParticipant.setFirstName(inputPerson.getFirstName());
                        myParticipant.setLastName(inputPerson.getLastName());
                        myParticipant.setSecondLastName(inputPerson.getSecondLastName());
                        myParticipant.setGender(inputPerson.getGender());
                        myParticipant.setBirthDate(inputPerson.getBirthDate());
                        myParticipant.setCountry(inputPerson.getCountry());
                        myParticipant.setNationality(inputPerson.getNationality());
                        myParticipant.setMaritalStatus(inputPerson.getMaritalStatus());
                        myParticipant.setOccupation(inputPerson.getOccupation());
                        myParticipant.setCustomerType(inputPerson.getCustomerType());
                        myParticipant.setCustomerSubType(inputPerson.getCustomerSubType());
                        myParticipant.setCustomerSegment(inputPerson.getCustomerSegment());
                    }else {
                        nonCustomerFromQuotation(myParticipant,input.getQuotationId(),quotationInformation);
                    }

                }

                fillTotalParticipantsPerGroup(participants, inputParticipant.getParticipantList(), myParticipant);
            }

        });
        return participants;
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

    public void fillTotalParticipantsPerGroup(List<Participant> participantOutList, List<ParticipantsDTO> inputParticipant, Participant participant){
        for (int i = 0; i < inputParticipant.size(); i++) {
            Participant participantOutput = participant.clone();
            inputParticipant.get(i).getIdentityDocuments().get(0).getDocumentType().setId(participantOutput.getDocumentType());
            ParticipantsDTO inputParticipantDTO = inputParticipant.get(i);
            participantOutput.setInputParticipant(inputParticipantDTO);
            participantOutput.setRolCode(inputParticipantDTO.getParticipantType().getId());
            participantOutList.add(participantOutput);
        }
    }

    public void nonCustomerFromQuotation(Participant participant, String internalQuotationId, QuotationCustomerDAO quotationInformation){



        if(ConstantsUtil.Product.BUSINESS_LIFE.getCode().equals(quotationInformation.getInsuranceBusiness().getInsuranceBusinessName())){
            String productId = quotationInformation.getInsuranceProduct().getInsuranceProductId().toString();
            String planId = quotationInformation.getQuotationMod().getInsuranceModalityType();
            QuotationLifeDAO insuredInformation = getInsuredFromQuotation(internalQuotationId,productId,planId,participant.getDocumentNumber(),participant.getDocumentType());
            LOGGER.info("** getConfig dataInsured -> {}",insuredInformation);
            participant.setNonCustomerLife(insuredInformation);
        }
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
