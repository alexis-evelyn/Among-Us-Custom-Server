package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Vanilla Supported Languages
 */
public enum Language {
	OTHER(1),
	SPANISH(2),
	KOREAN(4),
	RUSSIAN(8),
	PORTUGUESE(16),
	ARABIC(32),
	FILIPINO(64),
	POLISH(128),
	ENGLISH(256);

	private final int language;

	Language(int language) {
		this.language = language;
	}

	private static final java.util.Map<Integer, Language> languageSearch = new HashMap<>();

	/**
	 * Supposedly a UInt-32 according to <a href="https://wiki.weewoo.net/wiki/Languages">the wiki</a>. but I've only seen a UInt-16 LE.
	 *
	 * @return language as unsigned short (UInt-16)
	 */
	public int getUnsignedShort() {
		return this.language;
	}

	@Nullable
	public static Language getLanguage(int languageInt) {
		return languageSearch.get(languageInt);
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