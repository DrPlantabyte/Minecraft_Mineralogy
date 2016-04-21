package cyano.mineralogy.blocks;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import java.util.Random;

public class Ore extends Block {

	private final Item dropItem;
	private final int dropAdduct;
	private final int dropRange;
	
	
	
	public Ore(String name, Item oreDrop, int minNumberDropped, int maxNumberDropped, int pickLevel){
		super(Material.ROCK);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setHardness((float)1.5); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)5); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setSoundType(SoundType.STONE); // sound for stone
		this.setHarvestLevel("pickaxe", pickLevel);
		dropItem = oreDrop;
		dropAdduct = minNumberDropped;
		dropRange = (maxNumberDropped - minNumberDropped) + 1;
	}
	
	@Override public Item getItemDropped(IBlockState bs, Random prng, int p_149650_3_)
    {
        return dropItem;
    }
	@Override public int quantityDropped(Random prng)
    {
        return prng.nextInt(dropRange)+dropAdduct;
    }
}
