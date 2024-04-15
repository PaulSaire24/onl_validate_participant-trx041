package com.bbva.rbvd.util;

import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.mapper.ObjectMapperHelper;

import java.io.IOException;

public class MockDTO {
    private static final MockDTO INSTANCE = new MockDTO();
    private final ObjectMapperHelper objectMapperHelper = ObjectMapperHelper.getInstance();

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