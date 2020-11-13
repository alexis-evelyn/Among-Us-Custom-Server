package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Statistics About Sent/Received Packets
 *
 * Support For Individual Packet Types Such As Which Hats Are Most Common As Well As Number of Games Joined Coming Soon!!! Also, tracking number of packets sent/received too.
 */
public class Statistics {
	private BigInteger allSentBytes = BigInteger.ZERO;
	private BigInteger allReceivedBytes = BigInteger.ZERO;
	private BigInteger allSentPackets = BigInteger.ZERO;
	private BigInteger allReceivedPackets = BigInteger.ZERO;

	private BigInteger helloSentBytes = BigInteger.ZERO;
	private BigInteger helloReceivedBytes = BigInteger.ZERO;
	private BigInteger helloSentPackets = BigInteger.ZERO;
	private BigInteger helloReceivedPackets = BigInteger.ZERO;

	private BigInteger acknowledgeSentBytes = BigInteger.ZERO;
	private BigInteger acknowledgeReceivedBytes = BigInteger.ZERO;
	private BigInteger acknowledgeSentPackets = BigInteger.ZERO;
	private BigInteger acknowledgeReceivedPackets = BigInteger.ZERO;

	private BigInteger pingSentBytes = BigInteger.ZERO;
	private BigInteger pingReceivedBytes = BigInteger.ZERO;
	private BigInteger pingSentPackets = BigInteger.ZERO;
	private BigInteger pingReceivedPackets = BigInteger.ZERO;

	private BigInteger reliableSentBytes = BigInteger.ZERO;
	private BigInteger reliableReceivedBytes = BigInteger.ZERO;
	private BigInteger reliableSentPackets = BigInteger.ZERO;
	private BigInteger reliableReceivedPackets = BigInteger.ZERO;

	private BigInteger unreliableSentBytes = BigInteger.ZERO;
	private BigInteger unreliableReceivedBytes = BigInteger.ZERO;
	private BigInteger unreliableSentPackets = BigInteger.ZERO;
	private BigInteger unreliableReceivedPackets = BigInteger.ZERO;

	private BigInteger fragmentedSentBytes = BigInteger.ZERO;
	private BigInteger fragmentedReceivedBytes = BigInteger.ZERO;
	private BigInteger fragmentedSentPackets = BigInteger.ZERO;
	private BigInteger fragmentedReceivedPackets = BigInteger.ZERO;

	private BigInteger closeSentBytes = BigInteger.ZERO;
	private BigInteger closeReceivedBytes = BigInteger.ZERO;
	private BigInteger closeSentPackets = BigInteger.ZERO;
	private BigInteger closeReceivedPackets = BigInteger.ZERO;

