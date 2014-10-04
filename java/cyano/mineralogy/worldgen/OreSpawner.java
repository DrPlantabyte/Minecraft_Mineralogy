package cyano.mineralogy.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class OreSpawner implements IWorldGenerator {

	private final int frequency;
	private final 	WorldGenMinable oreGen;
	private final int minY;
	private final int maxY;
	private final long hash; // used to make prng's different
	
	private final Block ore;
	
	public OreSpawner(Block oreBlock, int minHeight, int maxHeight, int spawnFrequency, int spawnQuantity, long hash){
		oreGen = new WorldGenMinable(oreBlock, 0, spawnQuantity, Blocks.stone);
		frequency = spawnFrequency;
		minY = minHeight;
		maxY = maxHeight;
		ore = oreBlock;
		this.hash = hash;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		random.setSeed(random.nextLong() ^ hash);
		random.nextInt();
		for(int i = 0; i < frequency; i++){
			int x = (chunkX << 4) + random.nextInt(16);
            int y = random.nextInt(maxY - minY) + minY;
            int z = (chunkZ << 4) + random.nextInt(16);
        //    System.out.println("Generating deposite of "+ore.getUnlocalizedName()+" at ("+x+","+y+","+z+")");
            oreGen.generate(world, random, x, y, z);
		}
	}

}
