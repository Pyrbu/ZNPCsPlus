# ZNPCsPlus [![](https://img.shields.io/discord/1099449144948555957?label=Discord&logo=Discord&style=plastic)](https://discord.gg/MAZz6XpPcg) [![](https://img.shields.io/jenkins/build?jobUrl=https%3A%2F%2Fci.pyr.lol%2Fjob%2FZNPCsPlus%2F&style=plastic&logo=jenkins)](https://ci.pyr.lol/job/ZNPCsPlus/)
[![](https://img.shields.io/bstats/players/18244?style=plastic&label=bStats%20Players)]((https://bstats.org/plugin/bukkit/ZNPCsPlus/18244/)) [![](https://img.shields.io/bstats/servers/18244?style=plastic&label=bStats%20Servers)]((https://bstats.org/plugin/bukkit/ZNPCsPlus/18244/)) [![](https://img.shields.io/spiget/downloads/109380?style=plastic&label=Spigot%20Downloads)]((https://www.spigotmc.org/resources/znpcsplus.109380/))

[ZNPCsPlus](https://www.spigotmc.org/resources/znpcsplus.109380/) is a Spigot plugin that is used to create fake entities 
that players can interact with to perform actions like switching servers on a network or executing commands.

This plugin is a remake of a plugin called ZNPCs, we originally started because the maintainer of ZNPCs decided to announce that he was 
[dropping support for the plugin](https://github.com/Pyrbu/ZNPCsPlus/blob/2.X/.github/znpc.png?raw=true).

Looking for up-to-date builds of the plugin? Check out our [Jenkins](https://ci.pyr.lol/job/ZNPCsPlus/)

## Fork Notes (innerpine(me))
This repository contains custom fixes and build changes made for a self-hosted fork.

### What changed in this fork
- Build system migrated from Gradle to Maven (`pom.xml`, `api/pom.xml`, `plugin/pom.xml`)
- GitHub Actions CI switched to Maven (`mvn -B verify`)
- Fixed startup crash:
  `NoClassDefFoundError: ... snakeyaml/comments/CommentLine`
- Fixed NPC processing spam on Paper:
  `IllegalStateException: NpcSpawnEvent may only be triggered asynchronously`
- Added version guard for `fake-enforce-secure-chat` to avoid PacketEvents/NBT decode issues on old versions (like 1.16.x)
- Confirmed and kept `strider` NPC type registration in `NpcTypeRegistryImpl`
- Added and wired `frog_target_npc` serializer support
- Improved registry/storage stability (safer register/unregister and SQL save/load paths)
- Refactored `PropertySetCommand` parsing logic for maintainability
- Added tests for `NpcLocation` and `TargetNpcPropertySerializer`

### Build (Maven)
Requirements:
- Java 8+ (CI uses Java 21)
- Maven 3.9+

Commands:
```bash
mvn -B test
mvn -B -am -pl plugin package -DskipTests
```

Built plugin jar:
- `plugin/target/ZNPCsPlus.jar`

### Notes for publishing this fork
- Mainline build tool for this fork is Maven.
- If you publish this fork, keep this section updated as your fork diverges.
- If you run Paper 1.16.x, keep `fake-enforce-secure-chat` disabled in config (the code now also hard-guards old versions).

## Why is it so good?
- Packet-based NPC system with performance-focused scheduling
- Performance & stability oriented code
- Support for all versions from 1.8 to 1.21.8
- Support for multiple different storage options
- Intuitive command system

### Requirements, Extensions & Supported Software
Requirements:
- Java 8+
- Minecraft 1.8 - 1.21.8

Supported Softwares:
- Spigot ([Website](https://www.spigotmc.org/))
- Paper ([Github](https://github.com/PaperMC/Paper)) ([Website](https://papermc.io/software/paper))
- Folia ([Github](https://github.com/PaperMC/Folia)) ([Website](https://papermc.io/software/folia))
- ArcLight ([Github](https://github.com/IzzelAliz/Arclight))

Optional Dependencies/Extensions:
- PlaceholderAPI

## Found a bug?
Open an issue in the GitHub [issue tracker](https://github.com/Pyrbu/ZNPCsPlus/issues) or join our [support discord](https://discord.gg/MAZz6XpPcg)

## BStats
[![](https://bstats.org/signatures/bukkit/znpcsplus.svg)](https://bstats.org/plugin/bukkit/ZNPCsPlus/18244/)

#### Like what you see? Want the project to continue improving? Consider starring the repository & leaving a positive review on [Spigot](https://www.spigotmc.org/resources/znpcsplus.109380/)!

## Credits
- [PacketEvents 2.0](https://github.com/retrooper/packetevents) - Packet library
- [Minecraft Wiki Protocol (formally wiki.vg)](https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Main_Page) - Minecraft protocol documentation
- [gson](https://github.com/google/gson) - JSON parsing library made by Google
- [Mineskin.org](https://mineskin.org/) - Website for raw skin file uploads
- [adventure](https://docs.advntr.dev/) - Minecraft text api
- [DazzleConf](https://github.com/A248/DazzleConf) - Configuration library
- [Director](https://github.com/Pyrbu/Director) - Command library
- [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) - Universal string placeholder library
