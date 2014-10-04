package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import cyano.mineralogy.Mineralogy;

public class PhosphoriteDust extends net.minecraft.item.Item {
	
	public final static String itemName = "phosphorous_dust"; 
	public final static String dictionaryName = "dustPhosphorous";
	
	public PhosphoriteDust(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
		this.setTextureName(Mineralogy.MODID +":"+ itemName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
}
