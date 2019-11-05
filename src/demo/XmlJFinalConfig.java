package demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jfinal.config.JFinalConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.util.XMLResourceBundleControl;

public abstract class XmlJFinalConfig{
	private Properties xmlProperties;
	
	/**
	 * Load property file
	 * Example: loadPropertyFile("db_username_pass.txt");
	 * @param file the file in WEB-INF directory
	 */
	public Properties loadPropertyFile(String file) {
		if (StrKit.isBlank(file))
			throw new IllegalArgumentException("Parameter of file can not be blank");
		if (file.contains(".."))
			throw new IllegalArgumentException("Parameter of file can not contains \"..\"");
		
		if(XMLResourceBundleControl.isXML && file.endsWith(".properties")){
			file = file.replace("properties", "xml");
		}
		InputStream inputStream = null;
		String fullFile;	// String fullFile = PathUtil.getWebRootPath() + file;
		if (file.startsWith(File.separator))
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + file;
		else
			fullFile = PathKit.getWebRootPath() + File.separator + "WEB-INF" + File.separator + file;
		
		try {
			inputStream = new FileInputStream(new File(fullFile));
			Properties p = new Properties();
			if(XMLResourceBundleControl.isXML){
				p.loadFromXML(inputStream);
			}else{
				p.load(inputStream);
			}
			xmlProperties = p;
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Properties file not found: " + fullFile);
		} catch (IOException e) {
			throw new IllegalArgumentException("Properties file can not be loading: " + fullFile);
		}
		finally {
			try {if (inputStream != null) inputStream.close();} catch (IOException e) {e.printStackTrace();}
		}
		if (xmlProperties == null)
			throw new RuntimeException("Properties file loading failed: " + fullFile);
		return xmlProperties;
	}
	
	public String getProperty(String key) {
		checkPropertyLoading();
		return xmlProperties.getProperty(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		checkPropertyLoading();
		return xmlProperties.getProperty(key, defaultValue);
	}
	
	public Integer getPropertyToInt(String key) {
		checkPropertyLoading();
		Integer resultInt = null;
		String resultStr = xmlProperties.getProperty(key);
		if (resultStr != null)
			resultInt =  Integer.parseInt(resultStr);
		return resultInt;
	}
	
	public Integer getPropertyToInt(String key, Integer defaultValue) {
		Integer result = getPropertyToInt(key);
		return result != null ? result : defaultValue;
	}
	
	public Boolean getPropertyToBoolean(String key) {
		checkPropertyLoading();
		String resultStr = xmlProperties.getProperty(key);
		Boolean resultBool = null;
		if (resultStr != null) {
			if (resultStr.trim().equalsIgnoreCase("true"))
				resultBool = true;
			else if (resultStr.trim().equalsIgnoreCase("false"))
				resultBool = false;
		}
		return resultBool;
	}
	
	public Boolean getPropertyToBoolean(String key, boolean defaultValue) {
		Boolean result = getPropertyToBoolean(key);
		return result != null ? result : defaultValue;
	}
	
	private void checkPropertyLoading() {
		if (xmlProperties == null)
			throw new RuntimeException("You must load properties file by invoking loadPropertyFile(String) method in configConstant(Constants) method before.");
	}
	
}
