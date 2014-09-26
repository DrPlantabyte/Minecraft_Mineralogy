package cyano.mineralogy.worldgen;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cyano.mineralogy.Mineralogy;
import cyano.mineralogy.worldgen.math.NoiseLayer3I24x8;
import cyano.mineralogy.worldgen.math.PerlinNoise2D;

public class Geology {


	private final PerlinNoise2D geomeNoiseLayer;
	private final PerlinNoise2D rockNoiseLayer;
	private final long seed;
	
	private final double geomeSize;
	
	

	/** random implementation */
    private static final int multiplier = 1103515245;
    /** random implementation */
    private static final int addend = 12345;
    /** random implementation */
    private static final int mask = (1 << 31) - 1;
    
	/**
	 * 
	 * @param seed World seed
	 * @param geomeSize Approximate size of rock type layers (should be much bigger than <code>rockLayerSize</code>
	 * @param rockLayerSize Approximate diameter of layers in the X-Z plane
	 * @param layerFlatness Higher numbers results in thinner rock layers
	 */
	public Geology(long seed, double geomeSize, double rockLayerSize){
		this.seed = seed;
		int rockLayerUndertones = 4;
		int undertoneMultiplier = 1 << (rockLayerUndertones - 1);
		geomeNoiseLayer = new PerlinNoise2D(~seed,128,geomeSize,2);
		rockNoiseLayer = new PerlinNoise2D(seed,4*undertoneMultiplier,rockLayerSize*undertoneMultiplier,rockLayerUndertones);
		this.geomeSize = geomeSize;
	}
	/**
	 * This method gets the stone replacement for a given coordinate. It does not 
	 * check whether there should be stone at the given coordinate, just what 
	 * block to put there if there were to be stone at the given coordinate. 
	 * @param x X coordinate (block coordinate space)
	 * @param y Y coordinate (block coordinate space) 
	 * @param z Z coordinate (block coordinate space)
	 * @return A Block object from this mod's registry of stones
	 */
	public Block getStoneAt(int x, int y, int z){
		// new method: 2D perlin noise instead of 3D
		double geome = geomeNoiseLayer.valueAt(x,z)+y;
		int rv = (int)rockNoiseLayer.valueAt(x, z) + y;
		if(geome < -64){
			// RockType.IGNEOUS;
			return pickBlockFromList(rv,Mineralogy.igneousStones);
		} else if(geome < 64){
			// RockType.METAMORPHIC;
			return pickBlockFromList(rv,Mineralogy.metamorphicStones);
		} else {
			// RockType.SEDIMENTARY;
			return pickBlockFromList(rv,Mineralogy.sedimentaryStones);
		}
		
	}
	
	public void replaceStoneInChunk(int chunkX, int chunkZ, Block[] blockBuffer)
    {
		
		// TODO remove following block that makes air blocks
		// TODO try moving substitution to registered generator instead of provider
		if(chunkZ % 4 != 0){
			for(int i = 0; i < blockBuffer.length; i++){blockBuffer[i] = Blocks.air;}
		}
		int height = blockBuffer.length / 256;
		int xOffset = chunkX << 4;
		int zOffset = chunkZ << 4;
		for(int dx = 0; dx < 16; dx++){
			for(int dz = 0; dz < 16; dz++){
				int indexBase = (dx * 16 + dz) * height;
				int y = height-1;
				while(y > 0 && blockBuffer[indexBase+y] == Blocks.air){
					y--;
				}
				Block[] column = this.getStoneColumn(xOffset | dx, zOffset | dz, y);
				for(; y > 0; y--){
					int i = indexBase + y;
					if(blockBuffer[i] == Blocks.stone){
						blockBuffer[i] = column[y];
					}
				}
			}
		} 
		
    }
	
	public Block[] getStoneColumn(int x, int z, int height){
		Block[] col = new Block[height];
		int baseRockVal = (int)rockNoiseLayer.valueAt(x, z);
		double gbase = geomeNoiseLayer.valueAt(x, z);
		for(int y = 0; y < col.length; y++){
			double geome = gbase+y;
			if(geome < -32){
				// RockType.IGNEOUS;
				col[y] = pickBlockFromList(baseRockVal+y,Mineralogy.igneousStones);
			} else if(geome < 32){
				// RockType.METAMORPHIC;
				col[y] = pickBlockFromList(baseRockVal+y+3,Mineralogy.metamorphicStones);
			} else {
				// RockType.SEDIMENTARY;
				col[y] = pickBlockFromList(baseRockVal+y+5,Mineralogy.sedimentaryStones);
			}
		}
		return col;
	}
	
	/**
	 * given any number, this method grabs a block from the list based on that number. 
	 * @param value product of noise layer + height
	 * @param list
	 * @return
	 */
	private Block pickBlockFromList(int value, List<Block> list){
		int i = ((((int)(seed) ^ (value >> 2) ) * multiplier + addend) * multiplier + addend) & 0x7FFFFFFF;
		i %= list.size();
		return list.get(i);
	}
	// TODO get noise to look right
	@Deprecated public static void main(String[] args){
		int size = 512;
	//	Geology geome  = new Geology((new java.util.Random()).nextLong(),Mineralogy.GEOME_SIZE, Mineralogy.ROCK_LAYER_SIZE);
		PerlinNoise2D pn = new PerlinNoise2D(System.currentTimeMillis(), 1, 512, 2);
		java.awt.image.BufferedImage bimg = new java.awt.image.BufferedImage(size,size,java.awt.image.BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < size; x++){
			for(int y = 0; y < size; y++){
			//	double rv = geome.rockNoiseLayer.valueAt(x, y) / 3;
				double rv = pn.valueAt(x, y);
				rv = rv - (int)rv;
				if(rv < 0) rv += 1;
				bimg.setRGB(x, size - y - 1, java.awt.Color.HSBtoRGB(0f, 0f, (float)rv));
			}
		}
		javax.swing.JOptionPane.showMessageDialog(null, new javax.swing.JLabel(new javax.swing.ImageIcon(bimg)));
		
	}
	
	
}
