package com.knowology.km.UIAOInterfaceWS;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * <p>
 * An example of how this class may be used:
 * 
 * <pre>
 * UIAOInterfaceWebService service = new UIAOInterfaceWebService();
 * FindAnswerWSDelegate portType = service.getUIAOInterfaceWS();
 * portType.findAnswer(...);
 * </pre>
 * 
 * </p>
 * 
 */
@WebServiceClient(name = "UIAOInterfaceWebService", targetNamespace = "http://Services.UIAOInterfaceWebService.knowology.com/", wsdlLocation = "http://222.186.101.212:8282/UIAOInterfaceWebService/UIAOInterfaceWS?wsdl")
public class UIAOInterfaceWebService extends Service {

	private final static URL UIAOINTERFACEWEBSERVICE_WSDL_LOCATION;
	private final static Logger logger = Logger
			.getLogger(com.knowology.km.UIAOInterfaceWS.UIAOInterfaceWebService.class
					.getName());

	static {
		URL url = null;
		try {
			URL baseUrl;
			baseUrl = com.knowology.km.UIAOInterfaceWS.UIAOInterfaceWebService.class
					.getResource(".");
			url = new URL(baseUrl,
					"http://222.186.101.212:8282/UIAOInterfaceWebService/UIAOInterfaceWS?wsdl");
		} catch (MalformedURLException e) {
			logger
					.warning("Failed to create URL for the wsdl Location: 'http://222.186.101.212:8282/UIAOInterfaceWebService/UIAOInterfaceWS?wsdl', retrying as a local file");
			logger.warning(e.getMessage());
		}
		UIAOINTERFACEWEBSERVICE_WSDL_LOCATION = url;
	}

	public UIAOInterfaceWebService(URL wsdlLocation, QName serviceName) {
		super(wsdlLocation, serviceName);
	}

	public UIAOInterfaceWebService() {
		super(UIAOINTERFACEWEBSERVICE_WSDL_LOCATION, new QName(
				"http://Services.UIAOInterfaceWebService.knowology.com/",
				"UIAOInterfaceWebService"));
	}

	/**
	 * 
	 * @return returns FindAnswerWSDelegate
	 */
	@WebEndpoint(name = "UIAOInterfaceWS")
	public FindAnswerWSDelegate getUIAOInterfaceWS() {
		return super.getPort(new QName(
				"http://Services.UIAOInterfaceWebService.knowology.com/",
				"UIAOInterfaceWS"), FindAnswerWSDelegate.class);
	}

}
