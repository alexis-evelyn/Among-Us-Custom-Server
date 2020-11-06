# Contribution

This file is for describing how to contribute to the Crewmate project.

## Adding Dependencies

If you find yourself needing to add a dependency, there're some requirements that need to be fulfilled for the dependency to be added.

Requirements
* Make sure to only use the dependency if it can be substantially used in the project. So, adding a dependency just for one method call is not okay.
* The dependency's license info and link must be included above the dependency import in [build.gradle](build.gradle).
* The dependency's version info must be placed in [gradle.properties](gradle.properties) and then referenced in [build.gradle](build.gradle).
* An easily readable way of finding the latest version of a dependency must be put above the version line in a comment in [gradle.properties](gradle.properties). This allows for easily updating the dependency when needed.
* All licenses of the dependencies must be copied to [the licenses folder in META-INF](src/main/resources/META-INF/licenses).
* Also, the license information must also be appended to the [NOTICE file](src/main/resources/META-INF/NOTICE). Please stick to the format of the existing entries.

An example of [build.gradle](build.gradle) license info.

```Groovy
// License (Dual Licensed Under EPL v1.0 and LGPL 2.1) - https://logback.qos.ch/license.html
implementation "ch.qos.logback:logback-classic:${project.logback_version}"
shadow "ch.qos.logback:logback-classic:${project.logback_version}"

// License (Identical to MIT) - http://slf4j.org/license.html
implementation "org.slf4j:slf4j-api:${project.slf4j_api_version}"
shadow "org.slf4j:slf4j-api:${project.slf4j_api_version}"

// License (MIT) - https://github.com/luaj/luaj/blob/master/LICENSE
implementation "org.luaj:luaj-jse:${project.luaj_version}"
shadow "org.luaj:luaj-jse:${project.luaj_version}"
```

An example of [gradle.properties](gradle.properties) with an easily readable way to find the latest version of a dependency.

```Properties
# https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
logback_version = 1.3.0-alpha5

# https://mvnrepository.com/artifact/org.slf4j/slf4j-api
slf4j_api_version = 2.0.0-alpha1

# https://mvnrepository.com/artifact/org.luaj/luaj-jse
luaj_version = 3.0.1
```

This section is strict due to legal requirements, and the desire to stay in the clear legally speaking. This includes not using the software for evil as per the [JSON license](src/main/resources/META-INF/licenses/json-license.txt).

## Logging and Other Human Readable Strings

At the time of this writing, logging is performed by calling the [LogHelper class](src/main/java/me/alexisevelyn/crewmate/LogHelper.java). This will be replaced with [Logback](https://logback.qos.ch/) and [SLF4J](http://www.slf4j.org/) as soon as I figure out how to use it. Until the LogHelper method of logging is replaced, you must use it to log messages to the console. The new system will be put in place to allow logging to multiple types of outputs including [Mina-SSHD](https://github.com/apache/mina-sshd/blob/master/README.md) and to files. Since both [JLine](https://github.com/jline/jline3/blob/master/README.md) and Mina-SSHD supports SLF4J, it should work nicely with the new logging system by Logback.

As for any form of logging or human readable data, translation strings are a requirement. The currently existing system requires calling `Main.getTranslationBundle().getString("your_translation_string")` and then wrapping the String with `String.format` in order to put data into the String. This system will be replaced with a separate server/player translation system. Currently, only a server translation system exists as the ability to track the player's language was only recently implemented. If you want to, you can put a comment above your translation strings to ensure ease of finding them later to correctly translate for player messages.

If needed, `LogHelper.formatNamed(...)` exists to aid in more complex translations where the words or values may not necessarily stay in the same order between translations.

If sending a disconnect message, it is preferable to use the helper method `PacketHelper.closeWithMessage(String message)` and then just return the byte array to the calling function, so it can be passed up to the packet sending code in [Server.java](src/main/java/me/alexisevelyn/crewmate/Server.java).

## Code Format

Please don't stick single line functions in a single line. It is preferable to make the code easy to read without compacting it in a way to save space.

### What Not To Do
```Java
public class Example {

    String someRandomVariable;

    public void printLine() { System.out.println(); }

    public Example() {
        // Something Here
    }

    public void ifStatement() {
        if (true) doThis();
    }

    public void ifStatementTwo() {
    	SomeRandomCode();
        if (true)
            doThis();
    }

}
``` 

### What To Do
```Java
public class Example {
    String someRandomVariable;

    public Example() {
        // Something Here
    }

    public void printLine() {
        System.out.println();
    }

    public void ifStatement() {
        if (true)
            doThis();
    }

    public void ifStatementTwo() {
    	SomeRandomCode();

        if (true)
            doThis();
    }
}
``` 

The goal is to make the code legible and neat. Single line if statements are allowed to have the brackets removed, but are not a hard requirement. Keeping spacing between the top of the if statement and the previous line of code is preferred (unless the if statement is the first line of code in a method). Squishing methods and if statements such as what's seen in `printLine()` and `ifStatement()` is not allowed though. Your IDE should be able to auto-display the single line statements like that for you if you prefer that style. However, the file should be written as described in the "What To Do" section.

## Notes

As this software is still in the alpha stage, methods are likely to change locations, names, and even arguments rapidly and potentially without any deprecation notice. As the software becomes more stable, API Guardian will be used to help guide developers and modders in knowing what code is stable and meant to be accessed versus code that is internal and not guaranteed to stay the same. This contribution file will be updated accordingly as the software continues to grow, so please check it every time you submit a PR.

Also, please use the [bouncer pattern][bouncer] when writing functions.

Commits must be signed in order to validate authenticity of the source. This requirement might be removed in the future.

[bouncer]: https://wiki.c2.com/?BouncerPattern