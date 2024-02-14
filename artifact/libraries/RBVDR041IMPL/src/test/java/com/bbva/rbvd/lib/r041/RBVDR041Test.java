package com.bbva.rbvd.lib.r041;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.ksmk.dto.caas.OutputDTO;
import com.bbva.ksmk.lib.r002.KSMKR002;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurancedao.constants.PISDInsuranceErrors;
import com.bbva.pisd.dto.insurancedao.entities.InsuranceBusinessEntity;
import com.bbva.pisd.dto.insurancedao.entities.InsuranceProductEntity;
import com.bbva.pisd.dto.insurancedao.entities.QuotationEntity;
import com.bbva.pisd.dto.insurancedao.entities.QuotationModEntity;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r012.PISDR012;
import com.bbva.pisd.lib.r352.PISDR352;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.*;
import com.bbva.rbvd.dto.validateparticipant.dto.ResponseLibrary;
import com.bbva.rbvd.lib.r066.RBVDR066;
import com.bbva.rbvd.util.MockDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.aop.framework.Advised;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Collections;

import static org.mockito.Matchers.*;
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

	@Resource(name = "rbvdR041")
	private RBVDR041 rbvdR041;
	@Resource(name = "pbtqR002")
	private PBTQR002 pbtqr002;
	@Resource(name = "pisdR352")
	private PISDR352 pisdr352;
	@Resource(name = "ksmkR002")
	private KSMKR002 ksmkr002;
	@Resource(name = "pisdR601")
	private PISDR601 pisdr601;
	@Resource(name = "rbvdR066")
	private RBVDR066 rbvdr066;
	@Resource(name = "pisdR012")
	private PISDR012 pisdr012;
	@Resource(name = "applicationConfigurationService")
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
		ValidateParticipantDTO request = getMockRequestBodyValidateNaturalParticipants();
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("789956435"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestOkLifeProduct(){
		QuotationJoinCustomerInformationDTO quotation = buildFindQuotationJoinByPolicyQuotaInternalId("789956435");
		quotation.getInsuranceProduct().setInsuranceProductType("841");
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotation);
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeNaturalPersonTestOkWithNonCustomer(){
		ValidateParticipantDTO request = getMockRequestBodyValidateNaturalParticipants();
		request.getParticipants().get(2).getPerson().setCustomerId(null);
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("789956435"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase2(){

		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase2());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);

		ValidateParticipantDTO validateParticipantDTO =  getMockRequestBodyValidateNaturalParticipants();
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(validateParticipantDTO);
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase3(){

		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);

		ValidateParticipantDTO validateParticipantDTO =  getMockRequestBodyValidateNaturalParticipants();
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(validateParticipantDTO);
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase4(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase4());


		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase5(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase5());

		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase6(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase6());


		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithNaturalPersonDataTypeOkCase7(){
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(Mockito.anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(pbtqr002.executeSearchInHostByDocument(Mockito.anyString(),Mockito.anyString())).thenReturn(buildPersonHostDataResponseCase7());

		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNotNull(response);
		Assert.assertEquals(0, context.getAdviceList().size());

	}

	@Test
	public void executeLegalPersonTestOk() throws IOException {
		ValidateParticipantDTO request = getMockRequestBodyValidateLegalParticipants();
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("20123453922"));
		when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenReturn(buildRolByParticipantTypeResponse());
		Mockito.when(ksmkr002.executeKSMKR002(Mockito.anyList(),Mockito.anyString(),Mockito.anyString(),Mockito.anyObject())).thenReturn(getKSMKResponseOkMock());
		Mockito.when(rbvdr066.executeGetListBusinesses(Mockito.anyString(),Mockito.anyString())).thenReturn(MockDTO.getInstance().getListBusinessesOkMock());
		AgregarTerceroBO agregarTerceroBO = new AgregarTerceroBO();
		PayloadAgregarTerceroBO payloadAgregarTerceroBO = new PayloadAgregarTerceroBO();
		payloadAgregarTerceroBO.setCotizacion("cotizacion");
		agregarTerceroBO.setPayload(payloadAgregarTerceroBO);
		when(pisdr352.executeAddParticipantsService(anyObject(),anyString(),anyString(),anyString())).thenReturn(agregarTerceroBO);
		when(pbtqr002.executeSearchInHostByDocument(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeValidationWithWrongClientQuotationId(){
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("20123453922"));
		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenThrow(new BusinessException(PISDInsuranceErrors.PARAMETERS_INVALIDATE.getAdviceCode(), false, PISDInsuranceErrors.PARAMETERS_INVALIDATE.getMessage()));
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithWrongPlansConfiguredForProductModality(){

		Mockito.when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(Mockito.eq("0123489304"))).thenReturn(buildFindQuotationJoinByPolicyQuotaInternalId("71998384"));
		Mockito.when(pisdr012.executeGetParticipantRolesByCompany(anyMap())).thenThrow(new BusinessException(PISDInsuranceErrors.QUERY_EMPTY_RESULT.getAdviceCode(), false, PISDInsuranceErrors.QUERY_EMPTY_RESULT.getMessage()));

		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(getMockRequestBodyValidateNaturalParticipants());
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	@Test
	public void executeValidationWithWrongPersonTypeListFormatted(){

		ValidateParticipantDTO validateParticipantDTO = getMockRequestBodyValidateNaturalParticipants();
		validateParticipantDTO.getParticipants().get(0).getPerson().setPersonType("LEGAL");
		AgregarTerceroBO response =  rbvdR041.executeValidateAddParticipant(validateParticipantDTO);
		Assert.assertNull(response);
		Assert.assertEquals(1,this.context.getAdviceList().size());

	}

	public static ValidateParticipantDTO getMockRequestBodyValidateNaturalParticipants(){
		ValidateParticipantDTO requestBody = new ValidateParticipantDTO();
		requestBody.setQuotationId("0123489304");
		requestBody.setChannelId("PC");
		requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
		List<ParticipantsDTO> participantsList = new ArrayList<>();
		ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "78394872","NATURAL",false);
		ParticipantsDTO participant2 = buildParticipant("INSURED","DNI", "78394872","NATURAL",true);
		ParticipantsDTO participant3 = buildParticipant("CONTRACTOR","DNI", "78394872","NATURAL",true);
		participantsList.add(participant1);
		participantsList.add(participant2);
		participantsList.add(participant3);
		requestBody.setParticipants(participantsList);
		return requestBody;
	}

	public static ValidateParticipantDTO getMockRequestBodyValidateLegalParticipants(){
		ValidateParticipantDTO requestBody = new ValidateParticipantDTO();
		requestBody.setQuotationId("0123489304");
		requestBody.setChannelId("PC");
		requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
		List<ParticipantsDTO> participantsList = new ArrayList<>();
		ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","RUC", "20123453922","LEGAL", true);
		ParticipantsDTO participant2 = buildParticipant("INSURED","RUC", "20123453922","LEGAL", true);
		ParticipantsDTO participant3 = buildParticipant("CONTRACTOR","RUC", "20123453922","LEGAL",true);
		participantsList.add(participant1);
		participantsList.add(participant2);
		participantsList.add(participant3);
		requestBody.setParticipants(participantsList);
		return requestBody;
	}

	public static ParticipantsDTO buildParticipant(String typePerson, String typeDocument, String documentNumber, String personType, boolean contactDetailInd){
		ParticipantsDTO participant = new ParticipantsDTO();

		ParticipantTypeDTO participantType = new ParticipantTypeDTO();
		participantType.setId(typePerson);
		participant.setParticipantType(participantType);

		PersonDTO person = new PersonDTO();
		person.setCustomerId("97848900");
		person.setPersonType(personType);
		person.setFirstName("firstName");
		person.setMiddleName("middleName");
		person.setLastName("lastName");
		person.setSecondLastName("secondLastName");
		person.setBirthDate(new Date());

		GenderDTO gender = new GenderDTO();
		gender.setId("MALE");

		person.setGender(gender);

		if(personType.equals("LEGAL")){
			person.setDescription("legal description");
			person.setLegalName("legalName");
			person.setCreationDate(new Date());
		}

		participant.setPerson(person);

		List<IdentityDocumentDTO> identityDocumentList = new ArrayList<>();
		IdentityDocumentDTO identityDocument = new IdentityDocumentDTO();
		identityDocument.setValue(documentNumber);
		DocumentTypeDTO documentType = new DocumentTypeDTO();
		documentType.setId(typeDocument);
		identityDocument.setDocumentType(documentType);
		identityDocumentList.add(identityDocument);
		participant.setIdentityDocuments(identityDocumentList);
		if(contactDetailInd) {
			List<ContactDetailsDTO> contactDetailList = new ArrayList<>();
			ContactDetailsDTO contactDetail1 = new ContactDetailsDTO();
			contactDetail1.setContact("983949386");
			contactDetail1.setContactType("MOBILE_NUMBER");
			ContactDetailsDTO contactDetail2 = new ContactDetailsDTO();
			contactDetail2.setContact("example@bbva.com");
			contactDetail2.setContactType("EMAIL");
			ContactDetailsDTO contactDetail3 = new ContactDetailsDTO();
			contactDetail3.setContact("012243985");
			contactDetail3.setContactType("PHONE_NUMBER");
			contactDetailList.add(contactDetail1);
			contactDetailList.add(contactDetail2);
			contactDetailList.add(contactDetail3);
			participant.setContactDetails(contactDetailList);
		}

		AddressesDTO addressesDTO = new AddressesDTO();
		List<AddressesDTO> addressesDTOList = new ArrayList<>();
		addressesDTO.setFormattedAddress("CAL CIRCUNVALACION BRENA 200, AHH LOS NARANJOS");
		LocationDTO locationDTO = new LocationDTO();
		AddressComponentsDTO addressComponentsDTO1 = new AddressComponentsDTO();
		addressComponentsDTO1.setComponentTypes(Collections.singletonList("STREET"));
		addressComponentsDTO1.setName("CIRCUNVALACION BRENA");
		AddressComponentsDTO addressComponentsDTO2 = new AddressComponentsDTO();
		addressComponentsDTO2.setComponentTypes(Collections.singletonList("AAHH"));
		addressComponentsDTO2.setName("LOS NARANJOS");
		AddressComponentsDTO addressComponentsDTO3 = new AddressComponentsDTO();
		addressComponentsDTO3.setComponentTypes(Collections.singletonList("DEPARTMENT"));
		addressComponentsDTO3.setName("LIMA");
		AddressComponentsDTO addressComponentsDTO4 = new AddressComponentsDTO();
		addressComponentsDTO4.setComponentTypes(Collections.singletonList("PROVINCE"));
		addressComponentsDTO4.setName("LIMA");
		AddressComponentsDTO addressComponentsDTO5 = new AddressComponentsDTO();
		addressComponentsDTO5.setComponentTypes(Collections.singletonList("DISTRICT"));
		addressComponentsDTO5.setName("CHORRILLOS");
		AddressComponentsDTO addressComponentsDTO6 = new AddressComponentsDTO();
		addressComponentsDTO6.setComponentTypes(Collections.singletonList("EXTERIOR_NUMBER"));
		addressComponentsDTO6.setName("200");
		AddressComponentsDTO addressComponentsDTO7 = new AddressComponentsDTO();
		addressComponentsDTO7.setComponentTypes(Collections.singletonList("UBIGEO"));
		addressComponentsDTO7.setName("0101009");

		List<AddressComponentsDTO> addressComponentsDTOList = new ArrayList<>();
		addressComponentsDTOList.add(addressComponentsDTO1);
		addressComponentsDTOList.add(addressComponentsDTO2);
		addressComponentsDTOList.add(addressComponentsDTO3);
		addressComponentsDTOList.add(addressComponentsDTO4);
		addressComponentsDTOList.add(addressComponentsDTO5);
		addressComponentsDTOList.add(addressComponentsDTO6);
		addressComponentsDTOList.add(addressComponentsDTO7);
		locationDTO.setAddressComponent(addressComponentsDTOList);
		addressesDTO.setLocation(locationDTO);
		addressesDTOList.add(addressesDTO);
		participant.setAddresses(addressesDTOList);
		return participant;

	}

	private static QuotationJoinCustomerInformationDTO buildFindQuotationJoinByPolicyQuotaInternalId(String participantPersonalId){
		QuotationJoinCustomerInformationDTO quotationJoinInformation = new QuotationJoinCustomerInformationDTO();

		QuotationEntity quotationEntity = new QuotationEntity();
		QuotationModEntity quotationModEntity = new QuotationModEntity();
		InsuranceProductEntity insuranceProductEntity = new InsuranceProductEntity();
		InsuranceBusinessEntity insuranceBusinessEntity = new InsuranceBusinessEntity();

		quotationEntity.setInsuredCustomerName("customer name");
		quotationEntity.setClientLasName("client last name");
		quotationEntity.setInsuranceCompanyQuotaId("b5add021-a825-4ba1-a455-95e11015cff7");
		quotationEntity.setParticipantPersonalId(participantPersonalId);
		quotationModEntity.setContactEmailDesc("example@bbva");
		quotationModEntity.setCustomerPhoneDesc("CUSTOMER_PHONE_DESC");
		quotationModEntity.setInsuranceProductId(new BigDecimal(1));
		quotationModEntity.setInsuranceModalityType("02");

		insuranceProductEntity.setInsuranceProductType("830");

		insuranceBusinessEntity.setInsuranceBusinessName("VEHICULAR");

		quotationJoinInformation.setQuotation(quotationEntity);
		quotationJoinInformation.setQuotationMod(quotationModEntity);
		quotationJoinInformation.setInsuranceProduct(insuranceProductEntity);
		quotationJoinInformation.setInsuranceBusiness(insuranceBusinessEntity);
		return quotationJoinInformation;
	}


	private Map<String, Object> buildRolByParticipantTypeResponse(){
		Map<String, Object> line1 = new HashMap<>();
		line1.put("PARTICIPANT_ROLE_ID", new BigDecimal(7));
		line1.put("INSURANCE_COMPANY_ROLE_ID", "8");
		Map<String, Object> line2 = new HashMap<>();
		line2.put("PARTICIPANT_ROLE_ID",new BigDecimal(2));
		line2.put("INSURANCE_COMPANY_ROLE_ID","9");
		Map<String, Object> line3 = new HashMap<>();
		line3.put("PARTICIPANT_ROLE_ID",new BigDecimal(1));
		line3.put("INSURANCE_COMPANY_ROLE_ID","23");
		List<Map<String, Object>> listResponseDb = new ArrayList<>();
		listResponseDb.add(line1);
		listResponseDb.add(line2);
		listResponseDb.add(line3);

		return Collections.singletonMap("dtoInsurance",listResponseDb);

	}

	private static PEWUResponse buildPersonHostDataResponse(){
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
		pemsalwu.setNroext1("150");
		pemsalwu.setNroint1("201");
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

	private static PEWUResponse buildPersonHostDataResponseCase2(){
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
		pemsalwu.setApellim("X");
		pemsalwu.setNombres("NATIVIDAD");
		pemsalwu.setTitulo("ARQ.");
		pemsalwu.setEstadoc("S");
		pemsalwu.setSexo("");
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
		pemsalwu.setIdendi1("NA");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("NA");
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

	private static PEWUResponse buildPersonHostDataResponseCase4() {
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
		pemsalwu.setIdendi1("");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("NA");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("150");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static PEWUResponse buildPersonHostDataResponseCase5(){
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
		pemsalwu.setIdendi1("NA");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("AGR");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("150");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static PEWUResponse buildPersonHostDataResponseCase6(){
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
		pemsalwu.setIdendi2("NA");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("123");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static PEWUResponse buildPersonHostDataResponseCase7(){
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
		pemsalwu.setIdendi2("NA");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static PEWUResponse buildPersonHostDataResponseCase8(){
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
		pemsalwu.setIdendi1("");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("AGR");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("134");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static PEWUResponse buildPersonHostDataResponseCase9(){
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
		pemsalwu.setIdendi1("");
		pemsalwu.setNombdi1("LAS FLORES");
		pemsalwu.setIdendi2("AGR");
		pemsalwu.setNombdi2("TEST");
		pemsalwu.setDetalle("OCVALO");
		pemsalwu.setCodigod("01");
		pemsalwu.setCodigop("01");
		pemsalwu.setCodigdi("025");
		pemsalwu.setFedocac("2010-12-01");
		pemsalwu.setNroext1("");
		pemsalwu.setNroint1("201");
		pemsalwu.setManzana("");
		pemsalwu.setLote("");
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

	private static List<OutputDTO> getKSMKResponseOkMock(){
		List<OutputDTO> listDataOut = new ArrayList<>();
		OutputDTO outputDTO = new OutputDTO();
		outputDTO.setData("ksmkEncryptedDocument");
		listDataOut.add(outputDTO);
		return listDataOut;
	}

}
