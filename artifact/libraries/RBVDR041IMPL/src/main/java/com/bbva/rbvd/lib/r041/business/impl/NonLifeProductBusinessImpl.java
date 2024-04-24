package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.participant.dao.RolDAO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants.ParticipantType;

import com.bbva.rbvd.dto.participant.constants.RBVDInternalConstants;
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
import java.util.Objects;

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

        List<Participant> participantList = payloadConfig.getParticipants();
        List<RolDAO> selectedRoles = payloadConfig.getRegisteredRolesDB();

        List<PersonaBO> personaList = new ArrayList<>();
        List<OrganizacionBO> organizacionList = new ArrayList<>();

        participantList.forEach(part-> {
            if(RBVDInternalConstants.TypeParticipant.NATURAL.toString().equalsIgnoreCase(part.getInputParticipant().getPerson().getPersonType())){
                ParticipantType participantType1 = Objects.nonNull(part.getCustomer()) ? ParticipantType.CUSTOMER : ParticipantType.NON_CUSTOMER;
                com.bbva.rbvd.lib.r041.pattern.factory.Participant participant = ParticipantFactory.buildParticipant(participantType1);
                personaList.add(participant.createRequestParticipant(part.getCustomer(),part.getInputParticipant(), payloadConfig.getQuotationInformation(),
                        ValidationUtil.obtainExistingCompanyRole(part.getInputParticipant(),
                                payloadConfig.getParticipantProperties(),selectedRoles)));

                addTerceroByCompany.setPersona(personaList);
            }else{
                PersonaBO personaBO = ValidateRimacNaturalPerson.mapCustomerRequestData(part.getCustomer(),part.getInputParticipant(), payloadConfig.getQuotationInformation(),
                        null);
                OrganizacionBO personaBO2 = ValidateRimacLegalPerson.getDataOrganization(part.getLegalCustomer().getData().get(0), personaBO, payloadConfig.getQuotationInformation(), ValidationUtil.obtainExistingCompanyRole(part.getInputParticipant(),payloadConfig.getParticipantProperties(),selectedRoles),part.getInputParticipant());
                organizacionList.add(personaBO2);

                addTerceroByCompany.setOrganizacion(organizacionList);
            }
        });

        addTerceroByCompany.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestCompany.setPayload(addTerceroByCompany);
        FactoryProduct.enrichPayloadByProduct(addTerceroByCompany,payloadConfig.getQuotationInformation());

        LOGGER.info("** createRequestByCompany - request Company -> {}",requestCompany);

        //call to RIMAC add third
        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);

        String quotationId = payloadConfig.getQuotationInformation().getQuotation().getInsuranceCompanyQuotaId();
        String productId = payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductType();
        String traceId = payloadConfig.getInput().getTraceId();
        String channel = payloadConfig.getInput().getChannelId();
        return consumerService.executeValidateParticipantRimacService(requestCompany,quotationId,productId,traceId,channel);
    }
}
