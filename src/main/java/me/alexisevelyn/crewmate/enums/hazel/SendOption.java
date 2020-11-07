package me.alexisevelyn.crewmate.enums.hazel;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private static final java.util.Map<Byte, SendOption> sendOptionHashMap = new HashMap<>();

	SendOption(byte sendOption) {
		this.sendOption = sendOption;
	}

	@Nullable
	public static SendOption getByte(byte sendOptionValue) {
		return sendOptionHashMap.get(sendOptionValue);
	}

	@NotNull
	public static String getSendOptionName(@NotNull SendOption sendOptionType) {
		switch (sendOptionType) {
			case NONE:
				return Main.getTranslation("send_option_none");
			case RELIABLE:
				return Main.getTranslation("send_option_reliable");
			case HELLO:
				return Main.getTranslation("send_option_hello");
			case DISCONNECT:
				return Main.getTranslation("send_option_disconnect");
			case ACKNOWLEDGEMENT:
				return Main.getTranslation("send_option_acknowledgment");
			case FRAGMENT:
				return Main.getTranslation("send_option_fragment");
			case PING:
				return Main.getTranslation("send_option_ping");
			default:
				return Main.getTranslation("unknown");
		}
	}

	public byte getByte() {
		return this.sendOption;
	}

	static {
		for (SendOption sendOptionKey : SendOption.values()) {
			sendOptionHashMap.put(sendOptionKey.sendOption, sendOptionKey);
		}
	}
}