# Among-Us-Custom-Server

This is currently going to be the home of the proof of concept custom Among Us Server based on the work from @NickCis and me.

You can find out more from [this issue][nickcis-issue], [my Discord][discord], and the [protocol description][wiki] I'm working on.

It's too late at night to decide on a license to use and I'm sending in my laptop to be repaired tomorrow, so I'll just leave it as All Rights Reserved right now. I most likely will go MIT though.

I'm working on this server to make custom game modes possible to do in public settings and to make certain gamemodes feasible at all. This will end up being a modded type server where one can use modded clients if the server has a mod/plugin that works with it (or requires it).

Some dumps of what I wrote to some friends about this will be pasted below to help explain what my goals are before I write this properly.


### Me:

I'm working on a custom Among Us server which will allow for custom styled games. Especially minigames like what you may have seen some youtubers do (but at least the minigame will be enforceable by code so it can be played in public groups).
Unlike Minecraft which already has the protocol cracked which Minecraft uses to have servers/clients communicate with each other, Among Us hasn't had someone do that work yet, so I'm working on it with a couple of other people.

Basically, I'm working on a custom Among Us server. The official server has not been released to the public (afaict), so I'll have to make a custom one.

### Friend:

So modding for the purpose of adding things like new tasks, new maps, different character designs, etc?

### Me:

That will be the goal eventually, but that requires modding the client. I'll probably skip character designs/cosmetics as that is a paid for feature in the official game. As for now, I'm going to have different rules setup like hide and seek which is a minigame where you hide from the imposter and nobody reports the body. Another game I've seen is randomizing (or making it impossible to tell different names) people's player names and skins so that one cannot tell which person did what.
The second one is possible because the server does the work to prevent the same name/skin, but as a result of server bugs, I've seen invisible names and people sharing the same skin. The client does not enforce this, so it's something that can be done server side only.

I could also do things like make it where the task bar means something different than completed tasks and have it where killing someone does something unexpected. The use cases for this will come from the community that is more imaginative than me.
Teleportation should also be possible too.
Once a modded client exists (which will be built on top of the modded server by custom packets to see if a client is modded or not), then I can really have fun by changing the fundamental aspects of how the game works. The includes stuff like new tasks, maps, etc...

The only thing is, nobody's cracked the protocol yet to be able to build a server/client. So, that's what I set out to do.

My goals are:
* Crack the protocol
* Write a custom server
* Start adding things like custom game modes which can be toggled by "commands" in chat.
* Eventually start work on detecting if a client is custom/modded or not
* Then if modded, start sending custom data to detect things like mods installed, etc...

Dumps Done Some Data Ommitted As Not Necessary For Context

So, for the detecting the modded client, I'll see what packet headers are unused and see if the vanilla client handles unknown packets gracefully. If not, I'll use the init server side ping with a predefined, but not likely to be accidental sequence to see if a modded client responds with a custom packet (and vanilla just pings back and ignores it). The ping idea is a page out of port knocking, but instead of ports, it's ping messages.

[nickcis-issue]: https://github.com/NickCis/among-us-proxy/issues/4
[discord]: https://discord.com/invite/DFUaVMx
[wiki]: https://github.com/alexis-evelyn/Among-Us-Protocol/wiki
