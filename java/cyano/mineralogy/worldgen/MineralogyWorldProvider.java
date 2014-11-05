package cyano.mineralogy.worldgen;

import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

public class MineralogyWorldProvider extends WorldProviderSurface {

	public MineralogyWorldProvider(){}
	
	/**
     * Returns a new chunk provider which generates chunks for this world
     */
	@Override
    public IChunkProvider createChunkGenerator()
    {
		// Lost ability to check for flat worlds?
		//if(terrainType == WorldType.FLAT){
		//	return super.createChunkGenerator();
		//}
		String generatorOptionsString = worldObj.getWorldInfo().getGeneratorOptions();
        return new MineralogyChunkGenerator(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), generatorOptionsString, worldObj.getWorldType());
    }
	
}
