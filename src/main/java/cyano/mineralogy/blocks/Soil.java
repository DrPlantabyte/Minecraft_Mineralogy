package cyano.mineralogy.blocks;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class Soil extends Block{

	public Soil(String unlocalizedName){
        super(Material.GROUND);
        this.setUnlocalizedName(unlocalizedName);
        this.setSoundType(SoundType.GROUND);
		this.setHarvestLevel("shovel", 0);
		this.setHardness(0.5f); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance(0f); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	/**
     * Determines if the current block is replaceable by Ore veins during world generation.
     *
     * @param world The current world
     * @return True to allow this block to be replaced by a ore
     */
    @Override public boolean isReplaceableOreGen(IBlockState bs, IBlockAccess world, BlockPos coord, Predicate<IBlockState> predicate)
    {
        return true;
    }
	
}
