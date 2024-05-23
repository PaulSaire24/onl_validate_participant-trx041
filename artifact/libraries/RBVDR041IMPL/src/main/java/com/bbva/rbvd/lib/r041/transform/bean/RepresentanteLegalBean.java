package com.bbva.rbvd.lib.r041.transform.bean;

import com.bbva.rbvd.dto.insrncsale.bo.emision.RepresentanteLegalBO;
import com.bbva.rbvd.lib.r041.transfer.LegalRepresentative;

public class RepresentanteLegalBean {
    public static RepresentanteLegalBO createRepresentanteLegal(final LegalRepresentative legalRepresentative) {
        RepresentanteLegalBO representanteLegalBO = new RepresentanteLegalBO();
        representanteLegalBO.setTipoDocumento(legalRepresentative.getDocumentType());
        representanteLegalBO.setNroDocumento(legalRepresentative.getDocumentNumber());
        representanteLegalBO.setApePaterno(legalRepresentative.getLastName());
        representanteLegalBO.setApeMaterno(legalRepresentative.getSecondLastName());
        representanteLegalBO.setNombres(legalRepresentative.getFirstName());
        return representanteLegalBO;
    }

}
