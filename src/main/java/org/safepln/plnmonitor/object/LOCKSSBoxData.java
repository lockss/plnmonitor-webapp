package org.safepln.plnmonitor.object;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;


public class LOCKSSBoxData implements Serializable, IClusterable {
	public LOCKSSBoxData(long used, long free, long size, long activeAUs, long deletedAUs, long inactiveAUs,
			long orphanedAUs, double percentage, String repositorySpace, Date updatedAt) {
		super();
		this.used = used;
		this.free = free;
		this.size = size;
		this.activeAUs = activeAUs;
		this.deletedAUs = deletedAUs;
		this.inactiveAUs = inactiveAUs;
		this.orphanedAUs = orphanedAUs;
		this.percentage = percentage;
		this.repositorySpace = repositorySpace;
		this.updatedAt = updatedAt;
	}
	
	public long getUsed() {
		return used;
	}
	public void setUsed(long used) {
		this.used = used;
	}
	public long getFree() {
		return free;
	}
	public void setFree(long free) {
		this.free = free;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getActiveAUs() {
		return activeAUs;
	}
	public void setActiveAUs(long activeAUs) {
		this.activeAUs = activeAUs;
	}
	public long getDeletedAUs() {
		return deletedAUs;
	}
	public void setDeletedAUs(long deletedAUs) {
		this.deletedAUs = deletedAUs;
	}
	public long getInactiveAUs() {
		return inactiveAUs;
	}
	public void setInactiveAUs(long inactiveAUs) {
		this.inactiveAUs = inactiveAUs;
	}
	public long getOrphanedAUs() {
		return orphanedAUs;
	}
	public void setOrphanedAUs(long orphanedAUs) {
		this.orphanedAUs = orphanedAUs;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}
	public String getRepositorySpace() {
		return repositorySpace;
	}
	public void setRepositorySpace(String repositorySpace) {
		this.repositorySpace = repositorySpace;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	private long used;
	private long free;
	private long size;
	private long activeAUs;
	private long deletedAUs;
	private long inactiveAUs;
	private long orphanedAUs;
	private double percentage;
	private String repositorySpace;
	private Date updatedAt;
}
