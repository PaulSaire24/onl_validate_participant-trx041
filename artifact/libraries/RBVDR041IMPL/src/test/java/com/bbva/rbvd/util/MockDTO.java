package com.bbva.rbvd.util;

import com.bbva.pisd.dto.insurance.mapper.ObjectMapperHelper;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;

import java.io.IOException;

public class MockDTO {
    private static final MockDTO INSTANCE = new MockDTO();
    private final com.bbva.pisd.dto.insurance.mapper.ObjectMapperHelper objectMapperHelper = ObjectMapperHelper.getInstance();

    private MockDTO() {

    }
    public static MockDTO getInstance() {
        return INSTANCE;
    }

    public ListBusinessesASO getListBusinessesOkMock() throws IOException {
        return objectMapperHelper.readValue(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "Mock/request/ListBusinessesOK.json"),
                ListBusinessesASO.class);
    }

    public ListBusinessesASO getListBusinessesOkMockCase2() throws IOException {
        return objectMapperHelper.readValue(
                Thread.currentThread().getContextClassLoader().getResourceAsStream(
                        "Mock/request/ListBusinessesOKCase2.json"),
                ListBusinessesASO.class);
    }
}