package cyano.mineralogy.worldgen;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.CAVE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.MINESHAFT;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.RAVINE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.SCATTERED_FEATURE;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.STRONGHOLD;
import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.VILLAGE;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;

import cyano.mineralogy.Mineralogy;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

/**
 * Replaces minecraft generic stone with new mineralogy stone blocks
 * @author Cyanobacterium
 *
 */

public class MineralogyChunkGenerator extends ChunkProviderGenerate{

	final String generatorOptionsString;
	final WorldType worldType;
	final World worldObj;
	final Geology geome;
	final boolean mapFeaturesEnabled;
	final Random rand;
	
	public MineralogyChunkGenerator(World world, long seed,
			boolean mapFeaturesEnabled, String generatorOptionsString, WorldType worldType) {
		super(world,seed,mapFeaturesEnabled);
		this.worldType = worldType;
		this.worldObj = world;
		this.generatorOptionsString = generatorOptionsString;
		this.mapFeaturesEnabled = mapFeaturesEnabled;
		this.rand = new Random(seed);
		
		geome = new Geology(seed,Mineralogy.GEOME_SIZE,Mineralogy.ROCK_LAYER_SIZE);
	}


	
	
	private MapGenBase caveGenerator = new MapGenCaves();
    /** Holds Stronghold Generator */
    private MapGenStronghold strongholdGenerator = new MapGenStronghold();
    /** Holds Village Generator */
    private MapGenVillage villageGenerator = new MapGenVillage();
    /** Holds Mineshaft Generator */
    private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
    private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
    /** Holds ravine generator */
    private MapGenBase ravineGenerator = new MapGenRavine();
    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;
    {
        caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
        strongholdGenerator = (MapGenStronghold) TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
        villageGenerator = (MapGenVillage) TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
        mineshaftGenerator = (MapGenMineshaft) TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
        scatteredFeatureGenerator = (MapGenScatteredFeature) TerrainGen.getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
        ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
    }
	
	@Override public Chunk provideChunk(int chunkX, int chunkY)
    {
        this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkY * 132897987541L);
        Block[] ablock = new Block[65536];
        byte[] abyte = new byte[65536];
        this.func_147424_a(chunkX, chunkY, ablock);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, chunkX * 16, chunkY * 16, 16, 16);
        this.replaceBlocksForBiome(chunkX, chunkY, ablock, abyte, this.biomesForGeneration);
        this.caveGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
        this.ravineGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
        
        geome.replaceStoneInChunk(chunkX,chunkY,ablock);

        if (this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
            this.villageGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
            this.strongholdGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, chunkX, chunkY, ablock);
        }

        Chunk chunk = new Chunk(this.worldObj, ablock, abyte, chunkX, chunkY);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k)
        {
            abyte1[k] = (byte)this.biomesForGeneration[k].biomeID;
        }

        chunk.generateSkylightMap();
        return chunk;
    }
	

	/*
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		Abstract3FCoordinateNoiseGenerator perlinNoise = new Default3FCoordinateNoiseGenerator(new Default3CoordinatePRNG(world.getSeed(),-1f,1f),Mineralogy.ROCK_LAYER_SIZE);
		
		// Implementation is WAY TOO SLOW!
		if(world.provider.dimensionId == 0){ // only apply to overworld, not Nether or others
			for(int x = chunkX << 4; x < ((chunkX << 4) + 16); x++){
				for(int z = chunkZ << 4; z < ((chunkZ << 4) + 16); z++){
					for(int y = world.getHeightValue(x, z); y > 50; y--){
						if(world.getBlock(x, y, z) == Blocks.stone ){
							// replace "stone" with a real stone
							// TODO use geo-biomes (aka geomes)
							world.setBlock(x,y,z,pickBlockFromList(perlinNoise.getValue(x, y*Mineralogy.Y_SCALE_FACTOR, z),Mineralogy.igneousStones));
						}
					}
				}
			}
			// TODO
		}
	}*/
	
	
	/**
	 * given any number, this method grabs a block from the list based on that number. 
	 * A gradually increasing number would cycle through the list. 
	 * @param value
	 * @param list
	 * @return
	 */
	static Block pickBlockFromList(float value, List<Block> list){
		float w = value - floor(value);
		int index = (int)(w * list.size());
		return list.get(index);
	}
	
	/**
	 * Faster implementation than Math.floor(x). 
	 * @param x
	 * @return The greatest integer value less than x. If x is NaN, then 0 is 
	 * returned. If x is infinite (positive or negative), then Long.MAX_VALUE is returned.
	 */
	private static int floor(float x) {
	//	return (long)Math.floor(x);/*
		if(x < 0){
			return (int)x - 1;
		} else {
			return (int)x;
		}/* */
	}



}
