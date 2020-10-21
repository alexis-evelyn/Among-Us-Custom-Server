package me.alexisevelyn.crewmate.enums.hazel;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.ReliablePacketType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

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

	@Nullable
	public static SendOption getSendOption(byte sendOptionValue) {
		return sendOptionHashMap.get(sendOptionValue);
	}

	@NotNull
	public static String getSendOptionName(@NotNull SendOption sendOptionType) {
		ResourceBundle translation = Main.getTranslationBundle();

		if (sendOptionType == null)
			return translation.getString("unknown");

		switch (sendOptionType) {
			case NONE:
				return translation.getString("send_option_none");
			case RELIABLE:
				return translation.getString("send_option_reliable");
			case HELLO:
				return translation.getString("send_option_hello");
			case DISCONNECT:
				return translation.getString("send_option_disconnect");
			case ACKNOWLEDGEMENT:
				return translation.getString("send_option_acknowledgment");
			case FRAGMENT:
				return translation.getString("send_option_fragment");
			case PING:
				return translation.getString("send_option_ping");
			default:
				return translation.getString("unknown");
		}
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