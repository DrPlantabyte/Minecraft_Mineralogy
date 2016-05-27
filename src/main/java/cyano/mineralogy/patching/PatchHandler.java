package cyano.mineralogy.patching;

import cyano.mineralogy.Mineralogy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static cyano.mineralogy.Mineralogy.MODID;

/**
 * Created by Chris on 5/10/2016.
 */
public class PatchHandler {
	private static PatchHandler instance = null;
	private PatchHandler(){
		//
	}

	Block saprolite;
	Block pummice; // note the mispelling

	public static PatchHandler getInstance(){
		if(instance == null){
			instance = new PatchHandler();
		}
		return instance;
	}

	public void init(boolean enabled){
		if(enabled){
			saprolite = legacyBlock("saprolite", Mineralogy.mineralogyBlockRegistry.get("limestone").getDefaultState());
			pummice = legacyBlock("pummice", Mineralogy.blockPumice.getDefaultState());
		}
	}





	private static Block legacyBlock(String name, IBlockState replacement){
		Block b = new UpdateBlock(replacement);
		GameRegistry.register(b.setRegistryName(MODID,name));
		b.setUnlocalizedName(MODID+"."+name);
		return b;
	}
}
