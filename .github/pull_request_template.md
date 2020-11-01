## Description

Please include a summary of the change and which issue(s) have been fixed. Please also include relevant motivation and context. List any dependencies that are required for this change.

Fixes #issue ([1.](#reference-markers) e.g. #1) 

## Type of change

- [ ] Bug fix (non-breaking change which fixes an issue)
- [ ] New feature (non-breaking change which adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] This change requires a documentation update
- [ ] This change is a documentation update ([2.](#reference-markers) only applicable if no code changed except for Javadoc comments)
- [ ] This change is a translation file update ([3.](#reference-markers) only applicable if no code changed except for translation necessary changes)

## How Has This Been Tested?

Please describe the tests that you ran to verify your changes. Provide instructions, so we can reproduce. Please also list any relevant details for your test configuration.

- [ ] Test A
- [ ] Test B

**Test Configuration**:
- OS: [e.g. OSX Catalina 10.15.6]
- Java Version: [e.g. OpenJDK 64-Bit Server VM GraalVM CE 20.1.0 (build 11.0.7+10-jvmci-20.1-b02, mixed mode, sharing)]

## Checklist:

- [ ] My code follows the style guidelines of this project. See [CONTRIBUTING.md][contributing-file] for more info.
- [ ] My code keeps bracketless single If/Else statements where reasonable
- [ ] My code uses annotations where reasonable ([4.](#reference-markers) e.g. @NotNull where returns are never null and in method arguments where null is unexpected)
- [ ] I have performed a self-review of my own code
- [ ] I have commented my code, particularly in hard-to-understand areas
- [ ] I have made corresponding changes to the documentation
- [ ] If this is a translation update, check this box to signal that you would not/could not use Crowdin ([5.](#reference-markers) e.g. if you needed to update the code for a translation to work properly)

## Reference Markers:

1. The issue should be referenced in a way the Github can autolink it (e.g. #1). If the issue does not exist yet, create it and reference it. This is important because the auto-changelog generator uses issues and PRs to generate the changelog.
2. The Github Pages /docs/ does not count as code for this purpose, so it falls under here.
3. Translation necessary changes refers to parameters in code such as `String.format` or `LogHelper.formatNamed`.
Basically, the parameters in code would not work with the translation for that locale without changes to the code.
Please update the English/US translation files if applicable. Crowdin's setup to import the English/US files for translation.
4. Use Jetbrain's annotations over JavaX. The PR will be modified/denied unless the annotation cannot be performed with Jetbrains (and can be performed with JavaX).
5. If this is just a simple translation which doesn't modify code, then submit the translation through [Crowdin][crowdin-link] instead.

[crowdin-link]: <https://crwd.in/crewmate> "Crowdin Link"
[contributing-file]: <../CONTRIBUTING.md> "Contributing File"