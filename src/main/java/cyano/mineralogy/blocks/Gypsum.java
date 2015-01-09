package cyano.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cyano.mineralogy.Mineralogy;

public class Gypsum extends Rock{

	private final String name = "gypsum";
	public Gypsum(){
		super(false,(float)0.75,(float)1,0,Block.soundTypeGravel);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
	@Override public Item getItemDropped(IBlockState bs, Random prng, int p_149650_3_)
    {
        return Mineralogy.gypsumPowder;
    }
	@Override public int quantityDropped(Random prng)
    {
        return prng.nextInt(4)|1;
    }
}
