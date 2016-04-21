package cyano.mineralogy.blocks;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class DryWall extends net.minecraft.block.BlockPane{

	final static String itemName = "drywall";
	
	public DryWall(String color) {
		super( Material.ROCK, true);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName+"_"+color);
		this.useNeighborBrightness = true;
	}

	@Override public Item getItemDropped(IBlockState bs, Random prng, int enchantmentLevel){
		return Item.getItemFromBlock(this);
	}
	
}
