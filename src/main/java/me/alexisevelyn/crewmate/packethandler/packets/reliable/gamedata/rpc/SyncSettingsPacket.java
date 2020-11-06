package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class SyncSettingsPacket {
	@NotNull
	public static byte[] getLobbyGameSettings(DatagramPacket packet) {
		// TODO: Figure out what this packet is for!!!

		// Username Hi
		// C->S - 0000   01 00 04 ac 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 01 0f 01 01 00   .......x........
		// C->S - 0080   00 05 00 02 04 06 02 48 69 03 00 02 04 08 00 03   .......Hi.......
		// C->S - 0090   00 02 04 11 00 03 00 02 04 09 2b 03 00 02 04 0a   ..........+.....
		// C->S - 00a0   00 0e 00 02 02 1e 09 00 00 02 48 69 00 00 00 00   ..........Hi....
		// C->S - 00b0   00 00                                             ..

		// Username Alexis
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 01 0f 01 01 00   .......x........
		// C->S - 0080   00 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 00 03 00 02 04 11 00 03 00 02 04 09 2b 03   ..............+.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 00 00 00 00 00 00                     exis......

		// Username Alexis - Anonymous Votes On and Recommended Off
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 00 0f 01 01 01   .......x........
		// C->S - 0080   00 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 08 03 00 02 04 11 00 03 00 02 04 09 2c 03   ..............,.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 08 00 00 00 00 00                     exis......

		// Username Alexis - Everything Changed From Default
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 40 40 00 00   ............@@..
		// C->S - 0060   a0 40 00 00 a0 40 00 00 70 42 02 03 05 09 00 00   .@...@..pB......
		// C->S - 0070   00 01 02 78 00 00 00 2c 01 00 00 00 3c 00 00 01   ...x...,....<...
		// C->S - 0080   01 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 08 03 00 02 04 11 00 03 00 02 04 09 2c 03   ..............,.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 08 00 00 00 00 00                     exis......

		if (packet.getLength() < 4)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("initial_game_settings_invalid_size"));

		byte[] buffer = packet.getData();

		// Must Equal 01 00 03 (Join Game Via Code) or 01 00 04 (Create Game)
		// TODO: Toss Check!!!
		if (!(buffer[0] == SendOption.RELIABLE.getByte() && buffer[1] == 0x00) || !(buffer[2] == 0x04 || buffer[2] == 0x03))
			// return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("initial_game_settings_unknown_join_type"));
			return new byte[0];

		byte unknown = buffer[3]; // 180 for Alexis and 172 for Hi - +8
		byte unknownTwo = buffer[129]; // 9 for Alexis and 5 for Hi - +4

		byte nameLength = buffer[134];

		// Extract Name Bytes From Buffer
		byte[] nameBytes = new byte[nameLength];
		System.arraycopy(buffer, 135, nameBytes, 0, nameLength);

		byte unknownThree = buffer[159 + nameLength]; // Probably Associated With nameLength (Is 18 for Alexis and 14 for Hi) - +4
		byte unknownFour = buffer[164 + nameLength]; // Is 13 for Alexis and 9 for Hi - +4

		byte nameLengthTwo = buffer[167 + nameLength];
		byte[] nameBytesTwo = new byte[nameLength];
		System.arraycopy(buffer, 168 + nameLength, nameBytesTwo, 0, nameLengthTwo);

		// Temporary Logging, So No Translations
		LogHelper.printLineErr("Unknown: " + unknown);
		LogHelper.printLineErr("Unknown 2: " + unknownTwo);
		LogHelper.printLineErr("Unknown 3: " + unknownThree);
		LogHelper.printLineErr("Unknown 4: " + unknownFour);

		LogHelper.printLineErr("Name: " + new String(nameBytes, StandardCharsets.UTF_8));
		LogHelper.printLineErr("Name 2: " + new String(nameBytesTwo, StandardCharsets.UTF_8));

		return new byte[0];
	}
}
