package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * Vanilla Supported Languages
 */
public enum Language {
	// Not A Language
	UNSPECIFIED(0b0), // 0

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
	@Deprecated LANGUAGE_THIRTY_TWO(  0b10000000000000000000000000000000L); // 2,147,483,648 - Has to be explicitly a long or it will not work.

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
	@Deprecated
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

	/**
	 * Produce a human readable list of the language array
	 *
	 * @param languages Array of Languages
	 * @return String representation of the language array
	 */
	public static String getPrintableLanguagesList(Language... languages) {
		// List of Languages Being Included In Bitfield
		StringBuilder printableLanguagesList = new StringBuilder();

		// Append Delimiters (Usually Comma + Space) To List and Then Remove Last Delimiter
		String delimiter = Main.getTranslationBundle().getString("list_delimiter_logged");
		for (Language language : languages) {
			printableLanguagesList.append(getLanguageName(language)).append(delimiter);
		}
		printableLanguagesList.delete(printableLanguagesList.length() - delimiter.length(), printableLanguagesList.length());

		return printableLanguagesList.toString();
	}

	/**
	 * Convert Language bitfield to Language[]
	 *
	 * @param languageNumber byte to represent the language bitfield
	 * @return array of Languages
	 */
	@NotNull
	public static Language[] getLanguageArray(long languageNumber) {
		ArrayList<Language> languages = new ArrayList<>();
		// Not a Bitwise Operation
		if (UNSPECIFIED.getUnsignedInt() == languageNumber) {
			// Unspecified Language
			languages.add(UNSPECIFIED);
		}

		if ((OTHER.getUnsignedInt() & languageNumber) > 0) {
			// Other Language (In Search Means Any Language?)
			languages.add(OTHER);
		}

		if ((SPANISH.getUnsignedInt() & languageNumber) > 0) {
			// Spanish Language
			languages.add(SPANISH);
		}

		if ((KOREAN.getUnsignedInt() & languageNumber) > 0) {
			// Korean Language
			languages.add(KOREAN);
		}

		if ((RUSSIAN.getUnsignedInt() & languageNumber) > 0) {
			// Russian Language
			languages.add(RUSSIAN);
		}

		if ((PORTUGUESE.getUnsignedInt() & languageNumber) > 0) {
			// Portuguese Language
			languages.add(PORTUGUESE);
		}

		if ((ARABIC.getUnsignedInt() & languageNumber) > 0) {
			// Arabic Language
			languages.add(ARABIC);
		}

		if ((FILIPINO.getUnsignedInt() & languageNumber) > 0) {
			// Filipino Language
			languages.add(FILIPINO);
		}

		if ((POLISH.getUnsignedInt() & languageNumber) > 0) {
			// Polish Language
			languages.add(POLISH);
		}

		if ((ENGLISH.getUnsignedInt() & languageNumber) > 0) {
			// English Language
			languages.add(ENGLISH);
		}

		if ((LANGUAGE_TEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TEN);
		}

		if ((LANGUAGE_ELEVEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_ELEVEN);
		}

		if ((LANGUAGE_TWELVE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWELVE);
		}

		if ((LANGUAGE_THIRTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_THIRTEEN);
		}

		if ((LANGUAGE_FOURTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_FOURTEEN);
		}

		if ((LANGUAGE_FIFTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_FIFTEEN);
		}

		if ((LANGUAGE_SIXTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_SIXTEEN);
		}

		if ((LANGUAGE_SEVENTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_SEVENTEEN);
		}

		if ((LANGUAGE_EIGHTEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_EIGHTEEN);
		}

		if ((LANGUAGE_NINETEEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_NINETEEN);
		}

		if ((LANGUAGE_TWENTY.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY);
		}

		if ((LANGUAGE_TWENTY_ONE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_ONE);
		}

		if ((LANGUAGE_TWENTY_TWO.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_TWO);
		}

		if ((LANGUAGE_TWENTY_THREE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_THREE);
		}

		if ((LANGUAGE_TWENTY_FOUR.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_FOUR);
		}

		if ((LANGUAGE_TWENTY_FIVE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_FIVE);
		}

		if ((LANGUAGE_TWENTY_SIX.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_SIX);
		}

		if ((LANGUAGE_TWENTY_SEVEN.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_SEVEN);
		}

		if ((LANGUAGE_TWENTY_EIGHT.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_EIGHT);
		}

		if ((LANGUAGE_TWENTY_NINE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_TWENTY_NINE);
		}

		if ((LANGUAGE_THIRTY.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_THIRTY);
		}

		if ((LANGUAGE_THIRTY_ONE.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_THIRTY_ONE);
		}

		if ((LANGUAGE_THIRTY_TWO.getUnsignedInt() & languageNumber) > 0) {
			// Undefined Language
			languages.add(LANGUAGE_THIRTY_TWO);
		}

		// https://stackoverflow.com/a/5061692/6828099
		// Apparently it's supposed to be marked as empty now
		return languages.toArray(new Language[0]);
	}

	static {
		for (Language languageKey : Language.values()) {
			languageSearch.put(languageKey.language, languageKey);
		}
	}
}