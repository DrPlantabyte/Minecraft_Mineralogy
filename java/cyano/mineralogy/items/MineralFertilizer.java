package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import cyano.mineralogy.Mineralogy;

public class MineralFertilizer extends Item{

	public final static String itemName = "mineral_fertilizer"; 
	public final static String dictionaryName = "fertilizer";
	public MineralFertilizer(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	

	
	private final ItemStack phantomBonemeal = new ItemStack(Items.dye,1,15); 
	
	@Override public boolean onItemUse(ItemStack srcItemStack, EntityPlayer playerEntity, World world, BlockPos target, EnumFacing face, float par8, float par9, float par10){
		boolean canUse = ItemDye.func_179234_a(srcItemStack,world,target);//ItemDye.applyBonemeal(srcItemStack,world,target);
		if(canUse){
			phantomBonemeal.stackSize = 27;
			for(int dx = -1; dx <= 1; dx++){
				for(int dy = -1; dy <= 1; dy++){
					for(int dz = -1; dz <= 1; dz++){
						if((dx | dy | dz) == 0) continue;
						BlockPos t = target.add(dx, dy, dz);
						ItemDye.func_179234_a(phantomBonemeal,world,t);//ItemDye.applyBonemeal(phantomBonemeal,world,t);
					}
				}
			}
		}
		return canUse;
	}
}
