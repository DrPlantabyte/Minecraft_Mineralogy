package cyano.mineralogy.items;

import net.minecraft.creativetab.CreativeTabs;
import cyano.mineralogy.Mineralogy;

public class SulphurDust extends net.minecraft.item.Item {
	
	public final static String itemName = "sulphur_dust"; 
	public final static String dictionaryName = "dustSulphur";
	
	public SulphurDust(){
		super();
		this.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
		this.setTextureName(Mineralogy.MODID +":"+ itemName);
		this.setCreativeTab(CreativeTabs.tabMaterials);
	}
	
}
