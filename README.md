# Botania Revived (NeoForge)

This is an unofficial, modified port of [Botania](https://www.curseforge.com/minecraft/mc-mods/botania) to NeoForge, based on the original by [Vazkii](https://www.curseforge.com/members/vazkii/projects) and the Botania contributors. The current release **Minecraft 1.21.1**.

Botania is distributed under the [Botania License](https://botaniamod.net/license.html), which permits the use, sharing and adaptation of the mod subject to its license terms: credit to Vazkii as the creator, indicating that this is an altered version, no charging for the distribution, and keeping the project open source. This project follows those terms and is not affiliated with or endorsed by Vazkii or the Botania team.

Botania is a tech mod themed around natural magic: you grow flowers that generate and process mana, move that mana around with spreaders and sparks, and use it in mana pools, runic altars, elven trade, terrestrial agglomeration and the fight against the Gaia Guardian. Everything is documented in an in-game book, the Lexica Botania (Patchouli).

The current build is for **Minecraft 1.21.1 on NeoForge (21.1.240 or newer)**; a Minecraft 26.1.2 (NeoForge) port is planned. It was ported and adapted to the NeoForge APIs by VelronDevs. The content is still Botania: this port keeps the namespace `botania` and the mod id `botania`.

## Important notes

*   This is NOT the official Botania release. For the official mod see the original project: [Botania on CurseForge](https://www.curseforge.com/minecraft/mc-mods/botania), [botaniamod.net](https://botaniamod.net/) and [VazkiiMods/Botania on GitHub](https://github.com/VazkiiMods/Botania).
*   Please do not report problems with this port to Vazkii or to the official Botania team. Report them on this project instead (see "Reporting issues").
*   Because the Java package and internals differ from the official build, addons compiled against the official Botania will not necessarily work with this port.

## Features

The feature list below was checked against the Minecraft 1.21.1 (NeoForge) build.

*   Port of Botania to NeoForge: Minecraft 1.21.1 (NeoForge 21.1.240). A Minecraft 26.1.2 (NeoForge) port is planned.
    
*   In-game Lexica Botania built with Patchouli, with all of its text available in English and Spanish.
    
*   Wither Mana Rose, a new generating flower added to base Botania (based on MythicBotany's Wither Aconite, see credits): it burns Nether Stars into mana. It is made in two steps, an Unactivated Wither Mana Rose in the Petal Apothecary and the final flower in the Runic Altar. Everything about it (on/off switch, mana per star, speed, cooldown, buffer) is configurable in the `witherManaRose` section of `botania-common.toml`.
    
*   Loonium: by default it now drops dungeon and structure loot directly as items, like Botania 1.7.10 and 1.12.2, and does not spawn mobs. The mob-spawning behavior is still available with `loonium.mode = MOBS` in `botania-common.toml`; mana cost, cooldown, items per cycle and radius are configurable.
    
*   Terra Shatterer level cap: `terraShatterer.maxLevel` in `botania-common.toml` limits how high the Terra Shatterer can level (D, C, B, A, S or SS).
    
*   Recipe viewer integration: JEI, EMI and REI categories for mana infusion, petal apothecary, runic altar, terrestrial agglomeration plate, elven trade, brewery, pure daisy, Orechid, Orechid Ignem and Marimorphosis, with workstations and special crafting entries.
    
*   Block tooltips with Jade and WTHIT: mana bars, progress, flower timers and binding, runic altar, terrestrial agglomeration plate, brewery, mana enchanter, apothecary, elven gateway, avatar and more, with configurable categories and colors.
    
*   Accessories through Curios (optional): Botania baubles, rings and amulets use Curios slots when Curios is installed.
    
*   KubeJS integration (optional) with a `BotaniaEvents` group: `BotaniaEvents.registry(...)` in startup scripts (register runes) and `BotaniaEvents.server(...)` in server scripts (runic altar, petal apothecary, mana infusion, elven trade, terrestrial agglomeration, brewery and pure daisy recipes).
    
*   Corporea and Sodium compatibility.
    
*   Botany Pots integration (optional): Botania soils, flowers and mushrooms as crops, fertilizers and a mana-consuming soil.
    
*   Ponder integration (optional): Botania scenes for the mana network, crafting devices, flowers, sparks, Corporea and Alfheim.
    
*   Ars Nouveau integration (optional): Source Jars, Arcane Relays and Imbuement Chambers accept mana bursts and can be bound by flowers like a mana pool (1 Source = 15 mana), Mana Pools work as Source containers for Ars Nouveau relays, Sourcelinks, turrets and rituals, Botania mana items pay for Ars Nouveau spells when your own mana runs short, Botania tools can draw on your Ars Nouveau mana, and the Mana Cookie grants Mana Regen. Rates and switches are in `botania-ars_nouveau-common.toml`. The feature set follows Ars Botania by xypp/zxy19 (MIT); the code is an independent re-implementation.
    
*   Configurable modules: larger optional feature sets bundled inside Botania Revived can be switched on or off in the config (see "Modules" and "Enabling and disabling a module").
    
*   Translations: English, the human translations carried over from Botania, and Spanish (es\_es).
    

## Supported versions and requirements

Available now: Minecraft 1.21.1 (NeoForge). Planned: Minecraft 26.1.2 (NeoForge).

**Minecraft 1.21.1 build**

*   Minecraft 1.21.1
*   NeoForge 21.1.240 or newer (21.1.x)
*   Java 21
*   [Patchouli](https://www.curseforge.com/minecraft/mc-mods/patchouli) for 1.21.1 (build 1.21.1-93 or newer, required)

**Minecraft 26.1.2 build (planned, not released)**

*   Not released yet. Its requirements will be listed on its file page once it is published; the versions listed above apply to the 1.21.1 build only.

## License and attribution

Botania Revived is a modified (altered) version of Botania, created by Vazkii and the Botania team, and it is distributed under the **Botania License**. Original license: [botaniamod.net/license.html](https://botaniamod.net/license.html) (also in the original repository as [LICENSE.txt](https://github.com/VazkiiMods/Botania/blob/master/LICENSE.txt)). The license states: "You are completely free and have the right to Use, Share and Adapt the mod."

**Licensing of this project.** Botania Revived as a whole, including the code and assets derived from Botania, is distributed under the Botania License and its source is open. New code written specifically for this port is also released under the Botania License. Third-party material remains under its respective license. Third-party material keeps its own license even when it is bundled here: the modules credited below (for example MIT-licensed addons, the Apache-2.0 sprite of the MythicBotany Wither Aconite and the data of Simple Structure Botania (listed as MIT on CurseForge and as CC BY-4.0 on Modrinth, with attribution given)) are used under their own terms and with attribution, and each module section states the license of its source. If you are the author of any credited work and want its credit changed or the work removed, please contact me through this project's issues tab and I will do it promptly.

Content added by this port or by the bundled modules is credited separately below.

## Modules

Botania Revived includes a module framework: optional feature sets that live inside the mod under the same `botania` namespace and can be turned on or off from the config. Each module is based on an existing addon or mod. Credit goes to the original authors. The code was rewritten for NeoForge, and none of the original authors has reviewed, endorsed or supports this port. Please do not ask them for support with Botania Revived.

### Included now

**Botania Extras** (config key `modules.botania_extras`, enabled by default)

*   Based on **Botanical Addons** by **L0neKitsune** and **WireSegal** (Yrsegal).
*   Original project: [Botanical Addons on CurseForge](https://www.curseforge.com/minecraft/mc-mods/botanical-addons), [source on GitHub](https://github.com/L0neKitsune/Botanical-Addons).
*   Original license: Botania License (stated in the project's README).
*   Original version: the original addon was left at **Minecraft 1.7.10 (Forge)**, last file r0.1.20 from 2016. Its spiritual successor is [Natural Pledge](https://github.com/ejektaflex/NaturalPledge) (1.10.2 to 1.12.2).
*   What was ported: the code is a rewrite (the original is 1.7.10 Kotlin). The original textures are reused with credit under the Botania License, except the Bifrost flower textures, which were generated from the white flowers because the original does not include them.
*   Content available so far: iridescent dirt, grass and tall grass in 16 colors plus Bifrost, iridescent seeds, Bifrost flowers, petals and floral Bifrost powder, iridescent lantern, frozen stars, blaze kindling, Shimmerquartz, iridescent trees and 23 wood sets (16 iridescent colors, Bifrost and six alternative-grass woods) with the Iridescent Sapling, the Rods of the Vibrant Plains, Thundering Peaks, Stormy Sea and Prismatic Lake, the Phantom Flash lens, Rainbow and Phantom Mana Flash blocks, the Priestly Emblems of Thor, Sif and Njord (Curios) plus the creative-only Aesir emblem, item platforms, the Dendric Suffuser (tree infusion recipes) with the Thunderous, Infernal and Sealing magic oaks, the Sonic Amplifier, the Livingwood Funnel, the Manaseal Creeper, the Botanist's Toolbelt, the Clerical Colorizer and the Coats of Arms, and the Crysanthermum flower.

**GOG** (config key `modules.skybox`, disabled by default)

*   The sky rendering (starfield, planets, rainbow arc, aurora rays and the custom sky) comes from Botania's own Garden of Glass skybox by **Vazkii** and the Botania team, so it remains under the Botania License. It is not original work of this port.
*   What this port adds: the toggleable module (disabled by default), a flat void dimension (`botania:skybox`), the sky drawn in every dimension while the module is enabled, and a client change that hides the vanilla sun and moon while that sky is active. It uses Botania's own timer and helpers and the existing Botania sky textures, and it does not draw where Botania's own fancy skybox is already active.
*   Behavior: when the module is enabled, the sky is drawn in every dimension, including the Nether and the End, except where Botania's own fancy skybox is already drawing. With the module disabled nothing is registered or rendered and the dimension does not exist.

**Exploration** (config key `modules.exploration`, enabled by default)

*   Based on **Simple Structure Botania** by **Kononets** (datapack for Botania structures). The structures are a port and rewrite of its data: the 28 templates were converted to the Minecraft 1.21.1 format and the worldgen files were rewritten under the `botania` namespace.
*   Original: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/simple-structure-botania).
*   Original version: last left at **Minecraft 1.20.1 (Fabric/Forge, data only)**. The license is shown differently on its CurseForge page (MIT). In both cases attribution to the author is required and it is given here. Credits will be adjusted if the author clarifies.
*   Content: decorative Botania structures (giant mushrooms, large flowers, ruins, fallen trees, lakes and floating islands) generated in the Overworld, 6 structure sets using 28 templates. The module has no blocks, items or recipes, so it has no creative tab. With the module disabled none of them generate.

**Creative Crafting** (config key `modules.cre`, enabled by default)

*   Compatible with **Re-Avaritia** by Nova-Committee ([GitHub](https://github.com/Nova-Committee/Re-Avaritia), modid `avaritia`, code MIT, assets CC BY-NC-SA 4.0), itself a continuation of Avaritia by SpitefulFox. Re-Avaritia is optional: no code, art or textures of it are included or copied, and the module works without it.
*   Original version: Re-Avaritia is currently available for **Minecraft 1.21.1 (NeoForge)** and 1.20.1 (Forge).
*   Content: Terrestrial Agglomeration Plate recipes (1,000,000 or 500,000 mana, Botania ingredients only) for Botania's creative-only objects: creative mana pool, creative mana tablet, creative Corporea spark and unbreakable platform, plus the creative tab `botania:cre` that lists them. Only when Re-Avaritia is installed, four extra data-driven recipes (`avaritia:shaped_table`, tier 4, Botania ingredients only, ids `botania:cre/*_extreme`) let you craft the same objects on its Extreme Crafting Table (9x9), with a Lexicon entry that appears only with that mod.

**Asgard** (config key `modules.asg`, enabled by default)

*   Based on the **Asgardandelion** addon (Botania Asgard / "Avaritia Asgardandelions For Botania") by **Beecube31**. Texture credit in the original: NekitFbetc.
*   Original: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/asgardandelion-for-botania), [GitHub](https://github.com/beecupbe/botania-asgard). License: MIT (copyright (c) 2025 Beecube31).
*   Original version: last left at **Minecraft 1.20.1 (Forge)**. An older version also exists for 1.12.2.
*   What was ported: the code is a rewrite for NeoForge and Botania Revived; the 16x16 flower sprite of the original is reused with credit. Content: the Asgardandelion generating flower (with floating and potted variants) producing 1,000,000 mana per tick by default (configurable, with an on/off switch, in the `asgardandelion` section of `botania-common.toml`), a Lexicon entry, and the creative tab `botania:asg`. The original recipe needed Avaritia; here it is made on a Terrestrial Agglomeration Plate with Botania ingredients.

This module is not part of the current release. Its final name is not decided yet, so it is listed under the name of the original addon. Its module id `ext` is reserved. Credits and original versions are given in advance.

**Extra Botany / Extrabotany: Reburn**

*   Original **Extra Botany** by **ExtraMeteorP**: [GitHub](https://github.com/ExtraMeteorP/Extra-Botany), [CurseForge](https://www.curseforge.com/minecraft/mc-mods/extrabotany). License shown on CurseForge: MIT. Last left at **Minecraft 1.12.2 (Forge)**, with an unfinished **1.16.5 (Forge)** branch.

## Integrations and inspiration

The following integrations are separate projects whose authors have no connection with, and do not endorse, Botania Revived. They are optional and only activate when the other mod is installed. Each one is re-implemented on top of this port's API.

*   **Botany Pots** by Darkhax ([GitHub](https://github.com/Darkhax-Minecraft/BotanyPots)): Botania soils, flowers and mushrooms as crops, fertilizers and a mana-consuming soil (needs Botany Pots).
*   **Ponder** by the Creators of Create ([GitHub](https://github.com/Creators-of-Create/Ponder)): Botania ponder scenes for the mana network, crafting devices, flowers, sparks, Corporea and Alfheim (needs Ponder).
*   **Create** by the Creators of Create ([GitHub](https://github.com/Creators-of-Create/Create)): mana as a power source. Planned, not released yet.

## 📜 Credits

*   Full credits to **Vazkii** and the Botania team for the original Botania, and to its art, audio and code contributors (Hubry, Alwinfy, ArtemisSystem, williewillus, wiiv, dylan4ever, Falkory220 and the others listed on the [Botania credits page](https://botaniamod.net/credits.html)).
*   **Aidan C. Brady** and the Mekanism team for the code that Botania imports from Mekanism (MIT).
*   **L0neKitsune** and **WireSegal** for Botanical Addons, the base of the Botania Extras module.
*   **Kononets** for Simple Structure Botania, the base of the Exploration module.
*   **Beecube31** for the Asgardandelion addon, the base of the Asgard module.
*   **xypp / zxy19** for Ars Botania, the concept behind the Ars Nouveau integration.
*   **Nova-Committee** for Re-Avaritia, the inspiration for the Creative Crafting module (and the 9x9 table recipes when Re-Avaritia is installed).
*   **ExtraMeteorP** for Extra Botany .
*   [Patchouli](https://github.com/VazkiiMods/Patchouli) (Vazkii), [JEI](https://github.com/mezz/JustEnoughItems) (mezz), [EMI](https://github.com/emilyploszaj/emi) (Emily), [REI](https://github.com/shedaniel/RoughlyEnoughItems) (shedaniel), [Jade](https://github.com/Snownee/Jade) (Snownee), [WTHIT](https://github.com/badasintended/wthit) (badasintended), [KubeJS](https://github.com/KubeJS-Mods/KubeJS) (KubeJS Mods), [Curios](https://github.com/TheIllusiveC4/Curios) (TheIllusiveC4), [Sodium](https://github.com/CaffeineMC/sodium) (CaffeineMC), [Ars Nouveau](https://github.com/baileyholl/Ars-Nouveau) (Bailey Holl), [Botany Pots](https://github.com/Darkhax-Minecraft/BotanyPots) (Darkhax), [Ponder](https://github.com/Creators-of-Create/Ponder) (Creators of Create) and [NeoForge](https://neoforged.net/) for the libraries and integrations Botania Revived works with. Each belongs to its authors and is used through its public API or as an optional dependency.
*   **VelronDevs** for the NeoForge port, the datagen, the integrations and the new content (Wither Mana Rose, the modules and their glue code).

## Enabling and disabling a module

Botania Extras, Exploration, Creative Crafting and Asgard are enabled by default and Skybox is disabled by default. To change a module, open `config/botania-common.toml` and set its key to true or false in the `[modules]` section, then restart the game (the setting is read at startup). For example, to turn Botania Extras off and Skybox on:

```
[modules]
    botania_extras = false
    skybox = true
```

With a module disabled its blocks, items, recipes, loot and client rendering are not registered at all. Every module has its own key in the same section (`modules.botania_extras` for Botania Extras, `modules.skybox` for Skybox, `modules.exploration` for Exploration, `modules.cre` for Creative Crafting, `modules.asg` for Asgard, and so on for future modules).

## Reporting issues

Please report bugs and suggestions on this [Github issues](https://github.com/Velron-Devs/Botania-Revived) and include your logs. Do not contact Vazkii or the official Botania team about problems in this port.

***

Botania is created by Vazkii. Botania Revived is an unofficial, altered port made by VelronDevs and is distributed under the Botania License.
