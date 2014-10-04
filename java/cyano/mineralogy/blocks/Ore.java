package cyano.mineralogy.blocks;

import java.util.Random;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class Ore extends Block {

	private final Item dropItem;
	private final int dropAdduct;
	private final int dropRange;
	
	
	
	public Ore(String name, Item oreDrop, int minNumberDropped, int maxNumberDropped, int pickLevel){
		super(Material.rock);
		this.setBlockName(Mineralogy.MODID +"_"+ name);
		this.setBlockTextureName(Mineralogy.MODID +":"+ name);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setHardness((float)1.5); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)5); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(Block.soundTypePiston); // sound for stone
		this.setHarvestLevel("pickaxe", pickLevel);
		dropItem = oreDrop;
		dropAdduct = minNumberDropped;
		dropRange = (maxNumberDropped - minNumberDropped) + 1;
	}
	
	@Override public Item getItemDropped(int p_149650_1_, Random prng, int p_149650_3_)
    {
        return dropItem;
    }
	@Override public int quantityDropped(Random prng)
    {
        return prng.nextInt(dropRange)+dropAdduct;
    }
}
