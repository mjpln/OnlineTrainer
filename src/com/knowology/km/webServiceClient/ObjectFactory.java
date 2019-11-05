package com.knowology.km.webServiceClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.knowology.km.webServiceClient package.
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

	private final static QName _CreateQueue_QNAME = new QName(
			"http://service.DQ.knowology.com/", "createQueue");
	private final static QName _CreateQueueResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "createQueueResponse");
	private final static QName _SendMsg_QNAME = new QName(
			"http://service.DQ.knowology.com/", "sendMsg");
	private final static QName _GetIpResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getIpResponse");
	private final static QName _SetIp_QNAME = new QName(
			"http://service.DQ.knowology.com/", "setIp");
	private final static QName _GetMsgResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getMsgResponse");
	private final static QName _SetNameResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "setNameResponse");
	private final static QName _GetIp_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getIp");
	private final static QName _GetMsgById_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getMsgById");
	private final static QName _SetIpResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "setIpResponse");
	private final static QName _GetName_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getName");
	private final static QName _GetNameResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getNameResponse");
	private final static QName _SendMsgResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "sendMsgResponse");
	private final static QName _GetMsg_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getMsg");
	private final static QName _SetName_QNAME = new QName(
			"http://service.DQ.knowology.com/", "setName");
	private final static QName _GetMsgByIdResponse_QNAME = new QName(
			"http://service.DQ.knowology.com/", "getMsgByIdResponse");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.knowology.km.webServiceClient
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link GetMsgByIdResponse }
	 * 
	 */
	public GetMsgByIdResponse createGetMsgByIdResponse() {
		return new GetMsgByIdResponse();
	}

	/**
	 * Create an instance of {@link GetName }
	 * 
	 */
	public GetName createGetName() {
		return new GetName();
	}

	/**
	 * Create an instance of {@link SetName }
	 * 
	 */
	public SetName createSetName() {
		return new SetName();
	}

	/**
	 * Create an instance of {@link MsgQueue }
	 * 
	 */
	public MsgQueue createMsgQueue() {
		return new MsgQueue();
	}

	/**
	 * Create an instance of {@link CreateQueue }
	 * 
	 */
	public CreateQueue createCreateQueue() {
		return new CreateQueue();
	}

	/**
	 * Create an instance of {@link GetMsgById }
	 * 
	 */
	public GetMsgById createGetMsgById() {
		return new GetMsgById();
	}

	/**
	 * Create an instance of {@link SendMsg }
	 * 
	 */
	public SendMsg createSendMsg() {
		return new SendMsg();
	}

	/**
	 * Create an instance of {@link GetMsgResponse }
	 * 
	 */
	public GetMsgResponse createGetMsgResponse() {
		return new GetMsgResponse();
	}

	/**
	 * Create an instance of {@link SendMsgResponse }
	 * 
	 */
	public SendMsgResponse createSendMsgResponse() {
		return new SendMsgResponse();
	}

	/**
	 * Create an instance of {@link GetIpResponse }
	 * 
	 */
	public GetIpResponse createGetIpResponse() {
		return new GetIpResponse();
	}

	/**
	 * Create an instance of {@link SetIp }
	 * 
	 */
	public SetIp createSetIp() {
		return new SetIp();
	}

	/**
	 * Create an instance of {@link CreateQueueResponse }
	 * 
	 */
	public CreateQueueResponse createCreateQueueResponse() {
		return new CreateQueueResponse();
	}

	/**
	 * Create an instance of {@link GetMsg }
	 * 
	 */
	public GetMsg createGetMsg() {
		return new GetMsg();
	}

	/**
	 * Create an instance of {@link SetNameResponse }
	 * 
	 */
	public SetNameResponse createSetNameResponse() {
		return new SetNameResponse();
	}

	/**
	 * Create an instance of {@link GetIp }
	 * 
	 */
	public GetIp createGetIp() {
		return new GetIp();
	}

	/**
	 * Create an instance of {@link SetIpResponse }
	 * 
	 */
	public SetIpResponse createSetIpResponse() {
		return new SetIpResponse();
	}

	/**
	 * Create an instance of {@link GetNameResponse }
	 * 
	 */
	public GetNameResponse createGetNameResponse() {
		return new GetNameResponse();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link CreateQueue }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "createQueue")
	public JAXBElement<CreateQueue> createCreateQueue(CreateQueue value) {
		return new JAXBElement<CreateQueue>(_CreateQueue_QNAME,
				CreateQueue.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link CreateQueueResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "createQueueResponse")
	public JAXBElement<CreateQueueResponse> createCreateQueueResponse(
			CreateQueueResponse value) {
		return new JAXBElement<CreateQueueResponse>(_CreateQueueResponse_QNAME,
				CreateQueueResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendMsg }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "sendMsg")
	public JAXBElement<SendMsg> createSendMsg(SendMsg value) {
		return new JAXBElement<SendMsg>(_SendMsg_QNAME, SendMsg.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetIpResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getIpResponse")
	public JAXBElement<GetIpResponse> createGetIpResponse(GetIpResponse value) {
		return new JAXBElement<GetIpResponse>(_GetIpResponse_QNAME,
				GetIpResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SetIp }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "setIp")
	public JAXBElement<SetIp> createSetIp(SetIp value) {
		return new JAXBElement<SetIp>(_SetIp_QNAME, SetIp.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetMsgResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getMsgResponse")
	public JAXBElement<GetMsgResponse> createGetMsgResponse(GetMsgResponse value) {
		return new JAXBElement<GetMsgResponse>(_GetMsgResponse_QNAME,
				GetMsgResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SetNameResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "setNameResponse")
	public JAXBElement<SetNameResponse> createSetNameResponse(
			SetNameResponse value) {
		return new JAXBElement<SetNameResponse>(_SetNameResponse_QNAME,
				SetNameResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetIp }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getIp")
	public JAXBElement<GetIp> createGetIp(GetIp value) {
		return new JAXBElement<GetIp>(_GetIp_QNAME, GetIp.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetMsgById }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getMsgById")
	public JAXBElement<GetMsgById> createGetMsgById(GetMsgById value) {
		return new JAXBElement<GetMsgById>(_GetMsgById_QNAME, GetMsgById.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SetIpResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "setIpResponse")
	public JAXBElement<SetIpResponse> createSetIpResponse(SetIpResponse value) {
		return new JAXBElement<SetIpResponse>(_SetIpResponse_QNAME,
				SetIpResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetName }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getName")
	public JAXBElement<GetName> createGetName(GetName value) {
		return new JAXBElement<GetName>(_GetName_QNAME, GetName.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetNameResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getNameResponse")
	public JAXBElement<GetNameResponse> createGetNameResponse(
			GetNameResponse value) {
		return new JAXBElement<GetNameResponse>(_GetNameResponse_QNAME,
				GetNameResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SendMsgResponse }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "sendMsgResponse")
	public JAXBElement<SendMsgResponse> createSendMsgResponse(
			SendMsgResponse value) {
		return new JAXBElement<SendMsgResponse>(_SendMsgResponse_QNAME,
				SendMsgResponse.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link GetMsg }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getMsg")
	public JAXBElement<GetMsg> createGetMsg(GetMsg value) {
		return new JAXBElement<GetMsg>(_GetMsg_QNAME, GetMsg.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link SetName }{@code
	 * >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "setName")
	public JAXBElement<SetName> createSetName(SetName value) {
		return new JAXBElement<SetName>(_SetName_QNAME, SetName.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link GetMsgByIdResponse }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://service.DQ.knowology.com/", name = "getMsgByIdResponse")
	public JAXBElement<GetMsgByIdResponse> createGetMsgByIdResponse(
			GetMsgByIdResponse value) {
		return new JAXBElement<GetMsgByIdResponse>(_GetMsgByIdResponse_QNAME,
				GetMsgByIdResponse.class, null, value);
	}

}
