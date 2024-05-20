package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.product.IDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class DynamicLifeProductBusinessImpl implements IDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicLifeProductBusinessImpl.class);
    public DynamicLifeProductBusinessImpl() {
    }

    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        LOGGER.info("** doDynamicLife - rimacRequestData -> {}",rimacRequestData);
        /*AgregarTerceroBO requestRimac = new AgregarTerceroBO();
        PayloadAgregarTerceroBO  aggregateTercero = new PayloadAgregarTerceroBO();
        List<Participant> participants = payloadConfig.getParticipants();

        List<PersonaBO> personList = new ArrayList<>();*/
        boolean isParticipantsWithRolContractor = false;
        boolean isParticipantsWithRolInsured = false;

       /* for(Participant participant : participants) {
            PersonaBO person = new PersonaBO();
            if(ConstantsUtil.Rol.PAYMENT_MANAGER.getName().equalsIgnoreCase(participant.getRolCode())){
               person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
                person.setRolName(payloadConfig.getParticipantProperties().obtainRoleCodeByEnum(ConstantsUtil.Rol.PAYMENT_MANAGER.getName()));
            } else if (ConstantsUtil.Rol.CONTRACTOR.getName().equalsIgnoreCase(participant.getRolCode()) &&
                    Objects.nonNull(participant.getCustomer())) {
                isParticipantsWithRolContractor = true;
                person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
                person.setRolName(payloadConfig.getParticipantProperties().obtainRoleCodeByEnum(ConstantsUtil.Rol.CONTRACTOR.getName()));
            } else if (ConstantsUtil.Rol.INSURED.getName().equalsIgnoreCase(participant.getRolCode())) {
                isParticipantsWithRolInsured = true;
                if(Objects.nonNull(participant.getCustomer())){
                    person = PersonBean.buildPersonFromCustomer(participant.getCustomer(),participant.getRolCode());
                }else if (Objects.nonNull(participant.getNonCustomerFromDB())){
                     Optional<Participant> managerParticipant = participants.stream()
                                .filter(part -> part.getRolCode().equalsIgnoreCase(ConstantsUtil.Rol.PAYMENT_MANAGER.getName()))
                                .findFirst();
                    if(managerParticipant.isPresent()){
                        PersonaBO personManager = PersonBean.buildPersonFromCustomer(managerParticipant.get().getCustomer(),managerParticipant.get().getRolCode());
                        person = PersonBean.buildPersonFromNonCustomer(participant.getNonCustomerFromDB(),personManager);
                    }
                }
                person.setRolName(payloadConfig.getParticipantProperties().obtainRoleCodeByEnum(ConstantsUtil.Rol.INSURED.getName()));
            }
            personList.add(person);
        }*/

        // Check if there is a participant with the role of contractor

        List<PersonaBO> personaList = rimacRequestData.getPayload().getPersona();

        for (PersonaBO personaBo : personaList){
            if (personaBo.getRol() == ConstantsUtil.Rol.CONTRACTOR.getValue()){
                isParticipantsWithRolContractor = true;
            } else if (personaBo.getRol() == ConstantsUtil.Rol.INSURED.getValue()){
                isParticipantsWithRolInsured = true;
            }
        }

        PersonaBO personManager1 = personaList.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst().orElse(null);

        if(personManager1 != null) {
            String rolContractor = payloadConfig.getParticipantProperties().obtainPropertyFromConsole(ConstantsUtil.Rol.CONTRACTOR.getName());
            String rolInsured = payloadConfig.getParticipantProperties().obtainPropertyFromConsole(ConstantsUtil.Rol.INSURED.getName());
            enrichPerson(isParticipantsWithRolContractor, personManager1, personaList, ConstantsUtil.Rol.CONTRACTOR,rolContractor);
            enrichPerson(isParticipantsWithRolInsured, personManager1, personaList, ConstantsUtil.Rol.INSURED,rolInsured);
        }

        rimacRequestData.getPayload().setPersona(personaList);
        rimacRequestData.getPayload().setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        LOGGER.info("** doDynamicLife - rimacRequestData after enrich -> {}",rimacRequestData);

        return rimacRequestData;

    // finish refactor


    /*    LOGGER.info("** doDynamicLife - persona List -> {}",personList);
        Optional<PersonaBO> personManager = personList.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst();
        if(personManager.isPresent()) {
            String rolContractor = payloadConfig.getParticipantProperties().obtainRoleCodeByEnum(ConstantsUtil.Rol.CONTRACTOR.getName());
            String rolInsured = payloadConfig.getParticipantProperties().obtainRoleCodeByEnum(ConstantsUtil.Rol.INSURED.getName());
            enrichPerson(isParticipantsWithRolContractor, personManager.get(), personList, ConstantsUtil.Rol.CONTRACTOR,rolContractor);
            enrichPerson(isParticipantsWithRolInsured, personManager.get(), personList, ConstantsUtil.Rol.INSURED,rolInsured);
        }*/

        /*LOGGER.info("** doDynamicLife - persona List after enrich -> {}",personList);
        aggregateTercero.setPersona(personList);
        aggregateTercero.setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        requestRimac.setPayload(aggregateTercero);
        LOGGER.info("** doDynamicLife - request Rimac -> {}",requestRimac);
        return requestRimac;*/
    }

    private void enrichPerson(boolean isRolPresent, PersonaBO personManager, List<PersonaBO> personList,ConstantsUtil.Rol rol,String rolName) {
        if (!isRolPresent) {
            PersonaBO person = PersonBean.buildPersonFromManager(personManager, rol);
            person.setRolName(rolName);
            personList.add(person);
        }
    }

}
