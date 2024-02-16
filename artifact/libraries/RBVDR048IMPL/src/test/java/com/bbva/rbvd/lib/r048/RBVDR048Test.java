package com.bbva.rbvd.lib.r048;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.mock.MockData;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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

	@Resource(name = "rbvdR048")
	private RBVDR048 rbvdR048;
	@Resource(name = "pisdR014")
	private PISDR014 pisdr014;
	@Resource(name = "pisdR403")
	private PISDR403 pisdr403;

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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipantsDynamicLife(agregarTerceroBO,"quotationId","840","traceId");

		assertNotNull(validation);
		assertNotNull(validation.getPayload());
		assertNotNull(validation.getPayload().getStatus());
		assertNotNull(validation.getPayload().getMensaje());
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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipantsDynamicLife(new AgregarTerceroBO(),"quotationId","productId","traceId");

		assertNotNull(validation);
	}
	
}
