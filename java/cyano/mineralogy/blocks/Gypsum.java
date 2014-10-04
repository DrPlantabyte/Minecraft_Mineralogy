package cyano.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cyano.mineralogy.Mineralogy;

public class Gypsum extends Rock{

	private final String name = "gypsum";
	public Gypsum(){
		this.setBlockName(Mineralogy.MODID +"_"+ name);
		this.setBlockTextureName(Mineralogy.MODID +":"+ name);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness((float)0.75); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)1); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(Block.soundTypeGravel); // sound for stone
		this.setHarvestLevel("pickaxe", 0);
	}
	
	
	@Override public Item getItemDropped(int p_149650_1_, Random prng, int p_149650_3_)
    {
        return Mineralogy.gypsumPowder;
    }
	@Override public int quantityDropped(Random prng)
    {
        return prng.nextInt(4)|1;
    }
}
