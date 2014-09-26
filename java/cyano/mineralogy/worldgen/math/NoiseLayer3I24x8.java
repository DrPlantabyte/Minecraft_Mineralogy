package cyano.mineralogy.worldgen.math;
/**
 * Uses 32bit fixed-point numbers with 8 fractional bits
 * @author Cyanobacterium
 *
 */@Deprecated
public class NoiseLayer3I24x8 {

	private final long seed;

	/** from java.util.Random implementation */
    private static final long rand_multiplier = 0x5DEECE66DL;
    /** from java.util.Random implementation */
    private static final long rand_addend = 0xBL;
    /** from java.util.Random implementation */
    private static final long rand_mask = (1L << 48) - 1;
    
    private final int multiplier;
    
    private final int magnitude;
     
    
    static final int one = 1 << 8;
    
    public NoiseLayer3I24x8(long seed, double size, double magnitude){
    	this.seed = seed;
    	this.multiplier = (int)(one / size);
    	this.magnitude = (int)(one * magnitude);
    }
    
    public int getValueAt(int x, int y, int z){
    	x *= multiplier;
    	y *= multiplier;
    	z *= multiplier;
    	int gridX = x >> 8;//  / one;
    	int gridY = y >> 8;//  / one;
    	int gridZ = z >> 8;//  / one;
    	int[][][] local64 = new int[4][4][4];
    	for(int dx = 0; dx < 4; dx++){
    		for(int dy = 0; dy < 4; dy++){
        		for(int dz = 0; dz < 4; dz++){
        			local64[dx][dy][dz] = randAt(gridX + dx, gridY + dy, gridZ + dz);
        		}
    		}
    	}
    	return CubicInterpolator.interpolate3I24x8(x, y, z, local64);
    }
    
    protected int randAt(int x, int y, int z){
    	long h = hash(x,y,z,seed);
    	int value = (magnitude * (((int)h & 0x1FF)))/one - magnitude;
    	return value;
    }
    
    static long hash(int x, int y, int z, long seed){
    	long l = (seed ^ rand_multiplier) & rand_mask;
    	l = (((l * rand_multiplier) + rand_addend) ^ x) & rand_mask;
    	l = (((l * rand_multiplier) + rand_addend) ^ y) & rand_mask;
    	l = (((l * rand_multiplier) + rand_addend) ^ z) & rand_mask;
    	l = ((l * rand_multiplier) + rand_addend) & rand_mask;
    	return l;
    }
}
