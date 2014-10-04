package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import cyano.mineralogy.Mineralogy;

public class GypsumDust extends net.minecraft.item.Item {
	
	public final static String itemName = "gypsum_dust"; 
	public final static String dictionaryName = "dustGypsum";
	
	public GypsumDust(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
		this.setTextureName(Mineralogy.MODID +":"+ itemName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
}
