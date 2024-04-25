package com.bbva.rbvd.lib.r041.business;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.group.ParticipantGroupDTO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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

    public List<Participant> getParticipants(InputParticipantsDTO input) {
        List<ParticipantGroupDTO> groupParticipant = groupByDocumentNumberAndDocumentType(input);
        LOGGER.info(" ** GetConfig :: groupParticipant -> {}",groupParticipant);
        List<Participant> participants = new ArrayList<>();
        groupParticipant.forEach(part -> {
            String documentTypeHost = applicationConfigurationService.getProperty(part.getDocumentType());
            part.getParticipantList().forEach(p -> p.getIdentityDocuments().get(0).getDocumentType().setId(documentTypeHost));
            PersonDTO personDTO = part.getParticipantList().stream().findFirst().get().getPerson();
            if(ValidationUtil.isBBVAClient(part.getParticipantList().get(0).getPerson().getCustomerId())){

                Participant payloadProperties = new Participant();
                payloadProperties.setDocumentType(documentTypeHost);
                payloadProperties.setCustomerId(part.getParticipantList().get(0).getPerson().getCustomerId());
                payloadProperties.setDocumentNumber(part.getDocumentNumber());
                PEWUResponse customer = executeGetCustomer(part.getDocumentNumber(),documentTypeHost);
                payloadProperties.setCustomer(customer);

                if(StringUtils.startsWith(part.getDocumentNumber(), RBVDInternalConstants.Number.VEINTE)){
                    payloadProperties.setLegalCustomer(executeGetBusinessAgentASOInformation(part.getParticipantList().get(0).getPerson().getCustomerId()));
                }

                participants.add(payloadProperties);
            }else{
                Participant payloadPropertiesNonCustomer = new Participant();
                payloadPropertiesNonCustomer.setDocumentType(documentTypeHost);
                payloadPropertiesNonCustomer.setDocumentNumber(part.getDocumentNumber());
                participants.add(payloadPropertiesNonCustomer);
            }

        });
        return participants;
    }

    public  List<ParticipantGroupDTO> groupByDocumentNumberAndDocumentType(InputParticipantsDTO participant){
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
