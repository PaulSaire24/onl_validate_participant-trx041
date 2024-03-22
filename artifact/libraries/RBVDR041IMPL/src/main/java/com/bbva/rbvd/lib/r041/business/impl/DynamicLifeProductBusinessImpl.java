package com.bbva.rbvd.lib.r041.business.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.IThirdDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.service.api.ConsumerExternalService;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transform.bean.CustomerBean;
import com.bbva.rbvd.lib.r041.transform.bean.NonCustomerBean;
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
        participants.forEach(participant -> {
            PersonaBO person = new PersonaBO();
            if(ConstantsUtil.Rol.PAYMENT_MANAGER.getName().equalsIgnoreCase(participant.getRolCode())){
               person = CustomerBean.buildCustomer(participant);
            } else if (ConstantsUtil.Rol.CONTRACTOR.getName().equalsIgnoreCase(participant.getRolCode()) &&
                    Objects.nonNull(participant.getCustomer())) {
                person = CustomerBean.buildCustomer(participant);
            } else if (ConstantsUtil.Rol.INSURED.getName().equalsIgnoreCase(participant.getRolCode())) {
                if(Objects.nonNull(participant.getCustomer())){
                    person = CustomerBean.buildCustomer(participant);
                }else if (Objects.nonNull(participant.getNonCustomer())){
                    person = NonCustomerBean.buildNonCustomer(participant,personList);
                }
            }
            personList.add(person);
        });

        LOGGER.info("** doDynamicLife - persona List -> {}",personList);
        enrichPerson(personList);
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

    public static void enrichPerson(List<PersonaBO> personList){

        Optional<PersonaBO> personManager = personList.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst();
        if(personManager.isPresent()){
            PersonaBO personContractor = CustomerBean.getPerson(personManager.get(), ConstantsUtil.Rol.CONTRACTOR);

            if(ValidationUtil.isOnlyOneWithResponsibleRole(personList)){
                LOGGER.info("** enrichPerson - personList con solo rol 23");
                PersonaBO personInsured = CustomerBean.getPerson(personManager.get(), ConstantsUtil.Rol.INSURED);
                personList.add(personContractor);
                personList.add(personInsured);

            } else if (ValidationUtil.hasOneWithRole23AndOneWithRole9(personList)) {
                LOGGER.info("** enrichPerson - personList con solo rol 23 y 9");
                personList.add(personContractor);
            }

        }
    }

}
