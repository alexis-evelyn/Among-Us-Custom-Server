package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import org.jetbrains.annotations.NotNull;

/**
 * Statistics About Sent/Received Packets
 *
 * Support For Individual Packet Types Such As Which Hats Are Most Common As Well As Number of Games Joined Coming Soon!!!
 */
public class Statistics {
	private long allSentBytes = 0;
	private long allReceivedBytes = 0;

	private long helloSentBytes = 0;
	private long helloReceivedBytes = 0;

	private long acknowledgesSentBytes = 0;
	private long acknowledgesReceivedBytes = 0;

	private long pingSentBytes = 0;
	private long pingReceivedBytes = 0;

	private long reliableSentBytes = 0;
	private long reliableReceivedBytes = 0;

	private long unreliableSentBytes = 0;
	private long unreliableReceivedBytes = 0;

	private long fragmentedSentBytes = 0;
	private long fragmentedReceivedBytes = 0;

	private long closeSentBytes = 0;
	private long closeReceivedBytes = 0;

	// All Packets
	public void logAllSent(@NotNull byte... data) {
		this.allSentBytes += data.length;
	}

	public long getAllSent() {
		return this.allSentBytes;
	}

	public void logAllReceived(@NotNull byte... data) {
		this.allReceivedBytes += data.length;
	}

	public long getAllReceived() {
		return this.allReceivedBytes;
	}

	// Hello
	@Deprecated
	public void logHelloSent(@NotNull byte... data) {
		// Not Supported As This is Server Only
		this.helloSentBytes += data.length;
	}

	@Deprecated
	public long getHelloSent() {
		return this.helloSentBytes;
	}

	public void logHelloReceived(@NotNull byte... data) {
		this.helloReceivedBytes += data.length;
	}

	public long getHelloReceived() {
		return this.helloReceivedBytes;
	}

	// Acknowledgment
	public void logAcknowledgementSent(@NotNull byte... data) {
		this.acknowledgesSentBytes += data.length;
	}

	public long getAcknowledgementSent() {
		return this.acknowledgesSentBytes;
	}

	public void logAcknowledgementReceived(@NotNull byte... data) {
		this.acknowledgesReceivedBytes += data.length;
	}

	public long getAcknowledgementReceived() {
		return this.acknowledgesReceivedBytes;
	}

	// Ping
	public void logPingSent(@NotNull byte... data) {
		// Not Implemented On Server Yet
		this.pingSentBytes += data.length;
	}

	public long getPingSent() {
		return this.pingSentBytes;
	}

	public void logPingReceived(@NotNull byte... data) {
		this.pingReceivedBytes += data.length;
	}

	public long getPingReceived() {
		return this.pingReceivedBytes;
	}

	// Reliable
	public void logReliableSent(@NotNull byte... data) {
		this.reliableSentBytes += data.length;
	}

	public long getReliableSent() {
		return this.reliableSentBytes;
	}

	public void logReliableReceived(@NotNull byte... data) {
		this.reliableReceivedBytes += data.length;
	}

	public long getReliableReceived() {
		return this.reliableReceivedBytes;
	}

	// Unreliable
	public void logUnreliableSent(@NotNull byte... data) {
		this.unreliableSentBytes += data.length;
	}

	public long getUnreliableSent() {
		return this.unreliableSentBytes;
	}

	public void logUnreliableReceived(@NotNull byte... data) {
		this.unreliableReceivedBytes += data.length;
	}

	public long getUnreliableReceived() {
		return this.unreliableReceivedBytes;
	}

	// Fragment
	public void logFragmentSent(@NotNull byte... data) {
		this.fragmentedSentBytes += data.length;
	}

	public long getFragmentedSent() {
		return this.fragmentedSentBytes;
	}

	public void logFragmentReceived(@NotNull byte... data) {
		this.fragmentedReceivedBytes += data.length;
	}

	public long getFragmentedReceived() {
		return this.fragmentedReceivedBytes;
	}

	// Disconnect
	public void logDisconnectSent(@NotNull byte... data) {
		this.closeSentBytes += data.length;
	}

	public long getDisconnectSent() {
		return this.closeSentBytes;
	}

	public void logDisconnectReceived(@NotNull byte... data) {
		this.closeReceivedBytes += data.length;
	}

	public long getDisconnectReceived() {
		return this.closeReceivedBytes;
	}

	// Any Hazel Packet Type Not Pre-Known
	public void logUnknownSent(@NotNull byte... data) {
		// Sanitization Check
		if (data.length == 0)
			return;

		SendOption sendOption = SendOption.getByte(data[0]);

		// Sanitization Check
		if (sendOption == null)
			return;

		switch (sendOption) {
			case NONE:
				logUnreliableSent(data);
			case RELIABLE:
				logReliableSent(data);
			case HELLO:
				logHelloSent(data);
			case DISCONNECT:
				logDisconnectSent(data);
			case ACKNOWLEDGEMENT:
				logAcknowledgementSent(data);
			case FRAGMENT:
				logFragmentSent(data);
			case PING:
				logPingSent(data);
		}
	}

	public void logUnknownReceived(@NotNull byte... data) {
		// Sanitization Check
		if (data.length == 0)
			return;

		SendOption sendOption = SendOption.getByte(data[0]);

		// Sanitization Check
		if (sendOption == null)
			return;

		switch (sendOption) {
			case NONE:
				logUnreliableReceived(data);
			case RELIABLE:
				logReliableReceived(data);
			case HELLO:
				logHelloReceived(data);
			case DISCONNECT:
				logDisconnectReceived(data);
			case ACKNOWLEDGEMENT:
				logAcknowledgementReceived(data);
			case FRAGMENT:
				logFragmentReceived(data);
			case PING:
				logPingReceived(data);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Statistics statistics = (Statistics) super.clone();

		return statistics;
	}
}