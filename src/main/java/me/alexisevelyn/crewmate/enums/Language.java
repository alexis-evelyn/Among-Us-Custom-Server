package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Language {
	ENGLISH(PacketHelper.getUnsignedIntLE((byte) 0x00, (byte) 0x01)),
	OTHER(PacketHelper.getUnsignedIntLE((byte) 0x01, (byte) 0x00)),
	SPANISH(PacketHelper.getUnsignedIntLE((byte) 0x02, (byte) 0x00)),
	KOREAN(PacketHelper.getUnsignedIntLE((byte) 0x04, (byte) 0x00)),
	RUSSIAN(PacketHelper.getUnsignedIntLE((byte) 0x08, (byte) 0x00)),
	PORTUGUESE(PacketHelper.getUnsignedIntLE((byte) 0x10, (byte) 0x00)),
	ARABIC(PacketHelper.getUnsignedIntLE((byte) 0x20, (byte) 0x00)),
	FILIPINO(PacketHelper.getUnsignedIntLE((byte) 0x40, (byte) 0x00)),
	POLISH(PacketHelper.getUnsignedIntLE((byte) 0x80, (byte) 0x00));

	private final long language;

	Language(long language) {
		this.language = language;
	}

	private static final java.util.Map<Long, Language> languageSearch = new HashMap<>();

	public long getLong() {
		return this.language;
	}

	@Nullable
	public static Language getLanguage(long languageInteger) {
		return languageSearch.get(languageInteger);
	}

	@NotNull
	public static String getLanguageName(@NotNull Language language) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (language) {
			case ARABIC:
				return translation.getString("arabic");
			case ENGLISH:
				return translation.getString("english");
			case FILIPINO:
				return translation.getString("filipino");
			case KOREAN:
				return translation.getString("korean");
			case OTHER:
				return translation.getString("other");
			case POLISH:
				return translation.getString("polish");
			case PORTUGUESE:
				return translation.getString("portuguese");
			case RUSSIAN:
				return translation.getString("russian");
			case SPANISH:
				return translation.getString("spanish");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Language languageKey : Language.values()) {
			languageSearch.put(languageKey.language, languageKey);
		}
	}
}