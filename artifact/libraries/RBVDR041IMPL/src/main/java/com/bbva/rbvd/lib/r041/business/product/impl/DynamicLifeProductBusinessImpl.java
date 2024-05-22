package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.addThird.customer.CustomerBusiness;
import com.bbva.rbvd.lib.r041.business.product.IDynamicLifeBusiness;
import com.bbva.rbvd.lib.r041.transfer.Participant;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r041.transform.bean.PersonBean;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r041.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class DynamicLifeProductBusinessImpl implements IDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicLifeProductBusinessImpl.class);
    public DynamicLifeProductBusinessImpl() {
    }

    @Override
    public AgregarTerceroBO doDynamicLife(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData) {

        LOGGER.info("** doDynamicLife - PayloadConfig -> {}",payloadConfig);
        LOGGER.info("** doDynamicLife - rimacRequestData -> {}",rimacRequestData);

        boolean isParticipantsWithRolContractor = false;
        boolean isParticipantsWithRolInsured = false;

        List<PersonaBO> personas = rimacRequestData.getPayload().getPersona();

        List<Participant> participants = payloadConfig.getParticipants();
        participants.stream()
                .filter(participant -> ConstantsUtil.Rol.INSURED.getName().equalsIgnoreCase(participant.getRolCode()))
                .filter(participant -> Objects.nonNull(participant.getNonCustomerFromDB()))
                .forEach(participant -> {
                    Optional<Participant> managerParticipant = participants.stream()
                            .filter(part -> part.getRolCode().equalsIgnoreCase(ConstantsUtil.Rol.PAYMENT_MANAGER.getName()))
                            .findFirst();
                    if(managerParticipant.isPresent()){
                        PersonaBO personManager =  CustomerBusiness.mapCustomerRequestData(managerParticipant.get(), payloadConfig.getQuotationInformation(), ValidationUtil.getValueByName(managerParticipant.get().getRolCode()));
                        PersonaBO person = PersonBean.buildPersonFromNonCustomer(participant.getNonCustomerFromDB().getQuotationLife(),personManager);
                        person.setRolName(payloadConfig.getParticipantProperties().obtainPropertyFromConsole(ConstantsUtil.Rol.INSURED.getName()));
                        personas.add(person);
                    }
                });

        // Check if there is a participant with the role of contractor

        for (PersonaBO personaBo : personas){
            if (personaBo.getRol() == ConstantsUtil.Rol.CONTRACTOR.getValue() && Objects.nonNull(personaBo.getNroDocumento())){
                isParticipantsWithRolContractor = true;
            } else if (personaBo.getRol() == ConstantsUtil.Rol.INSURED.getValue() && Objects.nonNull(personaBo.getNroDocumento())){
                isParticipantsWithRolInsured = true;
            }
        }

        PersonaBO personManager1 = personas.stream().filter(person -> person.getRol() == ConstantsUtil.Rol.PAYMENT_MANAGER.getValue())
                .findFirst().orElse(null);

        if(personManager1 != null) {
            String rolContractor = payloadConfig.getParticipantProperties().obtainPropertyFromConsole(ConstantsUtil.Rol.CONTRACTOR.getName());
            String rolInsured = payloadConfig.getParticipantProperties().obtainPropertyFromConsole(ConstantsUtil.Rol.INSURED.getName());
            enrichPerson(isParticipantsWithRolContractor, personManager1, personas, ConstantsUtil.Rol.CONTRACTOR,rolContractor);
            enrichPerson(isParticipantsWithRolInsured, personManager1, personas, ConstantsUtil.Rol.INSURED,rolInsured);
        }

        personas.removeIf(person -> Objects.isNull(person.getNroDocumento()));
        rimacRequestData.getPayload().setPersona(personas);
        rimacRequestData.getPayload().setProducto(payloadConfig.getQuotationInformation().getInsuranceProduct().getInsuranceProductDesc());
        LOGGER.info("** doDynamicLife - rimacRequestData after enrich -> {}",rimacRequestData);

        return rimacRequestData;
    }

    private void enrichPerson(boolean isRolPresent, PersonaBO personManager, List<PersonaBO> personList,ConstantsUtil.Rol rol,String rolName) {
        if (!isRolPresent) {
            PersonaBO person = PersonBean.buildPersonFromManager(personManager, rol);
            person.setRolName(rolName);
            personList.add(person);
        }
    }

}
