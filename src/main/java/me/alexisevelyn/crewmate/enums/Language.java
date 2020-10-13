package me.alexisevelyn.crewmate.enums;

import java.util.HashMap;

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

	public static Language getLanguage(int languageInteger) {
		return languageSearch.get(languageInteger);
	}

	public static String getLanguageName(Language language) {
		switch (language) {
			case ARABIC:
				return "Arabic";
			case ENGLISH:
				return "English";
			case FILIPINO:
				return "Filipino";
			case KOREAN:
				return "Korean";
			case OTHER:
				return "Other";
			case POLISH:
				return "Polish";
			case PORTUGUESE:
				return "Portuguese";
			case RUSSIAN:
				return "Russian";
			case SPANISH:
				return "Spanish";
			default:
				return "Unknown";
		}
	}

	static {
		for (Language languageKey : Language.values()) {
			languageSearch.put(languageKey.language, languageKey);
		}
	}
}