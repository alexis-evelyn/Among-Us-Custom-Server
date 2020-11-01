# Crewmate

[![Maven Central Download Release][sonatype-release-badge]][maven-central-release-latest-download]
[![Maven Central Download Snapshot][sonatype-snapshot-badge]][maven-central-snapshot-latest-download]
<br>
[![Discord Link][discord-badge]][discord]
[![Documentation Status][read-the-docs-badge]][read-the-docs-link]
<br>
[![Github Actions Link][github-actions-badge]][github-actions-link]
[![Codacy Link][codacy-badge]][codacy-link]
[![Javadocs][javadocs-badge]][javadocs-link]

<!--[![Crowdin Link][crowdin-badge]][crowdin-link]--> <!-- Temporarily Disabled Until Approved -->

This will be a custom server which will support custom game modes and mini-games as well as improving the security of games by validating if certain actions are possible (e.g. killing if you are not the imposter and enforcing the kill cool down). This server will also be able to support modded clients and be able to be modded itself in order to add custom features which are not possible with a vanilla client.

You can find out more from [this issue][nickcis-issue], [my Discord][discord], and the [protocol description][wiki] I'm working on.

Some dumps of what I wrote to some friends about this will be pasted below to help explain what my goals are before I write this properly.

# Note

To connect to this server, you can either use the scripts [redirect-to-localhost.sh][redirect-file] and [remove-redirect-to-localhost.sh][remove-redirect-file] (to redirect the connection and remove the redirect respectively), or you can change your region file according to [this site][regionFileGenerator]. Eventually a region file generator will be implemented into this server to give to clients. If you are connecting to [localhost:22023][localhost], you can use this [region file][regionFile] I generated for that purpose.

# Contributing

For consistency's sake, I'll be writing a coding standard for the server soon. For now, just keep the code in the style that already exists.

### Me:

I'm working on a custom Among Us server which will allow for custom styled games. Especially minigames like what you may have seen some youtubers do (but at least the minigame will be enforceable by code so it can be played in public groups).
Unlike Minecraft which already has the protocol cracked which Minecraft uses to have servers/clients communicate with each other, Among Us hasn't had someone do that work yet, so I'm working on it with a couple of other people.

Basically, I'm working on a custom Among Us server. The official server has not been released to the public (afaict), so I'll have to make a custom one.

### Friend:

So modding for the purpose of adding things like new tasks, new maps, different character designs, etc?

### Me:

That will be the goal eventually, but that requires modding the client. I'll probably skip character designs/cosmetics as that is a paid for feature in the official game. As for now, I'm going to have different rules setup like hide-and-seek which is a mini-game where you hide from the imposter and nobody reports the body. Another game I've seen is randomizing (or making it impossible to tell different names) people's player names and skins so that one cannot tell which person did what.
The second one is possible because the server does the work to prevent the same name/skin, but as a result of server bugs, I've seen invisible names and people sharing the same skin. The client does not enforce this, so it's something that can be done server side only.

I could also do things like make it where the task bar means something different from the completed tasks and have it where killing someone does something unexpected. The use cases for this will come from the community which is more imaginative than me.
Teleportation should also be possible too.
Once a modded client exists (which will be built on top of the modded server by custom packets to see if a client is modded or not), then I can really have fun by changing the fundamental aspects of how the game works. The includes stuff like new tasks, maps, etc...

* I've modified the chat dump for spelling and removed non-essential messages.

My goals are:
* [ ] Crack the protocol (In Progress)
* [ ] Write a custom server (In Progress)
* [ ] Start adding things like custom game modes which can be toggled by "commands" in chat (Not Started)
* [ ] Eventually start work on detecting if a client is custom/modded or not (Not Started)
* [ ] Then if modded, start sending custom data to detect things like mods installed, etc... (Not Started)

So, for the detecting the modded client, I'll see what packet headers are unused and see if the vanilla client handles unknown packets gracefully. If not, I'll use the init server side ping with a predefined, but not likely to be accidental sequence to see if a modded client responds with a custom packet (and vanilla just pings back and ignores it). The ping idea is a page out of port knocking, but instead of ports, it's ping messages.

