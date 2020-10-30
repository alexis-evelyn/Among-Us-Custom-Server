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
	// Not A Language
	@Deprecated UNSPECIFIED(0b0), // 0

	// Existing Languages
	OTHER(     0b1), // 1
	SPANISH(   0b10), // 2
	KOREAN(    0b100), // 4
	RUSSIAN(   0b1000), // 8
	PORTUGUESE(0b10000), // 16
	ARABIC(    0b100000), // 32
	FILIPINO(  0b1000000), // 64
	POLISH(    0b10000000), // 128
	ENGLISH(   0b100000000), // 256

	// Not Implemented Languages
	@Deprecated LANGUAGE_TEN(         0b1000000000), // 512
	@Deprecated LANGUAGE_ELEVEN(      0b10000000000), // 1024
	@Deprecated LANGUAGE_TWELVE(      0b100000000000), // 2048
	@Deprecated LANGUAGE_THIRTEEN(    0b1000000000000), // 4096
	@Deprecated LANGUAGE_FOURTEEN(    0b10000000000000), // 8192
	@Deprecated LANGUAGE_FIFTEEN(     0b100000000000000), // 16,384
	@Deprecated LANGUAGE_SIXTEEN(     0b1000000000000000), // 32,768
	@Deprecated LANGUAGE_SEVENTEEN(   0b10000000000000000), // 65,536
	@Deprecated LANGUAGE_EIGHTEEN(    0b100000000000000000), // 131,072
	@Deprecated LANGUAGE_NINETEEN(    0b1000000000000000000), // 262,144
	@Deprecated LANGUAGE_TWENTY(      0b10000000000000000000), // 524,288
	@Deprecated LANGUAGE_TWENTY_ONE(  0b100000000000000000000), // 1,048,576
	@Deprecated LANGUAGE_TWENTY_TWO(  0b1000000000000000000000), // 2,097,152
	@Deprecated LANGUAGE_TWENTY_THREE(0b10000000000000000000000), // 4,194,304
	@Deprecated LANGUAGE_TWENTY_FOUR( 0b100000000000000000000000), // 8,388,608
	@Deprecated LANGUAGE_TWENTY_FIVE( 0b1000000000000000000000000), // 16,777,216
	@Deprecated LANGUAGE_TWENTY_SIX(  0b10000000000000000000000000), // 33,554,432
	@Deprecated LANGUAGE_TWENTY_SEVEN(0b100000000000000000000000000), // 67,108,864
	@Deprecated LANGUAGE_TWENTY_EIGHT(0b1000000000000000000000000000), // 134,217,728
	@Deprecated LANGUAGE_TWENTY_NINE( 0b10000000000000000000000000000), // 268,435,456
	@Deprecated LANGUAGE_THIRTY(      0b100000000000000000000000000000), // 536,870,912
	@Deprecated LANGUAGE_THIRTY_ONE(  0b1000000000000000000000000000000), // 1,073,741,824
	@Deprecated LANGUAGE_THIRTY_TWO(  0b10000000000000000000000000000000); // 2,147,483,648

	// Max Language Value - 0b10000000000000000000000000000000

	private final long language;

	Language(long language) {
		this.language = language;
	}

	private static final java.util.Map<Long, Language> languageSearch = new HashMap<>();

	/**
	 * Supposedly a UInt-32 according to <a href="https://wiki.weewoo.net/wiki/Languages">the wiki</a>. but I've only seen a UInt-16 LE.
	 *
	 * @return language as unsigned short (UInt-16)
	 */
	public long getUnsignedInt() {
		return this.language;
	}

	@Nullable
	public static Language getLanguage(long languageLong) {
		return languageSearch.get(languageLong);
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
			case LANGUAGE_TEN:
				return translation.getString("language_ten");
			case LANGUAGE_ELEVEN:
				return translation.getString("language_eleven");
			case LANGUAGE_TWELVE:
				return translation.getString("language_twelve");
			case LANGUAGE_THIRTEEN:
				return translation.getString("language_thirteen");
			case LANGUAGE_FOURTEEN:
				return translation.getString("language_fourteen");
			case LANGUAGE_FIFTEEN:
				return translation.getString("language_fifteen");
			case LANGUAGE_SIXTEEN:
				return translation.getString("language_sixteen");
			case LANGUAGE_SEVENTEEN:
				return translation.getString("language_seventeen");
			case LANGUAGE_EIGHTEEN:
				return translation.getString("language_eighteen");
			case LANGUAGE_NINETEEN:
				return translation.getString("language_nineteen");
			case LANGUAGE_TWENTY:
				return translation.getString("language_twenty");
			case LANGUAGE_TWENTY_ONE:
				return translation.getString("language_twenty_one");
			case LANGUAGE_TWENTY_TWO:
				return translation.getString("language_twenty_two");
			case LANGUAGE_TWENTY_THREE:
				return translation.getString("language_twenty_three");
			case LANGUAGE_TWENTY_FOUR:
				return translation.getString("language_twenty_four");
			case LANGUAGE_TWENTY_FIVE:
				return translation.getString("language_twenty_five");
			case LANGUAGE_TWENTY_SIX:
				return translation.getString("language_twenty_six");
			case LANGUAGE_TWENTY_SEVEN:
				return translation.getString("language_twenty_seven");
			case LANGUAGE_TWENTY_EIGHT:
				return translation.getString("language_twenty_eight");
			case LANGUAGE_TWENTY_NINE:
				return translation.getString("language_twenty_nine");
			case LANGUAGE_THIRTY:
				return translation.getString("language_thirty");
			case LANGUAGE_THIRTY_ONE:
				return translation.getString("language_thirty_one");
			case LANGUAGE_THIRTY_TWO:
				return translation.getString("language_thirty_two");
			case UNSPECIFIED:
				return translation.getString("unspecified");
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