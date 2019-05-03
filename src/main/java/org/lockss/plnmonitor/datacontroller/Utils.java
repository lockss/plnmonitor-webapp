package org.lockss.plnmonitor.datacontroller;


import java.io.IOException;
import java.sql.SQLException;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;



public class Utils {
	
	public static Logger log = Logger.getLogger(Utils.class);
	
	public static String CONFIG_PATH = null;
	
	public static String authServer = null;
	
	public static String UPLOAD_DIR = null;
	
	public static final String MODS_NAMESPACE_URI = "http://www.loc.gov/mods/v3";
	public static final String XML_NAMESPACE_URI = "http://www.w3.org/XML/1998/namespace";
	private static Object classLock = Utils.class;

//	public static final String ELEMENT_REQUEST = "request";
//	public static final String ELEMENT_RESPONSE = "response";
//	public static final String ATTRIBUTE_TYPE = "type";
//	public static final String ATTRIBUTE_VALUE_CREATE_ITEM = "create_item";
//	public static final String ATTRIBUTE_VALUE_SEARCH_DOUBLE = "search_doubles";

}
