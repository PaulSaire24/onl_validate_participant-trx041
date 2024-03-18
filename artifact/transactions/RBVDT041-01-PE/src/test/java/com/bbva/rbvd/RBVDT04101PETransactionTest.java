package com.bbva.rbvd;

import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.TransactionParameter;
import com.bbva.elara.domain.transaction.request.TransactionRequest;
import com.bbva.elara.domain.transaction.request.body.CommonRequestBody;
import com.bbva.elara.domain.transaction.request.header.CommonRequestHeader;
import com.bbva.elara.test.osgi.DummyBundleContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import com.bbva.rbvd.dto.insrncsale.bo.emision.AgregarTerceroBO;
import com.bbva.rbvd.dto.insrncsale.bo.emision.PayloadAgregarTerceroBO;
import com.bbva.rbvd.dto.participant.request.ParticipantsDTO;
import com.bbva.rbvd.dto.participant.request.ParticipantTypeDTO;
import com.bbva.rbvd.dto.participant.request.PersonDTO;
import com.bbva.rbvd.dto.participant.request.IdentityDocumentDTO;
import com.bbva.rbvd.dto.participant.request.DocumentTypeDTO;
import com.bbva.rbvd.dto.participant.request.ContactDetailsDTO;
import com.bbva.rbvd.dto.participant.request.GenderDTO;
import com.bbva.rbvd.lib.r041.RBVDR041;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/elara-test.xml",
		"classpath:/META-INF/spring/RBVDT04101PETest.xml" })
public class RBVDT04101PETransactionTest {

	@Autowired
	private RBVDT04101PETransaction transaction;

	@Resource(name = "dummyBundleContext")
	private DummyBundleContext bundleContext;

	@Mock
	private CommonRequestHeader header;

	@Mock
	private TransactionRequest transactionRequest;

	@Resource(name = "rbvdR041")
	private RBVDR041 rbvdr041;

	@Before
	public void initializeClass() throws Exception {
		// Initializing mocks
		MockitoAnnotations.initMocks(this);
		// Start BundleContext
		this.transaction.start(bundleContext);
		// Setting Context
		this.transaction.setContext(new Context());
		// Set Body
		CommonRequestBody commonRequestBody = new CommonRequestBody();
		commonRequestBody.setTransactionParameters(new ArrayList<>());
		this.transactionRequest.setBody(commonRequestBody);
		// Set Header Mock
		this.transactionRequest.setHeader(header);
		// Set TransactionRequest
		this.transaction.getContext().setTransactionRequest(transactionRequest);
	}


	@Test
	public void executeTestOk(){
		AgregarTerceroBO res = new AgregarTerceroBO();
		res.setPayload(new PayloadAgregarTerceroBO());
		Mockito.when(rbvdr041.executeValidateAddParticipant(Mockito.anyObject())).thenReturn(res);
		this.transaction.execute();
		Assert.assertEquals(Severity.OK.getValue(),this.transaction.getContext().getSeverity().getValue());
		Assert.assertNotNull(this.transaction);
	}

	@Test
	public void executeTestError(){
		Mockito.when(rbvdr041.executeValidateAddParticipant(Mockito.anyObject())).thenReturn(null);
		this.transaction.execute();
		assertEquals(Severity.ENR.getValue(), this.transaction.getSeverity().getValue());
	}


	// Add Parameter to Transaction
	private void addParameter(final String parameter, final Object value) {
		final TransactionParameter tParameter = new TransactionParameter(parameter, value);
		transaction.getContext().getParameterList().put(parameter, tParameter);
	}

	// Get Parameter from Transaction
	private Object getParameter(final String parameter) {
		final TransactionParameter param = transaction.getContext().getParameterList().get(parameter);
		return param != null ? param.getValue() : null;
	}
	public static ParticipantsDTO buildParticipant(){
		ParticipantsDTO participant = new ParticipantsDTO();

		ParticipantTypeDTO participantType = new ParticipantTypeDTO();
		participantType.setId("typePerson");
		participant.setParticipantType(participantType);

		PersonDTO person = new PersonDTO();
		person.setCustomerId("97848900");
		person.setPersonType("personType");
		person.setFirstName("firstName");
		person.setMiddleName("middleName");
		person.setLastName("lastName");
		person.setSecondLastName("secondLastName");
		person.setBirthDate(new Date());

		GenderDTO gender = new GenderDTO();
		gender.setId("MALE");

		person.setGender(gender);


		person.setDescription("legal description");
		person.setLegalName("legalName");
		person.setCreationDate(new Date());


		participant.setPerson(person);

		List<IdentityDocumentDTO> identityDocumentList = new ArrayList<>();
		IdentityDocumentDTO identityDocument = new IdentityDocumentDTO();
		identityDocument.setValue("documentNumber");
		DocumentTypeDTO documentType = new DocumentTypeDTO();
		documentType.setId("typeDocument");
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
}
