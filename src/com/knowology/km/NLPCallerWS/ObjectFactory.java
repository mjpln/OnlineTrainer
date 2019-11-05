package com.knowology.km.NLPCallerWS;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.knowology.km.NLPCallerWS package.
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

	private final static QName _InitKB4NLP_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "InitKB4NLP");
	private final static QName _UpdateKBResponse_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "UpdateKBResponse");
	private final static QName _DetailAnalyzeResponse_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/",
			"DetailAnalyzeResponse");
	private final static QName _UpdateKB_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "UpdateKB");
	private final static QName _KAnalyze_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "KAnalyze");
	private final static QName _InitKB4NLPResponse_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/",
			"InitKB4NLPResponse");
	private final static QName _OldDetailAnalyze_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "OldDetailAnalyze");
	private final static QName _OldDetailAnalyzeResponse_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/",
			"OldDetailAnalyzeResponse");
	private final static QName _DetailAnalyze_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "DetailAnalyze");
	private final static QName _KAnalyzeResponse_QNAME = new QName(
			"http://Services.NLPWebService.knowology.com/", "KAnalyzeResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.knowology.km.NLPCallerWS
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link UpdateKBResponse }
	 * 
	 */
	public UpdateKBResponse createUpdateKBResponse() {
		return new UpdateKBResponse();
	}

	/**
	 * Create an instance of {@link KAnalyze }
	 * 
	 */
	public KAnalyze createKAnalyze() {
		return new KAnalyze();
	}

	/**
	 * Create an instance of {@link UpdateKB }
	 * 
	 */
	public UpdateKB createUpdateKB() {
		return new UpdateKB();
	}

	/**
	 * Create an instance of {@link OldDetailAnalyze }
	 * 
	 */
	public OldDetailAnalyze createOldDetailAnalyze() {
		return new OldDetailAnalyze();
	}

	/**
	 * Create an instance of {@link DetailAnalyzeResponse }
	 * 
	 */
	public DetailAnalyzeResponse createDetailAnalyzeResponse() {
		return new DetailAnalyzeResponse();
	}

	/**
	 * Create an instance of {@link InitKB4NLP }
	 * 
	 */
	public InitKB4NLP createInitKB4NLP() {
		return new InitKB4NLP();
	}

	/**
	 * Create an instance of {@link InitKB4NLPResponse }
	 * 
	 */
	public InitKB4NLPResponse createInitKB4NLPResponse() {
		return new InitKB4NLPResponse();
	}

	/**
	 * Create an instance of {@link KAnalyzeResponse }
	 * 
	 */
	public KAnalyzeResponse createKAnalyzeResponse() {
		return new KAnalyzeResponse();
	}

	/**
	 * Create an instance of {@link DetailAnalyze }
	 * 
	 */
	public DetailAnalyze createDetailAnalyze() {
		return new DetailAnalyze();
	}

	/**
	 * Create an instance of {@link OldDetailAnalyzeResponse }
	 * 
	 */
	public OldDetailAnalyzeResponse createOldDetailAnalyzeResponse() {
		return new OldDetailAnalyzeResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link InitKB4NLP }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "InitKB4NLP")
	public JAXBElement<InitKB4NLP> createInitKB4NLP(InitKB4NLP value) {
		return new JAXBElement<InitKB4NLP>(_InitKB4NLP_QNAME, InitKB4NLP.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link UpdateKBResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "UpdateKBResponse")
	public JAXBElement<UpdateKBResponse> createUpdateKBResponse(
			UpdateKBResponse value) {
		return new JAXBElement<UpdateKBResponse>(_UpdateKBResponse_QNAME,
				UpdateKBResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link DetailAnalyzeResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "DetailAnalyzeResponse")
	public JAXBElement<DetailAnalyzeResponse> createDetailAnalyzeResponse(
			DetailAnalyzeResponse value) {
		return new JAXBElement<DetailAnalyzeResponse>(
				_DetailAnalyzeResponse_QNAME, DetailAnalyzeResponse.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link UpdateKB }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "UpdateKB")
	public JAXBElement<UpdateKB> createUpdateKB(UpdateKB value) {
		return new JAXBElement<UpdateKB>(_UpdateKB_QNAME, UpdateKB.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link KAnalyze }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "KAnalyze")
	public JAXBElement<KAnalyze> createKAnalyze(KAnalyze value) {
		return new JAXBElement<KAnalyze>(_KAnalyze_QNAME, KAnalyze.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link InitKB4NLPResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "InitKB4NLPResponse")
	public JAXBElement<InitKB4NLPResponse> createInitKB4NLPResponse(
			InitKB4NLPResponse value) {
		return new JAXBElement<InitKB4NLPResponse>(_InitKB4NLPResponse_QNAME,
				InitKB4NLPResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link OldDetailAnalyze }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "OldDetailAnalyze")
	public JAXBElement<OldDetailAnalyze> createOldDetailAnalyze(
			OldDetailAnalyze value) {
		return new JAXBElement<OldDetailAnalyze>(_OldDetailAnalyze_QNAME,
				OldDetailAnalyze.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link OldDetailAnalyzeResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "OldDetailAnalyzeResponse")
	public JAXBElement<OldDetailAnalyzeResponse> createOldDetailAnalyzeResponse(
			OldDetailAnalyzeResponse value) {
		return new JAXBElement<OldDetailAnalyzeResponse>(
				_OldDetailAnalyzeResponse_QNAME,
				OldDetailAnalyzeResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link DetailAnalyze }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "DetailAnalyze")
	public JAXBElement<DetailAnalyze> createDetailAnalyze(DetailAnalyze value) {
		return new JAXBElement<DetailAnalyze>(_DetailAnalyze_QNAME,
				DetailAnalyze.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link KAnalyzeResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://Services.NLPWebService.knowology.com/", name = "KAnalyzeResponse")
	public JAXBElement<KAnalyzeResponse> createKAnalyzeResponse(
			KAnalyzeResponse value) {
		return new JAXBElement<KAnalyzeResponse>(_KAnalyzeResponse_QNAME,
				KAnalyzeResponse.class, null, value);
	}

}
