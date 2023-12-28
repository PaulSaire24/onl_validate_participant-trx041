package com.bbva.rbvd.lib.r041.impl;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.impl.util.MapperHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The RBVDR041Impl class...
 */
public class RBVDR041Impl extends RBVDR041Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR041Impl.class);

	/**
	 * The execute method...
	 */
	private static final String RUC_ID = "RUC";

	@Override
	public void executeAddThird(List<ParticipantsDTO> participantsDTOList) {
		ParticipantsDTO aux = new ParticipantsDTO();
		PersonaBO persona = new PersonaBO();
		OrganizacionBO organizacion = new OrganizacionBO();
		AgregarTerceroBO agregarTerceroBO = new  AgregarTerceroBO();
		agregarTerceroBO.setPayload(new PayloadAgregarTerceroBO());
		agregarTerceroBO.getPayload().setBeneficiario(new ArrayList<>());
		agregarTerceroBO.getPayload().setOrganizacion(new ArrayList<>());
		for (ParticipantsDTO p : participantsDTOList) {
				if(RUC_ID.equalsIgnoreCase(p.getDocument().getDocumentType().getId()) && StringUtils.startsWith(p.getDocument().getDocumentNumber(), "20")){
					agregarTerceroBO.getPayload().getOrganizacion().add(p.equals(aux) ? organizacion : getBussinesses(p) );
				}else {
					agregarTerceroBO.getPayload().getBeneficiario().add(p.equals(aux) ? persona : getCustomer(p));
				}
				aux = p;
		}
		pisdR352.executeAddParticipantsService(agregarTerceroBO,"","","");
		LOGGER.info("execute");
		// TODO - Implementation of business logic
	}
	private OrganizacionBO getBussinesses(ParticipantsDTO participants){
		OrganizacionBO organizacion;
		ListBusinessesASO listBussinesses = this.rbvdR066.executeGetListBusinesses(participants.getDocument().getDocumentNumber(), null);
		if(Objects.isNull(listBussinesses)){
			organizacion = mapperHelper.convertParticipantToOrganization(participants );

		}else {
			organizacion = mapperHelper.convertBusinessToOrganization(listBussinesses.getData().get(0), participants );
		}
		return organizacion;
	}
	private PersonaBO getCustomer(ParticipantsDTO participants){
		PersonaBO persona;
		PEWUResponse listCustomer = this.pbtqR002.executeSearchInHostByCustomerId(participants.getCustomerId());
		if (Objects.isNull(listCustomer)){
			persona = mapperHelper.convertParticipantToPerson(participants);
		}else {
			persona = mapperHelper.convertLisCustomerToPerson(listCustomer);
		}
		return persona;
	}
}
