package cyano.mineralogy.blocks;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

public class Chert extends Rock{

	private final String name = "chert";
	public Chert(){
		super(false,(float)1.5,(float)10,1, SoundType.STONE);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
	
	
	@Override public Item getItemDropped(IBlockState bs, Random prng, int p_149650_3_)
    {
        if(prng.nextInt(10) == 0){ 
        	return Items.FLINT;
        }else{
        	return Item.getItemFromBlock(this);
        }
        
    }
	@Override public int quantityDropped(Random prng)
    {
        return 1;
    }
}
