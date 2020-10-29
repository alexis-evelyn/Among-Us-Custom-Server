package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum AlterGame {
	CHANGE_PRIVACY((byte) 0x0a); // 0x0a = 10

	private final byte alterGameFlag;

	AlterGame(byte alterGameFlag) {
		this.alterGameFlag = alterGameFlag;
	}

	private static final java.util.Map<Byte, AlterGame> alterGameFlagSearch = new HashMap<>();

	public byte getAlterGameFlag() {
		return this.alterGameFlag;
	}

	@Nullable
	public static AlterGame getAlterGameFlag(byte alterGameFlag) {
		return alterGameFlagSearch.get(alterGameFlag);
	}

	@NotNull
	public static String getAlterGameFlagName(@NotNull AlterGame alterGameFlag) {
		ResourceBundle translation = Main.getTranslationBundle();

		//noinspection SwitchStatementWithTooFewBranches
		switch (alterGameFlag) {
			case CHANGE_PRIVACY:
				return translation.getString("alter_game_change_privacy");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (AlterGame alterGameFlagKey : AlterGame.values()) {
			alterGameFlagSearch.put(alterGameFlagKey.alterGameFlag, alterGameFlagKey);
		}
	}
}
