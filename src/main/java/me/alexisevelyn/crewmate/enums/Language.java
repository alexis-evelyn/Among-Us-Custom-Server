package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Language {
	ENGLISH(convertToInt(0x00, 0x01)),
	OTHER(convertToInt(0x01, 0x00)),
	SPANISH(convertToInt(0x02, 0x00)),
	KOREAN(convertToInt(0x04, 0x00)),
	RUSSIAN(convertToInt(0x08, 0x00)),
	PORTUGUESE(convertToInt(0x10, 0x00)),
	ARABIC(convertToInt(0x20, 0x00)),
	FILIPINO(convertToInt(0x40, 0x00)),
	POLISH(convertToInt(0x80, 0x00));

	public static int convertToInt(int first, int second) {
		// https://stackoverflow.com/a/4768950
		return ((first & 0xff) << 8) | (second & 0xff);
	}

	private final int language;

	Language(int language) {
		this.language = language;
	}

	private static final java.util.Map<Integer, Language> languageSearch = new HashMap<>();

	public int getLanguage() {
		return this.language;
	}

	@Nullable
	public static Language getLanguage(int languageInteger) {
		return languageSearch.get(languageInteger);
	}

	@NotNull
	public static String getLanguageName(@NotNull Language language) {
		ResourceBundle translation = Main.getTranslationBundle();

		if (language == null)
			return translation.getString("unknown");

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