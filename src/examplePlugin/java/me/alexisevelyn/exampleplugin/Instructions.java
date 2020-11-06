package me.alexisevelyn.exampleplugin;

public class Instructions {
	// https://pmd.github.io/latest/pmd_rules_java_errorprone.html#donotterminatevm

	// To Provide Some Meaningful Information When Ran Directly (Or Could Be Used As Installer, Up To Plugin Developer)
	@SuppressWarnings("PMD.DoNotTerminateVM")
	public static void main(String... args) {
		System.out.println("This plugin is meant to be loaded by the Crewmate server!!!");
		System.exit(1);
	}
}
