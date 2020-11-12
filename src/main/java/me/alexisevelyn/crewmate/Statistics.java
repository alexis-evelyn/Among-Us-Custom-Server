package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import org.jetbrains.annotations.NotNull;

/**
 * Statistics About Sent/Received Packets
 *
 * Support For Individual Packet Types Such As Which Hats Are Most Common As Well As Number of Games Joined Coming Soon!!! Also, tracking number of packets sent/received too.
 */
public class Statistics {
	private long allSentBytes = 0;
	private long allReceivedBytes = 0;
	private long allSentPackets = 0;
	private long allReceivedPackets = 0;

	private long helloSentBytes = 0;
	private long helloReceivedBytes = 0;
	private long helloSentPackets = 0;
	private long helloReceivedPackets = 0;

	private long acknowledgeSentBytes = 0;
	private long acknowledgeReceivedBytes = 0;
	private long acknowledgeSentPackets = 0;
	private long acknowledgeReceivedPackets = 0;

	private long pingSentBytes = 0;
	private long pingReceivedBytes = 0;
	private long pingSentPackets = 0;
	private long pingReceivedPackets = 0;

	private long reliableSentBytes = 0;
	private long reliableReceivedBytes = 0;
	private long reliableSentPackets = 0;
	private long reliableReceivedPackets = 0;

	private long unreliableSentBytes = 0;
	private long unreliableReceivedBytes = 0;
	private long unreliableSentPackets = 0;
	private long unreliableReceivedPackets = 0;

	private long fragmentedSentBytes = 0;
	private long fragmentedReceivedBytes = 0;
	private long fragmentedSentPackets = 0;
	private long fragmentedReceivedPackets = 0;

	private long closeSentBytes = 0;
	private long closeReceivedBytes = 0;
	private long closeSentPackets = 0;
	private long closeReceivedPackets = 0;

	// All Packets
	public void logAllSent(@NotNull byte... data) {
		this.allSentPackets++;
		this.allSentBytes += data.length;
	}

	public long getAllSentBytes() {
		return this.allSentBytes;
	}

	public long getAllSentPackets() {
		return this.allSentPackets;
	}

	public void logAllReceived(@NotNull byte... data) {
		this.allReceivedPackets++;
		this.allReceivedBytes += data.length;
	}

	public long getAllReceivedBytes() {
		return this.allReceivedBytes;
	}

	public long getAllReceivedPackets() {
		return this.allReceivedPackets;
	}

	// Hello
	@Deprecated
	public void logHelloSent(@NotNull byte... data) {
		// Not Supported As This is Server Only
		this.helloSentPackets++;
		this.helloSentBytes += data.length;
	}

	@Deprecated
	public long getHelloSentBytes() {
		return this.helloSentBytes;
	}

	@Deprecated
	public long getHelloSentPackets() {
		return this.helloSentPackets;
	}

	public void logHelloReceived(@NotNull byte... data) {
		this.helloReceivedPackets++;
		this.helloReceivedBytes += data.length;
	}

	public long getHelloReceivedBytes() {
		return this.helloReceivedBytes;
	}

	public long getHelloReceivedPackets() {
		return this.helloReceivedPackets;
	}

	// Acknowledgment
	public void logAcknowledgementSent(@NotNull byte... data) {
		this.acknowledgeSentPackets++;
		this.acknowledgeSentBytes += data.length;
	}

	public long getAcknowledgementSentBytes() {
		return this.acknowledgeSentBytes;
	}

	public long getAcknowledgementSentPackets() {
		return this.acknowledgeSentPackets;
	}

	public void logAcknowledgementReceived(@NotNull byte... data) {
		this.acknowledgeReceivedPackets++;
		this.acknowledgeReceivedBytes += data.length;
	}

	public long getAcknowledgementReceivedBytes() {
		return this.acknowledgeReceivedBytes;
	}

	public long getAcknowledgementReceivedPackets() {
		return this.acknowledgeReceivedPackets;
	}

	// Ping
	public void logPingSent(@NotNull byte... data) {
		// Not Implemented On Server Yet
		this.pingSentPackets++;
		this.pingSentBytes += data.length;
	}

	public long getPingSentBytes() {
		return this.pingSentBytes;
	}

