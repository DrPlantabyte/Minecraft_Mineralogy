package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import cyano.mineralogy.Mineralogy;

public class SulfurDust extends net.minecraft.item.Item {
	
	public final static String itemName = "sulfur_dust";
	
	public SulfurDust(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"."+ itemName);
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}
	
}
