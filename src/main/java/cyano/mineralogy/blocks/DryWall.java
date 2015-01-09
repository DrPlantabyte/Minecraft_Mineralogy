package cyano.mineralogy.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cyano.mineralogy.Mineralogy;

public class DryWall extends net.minecraft.block.BlockPane{

	final static String itemName = "drywall";
	
	public DryWall(String color) {
		super( Material.rock, true);
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName+"_"+color);
	}

	@Override public Item getItemDropped(IBlockState bs, Random prng, int enchantmentLevel){
		return Item.getItemFromBlock(this);
	}
	
}
