package cyano.mineralogy.blocks;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Random;

public class Gypsum extends Rock{

	private final String name = "gypsum";
	public Gypsum(){
		super(false,(float)0.75,(float)1,0, SoundType.STONE);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
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
