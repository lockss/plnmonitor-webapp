package be.ulb.plnmonitor.datacontroller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;
import org.apache.wicket.request.Request;
import org.apache.wicket.protocol.http.WebSession;

import be.ulb.plnmonitor.ConfigurationManager;
import be.ulb.plnmonitor.SSLSocketException;
import be.ulb.plnmonitor.object.User;
import be.ulb.plnmonitor.rdbms.DatabaseManager;

public final class UserSession extends WebSession{

    public static Logger getLog() {
		return log;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user;
    private static final Logger log = Logger.getLogger(UserSession.class);

    /** Database connection */
    private transient Connection connection;

    private transient SSLSocketFactory factory;
    private transient SSLSocket sslConnection;
    
    public UserSession(Request request){
        super(request);
    	try {
			connection = DatabaseManager.getConnection();
			connection.setAutoCommit(true);
			log.info("connection open");
			
	/*		// CREATE SSL SOCKET
		    try {
				SSLContext ctx;
				KeyManagerFactory kmf;
				KeyStore ks1;
				TrustManagerFactory tmf;
				KeyStore ks2;
				char[] passphrase = "passphrase".toCharArray();
	
				ctx = SSLContext.getInstance("TLS");
				kmf = KeyManagerFactory.getInstance("SunX509");
				ks1 = KeyStore.getInstance("JKS");
				ks1.load(new FileInputStream(ConfigurationManager.getProperty("ssl.path")+"/testkeys"), passphrase);
				kmf.init(ks1, passphrase);
				
				tmf = TrustManagerFactory.getInstance("SunX509","SunJSSE");
				ks2 = KeyStore.getInstance("JKS");
				ks2.load(new FileInputStream(ConfigurationManager.getProperty("ssl.path")+"/cacerts"),null);
				tmf.init(ks2);
				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

				factory = ctx.getSocketFactory();
		    }
		    catch (NoSuchAlgorithmException e) {
		    	throw new SSLSocketException(e.getMessage());
			} catch (CertificateException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (FileNotFoundException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (IOException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (KeyStoreException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (NoSuchProviderException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (UnrecoverableKeyException e) {
				throw new SSLSocketException(e.getMessage());
			} catch (KeyManagementException e) {
				throw new SSLSocketException(e.getMessage());
			}*/
//			createNewSSLSocket();
//		} catch (SQLException e) {
//			log.fatal(e.getMessage());
		} catch (Exception e2) {
			log.fatal(e2.getMessage());
			log.fatal(e2.toString());
			
			e2.printStackTrace();
		}
    }

    /**
     * Get the database connection associated with the context
     * 
     * @return the database connection
     */
    public Connection getDBConnection()
    {
    	try {
	    	if(connection==null || connection.isClosed()){
	    		log.debug("new connection");
				connection = DatabaseManager.getConnection();
				connection.setAutoCommit(true);
	    	}
    	} catch (SQLException e) {
			log.fatal(e.getMessage());
			e.printStackTrace();
		}
        return connection;
    }
    
    public void closeDBConnection(){
    	if(connection!=null){
	    	log.debug("freeconnection");
	    	DatabaseManager.freeConnection(connection);
	    	connection = null;
    	}
    }
    
    public User getUser(){
        return user;
    }

    public void setUser(final User user){
        this.user = user;
    }
	public SSLSocket getSSLConnection() throws SSLSocketException{
		if(sslConnection==null){
			createNewSSLSocket();
		}
		return sslConnection;
	}
	public void closeSSLConnection(){
		if(sslConnection!=null){
			try {
				sslConnection.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
			sslConnection=null;
		}
	}
	private SSLSocket createNewSSLSocket()throws SSLSocketException{
		String host = ConfigurationManager.getProperty("ssl.host");
		int port = Integer.parseInt(ConfigurationManager.getProperty("ssl.port"));
		try {
			log.debug("host:"+host+"port:"+port);
			sslConnection = (SSLSocket)factory.createSocket(host,port);
			sslConnection.startHandshake();
			return sslConnection;
		} catch (UnknownHostException e) {
			throw new SSLSocketException(e.getMessage());
		} catch (IOException e) {
			throw new SSLSocketException(e.getMessage());
		}
	}
	
}