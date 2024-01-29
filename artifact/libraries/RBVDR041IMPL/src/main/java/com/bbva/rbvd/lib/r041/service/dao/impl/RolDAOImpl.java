package com.bbva.rbvd.lib.r041.service.dao.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.library.AbstractLibrary;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;
import com.bbva.rbvd.dto.validateparticipant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.validateparticipant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r041.service.dao.IRolDAO;
import com.bbva.rbvd.lib.r041.transform.bean.RolBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class RolDAOImpl extends AbstractLibrary implements IRolDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolDAOImpl.class);
    private PISDR012 pisdr012;
    @Override
    public List<RolDTO> getRolesByCompany(Map<String, Object> arguments) {
        try{
            LOGGER.info("***** RolDAOImpl - getRolesByCompany START *****");
            Map<String, Object> responseRolesByCompany = pisdr012.executeGetParticipantRolesByCompany(arguments);
            LOGGER.info("***** RolDAOImpl - getRolesByCompany | responseRolesByCompany {} *****",responseRolesByCompany);
            return RolBean.rolByParticipantTransformBean(responseRolesByCompany);
        }catch (BusinessException be){
            throw new BusinessException(be.getAdviceCode(), false, TypeErrorControllerEnum.ERROR_OBTAIN_PRODUCT_COMPANY_MODALITIES_FROM_DB.getValue());
        }
    }

    public void setPisdr012(PISDR012 pisdr012) {
        this.pisdr012 = pisdr012;
    }
}
