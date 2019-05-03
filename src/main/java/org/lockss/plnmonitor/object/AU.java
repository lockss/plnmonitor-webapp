package org.lockss.plnmonitor.object;
import java.io.Serializable;
import java.util.Date;

import org.lockss.ws.entities.AuWsResult;

/**
 * @author anleroy
 *
 */

public class AU implements Serializable {

	private long AUId; // AU id from database
	private String Name;
	private String PluginName;
	private String TdbYear;
	private String AccessType;
	private long ContentSize;
	private double RecentPollAgreement;
	private long CreationTime;
	private long Version;
	private Date CreatedAt;
	private Date UpdatedAt;
	
	private String Au_LOCKSS_Id;
	private String TdbPublisher;
	private String Volume;
	private long DiskUsage;
	private long LastCompletedCrawl;
	private long LastCompletedPoll;
	private long LastCrawl;
	private long LastPoll;
	
	private String CrawlPool;
	private String CrawlProxy;
	private String CrawlWindow;
	
	private String LastCrawlResult;
	private String LastPollResult;
	private String PublishingPlatform;
	private String RepositoryPath;
	
	private String SubscriptionStatus;
	private String SubstanceState;
	
	private boolean AvailableFromPublisher;

	private long lockssBoxId;

	public AU(){
	}

	public AU(long aUId, String name, String pluginName, String tdbYear, String accessType, long contentSize,
			double recentPollAgreement, long creationTime, long version, Date createdAt, Date updatedAt,
			String au_LOCKSS_Id, String tdbPublisher, String volume, long diskUsage, long lastCompletedCrawl,
			long lastCompletedPoll, long lastCrawl, long lastPoll, String crawlPool, String crawlProxy,
			String crawlWindow, String lastCrawlResult, String lastPollResult, String publishingPlatform,
			String repositoryPath, String subscriptionStatus, String substanceState, boolean availableFromPublisher,
			long lockssBoxId) {
		super();
		AUId = aUId;
		Name = name;
		PluginName = pluginName;
		TdbYear = tdbYear;
		AccessType = accessType;
		ContentSize = contentSize;
		RecentPollAgreement = recentPollAgreement;
		CreationTime = creationTime;
		Version = version;
		CreatedAt = createdAt;
		UpdatedAt = updatedAt;
		Au_LOCKSS_Id = au_LOCKSS_Id;
		TdbPublisher = tdbPublisher;
		Volume = volume;
		DiskUsage = diskUsage;
		LastCompletedCrawl = lastCompletedCrawl;
		LastCompletedPoll = lastCompletedPoll;
		LastCrawl = lastCrawl;
		LastPoll = lastPoll;
		CrawlPool = crawlPool;
		CrawlProxy = crawlProxy;
		CrawlWindow = crawlWindow;
		LastCrawlResult = lastCrawlResult;
		LastPollResult = lastPollResult;
		PublishingPlatform = publishingPlatform;
		RepositoryPath = repositoryPath;
		SubscriptionStatus = subscriptionStatus;
		SubstanceState = substanceState;
		AvailableFromPublisher = availableFromPublisher;
		this.lockssBoxId = lockssBoxId;
	}

	public long getAUId() {
		return AUId;
	}

