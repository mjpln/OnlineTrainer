package com.knowology.km.AnswerFindClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.knowology.km.AnswerFindClient package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _FindAnswer_QNAME = new QName(
			"http://Services.AnswerFinderWebService.knowology.com/",
			"FindAnswer");
	private final static QName _GetAnswerContentByAbsParas_QNAME = new QName(
			"http://Services.AnswerFinderWebService.knowology.com/",
			"GetAnswerContentByAbsParas");
	private final static QName _GetAnswerContentByAbsParasResponse_QNAME = new QName(
			"http://Services.AnswerFinderWebService.knowology.com/",
			"GetAnswerContentByAbsParasResponse");
	private final static QName _FindAnswerResponse_QNAME = new QName(
			"http://Services.AnswerFinderWebService.knowology.com/",
			"FindAnswerResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.knowology.km.AnswerFindClient
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GetAnswerContentByAbsParas }
	 * 
	 */
	public GetAnswerContentByAbsParas createGetAnswerContentByAbsParas() {
		return new GetAnswerContentByAbsParas();
	}

	/**
	 * Create an instance of {@link GetAnswerContentByAbsParasResponse }
	 * 
	 */
	public GetAnswerContentByAbsParasResponse createGetAnswerContentByAbsParasResponse() {
		return new GetAnswerContentByAbsParasResponse();
	}

	/**
	 * Create an instance of {@link FindAnswerResponse }
	 * 
	 */
	public FindAnswerResponse createFindAnswerResponse() {
		return new FindAnswerResponse();
	}

	/**
	 * Create an instance of {@link FindAnswer }
	 * 
	 */
	public FindAnswer createFindAnswer() {
		return new FindAnswer();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link FindAnswer }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.AnswerFinderWebService.knowology.com/", name = "FindAnswer")
	public JAXBElement<FindAnswer> createFindAnswer(FindAnswer value) {
		return new JAXBElement<FindAnswer>(_FindAnswer_QNAME, FindAnswer.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetAnswerContentByAbsParas }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.AnswerFinderWebService.knowology.com/", name = "GetAnswerContentByAbsParas")
	public JAXBElement<GetAnswerContentByAbsParas> createGetAnswerContentByAbsParas(
			GetAnswerContentByAbsParas value) {
		return new JAXBElement<GetAnswerContentByAbsParas>(
				_GetAnswerContentByAbsParas_QNAME,
				GetAnswerContentByAbsParas.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetAnswerContentByAbsParasResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.AnswerFinderWebService.knowology.com/", name = "GetAnswerContentByAbsParasResponse")
	public JAXBElement<GetAnswerContentByAbsParasResponse> createGetAnswerContentByAbsParasResponse(
			GetAnswerContentByAbsParasResponse value) {
		return new JAXBElement<GetAnswerContentByAbsParasResponse>(
				_GetAnswerContentByAbsParasResponse_QNAME,
				GetAnswerContentByAbsParasResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link FindAnswerResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.AnswerFinderWebService.knowology.com/", name = "FindAnswerResponse")
	public JAXBElement<FindAnswerResponse> createFindAnswerResponse(
			FindAnswerResponse value) {
		return new JAXBElement<FindAnswerResponse>(_FindAnswerResponse_QNAME,
				FindAnswerResponse.class, null, value);
	}

}
