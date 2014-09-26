package cyano.mineralogy.worldgen;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.IWorldGenerator;
import cyano.mineralogy.Mineralogy;

public class OrePlacer implements IWorldGenerator {

	HashSet<String> stoneEquivalents = null;
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		initStoneList();
		for(OrePlacement op : Mineralogy.mineralogyOreSpawnRegistry.values()){
			int freq = op.frequency;
			if(freq == 0){
				if(random.nextInt(op.rarity) == 0){
					generateOre(op,random,world, chunkX, chunkZ);
				} else {
					continue;
				}
			} else{
				for(int i = 0; i < freq; i++){
					generateOre(op,random,world, chunkX, chunkZ);
				}
			}
		}
	}

	private void initStoneList() {
		if(stoneEquivalents == null){
			stoneEquivalents = new HashSet<String>();
			List<ItemStack> stones = OreDictionary.getOres("stone");
			for(ItemStack e : stones){
				stoneEquivalents.add(e.getUnlocalizedName());
			}
		}
	}
	 

	private void generateOre(OrePlacement op, Random random, World world, int chunkX, int chunkZ) {
		int y = random.nextInt(op.maxSpawnHeight - op.minSpawnHeight)+ op.minSpawnHeight;
		int x = random.nextInt(16) | (chunkX << 4);
		int z = random.nextInt(16) | (chunkZ << 4);
		int count = op.size-1;
		Block ore = op.block;
		
		if( count < 9){
			// small ore deposit
			if(random.nextBoolean()){
				if(count-- == 0) return;
				placeOre(x,y,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x+1,y,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y+1,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x+1,y+1,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y,z+1,op.block,world);
				if(count-- == 0) return;
				placeOre(x+1,y,z+1,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y+1,z+1,op.block,world);
				if(count-- == 0) return;
				placeOre(x+1,y+1,z+1,op.block,world);
			} else {
				if(count-- == 0) return;
				placeOre(x,y,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x-1,y,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y-1,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x-1,y-1,z,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y,z-1,op.block,world);
				if(count-- == 0) return;
				placeOre(x-1,y,z-1,op.block,world);
				if(count-- == 0) return;
				placeOre(x,y-1,z-1,op.block,world);
				if(count-- == 0) return;
				placeOre(x-1,y-1,z-1,op.block,world);
			}
			return;
		} else if( count < 32){
			// large ore deposit
			// cube
			placeOre(x  ,y  ,z  ,op.block,world);count--;
			placeOre(x+1,y  ,z  ,op.block,world);count--;
			placeOre(x  ,y+1,z  ,op.block,world);count--;
			placeOre(x+1,y+1,z  ,op.block,world);count--;
			placeOre(x  ,y  ,z+1,op.block,world);count--;
			placeOre(x+1,y  ,z+1,op.block,world);count--;
			placeOre(x  ,y+1,z+1,op.block,world);count--;
			placeOre(x+1,y+1,z+1,op.block,world);count--;
			// add to top face
			placeOre(x  ,y+2,z  ,ore,world); if(count-- == 0) return;
			placeOre(x+1,y+2,z  ,ore,world); if(count-- == 0) return;
			placeOre(x  ,y+2,z+1,ore,world); if(count-- == 0) return;
			placeOre(x+1,y+2,z+1,ore,world); if(count-- == 0) return;
			// add to bottom face
			placeOre(x  ,y-1,z  ,ore,world); if(count-- == 0) return;
			placeOre(x+1,y-1,z  ,ore,world); if(count-- == 0) return;
			placeOre(x  ,y-1,z+1,ore,world); if(count-- == 0) return;
			placeOre(x+1,y-1,z+1,ore,world); if(count-- == 0) return;
			// add to side faces
			if(random.nextBoolean()){
				// x first
				placeOre(x-1,y  ,z  ,ore,world); if(count-- == 0) return;
				placeOre(x-1,y+1,z  ,ore,world); if(count-- == 0) return;
				placeOre(x-1,y  ,z+1,ore,world); if(count-- == 0) return;
				placeOre(x-1,y+1,z+1,ore,world); if(count-- == 0) return;
				placeOre(x+2,y  ,z  ,ore,world); if(count-- == 0) return;
				placeOre(x+2,y+1,z  ,ore,world); if(count-- == 0) return;
				placeOre(x+2,y  ,z+1,ore,world); if(count-- == 0) return;
				placeOre(x+2,y+1,z+1,ore,world); if(count-- == 0) return;
				placeOre(x  ,y  ,z-1,ore,world); if(count-- == 0) return;
				placeOre(x+1,y  ,z-1,ore,world); if(count-- == 0) return;
				placeOre(x  ,y+1,z-1,ore,world); if(count-- == 0) return;
				placeOre(x+1,y+1,z-1,ore,world); if(count-- == 0) return;
				placeOre(x  ,y  ,z+2,ore,world); if(count-- == 0) return;
				placeOre(x+1,y  ,z+2,ore,world); if(count-- == 0) return;
				placeOre(x  ,y+1,z+2,ore,world); if(count-- == 0) return;
				placeOre(x+1,y+1,z+2,ore,world); if(count-- == 0) return;
			}else{
				// z first
				placeOre(x  ,y  ,z-1,ore,world); if(count-- == 0) return;
				placeOre(x+1,y  ,z-1,ore,world); if(count-- == 0) return;
				placeOre(x  ,y+1,z-1,ore,world); if(count-- == 0) return;
				placeOre(x+1,y+1,z-1,ore,world); if(count-- == 0) return;
				placeOre(x  ,y  ,z+2,ore,world); if(count-- == 0) return;
				placeOre(x+1,y  ,z+2,ore,world); if(count-- == 0) return;
				placeOre(x  ,y+1,z+2,ore,world); if(count-- == 0) return;
				placeOre(x+1,y+1,z+2,ore,world); if(count-- == 0) return;
				placeOre(x-1,y  ,z  ,ore,world); if(count-- == 0) return;
				placeOre(x-1,y+1,z  ,ore,world); if(count-- == 0) return;
				placeOre(x-1,y  ,z+1,ore,world); if(count-- == 0) return;
				placeOre(x-1,y+1,z+1,ore,world); if(count-- == 0) return;
				placeOre(x+2,y  ,z  ,ore,world); if(count-- == 0) return;
				placeOre(x+2,y+1,z  ,ore,world); if(count-- == 0) return;
				placeOre(x+2,y  ,z+1,ore,world); if(count-- == 0) return;
				placeOre(x+2,y+1,z+1,ore,world); if(count-- == 0) return;
			}
			return;
		}else{
			//really big ore deposit
			placeOre(x,y,z,ore,world);
			if(count-- == 0) return;
			int r = 1;
			while (count > 0){
				for(int i = -1 * r; i <= r; i++){
					for(int j = -1 * r; j <= r; j++){
						// X-Z plane top
						placeOre(x+i,y+r,z+j,ore,world); if(count-- == 0) return;
						// X-Z plane bottom
						placeOre(x+i,y-r,z+j,ore,world); if(count-- == 0) return;
						// X-Y plane right
						placeOre(x+i,y+j,z+r,ore,world); if(count-- == 0) return;
						// X-Y plane left
						placeOre(x+i,y+j,z-r,ore,world); if(count-- == 0) return;
						// Z-Y plane right
						placeOre(x+r,y+i,z+j,ore,world); if(count-- == 0) return;
						// Z-Y plane left
						placeOre(x-r,y+i,z+j,ore,world); if(count-- == 0) return;
					}
				}
				// corners
				placeOre(x-r,y-r,z-r,ore,world); if(count-- == 0) return;
				placeOre(x+r,y-r,z-r,ore,world); if(count-- == 0) return;
				placeOre(x-r,y-r,z+r,ore,world); if(count-- == 0) return;
				placeOre(x+r,y-r,z+r,ore,world); if(count-- == 0) return;
				placeOre(x-r,y+r,z-r,ore,world); if(count-- == 0) return;
				placeOre(x+r,y+r,z-r,ore,world); if(count-- == 0) return;
				placeOre(x-r,y+r,z+r,ore,world); if(count-- == 0) return;
				placeOre(x+r,y+r,z+r,ore,world); if(count-- == 0) return;
				r++;
			}
			return;
		}
		
	}
	
	private void placeOre(int x, int y, int z, Block b, World w){
		String blockName = w.getBlock(x, y, z).getUnlocalizedName();
		if(stoneEquivalents.contains(blockName)){
			// is stone, place ore
			w.setBlock(x, y, z, b);
		}
	}

}
