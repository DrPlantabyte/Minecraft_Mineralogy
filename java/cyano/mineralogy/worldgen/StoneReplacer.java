package cyano.mineralogy.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import cpw.mods.fml.common.IWorldGenerator;
import cyano.mineralogy.Mineralogy;
@Deprecated // using a generator to make rock layers throws exception on World.setBlock(), presumable because it notifies blocks not yet loaded
public class StoneReplacer implements IWorldGenerator{

	private Geology geome = null;
	
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		// laxy initialization (to get world seed)
		if(geome == null)geome = new Geology(world.getSeed(),Mineralogy.GEOME_SIZE,Mineralogy.ROCK_LAYER_SIZE);
		int xOffset = chunkX << 4;
		int zOffset = chunkZ << 4;
		for(int dx = 0; dx < 16; dx++){
			int x = xOffset | dx;
			for(int dz = 0; dz < 16; dz++){
				int z = zOffset | dz;
				int y = 255;
				while(y > 0 && world.isAirBlock(x, y, z)){
					y--;
				}
				Block[] column = geome.getStoneColumn(x, z, y);
				for(; y > 0; y--){
					if(world.getBlock(x, y, z) == Blocks.stone){
						world.setBlock(x, y, z, column[y]);
					}
				}
			}
		}
	}

}
