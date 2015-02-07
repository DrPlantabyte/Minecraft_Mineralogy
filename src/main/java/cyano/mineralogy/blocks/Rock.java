package cyano.mineralogy.blocks;

import java.lang.reflect.Method;

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
		this.setHardness((float)hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(sound); // sound for stone
		this.setHarvestLevel("pickaxe", toolHardnessLevel);
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
        return true;
    }
    
    
}
