package me.alexisevelyn.crewmate.enums.hazel;

public enum SendOption {
	HELLO((byte) 0x08),
	DISCONNECT((byte) 0x09),
	ACKNOWLEDGEMENT((byte) 0x0A),
	FRAGMENT((byte) 0x0B),
	PING((byte) 0x0C);

	private final byte sendOption;

	SendOption(byte sendOption) {
		this.sendOption = sendOption;
	}


	public byte getSendOption() {
		return this.sendOption;
	}
}