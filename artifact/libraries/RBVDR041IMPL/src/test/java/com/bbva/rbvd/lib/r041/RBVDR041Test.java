package com.bbva.rbvd.lib.r041;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.lib.r041.impl.RBVDR041Impl;
import com.bbva.rbvd.lib.r066.RBVDR066;
import com.bbva.rbvd.mock.MockHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR041-app.xml",
		"classpath:/META-INF/spring/RBVDR041-app-test.xml",
		"classpath:/META-INF/spring/RBVDR041-arc.xml",
		"classpath:/META-INF/spring/RBVDR041-arc-test.xml" })
public class RBVDR041Test {

	@Spy
	private Context context;

	private RBVDR041Impl rbvdR041 = new RBVDR041Impl();
	private RBVDR066 rbvdR066;
	private PBTQR002 pbtqR002;
	private PISDR352 pisdR352;
	private PEWUResponse pewuResponse;
	private ListBusinessesASO listBussinesses = new ListBusinessesASO();
	List<ParticipantsDTO> participantsDTOList = new ArrayList<>();
	MockHelper mockHelper = new MockHelper();
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);
		rbvdR041 = new RBVDR041Impl();

		rbvdR066 = mock(RBVDR066.class);
		rbvdR041.setRbvdR066(rbvdR066);

		pbtqR002 = mock(PBTQR002.class);
		rbvdR041.setPbtqR002(pbtqR002);

		pisdR352 = mock(PISDR352.class);
		rbvdR041.setPisdR352(pisdR352);

		pewuResponse = MockHelper.getPEWUResponseMock();
		listBussinesses.setData(new ArrayList<>());
		listBussinesses.setData(Arrays.asList(MockHelper.getBusinessASOMock()) );
	}
	
	@Test
	public void executeaddThirdTest(){

		participantsDTOList.add(mockHelper.getParticipantsDTOMock());
		when(pbtqR002.executeSearchInHostByCustomerId(anyString())).thenReturn(pewuResponse);
		when(pisdR352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
		rbvdR041.addThird(participantsDTOList);

		participantsDTOList.get(0).getDocument().getDocumentType().setId("RUC20");
		when(rbvdR066.executeGetListBusinesses(anyString(),anyString())).thenReturn(listBussinesses);
		rbvdR041.addThird(participantsDTOList);
		Assert.assertEquals(0, context.getAdviceList().size());
	}
	
}