	public long getPingSentPackets() {
		return this.pingSentPackets;
	}

	public void logPingReceived(@NotNull byte... data) {
		this.pingReceivedPackets++;
		this.pingReceivedBytes += data.length;
	}

	public long getPingReceivedBytes() {
		return this.pingReceivedBytes;
	}

	public long getPingReceivedPackets() {
		return this.pingReceivedPackets;
	}

	// Reliable
	public void logReliableSent(@NotNull byte... data) {
		this.reliableSentPackets++;
		this.reliableSentBytes += data.length;
	}

	public long getReliableSentBytes() {
		return this.reliableSentBytes;
	}

	public long getReliableSentPackets() {
		return this.reliableSentPackets;
	}

	public void logReliableReceived(@NotNull byte... data) {
		this.reliableReceivedPackets++;
		this.reliableReceivedBytes += data.length;
	}

	public long getReliableReceivedBytes() {
		return this.reliableReceivedBytes;
	}

	public long getReliableReceivedPackets() {
		return this.reliableReceivedPackets;
	}

	// Unreliable
	public void logUnreliableSent(@NotNull byte... data) {
		this.unreliableSentPackets++;
		this.unreliableSentBytes += data.length;
	}

	public long getUnreliableSentBytes() {
		return this.unreliableSentBytes;
	}

	public long getUnreliableSentPackets() {
		return this.unreliableSentPackets;
	}

	public void logUnreliableReceived(@NotNull byte... data) {
		this.unreliableReceivedPackets++;
		this.unreliableReceivedBytes += data.length;
	}

	public long getUnreliableReceivedBytes() {
		return this.unreliableReceivedBytes;
	}

	public long getUnreliableReceivedPackets() {
		return this.unreliableReceivedPackets;
	}

	// Fragment
	public void logFragmentSent(@NotNull byte... data) {
		this.fragmentedSentPackets++;
		this.fragmentedSentBytes += data.length;
	}

	public long getFragmentedSentBytes() {
		return this.fragmentedSentBytes;
	}

	public long getFragmentedSentPackets() {
		return this.fragmentedSentPackets;
	}

	public void logFragmentReceived(@NotNull byte... data) {
		this.fragmentedReceivedPackets++;
		this.fragmentedReceivedBytes += data.length;
	}

	public long getFragmentedReceivedBytes() {
		return this.fragmentedReceivedBytes;
	}

	public long getFragmentedReceivedPackets() {
		return this.fragmentedReceivedPackets;
	}

	// Disconnect
	public void logDisconnectSent(@NotNull byte... data) {
		this.closeSentPackets++;
		this.closeSentBytes += data.length;
	}

	public long getDisconnectSentBytes() {
		return this.closeSentBytes;
	}

	public long getDisconnectSentPackets() {
		return this.closeSentPackets;
	}

	public void logDisconnectReceived(@NotNull byte... data) {
		this.closeReceivedPackets++;
		this.closeReceivedBytes += data.length;
	}

	public long getDisconnectReceivedBytes() {
		return this.closeReceivedBytes;
	}

	public long getDisconnectReceivedPackets() {
		return this.closeReceivedPackets;
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
				break;
			case RELIABLE:
				logReliableSent(data);
				break;
			case HELLO:
				// LogHelper.printLine("DEBUG: Hello Sent");
				// LogHelper.printPacketBytes(data);

				logHelloSent(data);
				break;
			case DISCONNECT:
				logDisconnectSent(data);
				break;
			case ACKNOWLEDGEMENT:
				logAcknowledgementSent(data);
				break;
			case FRAGMENT:
				logFragmentSent(data);
				break;
			case PING:
				logPingSent(data);
				break;
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
				break;
			case RELIABLE:
				logReliableReceived(data);
				break;
			case HELLO:
				logHelloReceived(data);
				break;
			case DISCONNECT:
				// LogHelper.printLine("DEBUG: Disconnect Received");
				logDisconnectReceived(data);
				break;
			case ACKNOWLEDGEMENT:
				logAcknowledgementReceived(data);
				break;
			case FRAGMENT:
				logFragmentReceived(data);
				break;
			case PING:
				logPingReceived(data);
				break;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Statistics statistics = (Statistics) super.clone();

		return statistics;
	}
}