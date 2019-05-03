package org.lockss.plnmonitor.object;

import java.io.Serializable;

public class Peer implements Serializable {



	private long peerId;
	private long lastPoll;
	private long pollsCalled;
	private long lastInvitation;
	private long lastVote;	
	private LOCKSSBox lockssBox;
	private String peerLockssId;
	private long lastMessage;
	private long invitationCount;
	private long messageCount;
	private String messageType;
	private long pollsRejected;
	private long votesCast;
	
	
	public Peer(long peerId, long lastPoll, long pollsCalled, long lastInvitation, long lastVote, LOCKSSBox lockssBox,
			String peerLockssId, long lastMessage, long invitationCount, long messageCount, String messageType,
			long pollsRejected, long votesCast) {
		super();
		this.peerId = peerId;
		this.lastPoll = lastPoll;
		this.pollsCalled = pollsCalled;
		this.lastInvitation = lastInvitation;
		this.lastVote = lastVote;
		this.lockssBox = lockssBox;
		this.peerLockssId = peerLockssId;
		this.lastMessage = lastMessage;
		this.invitationCount = invitationCount;
		this.messageCount = messageCount;
		this.messageType = messageType;
		this.pollsRejected = pollsRejected;
		this.votesCast = votesCast;
	}


	public long getPeerId() {
		return peerId;
	}


	public void setPeerId(long peerId) {
		this.peerId = peerId;
	}


	public long getLastPoll() {
		return lastPoll;
	}


	public void setLastPoll(long lastPoll) {
		this.lastPoll = lastPoll;
	}


	public long getPollsCalled() {
		return pollsCalled;
	}


	public void setPollsCalled(long pollsCalled) {
		this.pollsCalled = pollsCalled;
	}


	public long getLastInvitation() {
		return lastInvitation;
	}


	public void setLastInvitation(long lastInvitation) {
		this.lastInvitation = lastInvitation;
	}


	public long getLastVote() {
		return lastVote;
	}


	public void setLastVote(long lastVote) {
		this.lastVote = lastVote;
	}


	public LOCKSSBox getLockssBox() {
		return lockssBox;
	}


	public void setLockssBox(LOCKSSBox lockssBox) {
		this.lockssBox = lockssBox;
	}


	public String getPeerLockssId() {
		return peerLockssId;
	}


	public void setPeerLockssId(String peerLockssId) {
		this.peerLockssId = peerLockssId;
	}


	public long getLastMessage() {
		return lastMessage;
	}


	public void setLastMessage(long lastMessage) {
		this.lastMessage = lastMessage;
	}


	public long getInvitationCount() {
		return invitationCount;
	}


	public void setInvitationCount(long invitationCount) {
		this.invitationCount = invitationCount;
	}


	public long getMessageCount() {
		return messageCount;
	}


	public void setMessageCount(long messageCount) {
		this.messageCount = messageCount;
	}


	public String getMessageType() {
		return messageType;
	}


	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public long getPollsRejected() {
		return pollsRejected;
	}


	public void setPollsRejected(long pollsRejected) {
		this.pollsRejected = pollsRejected;
	}


	public long getVotesCast() {
		return votesCast;
	}


	public void setVotesCast(long votesCast) {
		this.votesCast = votesCast;
	}
	
	
	

}
