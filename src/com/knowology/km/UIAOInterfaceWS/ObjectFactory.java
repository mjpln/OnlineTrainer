package com.knowology.km.UIAOInterfaceWS;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.knowology.km.UIAOInterfaceWS package.
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
			"http://Services.UIAOInterfaceWebService.knowology.com/",
			"FindAnswer");
	private final static QName _NLPAnalyze_QNAME = new QName(
			"http://Services.UIAOInterfaceWebService.knowology.com/",
			"NLPAnalyze");
	private final static QName _FindAnswerResponse_QNAME = new QName(
			"http://Services.UIAOInterfaceWebService.knowology.com/",
			"FindAnswerResponse");
	private final static QName _NLPAnalyzeResponse_QNAME = new QName(
			"http://Services.UIAOInterfaceWebService.knowology.com/",
			"NLPAnalyzeResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.knowology.km.UIAOInterfaceWS
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link FindAnswer }
	 * 
	 */
	public FindAnswer createFindAnswer() {
		return new FindAnswer();
	}

	/**
	 * Create an instance of {@link NLPAnalyzeResponse }
	 * 
	 */
	public NLPAnalyzeResponse createNLPAnalyzeResponse() {
		return new NLPAnalyzeResponse();
	}

	/**
	 * Create an instance of {@link FindAnswerResponse }
	 * 
	 */
	public FindAnswerResponse createFindAnswerResponse() {
		return new FindAnswerResponse();
	}

	/**
	 * Create an instance of {@link NLPAnalyze }
	 * 
	 */
	public NLPAnalyze createNLPAnalyze() {
		return new NLPAnalyze();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link FindAnswer }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.UIAOInterfaceWebService.knowology.com/", name = "FindAnswer")
	public JAXBElement<FindAnswer> createFindAnswer(FindAnswer value) {
		return new JAXBElement<FindAnswer>(_FindAnswer_QNAME, FindAnswer.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link NLPAnalyze }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.UIAOInterfaceWebService.knowology.com/", name = "NLPAnalyze")
	public JAXBElement<NLPAnalyze> createNLPAnalyze(NLPAnalyze value) {
		return new JAXBElement<NLPAnalyze>(_NLPAnalyze_QNAME, NLPAnalyze.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link FindAnswerResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.UIAOInterfaceWebService.knowology.com/", name = "FindAnswerResponse")
	public JAXBElement<FindAnswerResponse> createFindAnswerResponse(
			FindAnswerResponse value) {
		return new JAXBElement<FindAnswerResponse>(_FindAnswerResponse_QNAME,
				FindAnswerResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link NLPAnalyzeResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.UIAOInterfaceWebService.knowology.com/", name = "NLPAnalyzeResponse")
	public JAXBElement<NLPAnalyzeResponse> createNLPAnalyzeResponse(
			NLPAnalyzeResponse value) {
		return new JAXBElement<NLPAnalyzeResponse>(_NLPAnalyzeResponse_QNAME,
				NLPAnalyzeResponse.class, null, value);
	}

}
