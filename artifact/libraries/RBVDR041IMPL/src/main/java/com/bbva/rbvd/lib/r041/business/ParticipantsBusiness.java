package com.bbva.rbvd.lib.r041.business;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.service.api.ConsumerInternalService;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticipantsBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsBusiness.class);
    private  ApplicationConfigurationService applicationConfigurationService;
    private RBVDR048 rbvdr048;

    public ParticipantsBusiness(ApplicationConfigurationService applicationConfigurationService,RBVDR048 rbvdr048) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.rbvdr048 = rbvdr048;
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
                Map<String,Object> insuredInformation = getInsuredFromQuotation(input.getQuotationId(),productId,planId,part.getIdentityDocuments().get(0).getValue(),documentType);
                LOGGER.info("** getConfig dataInsured -> {}",insuredInformation);
                participant.setNonCustomer(insuredInformation);

            }
            participants.add(participant);
        });
        return participants;
    }

    private PEWUResponse executeGetCustomer(String documentNumber,String documentType){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.executeGetCustomerServiceByDocType(documentNumber,documentType);
    }

    private Map<String,Object> getInsuredFromQuotation(String quotationId, String productId, String planId,String ducumentNumber,String documentType){
        ConsumerInternalService consumerInternalService = new ConsumerInternalService(rbvdr048);
        return consumerInternalService.getDataInsuredBD(quotationId,productId,planId,ducumentNumber,documentType);
    }


}
