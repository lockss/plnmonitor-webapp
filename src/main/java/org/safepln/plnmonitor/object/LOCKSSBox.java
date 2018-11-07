package org.safepln.plnmonitor.object;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;


public class LOCKSSBox implements Serializable, IClusterable {
	/**
	 * 
	 */

	private long plnId;
	private String hostname;
	private String userName;
	private String password;
	private String urlDSS;
	private String ipAddress;
	private String uiport;

	private String javaVersion;
	private String daemonFullVersion;
	private String platform;
	private long uptime;
	private String groups;
	private String disks;
	
	private long boxId;
	
	private double latitude;
	private double longitude;
	
	private String country;
	private String physicalAddress;
	
	private String name;
	
	private List<LOCKSSBoxData> lockssBoxData;
	
	private String adminEmail;
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJava_version(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getDaemon_full_version() {
		return daemonFullVersion;
	}

	public void setDaemonFullVersion(String daemonFullVersion) {
		this.daemonFullVersion = daemonFullVersion;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getDisks() {
		return disks;
	}

	public void setDisks(String disks) {
		this.disks = disks;
	}


	
	public List<LOCKSSBoxData> getLockssBoxData() {
		return lockssBoxData;
	}

	public void setLockssBoxData(List<LOCKSSBoxData> lockssBoxData) {
		this.lockssBoxData = lockssBoxData;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTechAdmin() {
		return techAdmin;
	}

	public void setTechAdmin(long techAdmin) {
		this.techAdmin = techAdmin;
	}

	public long getOrgAdmin() {
		return orgAdmin;
	}

	public void setOrgAdmin(long orgAdmin) {
		this.orgAdmin = orgAdmin;
	}

	private List<Peer> peers;
	private List<AU> AUs;
	
	private long techAdmin;
	private long orgAdmin;
	
	public LOCKSSBox(){
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrlDSS() {
		return urlDSS;
	}

	public void setUrlDSS(String urlDSS) {
		this.urlDSS = urlDSS;
	}

	public List<Peer> getPeers() {
		return peers;
	}

	public void setPeers(List<Peer> peers) {
		this.peers = peers;
	}



	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getUiport() {
		return uiport;
	}

	public void setUiport(String uiport) {
		this.uiport = uiport;
	}

	public long getBoxId() {
		return boxId;
	}

	public void setBoxId(long boxId) {
		this.boxId = boxId;
	}

	public long getPlnId() {
		return plnId;
	}

	public void setPlnId(long plnId) {
		this.plnId = plnId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhysicalAddress() {
		return physicalAddress;
	}

	public void setPhysicalAddress(String physicalAddress) {
		this.physicalAddress = physicalAddress;
	}

	public List<AU> getAUs() {
		return AUs;
	}

	public void setAUs(List<AU> aUs) {
		AUs = aUs;
	}
	
	   /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "[Box id=" + boxId + " ipAddress=" + ipAddress + " UIport=" + uiport +
                " hostname=" + hostname + " plnId=" + plnId + "]";
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (obj instanceof LOCKSSBox)
        {
            LOCKSSBox other = (LOCKSSBox)obj;
            return other.getIpAddress().equals(getIpAddress()) &&
                    other.getUiport().equals(getUiport());

        }
        else
        {
            return false;
        }
    }
//
//	public LOCKSSBox(long plnId, String hostname, String userName, String password, String urlDSS, String ipAddress,
//			String uiport, long boxId, double latitude, double longitude, String country, String physical_address,
//			long orgMan, long techMan, String name) {
//		super();
//		this.plnId = plnId;
//		this.hostname = hostname;
//		this.userName = userName;
//		this.password = password;
//		this.urlDSS = urlDSS;
//		this.ipAddress = ipAddress;
//		this.uiport = uiport;
//		this.boxId = boxId;
//		this.latitude = latitude;
//		this.longitude = longitude;
//		this.country = country;
//		this.physical_address = physical_address;
//		this.name = name;
//		this.orgAdmin = orgMan;
//		this.techAdmin = techMan;
//
//	}

	public LOCKSSBox(long plnId, String hostname, String userName, String password, String urlDSS, String ipAddress,
			String uiport, String java_version, String daemonFullVersion, String platform, long uptime, String groups,
			String disks, long boxId, double latitude, double longitude, String country, String physicalAddress,
			String name, String adminEmail, long techAdmin, long orgAdmin) {
		super();
		this.plnId = plnId;
		this.hostname = hostname;
		this.userName = userName;
		this.password = password;
		this.urlDSS = urlDSS;
		this.ipAddress = ipAddress;
		this.uiport = uiport;
		this.javaVersion = java_version;
		this.daemonFullVersion = daemonFullVersion;
		this.platform = platform;
		this.uptime = uptime;
		this.groups = groups;
		this.disks = disks;
		this.boxId = boxId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.physicalAddress = physicalAddress;
		this.name = name;
		this.adminEmail = adminEmail;
		this.techAdmin = techAdmin;
		this.orgAdmin = orgAdmin;
	}
}
