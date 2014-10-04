package cyano.mineralogy.blocks;

import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class Rock extends net.minecraft.block.Block{

	private static HashSet<String> stoneEquivalents = null;
	
	private static void initStoneList() {
		if(stoneEquivalents == null){
			stoneEquivalents = new HashSet<String>();
			List<ItemStack> stones = OreDictionary.getOres("stone");
			for(ItemStack e : stones){
				stoneEquivalents.add(e.getUnlocalizedName());
			}
		}
	}
	
	public Rock() {
		super(Material.rock);
		
	}
	
	private boolean isStoneEquivalent = false;
	private boolean needsInit = true;
	
	/**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param world The current world
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @param stone The generic target block the gen is looking for, Standards define stone
     *      for overworld generation, and neatherack for the nether.
     * @return True to allow this block to be replaced by a ore
     */
    @Override public boolean isReplaceableOreGen(World world, int x, int y, int z, Block stone)
    {
    	if(needsInit){
    		initStoneList();
    		isStoneEquivalent = stoneEquivalents.contains(this.getUnlocalizedName());
    		needsInit = false;
    	}
        return isStoneEquivalent;
    }

}
