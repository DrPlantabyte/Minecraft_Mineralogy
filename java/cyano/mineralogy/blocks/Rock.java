package cyano.mineralogy.blocks;

import java.util.HashSet;
import java.util.List;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Rock extends net.minecraft.block.Block{

	
	
	public Rock(boolean isStoneEquivalent,float hardness,float blastResistance,int toolHardnessLevel,SoundType sound) {
		super(Material.rock);
		this.isStoneEquivalent = isStoneEquivalent;
		this.setHardness((float)hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(Block.soundTypePiston); // sound for stone
		// TODO: Forge update:	this.setHarvestLevel("pickaxe", toolHardnessLevel);
	}
	
	public final boolean isStoneEquivalent;
	
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
    @Override public boolean isReplaceable(World world, BlockPos coord)
    {
        return Blocks.stone.isReplaceable(world, coord);
    }
    
    
}
