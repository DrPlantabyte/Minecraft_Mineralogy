package cyano.mineralogy.worldgen.math;
/**
 * Perlin Noise Octave
 * @author Cyanobacterium
 *
 */
public class NoiseLayer2D {
	
	private final long seed;

	/** from java.util.Random implementation */
    private static final long rand_multiplier = 0x5DEECE66DL;
    /** from java.util.Random implementation */
    private static final long rand_addend = 0xBL;
    /** from java.util.Random implementation */
    private static final long rand_mask = (1L << 48) - 1;
    
    private final double multiplier;
    
    private final double magnitude;
    
    private final int precisionMask = 0x0FFFFFFF; 
    private final double intConversionMultiplier = 2.0 / (double)precisionMask; // 2.0 because we will be subtracting 1 to make it range from -1 to 1
    
    public NoiseLayer2D(long seed, double size, double magnitude){
    	this.seed = seed;
    	this.multiplier = 1.0 / size;
    	this.magnitude = magnitude;
    }
    
    public double getValueAt(double x, double y){
    	x *= multiplier;
    	y *= multiplier;
    	int gridX = (int)(x);
    	int gridY = (int)(y);
    	double[][] local16 = new double[4][4];
    	for(int dx = 0; dx < 4; dx++){
    		for(int dy = 0; dy < 4; dy++){
    			local16[dx][dy] = randAt(gridX + dx, gridY + dy);
    		}
    	}
    	return CubicInterpolator.interpolate2d(x, y, local16);
    }
    
    protected double randAt(int x, int y){
    	long h = hash(x,y,seed);
    	double value = magnitude * ((h & precisionMask) * intConversionMultiplier - 1.0);
    	return value;
    }
    
    static long hash(int x, int y, long seed){
    	long l = (seed ^ rand_multiplier) & rand_mask;
    	l = (((l * rand_multiplier) + rand_addend) ^ x) & rand_mask;
    	l = (((l * rand_multiplier) + rand_addend) ^ y) & rand_mask;
    	l = ((l * rand_multiplier) + rand_addend) & rand_mask;
    	return l;
    }

}
