package com.bbva.rbvd.lib.r041;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW4;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurancedao.entities.InsuranceBusinessEntity;
import com.bbva.pisd.dto.insurancedao.entities.InsuranceProductEntity;
import com.bbva.pisd.dto.insurancedao.entities.QuotationEntity;
import com.bbva.pisd.dto.insurancedao.entities.QuotationModEntity;
import com.bbva.pisd.dto.insurancedao.join.QuotationJoinCustomerInformationDTO;
import com.bbva.pisd.lib.r601.PISDR601;
import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantsDTO;
import com.bbva.rbvd.dto.insurance.commons.ValidateParticipantDTO;
import com.bbva.rbvd.dto.insurance.commons.ParticipantTypeDTO;
import com.bbva.rbvd.dto.insurance.commons.PersonDTO;
import com.bbva.rbvd.dto.insurance.commons.IdentityDocumentDTO;
import com.bbva.rbvd.dto.insurance.commons.DocumentTypeDTO;
import com.bbva.rbvd.dto.insurance.commons.ContactDetailsDTO;
import com.bbva.rbvd.dto.insurance.commons.GenderDTO;
import com.bbva.rbvd.lib.r048.RBVDR048;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.aop.framework.Advised;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Collections;

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

	@Resource(name = "rbvdR041")
	private RBVDR041 rbvdR041;
	@Resource(name = "rbvdR048")
	private RBVDR048 rbvdr048;

	@Resource(name = "pisdR601")
	private PISDR601 pisdr601;
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
	public void executeTestOk(){
		QuotationJoinCustomerInformationDTO quotationJoinCustomerInformation = new QuotationJoinCustomerInformationDTO();
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

		ValidateParticipantDTO request = getMockRequestBodyValidateLegalParticipants();
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
		when(rbvdr048.executeAddParticipantsDynamicLife(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
		when(rbvdr048.executeGetCustomerService(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		when(rbvdr048.getDataInsuredBD(anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeTestOkAnotherTwo(){
		QuotationJoinCustomerInformationDTO quotationJoinCustomerInformation = new QuotationJoinCustomerInformationDTO();
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

		ValidateParticipantDTO request = getMockRequestBodyValidateLegalParticipantsTwo();
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
		when(rbvdr048.executeAddParticipantsDynamicLife(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
		when(rbvdr048.getDataInsuredBD(anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
		when(rbvdr048.executeGetCustomerService(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}

	@Test
	public void executeTestOkAnotherOne(){
		QuotationJoinCustomerInformationDTO quotationJoinCustomerInformation = new QuotationJoinCustomerInformationDTO();
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

		ValidateParticipantDTO request = getMockRequestBodyValidateLegalParticipantsOne();
		when(this.applicationConfigurationService.getProperty(anyString())).thenReturn("L");
		when(pisdr601.executeFindQuotationJoinByPolicyQuotaInternalId(anyString())).thenReturn(quotationJoinCustomerInformation);
		when(rbvdr048.executeAddParticipantsDynamicLife(anyObject(),anyString(),anyString(),anyString())).thenReturn(new AgregarTerceroBO());
		when(rbvdr048.executeGetCustomerService(anyString(),anyString())).thenReturn(buildPersonHostDataResponseCase3());
		when(rbvdr048.getDataInsuredBD(anyString(),anyString(),anyString())).thenReturn(responseInsuredBD);
		AgregarTerceroBO response = rbvdR041.executeValidateAddParticipant(request);
		Assert.assertNotNull(response);
		Assert.assertEquals(0,this.context.getAdviceList().size());
	}


	public static ValidateParticipantDTO getMockRequestBodyValidateLegalParticipants(){
		ValidateParticipantDTO requestBody = new ValidateParticipantDTO();
		requestBody.setQuotationId("0123489304");
		requestBody.setChannelId("PC");
		requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
		List<ParticipantsDTO> participantsList = new ArrayList<>();
		ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "46716129","LEGAL", true);
		ParticipantsDTO participant2 = buildParticipant("CONTRACTOR","DNI", "45093558","LEGAL", true);
		ParticipantsDTO participant3 = buildParticipant("INSURED","DNI", "00002023","LEGAL",false);
		participantsList.add(participant1);
		participantsList.add(participant2);
		participantsList.add(participant3);
		requestBody.setParticipants(participantsList);
		return requestBody;
	}
	public static ValidateParticipantDTO getMockRequestBodyValidateLegalParticipantsTwo(){
		ValidateParticipantDTO requestBody = new ValidateParticipantDTO();
		requestBody.setQuotationId("0123489304");
		requestBody.setChannelId("PC");
		requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
		List<ParticipantsDTO> participantsList = new ArrayList<>();
		ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "46716129","LEGAL", true);
		ParticipantsDTO participant3 = buildParticipant("INSURED","DNI", "00002023","LEGAL",false);
		participantsList.add(participant1);
		participantsList.add(participant3);
		requestBody.setParticipants(participantsList);
		return requestBody;
	}

	public static ValidateParticipantDTO getMockRequestBodyValidateLegalParticipantsOne(){
		ValidateParticipantDTO requestBody = new ValidateParticipantDTO();
		requestBody.setQuotationId("0123489304");
		requestBody.setChannelId("PC");
		requestBody.setTraceId("c05ed2bd-1a7c-47ca-b7c9-fc639f47790a");
		List<ParticipantsDTO> participantsList = new ArrayList<>();
		ParticipantsDTO participant1 = buildParticipant("PAYMENT_MANAGER","DNI", "46716129","LEGAL", true);
		participantsList.add(participant1);
		requestBody.setParticipants(participantsList);
		return requestBody;
	}

	public static ParticipantsDTO buildParticipant(String typePerson, String typeDocument, String documentNumber, String personType, boolean isClient){
		ParticipantsDTO participant = new ParticipantsDTO();

		ParticipantTypeDTO participantType = new ParticipantTypeDTO();
		participantType.setId(typePerson);
		participant.setParticipantType(participantType);

		PersonDTO person = new PersonDTO();
		if(isClient){
			person.setCustomerId("97848900");
		}else {
			person.setCustomerId("PE0011000011700");
		}
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


}
