package com.bbva.rbvd.lib.r041.util;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.OrganizacionBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PersonaBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.impl.util.MapperHelper;
import com.bbva.rbvd.mock.MockHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertNotNull;


public class MapperHelperTest {
    @Spy
    private Context context;
    PEWUResponse pewuResponse;
    MapperHelper mapperHelper = new MapperHelper();
    ParticipantsDTO participants;
    BusinessASO business;
    @Rule
    public ExpectedException exp = ExpectedException.none();
    @Before
    public void setUp() throws Exception  {
        MockitoAnnotations.initMocks(this);
        context = new Context();
        ThreadContext.set(context);

        MockHelper mockHelper = new MockHelper();
        pewuResponse = mockHelper.getPEWUResponseMock();
        participants = mockHelper.getParticipantsDTOMock();
        business = mockHelper.getBusinessASOMock();
    }

    @Test
    public void executeconvertLisCustomerToPersonTest() {
        PersonaBO persona;
        persona = mapperHelper.convertLisCustomerToPerson(pewuResponse);
        assertNotNull(persona);
    }
    @Test
    public void executeconvertBusinessToOrganizationTest() {
        OrganizacionBO organizacion;
        organizacion = mapperHelper.convertBusinessToOrganization(business,participants);
        assertNotNull(organizacion);
    }@Test
    public void executeConvertParticipantToPersonTest() {
        PersonaBO persona;
        persona = mapperHelper.convertParticipantToPerson(participants);
        assertNotNull(persona);
    }
    @Test
    public void executeconvertParticipantToOrganizationTest() {
        OrganizacionBO organizacion;
        organizacion = mapperHelper.convertParticipantToOrganization(participants);
        assertNotNull(organizacion);
    }
}
