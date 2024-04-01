package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants.ParticipantType;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
import com.bbva.rbvd.dto.participant.mapper.RolDTO;
import com.bbva.rbvd.lib.r041.business.INonLifeProductBusiness;
import com.bbva.rbvd.lib.r041.pattern.factory.ParticipantFactory;
import com.bbva.rbvd.lib.r041.pattern.factory.FactoryProduct;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacLegalPerson;
import com.bbva.rbvd.lib.r041.transform.bean.ValidateRimacNaturalPerson;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class NonLifeProductBusinessImpl implements INonLifeProductBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonLifeProductBusinessImpl.class);

    private RBVDR048 rbvdr048;

    public NonLifeProductBusinessImpl(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    @Override
    public AgregarTerceroBO createRequestByCompany(PayloadConfig payloadConfig) {
        AgregarTerceroBO requestCompany = new AgregarTerceroBO();
        PayloadAgregarTerceroBO addTerceroByCompany = new PayloadAgregarTerceroBO();
        List<ParticipantsDTO> participantsInputList = payloadConfig.getInput().getParticipants();//input de la trx
        List<Participant> participantsPropertiesList = payloadConfig.getParticipants();//properties agrupadas
        List<RolDTO> selectedRoles = payloadConfig.getRegisteredRolesDB();//roles

        if(RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(payloadConfig.getPersonType())){

            List<PersonaBO> personaList = new ArrayList<>();
            participantsInputList.forEach(partInput->
                participantsPropertiesList.forEach(partProp->{
                    //Factoria cliente,
                    ParticipantType participantType = ValidationUtil.isBBVAClient(partInput.getPerson().getCustomerId()) ? ParticipantType.CUSTOMER : ParticipantType.NON_CUSTOMER;
                    com.bbva.rbvd.lib.r041.pattern.factory.Participant participant = ParticipantFactory.buildParticipant(participantType);

                    if(validateDocumentEqualsCondition(partInput, partProp)){
                        personaList.add(participant.createRequestParticipant(partProp.getCustomer(),partInput, payloadConfig.getQuotationInformation(), ValidationUtil.obtainExistingCompanyRole(partInput,payloadConfig.getParticipantProperties(),selectedRoles)));
                    }
                }));
            addTerceroByCompany.setPersona(personaList);
            requestCompany.setPayload(addTerceroByCompany);
            FactoryProduct.enrichPayloadByProduct(addTerceroByCompany,payloadConfig.getQuotationInformation());
        }else{

            List<OrganizacionBO> organizacionList = new ArrayList<>();
            PersonaBO personaBO = ValidateRimacNaturalPerson.mapInRequestRimacNonLife(participantsPropertiesList.get(0).getCustomer(),participantsInputList.get(0), payloadConfig.getQuotationInformation(),
                    null);
            participantsInputList.forEach(partInput->
                participantsPropertiesList.forEach(partProp->{
                    if(validateDocumentEqualsCondition(partInput, partProp)){
                        OrganizacionBO personaB2 = ValidateRimacLegalPerson.getDataOrganization(partProp.getLegalCustomer().getData().get(0), personaBO, payloadConfig.getQuotationInformation(), ValidationUtil.obtainExistingCompanyRole(partInput,payloadConfig.getParticipantProperties(),selectedRoles),partInput);
                        organizacionList.add(personaB2);
                    }
                }));
            addTerceroByCompany.setOrganizacion(organizacionList);
            requestCompany.setPayload(addTerceroByCompany);
            FactoryProduct.enrichPayloadByProduct(addTerceroByCompany,payloadConfig.getQuotationInformation());
        }

        LOGGER.info("** createRequestByCompany - request Company -> {}",requestCompany);

        //call to RIMAC add third
        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);

        String quotationId = payloadConfig.getQuotationInformation().getQuotation().getInsuranceCompanyQuotaId();
        String productId = payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductType();
        String traceId = payloadConfig.getInput().getTraceId();

        return consumerService.executeValidateParticipantRimacService(requestCompany,quotationId,productId,traceId);
    }

    private boolean validateDocumentEqualsCondition(ParticipantsDTO participant, Participant payloadProperties){
        return payloadProperties.getDocumentType().equalsIgnoreCase(participant.getIdentityDocuments().get(0).getDocumentType().getId())
                && payloadProperties.getDocumentNumber().equalsIgnoreCase(participant.getIdentityDocuments().get(0).getValue());
    }
}
