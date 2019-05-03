package org.lockss.plnmonitor;

public class SSLSocketException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SSLSocketException()
    {
        super();
    }

    public SSLSocketException(String s, Throwable t)
    {
        super(s, t);
    }

    public SSLSocketException(String s)
    {
        super(s);
    }

    public SSLSocketException(Throwable t)
    {
        super(t);
    }
}
