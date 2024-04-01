package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



public class DynamicLifeProductBusinessImpl implements IThirdDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicLifeProductBusinessImpl.class);
    private RBVDR048 rbvdr048;
    public DynamicLifeProductBusinessImpl(RBVDR048 rbvdr048) {
        this.rbvdr048 = rbvdr048;
    }

    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  aggregateTercero = new PayloadAgregarTerceroBO();
        List<Participant> participants = payloadConfig.getParticipants();

        List<PersonaBO> personList = new ArrayList<>();
        Boolean isParticipantsWithRolContractor = false;
        Boolean isParticipantsWithRolInsured = false;

        for(Participant participant : participants) {
            PersonaBO person = new PersonaBO();
            if(ConstantsUtil.Rol.PAYMENT_MANAGER.getName().equalsIgnoreCase(participant.getRolCode()) &&
                    Objects.nonNull(participant.getCustomer())){
               person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
            } else if (ConstantsUtil.Rol.CONTRACTOR.getName().equalsIgnoreCase(participant.getRolCode()) &&
                    Objects.nonNull(participant.getCustomer())) {
                isParticipantsWithRolContractor = true;
                person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
            } else if (ConstantsUtil.Rol.INSURED.getName().equalsIgnoreCase(participant.getRolCode())) {
                isParticipantsWithRolInsured = true;
                if(Objects.nonNull(participant.getCustomer())){
                    person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
                }else if (Objects.nonNull(participant.getNonCustomer())){
                     Participant managerParticipant = participants.stream()
                                .filter(part -> part.getRolCode() == ConstantsUtil.Rol.PAYMENT_MANAGER.getName())
                                .findFirst().get();
                    PersonaBO personManager = PersonBean.buildPersonFromCustomer(managerParticipant.getCustomer(),managerParticipant.getRolCode());
                    person = PersonBean.buildPersonFromNonCustomer(participant.getNonCustomer(),personManager);
                }
            }
            personList.add(person);
        }

        LOGGER.info("** doDynamicLife - persona List -> {}",personList);
        Optional<PersonaBO> personManager = personList.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst();
        if(personManager.isPresent()) {
            enrichPerson(isParticipantsWithRolContractor, personManager, personList, ConstantsUtil.Rol.CONTRACTOR);
            enrichPerson(isParticipantsWithRolInsured, personManager, personList, ConstantsUtil.Rol.INSURED);
        }

        LOGGER.info("** doDynamicLife - persona List after enrich -> {}",personList);
        aggregateTercero.setPersona(personList);
        aggregateTercero.setProducto(ConstantsUtil.Product.DYNAMIC_LIFE.getName());
        requestRimac.setPayload(aggregateTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);

        //call to RIMAC add third
        ConsumerExternalService consumerService = new ConsumerExternalService(rbvdr048);

        String quotationId = payloadConfig.getQuotationId();
        String productId = ConstantsUtil.Product.DYNAMIC_LIFE.getCode();
        String traceId = payloadConfig.getInput().getTraceId();
        return consumerService.executeValidateParticipantRimacService(requestRimac,quotationId,productId,traceId);
    }

    private static void enrichPerson(Boolean isRolPresent, Optional<PersonaBO> personManager, List<PersonaBO> personList,ConstantsUtil.Rol rol) {
        if (!isRolPresent) {
            PersonaBO personContractor = PersonBean.buildPersonFromManager(personManager.get(), rol);
            personList.add(personContractor);
        }
    }

}
