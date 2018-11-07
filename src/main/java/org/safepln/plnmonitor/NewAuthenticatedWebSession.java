package org.safepln.plnmonitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.mindrot.jbcrypt.BCrypt;
import org.safepln.plnmonitor.object.Person;
import org.safepln.plnmonitor.object.User;
import org.safepln.plnmonitor.rdbms.DatabaseManager;


import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;

import org.safepln.plnmonitor.ConfigurationManager;
import org.safepln.plnmonitor.SSLSocketException;
import org.safepln.plnmonitor.dao.UserDAO;


/**
 * The Class NewAuthenticatedWebSession.
 * 
 * Manages SSL connection and authenticate user by checking password hash matches with the one in database 
 * (OpenBSD-style Blowfish)
 */
public class NewAuthenticatedWebSession extends AuthenticatedWebSession
{
    
    /** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5973372410342131303L;

	/**
	 * Gets the log.
	 *
	 * @return the log
	 */
	
	public static Logger getLog() {
		return log;
	}
	
	/** The user. */
	private User user;
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(NewAuthenticatedWebSession.class);

    /**  Database connection. */
    private transient Connection connection;

    /** The factory. */
    private transient SSLSocketFactory factory;
    
    /** The ssl connection. */
    private transient SSLSocket sslConnection;
    
	/**
     * Construct.
     * 
     * @param request
     *            The current request object
     */
    public NewAuthenticatedWebSession(Request request)
    {
        super(request);
    	try {
			connection = DatabaseManager.getConnection();
			connection.setAutoCommit(true);
			log.info("connection open");
			
//			// CREATE SSL SOCKET
//		    try {
//				SSLContext ctx;
//				KeyManagerFactory kmf;
//				KeyStore ks1;
//				TrustManagerFactory tmf;
//				KeyStore ks2;
//				char[] passphrase = "passphrase".toCharArray();
//	
//				ctx = SSLContext.getInstance("TLS");
//				kmf = KeyManagerFactory.getInstance("SunX509");
//				ks1 = KeyStore.getInstance("JKS");
//				ks1.load(new FileInputStream(ConfigurationManager.getProperty("ssl.path")+"/testkeys"), passphrase);
//				kmf.init(ks1, passphrase);
//				
//				tmf = TrustManagerFactory.getInstance("SunX509","SunJSSE");
//				ks2 = KeyStore.getInstance("JKS");
//				ks2.load(new FileInputStream(ConfigurationManager.getProperty("ssl.path")+"/cacerts"),null);
//				tmf.init(ks2);
//				ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
//
//				factory = ctx.getSocketFactory();
//		    }
//		    catch (NoSuchAlgorithmException e) {
//		    	throw new SSLSocketException(e.getMessage());
//			} catch (CertificateException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (FileNotFoundException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (IOException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (KeyStoreException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (NoSuchProviderException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (UnrecoverableKeyException e) {
//				throw new SSLSocketException(e.getMessage());
//			} catch (KeyManagementException e) {
//				throw new SSLSocketException(e.getMessage());
//			}
//			createNewSSLSocket();
////		} catch (SQLException e) {
////			log.fatal(e.getMessage());
			} catch (Exception e2) {
			log.fatal(e2.getMessage());
			log.fatal(e2.toString());
			
			e2.printStackTrace();
		}

    }

    /**
     * Authenticate.
     *
     * @param username the username
     * @param password the password
     * @return true, if successful
     * @see org.apache.wicket.authentication.NewAuthenticatedWebSession#authenticate(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean authenticate(final String username, final String password)
    {
        // Check username and password
        
        if (username == null || password == null) {
            return false;
        }

        UserDAO userDao = new UserDAO(this);
        
        User user = null;
		
        try {
			user = userDao.getUserData(username);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (user == null) {
            return false;
        }

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
        	log.info("User " + user.getUsername() + " logged in !");
            return true;
        } 
        else {
        	log.info("User " + user.getUsername() + " could not log in ");
        }
        return false;
    }

    /**
     * Gets the roles.
     *
     * @return the roles
     * @see org.apache.wicket.authentication.NewAuthenticatedWebSession#getRoles()
     */
    @Override
    public Roles getRoles()
    {
        if (isSignedIn())
        {
            // If the user is signed in, they have these roles
            return new Roles(Roles.ADMIN);
        }
        return null;
    }
    
    /**
     * Get the database connection associated with the context.
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
    
    /**
     * Close DB connection.
     */
    public void closeDBConnection(){
    	if(connection!=null){
	    	log.debug("freeconnection");
	    	DatabaseManager.freeConnection(connection);
	    	connection = null;
    	}
    }
    
    /**
     * Gets the user.
     *
     * @return the user
     */
    public User getUser(){
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the new user
     */
    public void setUser(final User user){
        this.user = user;
    }
	
	/**
	 * Gets the SSL connection.
	 *
	 * @return the SSL connection
	 * @throws SSLSocketException the SSL socket exception
	 */
	public SSLSocket getSSLConnection() throws SSLSocketException{
		if(sslConnection==null){
			createNewSSLSocket();
		}
		return sslConnection;
	}
	
	/**
	 * Close SSL connection.
	 */
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
	
	/**
	 * Creates the new SSL socket.
	 *
	 * @return the SSL socket
	 * @throws SSLSocketException the SSL socket exception
	 */
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