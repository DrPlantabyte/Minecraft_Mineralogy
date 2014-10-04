package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import cyano.mineralogy.Mineralogy;

public class NitrateDust extends net.minecraft.item.Item {
	
	public final static String itemName = "nitrate_dust"; 
	public final static String dictionaryName = "dustNitrate";
	
	public NitrateDust(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
		this.setTextureName(Mineralogy.MODID +":"+ itemName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
}
