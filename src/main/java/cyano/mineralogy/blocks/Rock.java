package cyano.mineralogy.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.google.common.base.Predicate;

public class Rock extends net.minecraft.block.Block{

	
	
	public Rock(boolean isStoneEquivalent,float hardness,float blastResistance,int toolHardnessLevel,SoundType sound) {
		super(Material.rock);
		this.isStoneEquivalent = isStoneEquivalent;
		super.setHardness((float)hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		//this.blockHardness = hardness;
		super.setResistance((float)blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		//this.blockResistance = blastResistance;
		super.setStepSound(Block.soundTypePiston); // sound for stone
		//this.stepSound = Block.soundTypePiston;
		super.setHarvestLevel("pickaxe", toolHardnessLevel);
	}
	
	public final boolean isStoneEquivalent;
	
	/**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param world The current world
     * @return True to allow this block to be replaced by a ore
     */
    @Override public boolean isReplaceableOreGen(World world, BlockPos coord, Predicate<IBlockState> predicate)
    {
        return Blocks.stone.isReplaceableOreGen(world, coord, predicate);
    }
    
    @Override public boolean isReplaceable(World world, BlockPos coord)
    {
        return Blocks.stone.isReplaceable(world, coord);
    }
    
    
}