	public void setAUId(long aUId) {
		AUId = aUId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getPluginName() {
		return PluginName;
	}

	public void setPluginName(String pluginName) {
		PluginName = pluginName;
	}

	public String getTdbYear() {
		return TdbYear;
	}

	public void setTdbYear(String tdbYear) {
		TdbYear = tdbYear;
	}

	public String getAccessType() {
		return AccessType;
	}

	public void setAccessType(String accessType) {
		AccessType = accessType;
	}

	public long getContentSize() {
		return ContentSize;
	}

	public void setContentSize(long contentSize) {
		ContentSize = contentSize;
	}

	public double getRecentPollAgreement() {
		return RecentPollAgreement;
	}

	public void setRecentPollAgreement(long recentPollAgreement) {
		RecentPollAgreement = recentPollAgreement;
	}

	public long getCreationTime() {
		return CreationTime;
	}

	public void setCreationTime(long creationTime) {
		CreationTime = creationTime;
	}

	public long getVersion() {
		return Version;
	}

	public void setVersion(long version) {
		Version = version;
	}

	public Date getCreatedAt() {
		return CreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}

	public Date getUpdatedAt() {
		return UpdatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		UpdatedAt = updatedAt;
	}

	public String getAu_LOCKSS_Id() {
		return Au_LOCKSS_Id;
	}

	public void setAu_LOCKSS_Id(String au_LOCKSS_Id) {
		Au_LOCKSS_Id = au_LOCKSS_Id;
	}

	public String getTdbPublisher() {
		return TdbPublisher;
	}

	public void setTdbPublisher(String tdbPublisher) {
		TdbPublisher = tdbPublisher;
	}

	public String getVolume() {
		return Volume;
	}

	public void setVolume(String volume) {
		Volume = volume;
	}

	public long getDiskUsage() {
		return DiskUsage;
	}

	public void setDiskUsage(long diskUsage) {
		DiskUsage = diskUsage;
	}

	public long getLastCompletedCrawl() {
		return LastCompletedCrawl;
	}

	public void setLastCompletedCrawl(long lastCompletedCrawl) {
		LastCompletedCrawl = lastCompletedCrawl;
	}

	public long getLastCompletedPoll() {
		return LastCompletedPoll;
	}

	public void setLastCompletedPoll(long lastCompletedPoll) {
		LastCompletedPoll = lastCompletedPoll;
	}

	public long getLastCrawl() {
		return LastCrawl;
	}

	public void setLastCrawl(long lastCrawl) {
		LastCrawl = lastCrawl;
	}

	public long getLastPoll() {
		return LastPoll;
	}

	public void setLastPoll(long lastPoll) {
		LastPoll = lastPoll;
	}

	public String getCrawlPool() {
		return CrawlPool;
	}

	public void setCrawlPool(String crawlPool) {
		CrawlPool = crawlPool;
	}

	public String getCrawlProxy() {
		return CrawlProxy;
	}

	public void setCrawlProxy(String crawlProxy) {
		CrawlProxy = crawlProxy;
	}

	public String getCrawlWindow() {
		return CrawlWindow;
	}

	public void setCrawlWindow(String crawlWindow) {
		CrawlWindow = crawlWindow;
	}

	public String getLastCrawlResult() {
		return LastCrawlResult;
	}

	public void setLastCrawlResult(String lastCrawlResult) {
		LastCrawlResult = lastCrawlResult;
	}

	public String getLastPollResult() {
		return LastPollResult;
	}

	public void setLastPollResult(String lastPollResult) {
		LastPollResult = lastPollResult;
	}

	public String getPublishingPlatform() {
		return PublishingPlatform;
	}

	public void setPublishingPlatform(String publishingPlatform) {
		PublishingPlatform = publishingPlatform;
	}

	public String getRepositoryPath() {
		return RepositoryPath;
	}

	public void setRepositoryPath(String repositoryPath) {
		RepositoryPath = repositoryPath;
	}

	public String getSubscriptionStatus() {
		return SubscriptionStatus;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		SubscriptionStatus = subscriptionStatus;
	}

	public String getSubstanceState() {
		return SubstanceState;
	}

	public void setSubstanceState(String substanceState) {
		SubstanceState = substanceState;
	}

	public boolean isAvailableFromPublisher() {
		return AvailableFromPublisher;
	}

	public void setAvailableFromPublisher(boolean availableFromPublisher) {
		AvailableFromPublisher = availableFromPublisher;
	}

	public long getLockssBoxId() {
		return lockssBoxId;
	}

	public void setLockssBox(long lockssBoxId) {
		this.lockssBoxId = lockssBoxId;
	}

}
