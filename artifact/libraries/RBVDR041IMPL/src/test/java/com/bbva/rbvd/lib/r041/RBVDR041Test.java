package com.bbva.rbvd.lib.r041;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.rbvd.dto.insrncsale.aso.BusinessTypeASO;
import com.bbva.rbvd.dto.insrncsale.aso.FormationASO;
import com.bbva.rbvd.dto.insrncsale.aso.listbusinesses.ListBusinessesASO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.participant.dao.QuotationCustomerDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationLifeDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceProductDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceBusinessDAO;
import com.bbva.rbvd.dto.participant.dao.InsuranceCompanyDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationModDAO;
import com.bbva.rbvd.dto.participant.dao.QuotationDAO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.utils.DatabaseParticipantErrors;
import com.bbva.rbvd.lib.r041.impl.RBVDR041Impl;
import com.bbva.rbvd.lib.r041.properties.ParticipantProperties;
import com.bbva.rbvd.lib.r041.util.ConstantsUtil;
import com.bbva.rbvd.lib.r048.RBVDR048;
import com.bbva.rbvd.util.MockDTO;
import com.bbva.rbvd.util.ParticipantsUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.aop.framework.Advised;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR041-app.xml",
		"classpath:/META-INF/spring/RBVDR041-app-test.xml",
		"classpath:/META-INF/spring/RBVDR041-arc.xml",
		"classpath:/META-INF/spring/RBVDR041-arc-test.xml" })

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RBVDR041Test {

	@Spy
	private Context context;

	@InjectMocks
	private RBVDR041Impl rbvdR041;

	@Mock
	private RBVDR048 rbvdr048;

	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

	@Mock
	private ParticipantProperties participantProperties;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		context = new Context();
		ThreadContext.set(context);
		getObjectIntrospection();

		when(this.applicationConfigurationService.getDefaultProperty("PAYMENT_MANAGER", ""))
				.thenReturn("7");
		when(this.applicationConfigurationService.getDefaultProperty("INSURED", ""))
				.thenReturn("2");
		when(this.applicationConfigurationService.getDefaultProperty("CONTRACTOR", ""))
				.thenReturn("1");
		when(this.applicationConfigurationService.getProperty("DNI"))
				.thenReturn("L");
		when(this.applicationConfigurationService.getProperty("RUC"))
				.thenReturn("R");
		when(this.applicationConfigurationService.getProperty("FOREIGNERS"))
				.thenReturn("F");
		when(this.applicationConfigurationService.getProperty(ConstantsUtil.ENABLED_LIFE_PRODUCTS))
				.thenReturn("841");
		when(this.applicationConfigurationService.getProperty(ConstantsUtil.ENABLED_NON_LIFE_PRODUCTS))
				.thenReturn("830");
		when(this.applicationConfigurationService.getProperty("legal-representative-code"))
				.thenReturn("LEGAL_REPRESENTATIVE");

		when(this.participantProperties.obtainRoleCodeByEnum("PAYMENT_MANAGER.bank.role")).thenReturn("7");
		when(this.participantProperties.obtainRoleCodeByEnum("INSURED.bank.role")).thenReturn("2");
		when(this.participantProperties.obtainRoleCodeByEnum("CONTRACTOR.bank.role")).thenReturn("1");
		when(this.participantProperties.obtainRoleCodeByEnum("LEGAL_REPRESENTATIVE.bank.role")).thenReturn("");
		when(this.participantProperties.obtainRoleCodeByEnum("EXAMPLE_ROL.bank.role")).thenReturn("");
	}
	
	private Object getObjectIntrospection() throws Exception{
		Object result = this.rbvdR041;
		if(this.rbvdR041 instanceof Advised){
			Advised advised = (Advised) this.rbvdR041;
			result = advised.getTargetSource().getTarget();
		}
		return result;
	}

	@Test
	public void executeNaturalPersonTestOkNonLifeProduct(){
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestInvalidPorduct(){
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		QuotationCustomerDAO quotationCustomerDAO = ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435","VIDA","800");
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotationCustomerDAO);
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestOkNonLifeProductInvalidCompanyRole(){
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		request.getParticipants().get(2).getParticipantType().setId("EXAMPLE_ROL");
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

    @Test
    public void executeTestDynamcLifeOk(){
        QuotationCustomerDAO quotationJoinCustomerInformation = new QuotationCustomerDAO();
        quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
        quotationJoinCustomerInformation.setQuotation(new QuotationDAO());
		quotationJoinCustomerInformation.setQuotationMod(new QuotationModDAO());
		quotationJoinCustomerInformation.setInsuranceBusiness(new InsuranceBusinessDAO());
		quotationJoinCustomerInformation.setInsuranceCompanyDAO(new InsuranceCompanyDAO());
		quotationJoinCustomerInformation.getInsuranceBusiness().setInsuranceBusinessName("VIDA");
        quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
        quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductId(new BigDecimal(11));
		quotationJoinCustomerInformation.getQuotationMod().setInsuranceModalityType("02");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductDesc("VIDAINVERSION");
		quotationJoinCustomerInformation.getInsuranceCompanyDAO().setInsuranceCompanyId(new BigDecimal(21));
		QuotationLifeDAO quotationLifeDAOResponseDB = new QuotationLifeDAO();
		quotationLifeDAOResponseDB.setClientLastName("Romero|Aguilar");
		quotationLifeDAOResponseDB.setCustomerBirthDate("2023-05-15");
		quotationLifeDAOResponseDB.setPersonalId("71838402");
		quotationLifeDAOResponseDB.setInsuredCustomerName("Paul");
		quotationLifeDAOResponseDB.setGenderId("F");
		quotationLifeDAOResponseDB.setPhoneId("960675837");
		quotationLifeDAOResponseDB.setCustomerDocumentType("L");
		quotationLifeDAOResponseDB.setUserEmailPersonalDesc("huhuh@gmail.com");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
        when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAOResponseDB);
        AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(rbvdR041.executeValidateParticipants(request) instanceof  AgregarTerceroBO);
    }

	@Test
	public void executeNaturalPersonTestOkLifeProduct(){
		QuotationCustomerDAO quotation = ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435","VIDA","841");
		quotation.getInsuranceProduct().setInsuranceProductType("841");
		QuotationLifeDAO quotationLifeDAOResponseDB = new QuotationLifeDAO();
		quotationLifeDAOResponseDB.setClientLastName("Romero|Aguilar");
		quotationLifeDAOResponseDB.setCustomerBirthDate("2023-05-15");
		quotationLifeDAOResponseDB.setPersonalId("71838402");
		quotationLifeDAOResponseDB.setInsuredCustomerName("Paul");
		quotationLifeDAOResponseDB.setGenderId("F");
		quotationLifeDAOResponseDB.setPhoneId("960675837");
		quotationLifeDAOResponseDB.setCustomerDocumentType("L");
		quotationLifeDAOResponseDB.setUserEmailPersonalDesc("huhuh@gmail.com");
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotation);
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAOResponseDB);
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsLifeCase1());
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());

		AgregarTerceroBO response1 = rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsLifeCase2());
		Assert.assertNotNull(response1);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestOkWithNonCustomer(){
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		request.getParticipants().get(2).getPerson().setCustomerId(null);
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase2(){

		when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		when(rbvdr048.executeGetCustomerByDocType(anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase2());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);

		InputParticipantsDTO InputParticipantsDTO =  ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(InputParticipantsDTO);
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase3(){

		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
        Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
        Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(), Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
        PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
        payloadAgregarTerceroBO.setCotizacion("cotizacion");
        agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
        when(rbvdr048.executeAddParticipants(anyObject(), anyString(), anyString(), anyString(),anyString())).thenReturn(agregarTerceroBO);

        InputParticipantsDTO InputParticipantsDTO = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
        AgregarTerceroBO response = rbvdR041.executeValidateParticipants(InputParticipantsDTO);
        Assert.assertNotNull(response);
        Assert.assertEquals(0, context.getAdviceList().size());

    }

    @Test
    public void executeValidationWithNaturalPersonDataTypeOkCase4(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase4());


		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase5(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase5());

		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase6(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase6());

		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}


	@Test
	public void executeLegalPersonTestOk() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(MockDTO.getInstance().getListBusinessesOkMock());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}
	@Test
	public void executeVehicleLegalPersonTestOk() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getVehicleMockRequestBodyValidateLegalParticipants();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(MockDTO.getInstance().getListBusinessesOkMock());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());

		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}



	@Test
	public void executeValidationWithNaturalPersonDataWrongPewuResponse(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(new PEWUResponse());

		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeLegalPersonTestOkCase2() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		ListBusinessesASO listBusinessesASO = MockDTO.getInstance().getListBusinessesOkMockCase2();
		listBusinessesASO.getData().get(0).setLegalName(null);
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(listBusinessesASO);
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());

		listBusinessesASO.getData().get(0).setLegalName("Ñandú");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(listBusinessesASO);
		AgregarTerceroBO response2 = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response2);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeLegalPersonTestOkCase3() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		ListBusinessesASO listBusinessesASO = MockDTO.getInstance().getListBusinessesOkMockCase2();
		FormationASO formationASO = new FormationASO();
		BusinessTypeASO businessTypeASO = new BusinessTypeASO();
		businessTypeASO.setId("id");
		businessTypeASO.setAmount(23.02);
		formationASO.setBusinessType(businessTypeASO);
		listBusinessesASO.getData().get(0).setFormation(formationASO);
		listBusinessesASO.getData().get(0).setAnnualSales(null);
		listBusinessesASO.getData().get(0).setLegalName("null");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(listBusinessesASO);
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());

		listBusinessesASO.getData().get(0).setLegalName(" ");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(listBusinessesASO);
		AgregarTerceroBO response2 = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response2);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonWithBusinessTestOk() {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantWithBusiness();
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		when(rbvdr048.executeGetRolesByCompany(anyObject())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

    @Test
    public void executeTestDynamicLifeWithTwoPersonOK(){
		QuotationCustomerDAO quotationJoinCustomerInformation = new QuotationCustomerDAO();
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setQuotation(new QuotationDAO());
		quotationJoinCustomerInformation.setQuotationMod(new QuotationModDAO());
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setInsuranceBusiness(new InsuranceBusinessDAO());
		quotationJoinCustomerInformation.setInsuranceCompanyDAO(new InsuranceCompanyDAO());
		quotationJoinCustomerInformation.getInsuranceBusiness().setInsuranceBusinessName("VIDA");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductDesc("VIDAINVERSION");
		quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductId(new BigDecimal(1));
		quotationJoinCustomerInformation.getQuotationMod().setInsuranceModalityType("02");
		quotationJoinCustomerInformation.getInsuranceCompanyDAO().setInsuranceCompanyId(new BigDecimal(21));

		QuotationLifeDAO quotationLifeDAOResponseDB = new QuotationLifeDAO();
		quotationLifeDAOResponseDB.setClientLastName("Romero|Aguilar");
		quotationLifeDAOResponseDB.setCustomerBirthDate("2023-05-15");
		quotationLifeDAOResponseDB.setPersonalId("71838402");
		quotationLifeDAOResponseDB.setInsuredCustomerName("Paul");
		quotationLifeDAOResponseDB.setGenderId("F");
		quotationLifeDAOResponseDB.setPhoneId("960675837");
		quotationLifeDAOResponseDB.setCustomerDocumentType("L");
		quotationLifeDAOResponseDB.setUserEmailPersonalDesc("huhuh@gmail.com");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsTwoLifeCase();
        when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAOResponseDB);
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(rbvdR041.executeValidateParticipants(request) instanceof  AgregarTerceroBO);

		InputParticipantsDTO request2 = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsTwoLifeCase();
		ParticipantsDTO participantsContractor = ParticipantsUtil.buildParticipant("CONTRACTOR","DNI", "00002024","NATURAL",true);
		participantsContractor.getPerson().setCustomerId("97854521");
		participantsContractor.getPerson().setFirstName(null);
		participantsContractor.getPerson().setSecondLastName(null);
		request2.getParticipants().add(participantsContractor);
		AgregarTerceroBO response2 = rbvdR041.executeValidateParticipants(request2);
		Assert.assertNotNull(response2);
		Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(rbvdR041.executeValidateParticipants(request2) instanceof  AgregarTerceroBO);
    }

    @Test
    public void executeTestDynamicLifeWithOnePersonOK(){
		QuotationCustomerDAO quotationJoinCustomerInformation = new QuotationCustomerDAO();
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setQuotation(new QuotationDAO());
		quotationJoinCustomerInformation.setQuotationMod(new QuotationModDAO());
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setInsuranceBusiness(new InsuranceBusinessDAO());
		quotationJoinCustomerInformation.setInsuranceCompanyDAO(new InsuranceCompanyDAO());
		quotationJoinCustomerInformation.getInsuranceBusiness().setInsuranceBusinessName("VIDA");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductDesc("VIDAINVERSION");
		quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductId(new BigDecimal(1));
		quotationJoinCustomerInformation.getQuotationMod().setInsuranceModalityType("02");
		quotationJoinCustomerInformation.getInsuranceCompanyDAO().setInsuranceCompanyId(new BigDecimal(21));
        //request of trx

		QuotationLifeDAO quotationLifeDAOResponseDB = new QuotationLifeDAO();
		quotationLifeDAOResponseDB.setClientLastName("Romero|Aguilar");
		quotationLifeDAOResponseDB.setCustomerBirthDate("2023-05-15");
		quotationLifeDAOResponseDB.setPersonalId("71838402");
		quotationLifeDAOResponseDB.setInsuredCustomerName("Paul");
		quotationLifeDAOResponseDB.setGenderId("F");
		quotationLifeDAOResponseDB.setPhoneId("960675837");
		quotationLifeDAOResponseDB.setCustomerDocumentType("L");
		quotationLifeDAOResponseDB.setUserEmailPersonalDesc("huhuh@gmail.com");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsOne();
        when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAOResponseDB);
        AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);

		Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(response instanceof  AgregarTerceroBO);
    }

	@Test
    public void testExecuteDynamicLifeBusinessException() {
		QuotationCustomerDAO quotationJoinCustomerInformation = new QuotationCustomerDAO();
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setQuotation(new QuotationDAO());
		quotationJoinCustomerInformation.setQuotationMod(new QuotationModDAO());
		quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductDAO());
		quotationJoinCustomerInformation.setInsuranceCompanyDAO(new InsuranceCompanyDAO());
		quotationJoinCustomerInformation.setInsuranceBusiness(new InsuranceBusinessDAO());
		quotationJoinCustomerInformation.getInsuranceBusiness().setInsuranceBusinessName("VIDA");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductDesc("VIDAINVERSION");
		quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
		quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductId(new BigDecimal(1));
		quotationJoinCustomerInformation.getQuotationMod().setInsuranceModalityType("02");
		quotationJoinCustomerInformation.getInsuranceCompanyDAO().setInsuranceCompanyId(new BigDecimal(21));

        Map<String,Object> responseData = new HashMap<>();
        responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
        responseData.put("INSURANCE_MODALITY_TYPE","02");

		QuotationLifeDAO quotationLifeDAOResponseDB = new QuotationLifeDAO();
		quotationLifeDAOResponseDB.setClientLastName("Romero|Aguilar");
		quotationLifeDAOResponseDB.setCustomerBirthDate("2023-05-15");
		quotationLifeDAOResponseDB.setPersonalId("71838402");
		quotationLifeDAOResponseDB.setInsuredCustomerName("Paul");
		quotationLifeDAOResponseDB.setGenderId("F");
		quotationLifeDAOResponseDB.setPhoneId("960675837");
		quotationLifeDAOResponseDB.setCustomerDocumentType("L");
		quotationLifeDAOResponseDB.setUserEmailPersonalDesc("huhuh@gmail.com");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsTwoLifeCase();
        when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(this.rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString(),anyString())).thenThrow(new BusinessException("BBVA14554",false,"businessError"));
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(quotationLifeDAOResponseDB);
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());

		AgregarTerceroBO response = rbvdR041.executeValidateParticipants(request);

		assertNull(response.getPayload());
		Assert.assertEquals(1,this.context.getAdviceList().size());
    }

	@Test
	public void executeValidationWithWrongClientQuotationId(){
		when(rbvdr048.executeGetCustomerInformationFromQuotation(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenThrow(new BusinessException(DatabaseParticipantErrors.PARAMETERS_INVALIDATE.getAdviceCode(), false, DatabaseParticipantErrors.PARAMETERS_INVALIDATE.getMessage()));
		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithWrongPlansConfiguredForProductModality(){

		Mockito.when(rbvdr048.executeGetCustomerInformationFromQuotation(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384","VEHICULAR","830"));
		Mockito.when(rbvdr048.executeGetRolesByCompany(anyObject())).thenThrow(new BusinessException(DatabaseParticipantErrors.QUERY_EMPTY_RESULT.getAdviceCode(), false, DatabaseParticipantErrors.QUERY_EMPTY_RESULT.getMessage()));

		AgregarTerceroBO response =  rbvdR041.executeValidateParticipants(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

}
