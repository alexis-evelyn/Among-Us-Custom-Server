package me.alexisevelyn.crewmate.enums.hazel;

import java.util.HashMap;

public enum SendOption {
	// Names From: https://github.com/willardf/Hazel-Networking/blob/master/Hazel/SendOption.cs
	// As Well As: https://github.com/willardf/Hazel-Networking/blob/master/Hazel/Udp/SendOptionInternal.cs

	NONE((byte) 0x00),
	RELIABLE((byte) 0x01),
	HELLO((byte) 0x08),
	DISCONNECT((byte) 0x09),
	ACKNOWLEDGEMENT((byte) 0x0A),
	FRAGMENT((byte) 0x0B),
	PING((byte) 0x0C);

	private final byte sendOption;

	SendOption(byte sendOption) {
		this.sendOption = sendOption;
	}

	private static final java.util.Map<Byte, SendOption> sendOptionHashMap = new HashMap<>();

	public static SendOption getSendOption(byte sendOptionValue) {
		return sendOptionHashMap.get(sendOptionValue);
	}

	public byte getSendOption() {
		return this.sendOption;
	}

	static {
		for (SendOption sendOptionKey : SendOption.values()) {
			sendOptionHashMap.put(sendOptionKey.sendOption, sendOptionKey);
		}
	}
}