## Modding Future

Given that the Imposter mod loader is currently private and there are disagreements on how the protocol works (including some wanting to break compatibility with the vanilla servers), I'll probably implement my own modding protocol after vanilla support is done.

My goals are: 

* keep the modded client compatible with vanilla unless a mod requires breaking it.
* implement sandbox mod support so servers can send mods on connection.
* send the locale to server on handshake
* allow mods to be able to be turned on/off with a switch (and potentially profiles too).
* make mods cross compatible so only the client has to be platform dependent
* runtime permission system for mods that need to break sandbox
* obviously, add proper custom server support, so we don't need to faff with region files

<!-- Documentation Links -->
[nickcis-issue]: <https://github.com/NickCis/among-us-proxy/issues/4> "Original Issue Responsible For Development of Crewmate"
[wiki]: <https://github.com/alexis-evelyn/Among-Us-Protocol/wiki> "Detailed Protocol Analysis Wiki"

<!-- Connection Links -->
[regionFileGenerator]: <https://aeonlucid.com/Impostor/> "Online Region File Generator"
[localhost]: <udp://127.0.0.1:22023/> "Development Server URL"
[redirect-file]: <resources/redirect-to-localhost.sh> "Pre-Region File Add Redirect Script"
[remove-redirect-file]: <resources/remove-redirect-to-localhost.sh> "Pre-Region File Remove Redirect Script"
[regionFile]: <resources/regionInfo.dat> "Pre-made Region File"

<!-- Maven Central Downloads -->
[maven-central-release-latest-download]: <https://mvnrepository.com/artifact/me.alexisevelyn/crewmate/latest> "Maven Central Release Latest Download"
[maven-central-snapshot-latest-download]: <https://oss.sonatype.org/#nexus-search;gav~me.alexisevelyn~crewmate~~~> "Maven Central Snapshot Search"

<!-- Badges -->
[discord-badge]: <https://discord.com/api/guilds/750301084202958899/widget.png> "Discord Badge"
[crowdin-badge]: <https://badges.crowdin.net/crewmate/localized.svg> "Crowdin Badge"
[read-the-docs-badge]: <https://readthedocs.org/projects/crewmate/badge/?version=latest> "Read The Docs Badge"
[javadocs-badge]: <https://javadoc.io/badge2/me.alexisevelyn/crewmate/Crewmate%20Javadocs.svg> "Javadocs Badge"
[github-actions-badge]: <https://github.com/alexis-evelyn/Crewmate/workflows/Build%20Server/badge.svg> "Github Actions Badge"
[codacy-badge]: <https://app.codacy.com/project/badge/Grade/75f47a57f41a453983985f0279dfb424> "Codacy Badge"

<!-- Badge Links -->
[discord]: <https://discord.com/invite/DFUaVMx> "Development Discord"
[crowdin-link]: <https://crwd.in/crewmate> "Crowdin Link"
[read-the-docs-link]: <https://crewmate.alexisevelyn.me/en/latest/?badge=latest> "Read The Docs Link"
[javadocs-link]: <https://javadoc.io/doc/me.alexisevelyn/crewmate> "Javadocs Link"
[github-actions-link]: <https://github.com/alexis-evelyn/Crewmate/actions> "Github Actions Link"
[codacy-link]: <https://www.codacy.com/gh/alexis-evelyn/Crewmate/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=alexis-evelyn/Crewmate&amp;utm_campaign=Badge_Grade> "Codacy Link"

<!-- Sonatype Badges -->
[sonatype-release-badge]: <https://img.shields.io/nexus/r/me.alexisevelyn/crewmate.svg?server=https%3A%2F%2Foss.sonatype.org&style=flat&color=brightgreen&label=Crewmate%20Release> "Sonatype Release Badge"
[sonatype-snapshot-badge]: <https://img.shields.io/nexus/s/me.alexisevelyn/crewmate.svg?server=https%3A%2F%2Foss.sonatype.org&style=flat&color=brightgreen&label=Crewmate%20Snapshot> "Sonatype Snapshot Badge"