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
                    myParticipant.setCustomer(executeGetCustomer(inputParticipant.getDocumentNumber(),documentType));
                    if(isCompanyCustomer(inputParticipant)){
                        myParticipant.setLegalCustomer(executeGetBusinessAgentASOInformation(inputPerson.getCustomerId()));
                    }

                }else{
                    myParticipant.setDocumentType(documentType);
                    myParticipant.setDocumentNumber(inputParticipant.getDocumentNumber());

                    nonCustomerFromQuotation(myParticipant,input.getQuotationId(),quotationInformation);
                    }

                }

                fillTotalParticipantsPerGroup(participants, inputParticipant.getParticipantList(), myParticipant);

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
