package cyano.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import cyano.mineralogy.Mineralogy;

public class Chert extends Rock{

	private final String name = "chert";
	public Chert(){
		this.setBlockName(Mineralogy.MODID +"_"+ name);
		this.setBlockTextureName(Mineralogy.MODID +":"+ name);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness((float)1.5); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)10); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(Block.soundTypePiston); // sound for stone
		this.setHarvestLevel("pickaxe", 1);
	}
	
	
	@Override public Item getItemDropped(int p_149650_1_, Random prng, int p_149650_3_)
    {
        if(prng.nextInt(10) == 0){ 
        	return Items.flint;
        }else{
        	return Item.getItemFromBlock(this);
        }
        
    }
	@Override public int quantityDropped(Random prng)
    {
        return prng.nextInt(4)|1;
    }
}
