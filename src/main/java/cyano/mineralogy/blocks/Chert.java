package cyano.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cyano.mineralogy.Mineralogy;

public class Chert extends Rock{

	private final String name = "chert";
	public Chert(){
		super(false,(float)1.5,(float)10,1,Block.soundTypePiston);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
	}
	
	
	@Override public Item getItemDropped(IBlockState bs, Random prng, int p_149650_3_)
    {
        if(prng.nextInt(10) == 0){ 
        	return Items.flint;
        }else{
        	return Item.getItemFromBlock(this);
        }
        
    }
	@Override public int quantityDropped(Random prng)
    {
        return 1;
    }
}