	// All Packets
	public void logAllSent(@NotNull byte... data) {
		this.allSentPackets = this.allSentPackets.add(BigInteger.ONE);
		this.allSentBytes = this.allSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getAllSentBytes() {
		return this.allSentBytes;
	}

	@NotNull
	public BigInteger getAllSentPackets() {
		return this.allSentPackets;
	}

	public void logAllReceived(@NotNull byte... data) {
		this.allReceivedPackets = this.allReceivedPackets.add(BigInteger.ONE);
		this.allReceivedBytes = this.allReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getAllReceivedBytes() {
		return this.allReceivedBytes;
	}

	@NotNull
	public BigInteger getAllReceivedPackets() {
		return this.allReceivedPackets;
	}

	// Hello
	@Deprecated
	public void logHelloSent(@NotNull byte... data) {
		// Not Supported As This is Server Only
		this.helloSentPackets = this.helloSentPackets.add(BigInteger.ONE);
		this.helloSentBytes = this.helloSentBytes.add(BigInteger.valueOf(data.length));
	}

	@Deprecated
	@NotNull
	public BigInteger getHelloSentBytes() {
		return this.helloSentBytes;
	}

	@Deprecated
	@NotNull
	public BigInteger getHelloSentPackets() {
		return this.helloSentPackets;
	}

	public void logHelloReceived(@NotNull byte... data) {
		this.helloReceivedPackets = this.helloReceivedPackets.add(BigInteger.ONE);
		this.helloReceivedBytes = this.helloReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getHelloReceivedBytes() {
		return this.helloReceivedBytes;
	}

	@NotNull
	public BigInteger getHelloReceivedPackets() {
		return this.helloReceivedPackets;
	}

	// Acknowledgment
	public void logAcknowledgementSent(@NotNull byte... data) {
		this.acknowledgeSentPackets = this.acknowledgeSentPackets.add(BigInteger.ONE);
		this.acknowledgeSentBytes = this.acknowledgeSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getAcknowledgementSentBytes() {
		return this.acknowledgeSentBytes;
	}

	@NotNull
	public BigInteger getAcknowledgementSentPackets() {
		return this.acknowledgeSentPackets;
	}

	public void logAcknowledgementReceived(@NotNull byte... data) {
		this.acknowledgeReceivedPackets = this.acknowledgeReceivedPackets.add(BigInteger.ONE);
		this.acknowledgeReceivedBytes = this.acknowledgeReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getAcknowledgementReceivedBytes() {
		return this.acknowledgeReceivedBytes;
	}

	@NotNull
	public BigInteger getAcknowledgementReceivedPackets() {
		return this.acknowledgeReceivedPackets;
	}

	// Ping
	public void logPingSent(@NotNull byte... data) {
		// Not Implemented On Server Yet
		this.pingSentPackets = this.pingSentPackets.add(BigInteger.ONE);
		this.pingSentBytes = this.pingSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getPingSentBytes() {
		return this.pingSentBytes;
	}

	@NotNull
	public BigInteger getPingSentPackets() {
		return this.pingSentPackets;
	}

	public void logPingReceived(@NotNull byte... data) {
		this.pingReceivedPackets = this.pingReceivedPackets.add(BigInteger.ONE);
		this.pingReceivedBytes = this.pingReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getPingReceivedBytes() {
		return this.pingReceivedBytes;
	}

	@NotNull
	public BigInteger getPingReceivedPackets() {
		return this.pingReceivedPackets;
	}

	// Reliable
	public void logReliableSent(@NotNull byte... data) {
		this.reliableSentPackets = this.reliableSentPackets.add(BigInteger.ONE);
		this.reliableSentBytes = this.reliableSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getReliableSentBytes() {
		return this.reliableSentBytes;
	}

	@NotNull
	public BigInteger getReliableSentPackets() {
		return this.reliableSentPackets;
	}

	public void logReliableReceived(@NotNull byte... data) {
		this.reliableReceivedPackets = this.reliableReceivedPackets.add(BigInteger.ONE);
		this.reliableReceivedBytes = this.reliableReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getReliableReceivedBytes() {
		return this.reliableReceivedBytes;
	}

	@NotNull
	public BigInteger getReliableReceivedPackets() {
		return this.reliableReceivedPackets;
	}

	// Unreliable
	public void logUnreliableSent(@NotNull byte... data) {
		this.unreliableSentPackets = this.unreliableSentPackets.add(BigInteger.ONE);
		this.unreliableSentBytes = this.unreliableSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getUnreliableSentBytes() {
		return this.unreliableSentBytes;
	}

	@NotNull
	public BigInteger getUnreliableSentPackets() {
		return this.unreliableSentPackets;
	}

	public void logUnreliableReceived(@NotNull byte... data) {
		this.unreliableReceivedPackets = this.unreliableReceivedPackets.add(BigInteger.ONE);
		this.unreliableReceivedBytes = this.unreliableReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getUnreliableReceivedBytes() {
		return this.unreliableReceivedBytes;
	}

	@NotNull
	public BigInteger getUnreliableReceivedPackets() {
		return this.unreliableReceivedPackets;
	}

	// Fragment
	public void logFragmentSent(@NotNull byte... data) {
		this.fragmentedSentPackets = this.fragmentedSentPackets.add(BigInteger.ONE);
		this.fragmentedSentBytes = this.fragmentedSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getFragmentedSentBytes() {
		return this.fragmentedSentBytes;
	}

	@NotNull
	public BigInteger getFragmentedSentPackets() {
		return this.fragmentedSentPackets;
	}

	public void logFragmentReceived(@NotNull byte... data) {
		this.fragmentedReceivedPackets = this.fragmentedReceivedPackets.add(BigInteger.ONE);
		this.fragmentedReceivedBytes = this.fragmentedReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getFragmentedReceivedBytes() {
		return this.fragmentedReceivedBytes;
	}

	@NotNull
	public BigInteger getFragmentedReceivedPackets() {
		return this.fragmentedReceivedPackets;
	}

	// Disconnect
	public void logDisconnectSent(@NotNull byte... data) {
		this.closeSentPackets = this.closeSentPackets.add(BigInteger.ONE);
		this.closeSentBytes = this.closeSentBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getDisconnectSentBytes() {
		return this.closeSentBytes;
	}

	@NotNull
	public BigInteger getDisconnectSentPackets() {
		return this.closeSentPackets;
	}

	public void logDisconnectReceived(@NotNull byte... data) {
		this.closeReceivedPackets = this.closeReceivedPackets.add(BigInteger.ONE);
		this.closeReceivedBytes = this.closeReceivedBytes.add(BigInteger.valueOf(data.length));
	}

	@NotNull
	public BigInteger getDisconnectReceivedBytes() {
		return this.closeReceivedBytes;
	}

	@NotNull
	public BigInteger getDisconnectReceivedPackets() {
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