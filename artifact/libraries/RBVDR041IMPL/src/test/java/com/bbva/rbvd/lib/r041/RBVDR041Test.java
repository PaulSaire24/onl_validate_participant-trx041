package com.bbva.rbvd.lib.r041;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.pisd.dto.insurancedao.constants.PISDInsuranceErrors;
import com.bbva.pisd.dto.insurancedao.entities.InsuranceProductEntity;
import com.bbva.pisd.dto.insurancedao.entities.QuotationEntity;
import com.bbva.pisd.dto.insurancedao.join.QuotationCustomerDTO;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.InputParticipantsDTO;
import com.bbva.rbvd.lib.r041.impl.RBVDR041Impl;
import com.bbva.rbvd.lib.r048.RBVDR048;
import com.bbva.rbvd.util.MockDTO;
import com.bbva.rbvd.util.ParticipantsUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.framework.Advised;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
	private PISDR601 pisdr601;

	@Mock
    private PISDR012 pisdr012;

	@Mock
	private ApplicationConfigurationService applicationConfigurationService;

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
		Map<String,Object> responseData = new HashMap<>();
		responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
		responseData.put("INSURANCE_MODALITY_TYPE","02");
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

    @Test
    public void executeTestDynamcLifeOk(){
        QuotationCustomerDTO quotationJoinCustomerInformation = new QuotationCustomerDTO();
        quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductEntity());
        quotationJoinCustomerInformation.setQuotation(new QuotationEntity());
        quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
        quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
        Map<String,Object> responseInsuredBD = new HashMap<>();
        responseInsuredBD.put("CLIENT_LAST_NAME","Romero|Aguilar");
        responseInsuredBD.put("INSURED_CUSTOMER_NAME","Paul");
        responseInsuredBD.put("GENDER_ID","F");
        responseInsuredBD.put("USER_EMAIL_PERSONAL_DESC","huhuh@gmail.com");
        responseInsuredBD.put("PHONE_ID","960675837");
        responseInsuredBD.put("CUSTOMER_BIRTH_DATE","2023-05-15");
        //request of trx
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
        responseData.put("INSURANCE_MODALITY_TYPE","02");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
        when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
        when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
        when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);
        AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(rbvdR041.executeValidateAddParticipant(request) instanceof  AgregarTerceroBO);
    }

	@Test
	public void executeNaturalPersonTestOkLifeProduct(){
		QuotationCustomerDTO quotation = ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435");
		quotation.getInsuranceProduct().setInsuranceProductType("841");
		Map<String,Object> responseInsuredBD = new HashMap<>();
		responseInsuredBD.put("CLIENT_LAST_NAME","Romero|Aguilar");
		responseInsuredBD.put("INSURED_CUSTOMER_NAME","Paul");
		responseInsuredBD.put("GENDER_ID","F");
		responseInsuredBD.put("USER_EMAIL_PERSONAL_DESC","huhuh@gmail.com");
		responseInsuredBD.put("PHONE_ID","960675837");
		responseInsuredBD.put("CUSTOMER_BIRTH_DATE","2023-05-15");
		Map<String,Object> responseData = new HashMap<>();
		responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
		responseData.put("INSURANCE_MODALITY_TYPE","02");
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotation);
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);
		when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsLifeCase1());
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());

		AgregarTerceroBO response1 = rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipantsLifeCase2());
		Assert.assertNotNull(response1);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestOkWithNonCustomer(){
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		request.getParticipants().get(2).getPerson().setCustomerId(null);
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("789956435"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase2(){

		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		when(rbvdr048.executeGetCustomerByDocType(anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase2());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);

		InputParticipantsDTO InputParticipantsDTO =  ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(InputParticipantsDTO);
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase3(){

		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
        Mockito.when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
        Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(), Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
        PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
        payloadAgregarTerceroBO.setCotizacion("cotizacion");
        agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
        when(rbvdr048.executeAddParticipants(anyObject(), anyString(), anyString(), anyString())).thenReturn(agregarTerceroBO);

        InputParticipantsDTO InputParticipantsDTO = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
        AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(InputParticipantsDTO);
        Assert.assertNotNull(response);
        Assert.assertEquals(0, context.getAdviceList().size());

    }

    @Test
    public void executeValidationWithNaturalPersonDataTypeOkCase4(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase4());


		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase5(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeGetCustomerByDocType(Mockito.anyString(),Mockito.anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase5());

		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeLegalPersonTestOk() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(MockDTO.getInstance().getListBusinessesOkMock());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());

	}

	@Test
	public void executeLegalPersonTestOkCase2() throws IOException {
		InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipants();
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(ParticipantsUtil.buildRolByParticipantTypeResponse());
		Mockito.when(rbvdr048.executeKsmkCryptography(Mockito.anyString())).thenReturn("getKSMKResponseOkMock");
		Mockito.when(rbvdr048.executeListBusiness(Mockito.anyString())).thenReturn(MockDTO.getInstance().getListBusinessesOkMockCase2());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

    @Test
    public void executeTestDynamicLifeWithTwoPersonOK(){
        QuotationCustomerDTO quotationJoinCustomerInformation = new QuotationCustomerDTO();
        quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductEntity());
        quotationJoinCustomerInformation.setQuotation(new QuotationEntity());
        quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
        quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
        Map<String,Object> responseInsuredBD = new HashMap<>();
        responseInsuredBD.put("CLIENT_LAST_NAME","Romero|Aguilar");
        responseInsuredBD.put("INSURED_CUSTOMER_NAME","Paul");
        responseInsuredBD.put("GENDER_ID","F");
        responseInsuredBD.put("USER_EMAIL_PERSONAL_DESC","huhuh@gmail.com");
        responseInsuredBD.put("PHONE_ID","960675837");
        responseInsuredBD.put("CUSTOMER_BIRTH_DATE","2023-05-15");
        //request of trx

        Map<String,Object> responseData = new HashMap<>();
        responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
        responseData.put("INSURANCE_MODALITY_TYPE","02");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipantsTwo();
        when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
        when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);
        AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(rbvdR041.executeValidateAddParticipant(request) instanceof  AgregarTerceroBO);
    }

    @Test
    public void executeTestDynamicLifeWithOnePersonOK(){
        QuotationCustomerDTO quotationJoinCustomerInformation = new QuotationCustomerDTO();
        quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductEntity());
        quotationJoinCustomerInformation.setQuotation(new QuotationEntity());
        quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
        quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");
        //request of trx

        Map<String,Object> responseInsuredBD = new HashMap<>();
        responseInsuredBD.put("CLIENT_LAST_NAME","Romero|Aguilar");
        responseInsuredBD.put("INSURED_CUSTOMER_NAME","Paul");
        responseInsuredBD.put("GENDER_ID","F");
        responseInsuredBD.put("USER_EMAIL_PERSONAL_DESC","huhuh@gmail.com");
        responseInsuredBD.put("PHONE_ID","960675837");
        responseInsuredBD.put("CUSTOMER_BIRTH_DATE","2023-05-15");

        Map<String,Object> responseData = new HashMap<>();
        responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
        responseData.put("INSURANCE_MODALITY_TYPE","02");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipantsOne();
        when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
        when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
        when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);
        AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);

		Assert.assertNotNull(response);
        Assert.assertEquals(0,this.context.getAdviceList().size());
		Assert.assertTrue(response instanceof  AgregarTerceroBO);
		//Mockito.verify(rbvdR041,Mockito.atLeastOnce()).executeValidateAddParticipant(request);
    }

	@Test
    public void testExecuteDynamicLifeBusinessException() {
        QuotationCustomerDTO quotationJoinCustomerInformation = new QuotationCustomerDTO();
        quotationJoinCustomerInformation.setInsuranceProduct(new InsuranceProductEntity());
        quotationJoinCustomerInformation.setQuotation(new QuotationEntity());
        quotationJoinCustomerInformation.getInsuranceProduct().setInsuranceProductType("841");
        quotationJoinCustomerInformation.getQuotation().setInsuranceCompanyQuotaId("0814000038990");

        Map<String,Object> responseData = new HashMap<>();
        responseData.put("INSURANCE_PRODUCT_ID",new BigDecimal(21));
        responseData.put("INSURANCE_MODALITY_TYPE","02");

        Map<String,Object> responseInsuredBD = new HashMap<>();
        responseInsuredBD.put("CLIENT_LAST_NAME","Romero|Aguilar");
        responseInsuredBD.put("INSURED_CUSTOMER_NAME","Paul");
        responseInsuredBD.put("GENDER_ID","F");
        responseInsuredBD.put("USER_EMAIL_PERSONAL_DESC","huhuh@gmail.com");
        responseInsuredBD.put("PHONE_ID","960675837");
        responseInsuredBD.put("CUSTOMER_BIRTH_DATE","2023-05-15");

        InputParticipantsDTO request = ParticipantsUtil.getMockRequestBodyValidateLegalParticipantsTwo();
        when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
        when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
        when(this.rbvdr048.executeAddParticipants(anyObject(),anyString(),anyString(),anyString())).thenThrow(new BusinessException("BBVA14554",false,"businessError"));
        when(rbvdr048.executeGetDataInsuredBD(anyString(),anyString(),anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
        when(rbvdr048.executeGetCustomerByDocType(anyString(),anyString())).thenReturn(ParticipantsUtil.buildPersonHostDataResponseCase3());
        when(rbvdr048.executeGetProducAndPlanByQuotation(anyString())).thenReturn(responseData);

		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);

		assertNull(response.getPayload());
		Assert.assertEquals(1,this.context.getAdviceList().size());
    }

	@Test
	public void executeValidationWithWrongClientQuotationId(){
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("20123453922"));
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenThrow(new BusinessException(PISDInsuranceErrors.PARAMETERS_INVALIDATE.getAdviceCode(), false, PISDInsuranceErrors.PARAMETERS_INVALIDATE.getMessage()));
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithWrongPlansConfiguredForProductModality(){

		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(ParticipantsUtil.buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenThrow(new BusinessException(PISDInsuranceErrors.QUERY_EMPTY_RESULT.getAdviceCode(), false, PISDInsuranceErrors.QUERY_EMPTY_RESULT.getMessage()));

		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithWrongPersonTypeListFormatted(){

		InputParticipantsDTO InputParticipantsDTO = ParticipantsUtil.getMockRequestBodyValidateNaturalParticipants();
		InputParticipantsDTO.getParticipants().get(0).getPerson().setPersonType("LEGAL");
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(InputParticipantsDTO);
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

}
