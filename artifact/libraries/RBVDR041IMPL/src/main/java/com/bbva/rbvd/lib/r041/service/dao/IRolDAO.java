package com.bbva.rbvd.lib.r041.service.dao;

import com.bbva.rbvd.dto.validateparticipant.dto.RolDTO;

import java.util.List;
import java.util.Map;

public interface IRolDAO {
    List<RolDTO> getRolesByCompany(Map<String, Object> arguments);
}
