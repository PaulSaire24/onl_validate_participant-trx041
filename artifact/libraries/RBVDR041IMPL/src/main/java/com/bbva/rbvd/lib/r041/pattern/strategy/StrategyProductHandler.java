package com.bbva.rbvd.lib.r041.pattern.strategy;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.lib.r041.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r048.RBVDR048;

public interface StrategyProductHandler {

    AgregarTerceroBO addParticipantsToInsuranceCompany(PayloadConfig payloadConfig, RBVDR048 rbvdr048);

}
