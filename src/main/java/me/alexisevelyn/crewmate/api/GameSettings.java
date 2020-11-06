package me.alexisevelyn.crewmate.api;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.MapSearch;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

public class GameSettings {
	private int payloadLength;
	private int gameSettingsVersion;
	private int maxPlayers;
	private Language[] languages;
	private Map[] maps;
	private float playerSpeedModifier;
	private float crewLightModifier;
	private float imposterLightModifier;
	private float killCooldown;
	private int commonTaskCount;
	private int longTaskCount;
	private int shortTaskCount;
	private long emergencyMeetingCount;
	private int imposterCount;
	private int killDistance;
	private long discussionTime;
	private long voteTime;
	private boolean defaultSettings;
	private int emergencyCooldown;
	private boolean confirmEjects;
	private boolean visualTasks;
	private boolean anonymousVoting;
	private TaskbarUpdates taskbarUpdates;

	public GameSettings(byte... settingsBytes) throws InvalidBytesException {
		this(false, settingsBytes);
	}

	public GameSettings(boolean search, byte... settingsBytes) throws InvalidBytesException {
		this.parseGameSettings(search, settingsBytes);
	}

	private void parseGameSettings(boolean search, @NotNull byte... payloadBytes) throws InvalidBytesException {
		/*
	     00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a 2b 2c 2d 2e
	     --------------------------------------------------------------------------------------------------------------------------------------------
	     2A 02 0A 00 01 00 00 07 00 00 80 3F 00 00 80 3F 00 00 C0 3F 00 00 70 41 01 01 02 01 00 00 00 02 01 0F 00 00 00 78 00 00 00 01 0F - Search Packet
		 2A 02 09 02 00 00 00 01 00 00 C0 3F 00 00 00 3F 00 00 80 3F 00 00 F0 41 02 02 03 01 00 00 00 03 00 0F 00 00 00 78 00 00 00 00 0F - Host Game Packet
	     --------------------------------------------------------------------------------------------------------------------------------------------
	     PL GV MP LA LA LA LA CM PS PS PS PS CL CL CL CL IL IL IL IL KC KC KC KC CT LT ST EM EM EM EM IC KD DT DT DT DT VT VT VT VT DS EC CE VI AV TU
	     PL = Payload Length As Packed Int (Supposed to be uint-32?) (Length 42)
	     GV = Game Options Version (Version 2)
	     MP = Max Players (10 or 9)
	     LA = Language (English or Spanish)
	     CM = Chosen Map (Skeld, Mira, Polus as Bitwise or Mira)
	     PS = Player Speed Modifier (Float 32)
	     CL = Crew Light Modifier (Float 32)
	     IL = Imposter Light Modifier (Float 32)
	     KC = Kill Cooldown (Float 32)
	     CT = Common Tasks (Count) (1 or 2)
	     LT = Long Tasks (Count) (1 or 2)
	     ST = Short Tasks (Count) (2 or 3)
	     EM = Emergencies (Int-32)
	     IC = Imposter Count (2 or 3)
	     KD = Kill Distance (Unknown Units) (Medium or Short)
	     DT = Discussion Time (Int-32)
	     VT = Voting Time (Int-32)
	     DS = Default Settings (true or false)
	     EC = Emergency Cooldown (Version 2+)

	     The game doesn't send these at all when starting a lobby for hosting the game
	     CE = Confirm Ejects (Version 3+)
	     VI = Visual Tasks (Version 3+)
	     AV = Anonymous Voting (Version 4+)
	     TU = Task Bar Updates (Version 4+)
    */

		// Ensure Minimum Size
		if (payloadBytes.length < 42)
			throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 43));

		// Data
		payloadLength = PacketHelper.unpackInteger(payloadBytes[0]);
		gameSettingsVersion = payloadBytes[1];

		if (payloadLength > payloadBytes.length)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("game_settings_length_mismatch"));

		byte[] gameSettingsBytes = PacketHelper.extractSecondPartBytes(2, payloadBytes);

		// LogHelper.printLine("Game Settings Version: " + gameSettingsVersion);
		switch (gameSettingsVersion) {
			case 1:
				parseGameSettingsV1(search, gameSettingsBytes);
				break;
			case 2:
				if (payloadLength < 41)
					throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 41));

				parseGameSettingsV2(search, gameSettingsBytes);
				break;
			case 3:
				if (payloadLength < 43)
					throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 43));

				parseGameSettingsV3(search, gameSettingsBytes);
				break;
			case 4:
				if (payloadLength < 45)
					throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 45));

				parseGameSettingsV4(search, gameSettingsBytes);
			default:
				// throw new InvalidBytesException(Main.getTranslationBundle().getString("unsupported_game_settings_version")); // Should this be an exception?
				parseGameSettingsV1(search, gameSettingsBytes);
		}
	}

	public void setMaxPlayers(int maxPlayers) {
		maxPlayers = Math.max(0, maxPlayers);
		maxPlayers = Math.min(15, maxPlayers); // TODO: Make this configurable

		this.maxPlayers = maxPlayers;
	}

	public void setLanguages(long language) {
		this.languages = Language.getLanguageArray(language);
	}

	public void setMaps(boolean search, byte map) {
		if (search)
			setMapsSearch(map);
		else
			setMapsHost(map);
	}

	public void setMapsSearch(byte map) {
		this.maps = MapSearch.getMapArray(map);
	}

	public void setMapsHost(byte map) {
		this.maps = new Map[] {Map.getMap(map)};
	}

	public void setPlayerSpeedModifier(float playerSpeedModifier) {
		this.playerSpeedModifier = playerSpeedModifier;
	}

	public void setCrewLightModifier(float crewLightModifier) {
		this.crewLightModifier = crewLightModifier;
	}

	public void setImposterLightModifier(float imposterLightModifier) {
		this.imposterLightModifier = imposterLightModifier;
	}

	public void setKillCooldown(float killCooldown) {
		this.killCooldown = killCooldown;
	}

	public void setCommonTaskCount(int commonTaskCount) {
		this.commonTaskCount = commonTaskCount;
	}

	public void setLongTaskCount(int longTaskCount) {
		this.longTaskCount = longTaskCount;
	}

	public void setShortTaskCount(int shortTaskCount) {
		this.shortTaskCount = shortTaskCount;
	}

	public void setEmergencyMeetingCount(long emergencyMeetingCount) {
		this.emergencyMeetingCount = emergencyMeetingCount;
	}

	public void setImposterCount(int imposterCount) {
		imposterCount = Math.max(0, imposterCount);
		imposterCount = Math.min(3, imposterCount); // TODO: Make this configurable

		this.imposterCount = imposterCount;
	}

	public void setKillDistance(int killDistance) {
		this.killDistance = killDistance;
	}

	public void setDiscussionTime(long discussionTime) {
		this.discussionTime = discussionTime;
	}

	public void setVoteTime(long voteTime) {
		this.voteTime = voteTime;
	}

	public void setDefaultSettings(byte defaultSettings) {
		// Boolean
		if (defaultSettings == BooleanByte.FALSE.getBooleanByte())
			this.defaultSettings = false;
		else if (defaultSettings == BooleanByte.TRUE.getBooleanByte())
			this.defaultSettings = true;
	}

	public void setEmergencyCooldown(int emergencyCooldown) {
		this.emergencyCooldown = emergencyCooldown;
	}

	public void setConfirmEjects(byte confirmEjects) {
		// Boolean
		if (confirmEjects == BooleanByte.FALSE.getBooleanByte())
			this.confirmEjects = false;
		else if (confirmEjects == BooleanByte.TRUE.getBooleanByte())
			this.confirmEjects = true;
	}

	public void setVisualTasks(byte visualTasks) {
		// Boolean
		if (visualTasks == BooleanByte.FALSE.getBooleanByte())
			this.visualTasks = false;
		else if (visualTasks == BooleanByte.TRUE.getBooleanByte())
			this.visualTasks = true;
	}

	public void setAnonymousVoting(byte anonymousVoting) {
		// Boolean
		if (anonymousVoting == BooleanByte.FALSE.getBooleanByte())
			this.anonymousVoting = false;
		else if (anonymousVoting == BooleanByte.TRUE.getBooleanByte())
			this.anonymousVoting = true;
	}

	public void setTaskBarUpdates(byte taskBarUpdates) {
		if (taskBarUpdates == TaskbarUpdates.TASKBAR_ALWAYS.getTaskbarStateByte()) // Taskbar Always
			taskbarUpdates = TaskbarUpdates.TASKBAR_ALWAYS;

		else if (taskBarUpdates == TaskbarUpdates.TASKBAR_MEETINGS.getTaskbarStateByte()) // Taskbar Meetings
			taskbarUpdates = TaskbarUpdates.TASKBAR_MEETINGS;

		else if (taskBarUpdates == TaskbarUpdates.TASKBAR_NEVER.getTaskbarStateByte()) // Taskbar Never
			taskbarUpdates = TaskbarUpdates.TASKBAR_NEVER;
	}

	@API(status = API.Status.INTERNAL)
	public int getPayloadLength() {
		return this.payloadLength;
	}

	public int getGameSettingsVersion() {
		return this.gameSettingsVersion;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	@NotNull
	public Language[] getLanguages() {
		if (languages == null || languages.length <= 0 || languages[0].equals(Language.UNSPECIFIED))
			return new Language[] {Language.OTHER};

		return this.languages;
	}

	public float getPlayerSpeedModifier() {
		return this.playerSpeedModifier;
	}

	public float getCrewLightModifier() {
		return this.crewLightModifier;
	}

	public float getImposterLightModifier() {
		return this.imposterLightModifier;
	}

	public float getKillCooldown() {
		return this.killCooldown;
	}

	public int getCommonTaskCount() {
		return this.commonTaskCount;
	}

	public int getLongTaskCount() {
		return this.longTaskCount;
	}

	public int getShortTaskCount() {
		return this.shortTaskCount;
	}

	public long getEmergencyMeetingCount() {
		return this.emergencyMeetingCount;
	}

	@NotNull
	public Map[] getMaps() {
		// Should I Specify A Default Map?
		if (maps == null || maps.length <= 0 || maps[0].equals(Map.UNSPECIFIED))
			return new Map[] {Map.SKELD};

		return this.maps;
	}

	public int getImposterCount() {
		return this.imposterCount;
	}

	public int getKillDistance() {
		return this.killDistance;
	}

	public long getDiscussionTime() {
		return this.discussionTime;
	}

	public long getVoteTime() {
		return this.voteTime;
	}

	public boolean isDefaultSettings() {
		return this.defaultSettings;
	}

	public int getEmergencyCooldown() {
		return this.emergencyCooldown;
	}

	public boolean isConfirmEjects() {
		return this.confirmEjects;
	}

	public boolean isVisualTasks() {
		return this.visualTasks;
	}

	public boolean isAnonymousVoting() {
		return this.anonymousVoting;
	}

	@NotNull
	public TaskbarUpdates getTaskBarUpdates() {
		// Should I Specify A Default Value
		if (taskbarUpdates == null)
			return TaskbarUpdates.TASKBAR_ALWAYS;

		return this.taskbarUpdates;
	}

	private void parseGameSettingsV1(boolean search, @NotNull byte... payloadBytes) {
		setMaxPlayers(payloadBytes[0]);
		setLanguages(PacketHelper.getUnsignedIntLE(payloadBytes[1], payloadBytes[2], payloadBytes[3], payloadBytes[4]));
		setMaps(search, payloadBytes[5]);

		// Floats
		setPlayerSpeedModifier(PacketHelper.getUnsignedFloatLE(payloadBytes[6], payloadBytes[7], payloadBytes[8], payloadBytes[9]));
		setCrewLightModifier(PacketHelper.getUnsignedFloatLE(payloadBytes[10], payloadBytes[11], payloadBytes[12], payloadBytes[13]));
		setImposterLightModifier(PacketHelper.getUnsignedFloatLE(payloadBytes[14], payloadBytes[15], payloadBytes[16], payloadBytes[17]));
		setKillCooldown(PacketHelper.getUnsignedFloatLE(payloadBytes[18], payloadBytes[19], payloadBytes[20], payloadBytes[21]));

		// Counts
		setCommonTaskCount(payloadBytes[22]);
		setLongTaskCount(payloadBytes[23]);
		setShortTaskCount(payloadBytes[24]);
		setEmergencyMeetingCount(PacketHelper.getUnsignedIntLE(payloadBytes[25], payloadBytes[26], payloadBytes[27], payloadBytes[28]));
		setImposterCount(payloadBytes[29]);

		// Distance
		setKillDistance(payloadBytes[30]);

		// Time
		setDiscussionTime(PacketHelper.getUnsignedIntLE(payloadBytes[31], payloadBytes[32], payloadBytes[33], payloadBytes[34]));
		setVoteTime(PacketHelper.getUnsignedIntLE(payloadBytes[35], payloadBytes[36], payloadBytes[37], payloadBytes[38]));

		// Default Settings
		setDefaultSettings(payloadBytes[39]);
	}

	private void parseGameSettingsV2(boolean search, @NotNull byte... payloadBytes) {
		parseGameSettingsV1(search, payloadBytes);

		// V2
		setEmergencyCooldown(payloadBytes[40]);
	}

	private void parseGameSettingsV3(boolean search, @NotNull byte... payloadBytes) {
		parseGameSettingsV2(search, payloadBytes);

		setConfirmEjects(payloadBytes[41]);
		setVisualTasks(payloadBytes[42]);
	}

	private void parseGameSettingsV4(boolean search, @NotNull byte... payloadBytes) {
		parseGameSettingsV3(search, payloadBytes);

		setAnonymousVoting(payloadBytes[43]);
		setTaskBarUpdates(payloadBytes[44]);
	}

	public enum TaskbarUpdates {
		TASKBAR_ALWAYS((byte) 0x00),
		TASKBAR_MEETINGS((byte) 0x01),
		TASKBAR_NEVER((byte) 0x02);

		private final byte taskbarState;

		public byte getTaskbarStateByte() {
			return this.taskbarState;
		}

		TaskbarUpdates(byte bite) {
			this.taskbarState = bite;
		}
	}

	private enum BooleanByte {
		FALSE((byte) 0x00),
		TRUE((byte) 0x01);

		private final byte booleanByte;

		public byte getBooleanByte() {
			return this.booleanByte;
		}

		BooleanByte(byte bite) {
			this.booleanByte = bite;
		}
	}
}
