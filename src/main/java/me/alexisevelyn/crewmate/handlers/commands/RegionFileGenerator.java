package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class RegionFileGenerator implements Command {
	public void execute(String command, Terminal terminal) {
		// TODO: Take Input From Server Owner/Administrator
		String[] arguments = command.trim().split("\\s+");

		// TODO: Replace With Better Argument Parser
		if (arguments.length <= 3)
			this.simpleRegionFileSettings(arguments);
		else
			this.simpleNamedRegionFileSettings(arguments);
	}

	private void simpleRegionFileSettings(String... arguments) {
		ResourceBundle translation = Main.getTranslationBundle();

		// Defaults
		String ipAddressRaw = "127.0.0.1";
		int port = 22023;

		// Default Arguments
		if (arguments.length == 2) {
			ipAddressRaw = arguments[1];
		} else if (arguments.length >= 3) {
			ipAddressRaw = arguments[1];

			try {
				port = Integer.parseInt(arguments[2]);
			} catch (NumberFormatException e) {
				LogHelper.printLineErr(translation.getString("region_file_generator_command_invalid_port"));
			}
		}

		LogHelper.printLine(String.format(translation.getString("region_file_generator_command_settings_simple"), ipAddressRaw, port));
		this.createRegionFile(ipAddressRaw, port);
	}

	private void simpleNamedRegionFileSettings(String... arguments) {
		ResourceBundle translation = Main.getTranslationBundle();
		String displayName;
		String ipAddressRaw;
		int port;

		// Set IP Address
		ipAddressRaw = arguments[1];

		// Set Port
		try {
			port = Integer.parseInt(arguments[2]);
		} catch (NumberFormatException e) {
			LogHelper.printLineErr(translation.getString("region_file_generator_command_invalid_port"));
			return;
		}

		// Set Display Name
		displayName = arguments[3];

		LogHelper.printLine(String.format(translation.getString("region_file_generator_command_settings_simple_named"), displayName, ipAddressRaw, port));
		this.createRegionFile(ipAddressRaw, port, displayName);
	}

	private void createRegionFile(@NotNull String ipAddressRaw, int port) {
		String displayName = Main.getTranslationBundle().getString("region_file_generator_command_default_display_name");

		this.createRegionFile(ipAddressRaw, port, displayName);
	}

	private void createRegionFile(@NotNull String ipAddressRaw, int port, @NotNull String displayName) {
		String masterServerName = "Crewmate-Master-1";
		File outFile = new File("regionInfo.dat");

		this.createRegionFile(ipAddressRaw, port, displayName, masterServerName, outFile);
	}

	private void createRegionFile(@NotNull String ipAddressRaw, int port, @NotNull String displayName, @NotNull String masterServerName, @NotNull File outFile) {
		try {
			InetAddress ipAddress = InetAddress.getByName(ipAddressRaw);

			byte[] regionFileData = createRegionFileBytes(ipAddress, port, displayName, masterServerName);

			saveRegionFile(outFile, regionFileData);
		} catch (UnknownHostException e) {
			LogHelper.printLineErr(
					String.format(
							Main.getTranslationBundle().getString("region_file_generator_command_unknown_host"),
							ipAddressRaw
					)
			);
		} catch (IOException e) {
			LogHelper.printLineErr(
					String.format(
							Main.getTranslationBundle().getString("region_file_generator_command_failed_create_file"),
							outFile.getAbsolutePath()
					)
			);

			// Print Stacktrace
			// LogHelper.printLineErr(Arrays.toString(e.getStackTrace()));
			e.printStackTrace();
		}
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("region_file_generator_command");
	}

	@Override
	public String getHelp() {
		String helpString = Main.getTranslationBundle().getString("region_file_generator_command_help");

		return String.format(helpString, System.getProperty("user.dir"));
	}

	private static byte[] createRegionFileBytes(InetAddress ipAddress, int port, String displayName, String masterServerName) {
		// TODO: Add Support For Multiple Masters

		// 00000000: 0000 0000 0843 7265 776d 6174 6509 3132  .....Crewmate.12
		// 00000010: 372e 302e 302e 3101 0000 0011 4372 6577  7.0.0.1.....Crew
		// 00000020: 6d61 7465 2d4d 6173 7465 722d 317f 0000  mate-Master-1...
		// 00000030: 0107 5600 0000 00                        ..V....

		// Int-32 0
		byte[] header = {0x00, 0x00, 0x00, 0x00};

		// Display Name
		byte[] displayNameBytes = displayName.getBytes();
		byte[] displayNameLength = {(byte) displayNameBytes.length};

		// IP Address
		byte[] ipAddressBytes = ipAddress.getHostAddress().getBytes();
		byte[] ipAddressLength = {(byte) ipAddressBytes.length};

		// Number of Masters
		byte[] numberOfMasters = {(byte) 1};

		// Message Length and Flag (TODO: Verify)
		byte[] messageLength = {0x00, 0x00};
		byte[] messageFlag = {0x00};

		// Master Name
		byte[] masterNameBytes = masterServerName.getBytes();
		byte[] masterNameLength = {(byte) masterNameBytes.length};

		// IP Address Byte Form
		byte[] ipAddressByteForm = ipAddress.getAddress();

		// Port - Converts Integer port to bytes INT16 - Little Endian (BA)
		byte[] portBytes = PacketHelper.convertShortToLE((short) port);

//		LogHelper.print("Port Bytes: ");
//		LogHelper.printPacketBytes(portBytes.length, portBytes);
//		LogHelper.printLine();

		// Footer
		byte[] footerBytes = {0x00, 0x00, 0x00, 0x00};

		return PacketHelper.mergeBytes(header,
				displayNameLength,
				displayNameBytes,
				ipAddressLength,
				ipAddressBytes,
				numberOfMasters,
				messageLength,
				messageFlag,
				masterNameLength,
				masterNameBytes,
				ipAddressByteForm,
				portBytes,
				footerBytes);
	}

	private static void saveRegionFile(File outFile, byte... bytes) throws IOException {
		if (outFile.exists() && !outFile.delete())
			throw new IOException("Failed to delete existing file!!!");

		if (!outFile.createNewFile())
			throw new IOException("Failed to create new file!!!");

		try (FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
			fileOutputStream.write(bytes);

			fileOutputStream.flush();
			// fileOutputStream.close(); // Apparently this is redundant

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("region_file_generator_command_success_create_file"), outFile.getAbsolutePath()));
		}
	}
}
