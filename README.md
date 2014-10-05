# Mineralogy
## Cyano's Minecraft Mineralogy Mod
This mod replaces the generic "stone" in Minecraft with real-world rock types.

## Requirements
This mod requires that you install Minecraft Forge version 1.7.10-10.13.1.1219 or later (ealier versions of Forge for Minecraft 1.7.10 may work, but no guarentees).

## Installing
After you have successfully installed Forge, simply place the file *mineralogy-#.#.jar* in your *mods* folder. You can get the mineralogy-#.#.jar file (as well as the texture packs) from the Releases tab of this repository page.

## Texture Packs
There are three levels of texture resolution available. The default textures are low-resolution (16x16 pixels, same as Minecraft), but I recommend trying the high-resolution (64x64 pixels) textures via the high-res texture pack (available on the Release page).

## Q&A
Q: Why make this mod?
A: Minecraft is a game that involves a lot of mining, yet it takes very little inspiration from actual geology. I made this mod to give Minecraft more of a geology vibe. After all, there's no mineral called "stone" in the geology textbook.

Q: Where's the cobblestone?!
A: Many of the stone types can be used as cobblestone or as stone in crafting recipes. If you want "Stone", smelt gravel. If you want "Cobblestone", craft two blocks of rock with 2 blocks of gravel.

Q: There's too much lag when generating new chunks!
A: Yes, that can happen. Mineralogy puts a lot more computation into world generation, so you don't want to run the server (or play single-player) on a computer with a slow CPU. I tried to improve performance as much as possible, but there's no getting around the fact that the stone type needs to be calculated for every single underground block in the game.

Q: Why do the ores look funny?
A: I retextured the ores to better match the appearance of the new rock types. You can change them back by making your own texture pack from the default Minecraft resources.

## Notes to Other Modders
This mod replaces the WorldProvider for the Overworld in Minecraft. There can only be one provider per dimension, so this mod is therefore incompatible with other mods that also replace the same WorldProvider.

The stone types are all registered with the OreDictionary as Stone and/or Cobblestone. If you use OreDictionary crafting recipes (ShapelessOreRecipe or ShapedOreRecipe), then there shouldn't be any trouble. However, if you manually specify the Stone or Cobblestone blocks in a standard crafting recipe object, then users of the Mineralogy mod will have to craft Cobblestone (2 gravel + 2 rock blocks) to use your recipe.
