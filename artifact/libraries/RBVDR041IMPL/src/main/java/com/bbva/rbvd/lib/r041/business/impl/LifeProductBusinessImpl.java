package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



public class LifeProductBusinessImpl implements IThirdDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifeProductBusinessImpl.class);
    private RBVDR048 rbvdr048;
    public LifeProductBusinessImpl(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  aggregateTercero = new PayloadAgregarTerceroBO();
        List<Participant> participants = payloadConfig.getParticipants();

        List<PersonaBO> personList = new ArrayList<>();
        boolean isParticipantsWithRolContractor = false;
        boolean isParticipantsWithRolInsured = false;

        for(Participant participant : participants) {
            PersonaBO person = new PersonaBO();
            if(ConstantsUtil.Rol.PAYMENT_MANAGER.getName().equalsIgnoreCase(participant.getRolCode())){
               person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
            } else if (ConstantsUtil.Rol.CONTRACTOR.getName().equalsIgnoreCase(participant.getRolCode()) &&
                    Objects.nonNull(participant.getCustomer())) {
                isParticipantsWithRolContractor = true;
                person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
            } else if (ConstantsUtil.Rol.INSURED.getName().equalsIgnoreCase(participant.getRolCode())) {
                isParticipantsWithRolInsured = true;
                if(Objects.nonNull(participant.getCustomer())){
                    person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
                }else if (Objects.nonNull(participant.getNonCustomerLife())){
                     Optional<Participant> managerParticipant = participants.stream()
                                .filter(part -> part.getRolCode().equalsIgnoreCase(ConstantsUtil.Rol.PAYMENT_MANAGER.getName()))
                                .findFirst();
                    if(managerParticipant.isPresent()){
                        PersonaBO personManager = PersonBean.buildPersonFromCustomer(managerParticipant.get().getCustomer(),managerParticipant.get().getRolCode());
                        person = PersonBean.buildPersonFromNonCustomer(participant.getNonCustomerLife(),personManager);
                    }
                }
            }
            personList.add(person);
        }

        LOGGER.info("** doDynamicLife - persona List -> {}",personList);
        Optional<PersonaBO> personManager = personList.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst();
        if(personManager.isPresent()) {
            enrichPerson(isParticipantsWithRolContractor, personManager.get(), personList, ConstantsUtil.Rol.CONTRACTOR);
            enrichPerson(isParticipantsWithRolInsured, personManager.get(), personList, ConstantsUtil.Rol.INSURED);
        }

        LOGGER.info("** doDynamicLife - persona List after enrich -> {}",personList);
        aggregateTercero.setPersona(personList);
        aggregateTercero.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestRimac.setPayload(aggregateTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);

        //call to RIMAC add third
        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);

        String quotationId = payloadConfig.getQuotationId();
        String productId = payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc();
        String traceId = payloadConfig.getInput().getTraceId();
        String channelCode = payloadConfig.getInput().getChannelId();
        return consumerService.executeValidateParticipantRimacService(requestRimac,quotationId,productId,traceId,channelCode);
    }

    private static void enrichPerson(boolean isRolPresent, PersonaBO personManager, List<PersonaBO> personList,ConstantsUtil.Rol rol) {
        if (!isRolPresent) {
            PersonaBO personContractor = PersonBean.buildPersonFromManager(personManager, rol);
            personList.add(personContractor);
        }
    }

}
