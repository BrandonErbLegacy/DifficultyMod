# Difficulty by NightWraid
Making the world a more challenging place!

## Core Features
* Mob stats scale with difficulty level
* Unlock special types of mob modifiers as you level
* Difficulty level is unique per player. Not server based!
* Players playing as a group have the difficulty of the world scale around them based on the highest difficulty in their vicinity

## Mob Types
* Hellspawn
* Mystic
* Raid Boss
* Bulwark
* Decaying
* Wrathful

### Hellspawn
Mobs of the hellspawn variety are immune to fire damage by default. They have several abilities they gain as you level up in difficulty. 

* Firey Touch - When a hellspawn mob hits you, they will set you on fire
* World Blaze - Sets all players within a certain range on fire
* Vengeful Death - Explodes on death

### Mystic 
A mystic mob has two main abilities. When a mystic mob reaches low HP it will attempt to teleport to safety and apply a regen buff to itself. If it successfully teleports to safety and regens a percent of it's HP it will teleport back to the attacker again. Additionally, if it is unable to reach it's target it will begin to charge a siege attack. A siege attack will allow the mob to teleport itself and any nearby mobs directly to the target. Such a deadly attack has a very long charge time. Dealing damage to the mob will reset the mob's progress on charging the siege attack.

### Raid Boss
These types of modded mobs are simple, but dangerous. A raid boss will summon more minions of the same type whenever it takes damage. Bosses can not have this modifier. Minions spawned by a raid boss can also potentially be modded, but they cannot possess the raid boss mod themselves.

### Bulwark
Bulwark mobs are tough to kill. As such they have a few innate abilities that make them quite tanky.

* Bulwark Passive - They take reduced damage from all sources of damage (defaults to 15%)
* Regeneration - If the bulwark mob is not an undead creature it will apply a regen buff to itself after taking any damage
* Absorption - Bulwark mobs spawn with a portion of their health as shielding
* Undying - At higher difficulties a bulwark mob will negate any attacks that will one-shot it from full HP. 

### Decaying
Mobs of this variety are rather simple and only have two main attributes. When they take damage they will decay your equipment some. The amount they decay your equipment has three stages depending on difficulty. Additionally being hit by a decaying creature will apply wither to it's target

### Wrathful
Wrathful mobs only have one ability. As they get lower on health their damage and speed both increase by the percent of hp they're missing. While they're simple creatures, they can also become quite dangerous. 

## Commands
* getcurrentkillcounts (kc) - View the kill counts tracked by all players on the server
* getkillstolevel (kl) - View the number of kills it will take to level up
* getdifficulty (gd) - View your current difficulty level
* getlocaldifficulty (ld) - View the highest difficulty in your area
* setdebugmode (dm) - Turn on error messages in the console, as well as debugging info. Possibly doesn't work right now (OP only)
* setdifficulty (sd) - Set your difficulty level, or another players (OP only)
* spawnmodmob (smm) - Spawn a zombie with the corresponding modifier that you supply. 

## Credits
The author of EpicSiegeMod (funwayguy): https://github.com/Funwayguy/Epic-Siege-Mod . His difficulty system inspired my mod.
