package me.alexisevelyn.crewmate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ValidateWordLists {
	@Test
	public void testMainList() {
		URL list = Main.class.getClassLoader().getResource("codes/words.txt");

		this.validateList(list);
	}

	@Test
	public void testNamesList() {
		URL list = Main.class.getClassLoader().getResource("codes/names.txt");

		this.validateList(list);
	}

	@Test
	public void testAcronymList() {
		URL list = Main.class.getClassLoader().getResource("codes/acronym.txt");

		this.validateList(list);
	}

	@Test
	public void testProfanityList() {
		URL list = Main.class.getClassLoader().getResource("codes/prof. anity.txt");

		this.validateList(list);
	}

	@Test
	public void testUltraBannedList() {
		URL list = Main.class.getClassLoader().getResource("codes/ultrabanned.txt");

		this.validateList(list);
	}

	@Test
	public void testOtherList() {
		URL list = Main.class.getClassLoader().getResource("codes/other.txt");

		this.validateList(list);
	}

	private void validateList(URL list) {
		if (list == null) {
			LogHelper.printLineErr("List is Null!!!");
			return;
		}

		try {
			if (!this.checkChosenList(list)) {
				Assertions.fail("Failed List Check!!!");
			}
		} catch (IOException exception) {
			LogHelper.printLineErr("List IO Exception!!!");
			exception.printStackTrace();
		}
	}

	/**
	 * Checks Game Code List For Invalid Room Codes
	 *
	 * @param list URL of File To Read
	 * @return false if word that's not 4 or 6 letters is encountered or if the word fails to be a valid game code
	 * @throws IOException If list contents could not be read
	 */
	private boolean checkChosenList(URL list) throws IOException {
		byte[] listBytes = list.openStream().readAllBytes();
		String listContents = new String(listBytes, StandardCharsets.UTF_8);

		boolean passedCheck = true;
		Scanner scanner = new Scanner(listContents);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

            if ((line.length() != 4 && line.length() != 6) || !line.matches("([A-Z]|[a-z])+")) {
                LogHelper.printLineErr(String.format("Word: '%s' | Character Count: %d", line, line.length()));
	            passedCheck = false;
            }
		}

		scanner.close();

		return passedCheck;
	}
}
