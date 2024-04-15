package com.bbva.rbvd.lib.r048;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.dto.insurance.aso.GetContactDetailsASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.pisd.lib.r040.PISDR040;
import com.bbva.pisd.lib.r403.PISDR403;
import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW5;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.BusinessASO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.mock.MockData;
import com.bbva.rbvd.dto.participant.dao.*;
import com.bbva.rbvd.dto.participant.utils.TypeErrorControllerEnum;
import com.bbva.rbvd.dto.participant.utils.ValidateParticipantErrors;
import com.bbva.rbvd.lib.r048.impl.RBVDR048Impl;
import com.bbva.rbvd.mock.MockBundleContext;
import com.bbva.rbvd.dto.insuranceroyal.error.ErrorResponseDTO;
import com.bbva.rbvd.lib.r066.RBVDR066;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR048-app.xml",
		"classpath:/META-INF/spring/RBVDR048-app-test.xml",
		"classpath:/META-INF/spring/RBVDR048-arc.xml",
		"classpath:/META-INF/spring/RBVDR048-arc-test.xml" })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RBVDR048Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR048Test.class);

	@Spy
	private Context context;

	@InjectMocks
	private RBVDR048Impl rbvdR048;

	@Mock
	private PISDR014 pisdr014;

	@Mock
	private PISDR403 pisdr403;


	@Mock
	private PISDR040 pisdr040;


	@Mock
	private PBTQR002 pbtqr002;

	@Mock
    private KSMKR002 ksmkr002;

	@Mock
    private RBVDR066 rbvdr066;

	private MockData mockData;

	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

	@Mock
	private APIConnector externalApiConnector;

	@Before
	public void setUp() throws Exception {

		mockData = MockData.getInstance();
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);
		getObjectIntrospection();
		MockBundleContext mockBundleContext = mock(MockBundleContext.class);

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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(agregarTerceroBO,"quotationId","840","traceId", "PC");

		assertNotNull(validation);
		assertNotNull(validation.getPayload());
		assertNotNull(validation.getPayload().getStatus());
		assertNotNull(validation.getPayload().getMensaje());

        AgregarTerceroBO validation1 = this.rbvdR048.executeAddParticipants(agregarTerceroBO,"quotationId","830","traceId", "PC");
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

	@Test
	public void testExecuteGetDataInsured() {

		QuotationLifeDAO quotationLifeDAO = new QuotationLifeDAO();
		quotationLifeDAO.setClientLastName("clientLastName");
		quotationLifeDAO.setCustomerBirthDate("customerBirthDate");
		quotationLifeDAO.setPersonalId("personalId");
		quotationLifeDAO.setInsuredCustomerName("insuredCustomerName");
		quotationLifeDAO.setGenderId("genderId");
		quotationLifeDAO.setPhoneId("phoneId");
		quotationLifeDAO.setCustomerDocumentType("customerDocumentType");
		quotationLifeDAO.setUserEmailPersonalDesc("userEmailPersonalDesc");

		when(this.pisdr040.executeGetInsuredQuotationLife(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAO);
		QuotationLifeDAO response = this.rbvdR048.executeGetDataInsuredBD("0814000039658","148","01","70221978","L");

		assertNotNull(response);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteGetDataInsuredWithException() {

		QuotationLifeDAO quotationLifeDAO = new QuotationLifeDAO();
		quotationLifeDAO.setClientLastName("clientLastName");
		quotationLifeDAO.setCustomerBirthDate("customerBirthDate");
		quotationLifeDAO.setPersonalId("personalId");
		quotationLifeDAO.setInsuredCustomerName("insuredCustomerName");
		quotationLifeDAO.setGenderId("genderId");
		quotationLifeDAO.setPhoneId("phoneId");
		quotationLifeDAO.setCustomerDocumentType("customerDocumentType");
		quotationLifeDAO.setUserEmailPersonalDesc("userEmailPersonalDesc");

		when(this.pisdr040.executeGetInsuredQuotationLife(anyString(),anyString(),anyString(),anyString(),anyString())).thenThrow(new BusinessException(ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage()));
		QuotationLifeDAO response = this.rbvdR048.executeGetDataInsuredBD("0814000039658","148","01","70221978","L");

		assertNull(response);
	}

	@Test
	public void testExecuteGetCustomerInformationFromQuotation() {

		QuotationCustomerDAO quotationJoinInformation = new QuotationCustomerDAO();

		QuotationDAO quotationEntity = new QuotationDAO();
		QuotationModDAO quotationModEntity = new QuotationModDAO();
		InsuranceProductDAO insuranceProductEntity = new InsuranceProductDAO();
		InsuranceBusinessDAO insuranceBusinessEntity = new InsuranceBusinessDAO();

		quotationEntity.setInsuredCustomerName("customer name");
		quotationEntity.setClientLasName("client last name");
		quotationEntity.setInsuranceCompanyQuotaId("b5add021-a825-4ba1-a455-95e11015cff7");
		quotationEntity.setParticipantPersonalId("participantPersonalId");
		quotationModEntity.setContactEmailDesc("example@bbva");
		quotationModEntity.setCustomerPhoneDesc("923453849");
		quotationModEntity.setInsuranceProductId(new BigDecimal(1));
		quotationModEntity.setInsuranceModalityType("02");

		insuranceProductEntity.setInsuranceProductType("830");
		insuranceProductEntity.setInsuranceProductId(new BigDecimal(1));
		insuranceBusinessEntity.setInsuranceBusinessName("VEHICULAR");

		quotationJoinInformation.setQuotation(quotationEntity);
		quotationJoinInformation.setQuotationMod(quotationModEntity);
		quotationJoinInformation.setInsuranceProduct(insuranceProductEntity);
		quotationJoinInformation.setInsuranceBusiness(insuranceBusinessEntity);

		when(this.pisdr040.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinInformation);
		QuotationCustomerDAO result = this.rbvdR048.executeGetCustomerInformationFromQuotation("0814000039658");
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getQuotation());
		Assert.assertEquals("customer name",result.getQuotation().getInsuredCustomerName());
		Assert.assertEquals("client last name",result.getQuotation().getClientLasName());
		Assert.assertNotNull(result.getQuotationMod());
		Assert.assertEquals("example@bbva",result.getQuotationMod().getContactEmailDesc());
		Assert.assertEquals("923453849",result.getQuotationMod().getCustomerPhoneDesc());
		Assert.assertNotNull(result.getInsuranceProduct());
		Assert.assertEquals("830",result.getInsuranceProduct().getInsuranceProductType());
		Assert.assertNotNull(result.getInsuranceBusiness());
		Assert.assertEquals("VEHICULAR",result.getInsuranceBusiness().getInsuranceBusinessName());

	}

	@Test(expected = BusinessException.class)
	public void testExecuteGetCustomerInformationFromQuotationWithExepction() {

		QuotationCustomerDAO quotationJoinInformation = new QuotationCustomerDAO();

		QuotationDAO quotationEntity = new QuotationDAO();
		QuotationModDAO quotationModEntity = new QuotationModDAO();
		InsuranceProductDAO insuranceProductEntity = new InsuranceProductDAO();
		InsuranceBusinessDAO insuranceBusinessEntity = new InsuranceBusinessDAO();

		quotationEntity.setInsuredCustomerName("customer name");
		quotationEntity.setClientLasName("client last name");
		quotationEntity.setInsuranceCompanyQuotaId("b5add021-a825-4ba1-a455-95e11015cff7");
		quotationEntity.setParticipantPersonalId("participantPersonalId");
		quotationModEntity.setContactEmailDesc("example@bbva");
		quotationModEntity.setCustomerPhoneDesc("923453849");
		quotationModEntity.setInsuranceProductId(new BigDecimal(1));
		quotationModEntity.setInsuranceModalityType("02");

		insuranceProductEntity.setInsuranceProductType("830");
		insuranceProductEntity.setInsuranceProductId(new BigDecimal(1));
		insuranceBusinessEntity.setInsuranceBusinessName("VEHICULAR");

		quotationJoinInformation.setQuotation(quotationEntity);
		quotationJoinInformation.setQuotationMod(quotationModEntity);
		quotationJoinInformation.setInsuranceProduct(insuranceProductEntity);
		quotationJoinInformation.setInsuranceBusiness(insuranceBusinessEntity);

		when(this.pisdr040.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenThrow(new BusinessException(ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage()));
		QuotationCustomerDAO result = this.rbvdR048.executeGetCustomerInformationFromQuotation("0814000039658");
		Assert.assertNull(result);

	}

	@Test
	public void testExecuteGetRolesByCompany() {

		List<RolDAO> listResponseDb = new ArrayList<>();

		RolDAO line1 = new RolDAO();
		line1.setParticipantRoleId(new Integer(7));
		line1.setInsuranceCompanyRoleId("8");
		listResponseDb.add(line1);
		RolDAO line2 = new RolDAO();
		line2.setParticipantRoleId(new Integer(2));
		line2.setInsuranceCompanyRoleId("9");
		listResponseDb.add(line2);
		RolDAO line3 = new RolDAO();
		line3.setParticipantRoleId(new Integer(1));
		line3.setInsuranceCompanyRoleId("23");
		listResponseDb.add(line3);

		when(this.pisdr040.executeListParticipantRolesByCompanyId(anyObject())).thenReturn(listResponseDb);
		List<RolDAO> result = this.rbvdR048.executeGetRolesByCompany(new BigDecimal(1));
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.get(0));
		Assert.assertNotNull(result.get(1));
		Assert.assertNotNull(result.get(2));
		Assert.assertEquals("8",result.get(0).getInsuranceCompanyRoleId());
		Assert.assertEquals("9",result.get(1).getInsuranceCompanyRoleId());
		Assert.assertEquals("23",result.get(2).getInsuranceCompanyRoleId());
		Assert.assertEquals(new Integer(7),result.get(0).getParticipantRoleId());
		Assert.assertEquals(new Integer(2),result.get(1).getParticipantRoleId());
		Assert.assertEquals(new Integer(1),result.get(2).getParticipantRoleId());
	}

	@Test(expected = BusinessException.class)
	public void testExecuteGetRolesByCompanyWithException() {

		List<RolDAO> listResponseDb = new ArrayList<>();

		RolDAO line1 = new RolDAO();
		line1.setParticipantRoleId(new Integer(7));
		line1.setInsuranceCompanyRoleId("8");
		listResponseDb.add(line1);
		RolDAO line2 = new RolDAO();
		line2.setParticipantRoleId(new Integer(2));
		line2.setInsuranceCompanyRoleId("9");
		listResponseDb.add(line2);
		RolDAO line3 = new RolDAO();
		line3.setParticipantRoleId(new Integer(1));
		line3.setInsuranceCompanyRoleId("23");
		listResponseDb.add(line3);

		when(this.pisdr040.executeListParticipantRolesByCompanyId(anyObject())).thenThrow(new BusinessException(ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getAdviceCode(), false, ValidateParticipantErrors.SELECT_DB_ORACLE_ERROR.getMessage()));
		List<RolDAO> result = this.rbvdR048.executeGetRolesByCompany(new BigDecimal(1));
		Assert.assertNull(result);
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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionWrongErrorFormat() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithRestClientException...");

		String responseBody = "{\n" +
				"    \"errorWrongFormat\": {\n" +
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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionWrongDetailsFormat() {
		LOGGER.info("RBVDR048 - Executing testExecuteAddParticipantsServiceWithRestClientException...");

		String responseBody = "{\n" +
				"    \"error\": {\n" +
				"        \"code\": \"VIDACOT005\",\n" +
				"        \"message\": \"Validacion de Datos\",\n" +
				"        \"detailsWrongFormat\": {\n" +
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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithRestClientExceptionWrongHttpStatusFormat() {
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
				"        \"httpStatusWrongFormat\": 403\n" +
				"    }\n" +
				"}";

		ErrorResponseDTO res = new ErrorResponseDTO();
		res.setCode("Bbva41255");
		res.setMessage("Error al consumir serviciso rimac ");
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

		assertNotNull(validation);
	}

	@Test(expected = BusinessException.class)
	public void testExecuteAddParticipantsServiceWithOutResponseOdDataBase() {


		String responseBody = "{\n" +
				"    \"error\": {\n" +
				"        \"code\": \"VIDACOT005\",\n" +
				"        \"message\": \"Validacion de Datos\",\n" +
				"        \"details\": {\n" +
				"            \"PE008002\": \"El campo apePaterno de persona en su elemento 3 es requerido\",\n" +
				"            \"PE009002\": \"El campo apeMaterno de persona en su elemento 3 es requerido\",\n" +
				"            \"PE011002\": \"El campo fechaNacimiento de persona en su elemento 3 es requerido\"\n" +
				"        },\n" +
				"        \"httpStatusWrongFormat\": 403\n" +
				"    }\n" +
				"}";

		ErrorResponseDTO res = new ErrorResponseDTO();
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("https://apitest.rimac.com/api-vida/V1/cotizaciones/{cotizacion}/persona-agregar");
		when(this.externalApiConnector.exchange(anyString(), anyObject(),anyObject(), (Class<AgregarTerceroBO>) any(), anyMap()))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8));
		when(pisdr403.executeFindError(anyObject())).thenReturn(res);
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

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
		AgregarTerceroBO validation = this.rbvdR048.executeAddParticipants(new AgregarTerceroBO(),"quotationId","productId","traceId", "PC");

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


	@Test
	public void testExecutegetCustomerok() {
		LOGGER.info("RBVDR048 - Executing executeGetCustomerService ...");
		when(this.pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		PEWUResponse response = this.rbvdR048.executeGetCustomerByDocType(anyString(),anyString());

		assertNotNull(response);

	}

	@Test(expected = BusinessException.class)
	public void testExecuteGetCustomerError() {
		LOGGER.info("RBVDR048 - Executing executeGetCustomerService ...");
		PEWUResponse pemsalwu = new PEWUResponse();
		pemsalwu.setHostAdviceCode("124567");
		when(this.pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(pemsalwu);
		PEWUResponse response = this.rbvdR048.executeGetCustomerByDocType(anyString(),anyString());

		assertNotNull(response);
	}

	private static PEWUResponse buildPersonHostDataResponseCase3(){
		PEWUResponse pewuResponse = new PEWUResponse();
		PEMSALWU pemsalwu = new PEMSALWU();
		pemsalwu.setTdoi("L");
		pemsalwu.setNdoi("00932622");
		pemsalwu.setNroclie("77809762");
		pemsalwu.setFechav("2022-02-17");
		pemsalwu.setFechaal("2002-01-02");
		pemsalwu.setTipoper("F00");
		pemsalwu.setOficina("0199");
		pemsalwu.setTiponac("N");
		pemsalwu.setTipores("R");
		pemsalwu.setApellip("SALAS");
		pemsalwu.setApellim("FASABI");
		pemsalwu.setNombres("NATIVIDAD");
		pemsalwu.setTitulo("ARQ.");
		pemsalwu.setEstadoc("S");
		pemsalwu.setSexo("F");
		pemsalwu.setFechan("1968-02-05");
		pemsalwu.setPaisn("PER");
		pemsalwu.setPaisd1("paisd1");
		pemsalwu.setPaisre("PER");
		pemsalwu.setPaisna("PER");
		pemsalwu.setFechare("2010-12-0");
		pemsalwu.setCodact("");
		pemsalwu.setOcupaci("ASA");
		pemsalwu.setCentro("BBVA");
		pemsalwu.setSegment("80500");
		pemsalwu.setDescseg("PRIVADA");
		pemsalwu.setIdencon("");
		pemsalwu.setTipocon("");
		pemsalwu.setContact("");
		pemsalwu.setTipoco2("MV");
		pemsalwu.setContac2("969100232");
		pemsalwu.setIdenco3("EMAIL");
		pemsalwu.setTipoco3("MA");
		pemsalwu.setContac3("CLAUDI3_HUACHES@HOTMAIL.COM");
		pemsalwu.setTipodir("H");
		pemsalwu.setIdendi1("AV.");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("AGR");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("");
		pemsalwu.setNroint1("");
		pemsalwu.setManzana("D");
		pemsalwu.setLote("4");
		pemsalwu.setCuadran("");
		pewuResponse.setPemsalwu(pemsalwu);

		PEMSALW4 pemsalw4 = new PEMSALW4();
		pemsalw4.setDepetdo("HOGAR");
		pemsalw4.setDescvia("AVENIDA");
		pemsalw4.setDescurb("AGRUPACION");
		pemsalw4.setDesdept("LIMA");
		pemsalw4.setDesprov("LIMA");
		pemsalw4.setDesdist("RIMAC");

		pewuResponse.setPemsalw4(pemsalw4);

		return pewuResponse;
	}

}
