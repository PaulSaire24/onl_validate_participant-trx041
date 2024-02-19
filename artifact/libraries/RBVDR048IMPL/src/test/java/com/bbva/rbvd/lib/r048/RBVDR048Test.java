package com.bbva.rbvd.lib.r048;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW5;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.dto.insurance.aso.GetContactDetailsASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.mock.MockData;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.lib.r048.factory.ApiConnectorFactoryTest;
import com.bbva.rbvd.lib.r048.impl.RBVDR048Impl;
import com.bbva.rbvd.lib.r066.RBVDR066;
import com.bbva.rbvd.mock.MockBundleContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR048-app.xml",
		"classpath:/META-INF/spring/RBVDR048-app-test.xml",
		"classpath:/META-INF/spring/RBVDR048-arc.xml",
		"classpath:/META-INF/spring/RBVDR048-arc-test.xml" })
public class RBVDR048Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR048Test.class);

	@Spy
	private Context context;


	private RBVDR048Impl rbvdR048  =new RBVDR048Impl();
	@Resource(name = "pisdR014")
	private PISDR014 pisdr014;
	@Resource(name = "pisdR403")
	private PISDR403 pisdr403;

	@Resource(name = "pbtqR002")
	private PBTQR002 pbtqr002;
	@Resource(name = "ksmkR002")
	private KSMKR002 ksmkr002;
	@Resource(name = "rbvdR066")
	private RBVDR066 rbvdr066;

	private MockData mockData;

	@Resource(name = "applicationConfigurationService")
	private ApplicationConfigurationService applicationConfigurationService;

	@Resource(name = "externalApiConnector")
	private APIConnector externalApiConnector;


	@Before
	public void setUp() throws Exception {

		mockData = MockData.getInstance();
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);
		getObjectIntrospection();
		MockBundleContext mockBundleContext = mock(MockBundleContext.class);


		ApiConnectorFactoryTest apiConnectorFactoryMock = new ApiConnectorFactoryTest();

		externalApiConnector = apiConnectorFactoryMock.getAPIConnector(mockBundleContext, false);
		rbvdR048.setExternalApiConnector(externalApiConnector);
		applicationConfigurationService = mock(ApplicationConfigurationService.class);
		rbvdR048.setApplicationConfigurationService(applicationConfigurationService);
		pisdr014 = mock(PISDR014.class);
		rbvdR048.setPisdR014(pisdr014);
		pisdr403 = mock(PISDR403.class);
		pbtqr002 = mock(PBTQR002.class);
		rbvdR048.setPbtqR002(pbtqr002);
		ksmkr002 = mock(KSMKR002.class);
		rbvdR048.setKsmkR002(ksmkr002);
		rbvdr066 = mock(RBVDR066.class);
		rbvdR048.setRbvdR066(rbvdr066);
		rbvdR048.setPisdR403(pisdr403);
		when(applicationConfigurationService.getDefaultProperty(anyString(),anyString())).thenReturn("PATCH");
		when(pisdr014.executeSignatureConstruction(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(new SignatureAWS("", "", "", ""));
	}
	
	private Object getObjectIntrospection() throws Exception{
		Object result = this.rbvdR048;
		if(this.rbvdR048 instanceof Advised){
			Advised advised = (Advised) this.rbvdR048;
			result = advised.getTargetSource().getTarget();
		}
		return result;
	}



	@Test
	public void testExecuteAddParticipantOK() throws IOException {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsService_OK...");
		AgregarTerceroBO response = mockData.getAddParticipantsRimacResponse();

		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setProducto("841");
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);

		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap())).thenReturn(new ResponseEntity<>(response, HttpStatus.OK));
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(agregarTerceroBO,"quotationId","840","traceId");

		assertNotNull(validation);
		assertNotNull(validation.getPayload());
		assertNotNull(validation.getPayload().getStatus());
		assertNotNull(validation.getPayload().getMensaje());

		AgregarTerceroBO validation1 = this.rbvdR048.executeAddParticipants(agregarTerceroBO,"quotationId","830","traceId");
		assertNotNull(validation1);
		assertNotNull(validation1.getPayload());
		assertNotNull(validation1.getPayload().getStatus());
		assertNotNull(validation1.getPayload().getMensaje());
		assertEquals("1",validation1.getPayload().getStatus());
		assertNotNull(validation1.getPayload().getTerceros());
		assertEquals(3,validation1.getPayload().getTerceros().size());
		assertNotNull(validation1.getPayload().getTerceros().get(0));
		assertNotNull(validation1.getPayload().getTerceros().get(1));
		assertNotNull(validation1.getPayload().getTerceros().get(2));
		assertEquals(0,validation1.getPayload().getBeneficiario().size());
	}


	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionVD() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithRestClientException...");

		String responseBody = "{\n" +
				"    \"error\": {\n" +
				"        \"code\": \"VIDACOT005\",\n" +
				"        \"message\": \"Validacion de Datos\",\n" +
				"        \"details\": {\n" +
				"            \"PE008002\": \"El campo apePaterno de persona en su elemento 3 es requerido\",\n" +
				"            \"PE009002\": \"El campo apeMaterno de persona en su elemento 3 es requerido\",\n" +
				"            \"PE011002\": \"El campo fechaNacimiento de persona en su elemento 3 es requerido\"\n" +
				"        },\n" +
				"        \"httpStatus\": 403\n" +
				"    }\n" +
				"}";

		ErrorResponseDTO res = new ErrorResponseDTO();
		res.setCode("Bbva41255");
		res.setMessage("Error al consumir serviciso rimac ");
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionUnrecognized() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithRestClientExceptionUnrecognized...");

		String responseBody = "";

		ErrorResponseDTO res = new ErrorResponseDTO();
		res.setCode("Bbva41255");
		res.setMessage("Error al consumir serviciso rimac ");
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionInstance() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithRestClientExceptionInstance...");

		String responseBody = "";

		ErrorResponseDTO res = new ErrorResponseDTO();
		res.setCode("Bbva41255");
		res.setMessage("Error al consumir serviciso rimac ");
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new RestClientException(responseBody));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithTimeoutException() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithTimeoutException...");

		ErrorResponseDTO res = new ErrorResponseDTO();
		res.setCode("Bbva41255");
		res.setMessage("Error al consumir serviciso rimac ");
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new TimeoutException("RBVD01020044"));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId");

		assertNotNull(validation);
	}

	@Test
	public void executeGetListCustomerHostOk() {
		LOGGER.info("RBVDR048 - Executing executeGetListCustomerHostOk...");

		PEWUResponse responseHost = new PEWUResponse();
		PEMSALW4 dataAdress = new PEMSALW4();
		PEMSALWU data = new PEMSALWU();
		data.setTdoi("L");
		data.setSexo("M");
		data.setContact("123123123");
		data.setContac2("123123123");
		data.setContac3("123123123");
		data.setContac3("123123123");
		dataAdress.setDesrela("FAMILIA");

		data.setTipodir("dep"); // map address type

		responseHost.setPemsalwu(data);
		responseHost.setPemsalw4(dataAdress);
		responseHost.setPemsalw5(new PEMSALW5());
		responseHost.setHostAdviceCode(null);

		when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseHost);
		when(applicationConfigurationService.getProperty(anyString())).thenReturn("DNI");

		PEWUResponse validation = rbvdR048.executeGetCustomerByDocType("00000000","L");
		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void executeGetListCustomerHostWithAdvice() {
		LOGGER.info("RBVDR048 - Executing executeGetListCustomerHostWithAdvice...");

		PEWUResponse responseHost = new PEWUResponse();
		responseHost.setHostAdviceCode("code");
		responseHost.setHostMessage("some error");
		List<ContactDetailsBO> contactDetailsBO = new ArrayList<>();
		GetContactDetailsASO contactDetailsASO = new GetContactDetailsASO();
		contactDetailsASO.setData(contactDetailsBO);

		when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(responseHost);

		PEWUResponse validation = rbvdR048.executeGetCustomerByDocType("00000000","L");
		assertNull(validation);
	}

	@Test
	public void executeInvokeCypherServiceOk(){
		LOGGER.info("RBVDR048 - Executing executeInvokeCypherServiceOk...");
		OutputDTO firstOutput = new OutputDTO();
		firstOutput.setData("emhSTGcxRnM");
		when(ksmkr002.executeKSMKR002(anyList(), anyString(), anyString(), anyObject())).thenReturn(singletonList(firstOutput));

		String validation = rbvdR048.executeKsmkCryptography("customerId");
		assertNotNull(validation);
		assertEquals("emhSTGcxRnM",validation);
	}

	@Test(expected = BusinessException.class)
	public void executeInvokeCypherServiceWithEmptyResult(){
		LOGGER.info("RBVDR048 - Executing executeInvokeCypherServiceOk...");
		when(ksmkr002.executeKSMKR002(anyList(), anyString(), anyString(), anyObject())).thenReturn(emptyList());

		String validation = rbvdR048.executeKsmkCryptography("customerId");
		assertNull(validation);
	}

	@Test
	public void executeInvokeListBusinesServiceOk(){
		LOGGER.info("RBVDR048 - Executing executeInvokeListBusinesServiceOk...");
		ListBusinessesASO businesses = new ListBusinessesASO();
		BusinessASO businessASO = new BusinessASO();
		businessASO.setDoingBusinessAs("doingBusinessAs");
		List<BusinessASO> businessASOList = new ArrayList<>();
		businessASOList.add(businessASO);
		businesses.setData(businessASOList);
		when(rbvdr066.executeGetListBusinesses(anyString(),anyObject())).thenReturn(businesses);

		ListBusinessesASO validation = rbvdR048.executeListBusiness("customerId");
		assertNotNull(validation);
		assertNotNull(validation.getData());
		assertNotNull(validation.getData().get(0));
	}

	@Test(expected = BusinessException.class)
	public void executeInvokeListBusinesServiceWithEmptyResult(){
		LOGGER.info("RBVDR048 - Executing executeInvokeListBusinesServiceWithEmptyResult...");
		ListBusinessesASO businesses = new ListBusinessesASO();
		businesses.setData(new ArrayList<>());
		when(rbvdr066.executeGetListBusinesses(anyString(),anyObject())).thenReturn(businesses);

		ListBusinessesASO validation = rbvdR048.executeListBusiness("customerId");
		assertNull(validation);
	}


	
}
