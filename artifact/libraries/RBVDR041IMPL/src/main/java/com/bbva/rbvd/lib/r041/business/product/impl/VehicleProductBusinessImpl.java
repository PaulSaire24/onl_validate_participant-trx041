package com.bbva.rbvd.lib.r041.business.product.impl;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.lib.r041.business.product.IVehicleBusiness;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;

import java.util.List;

public class VehicleProductBusinessImpl implements IVehicleBusiness {
    @Override
    public AgregarTerceroBO doVehicleProduct(PayloadConfig payloadConfig, AgregarTerceroBO rimacRequestData){

        List<PersonaBO> listPeople = rimacRequestData.getPayload().getPersona();
        if (listPeople != null) {
            listPeople.forEach(person -> {
                person.setProteccionDatosPersonales("S");
                person.setEnvioComunicacionesComerciales("N");
            });
        }

        List<OrganizacionBO> listOrganization = rimacRequestData.getPayload().getOrganizacion();
        if (listOrganization != null) {
            listOrganization.forEach(organization ->{
                organization.setProteccionDatosPersonales("S");
                organization.setEnvioComunicacionesComerciales("N");
            });
        }

        rimacRequestData.getPayload().setCotizacion(payloadConfig.getQuotationInformation().getQuotation().getInsuranceCompanyQuotaId());
        return rimacRequestData;
    }
}
