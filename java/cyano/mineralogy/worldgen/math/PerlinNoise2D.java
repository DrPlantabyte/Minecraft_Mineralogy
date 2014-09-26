package cyano.mineralogy.worldgen.math;

public class PerlinNoise2D {

	private final NoiseLayer2D[] layers;
	
	

	/** from java.util.Random implementation */
    private static final long rand_multiplier = 0x5DEECE66DL;
    /** from java.util.Random implementation */
    private static final long rand_addend = 0xBL;
    /** from java.util.Random implementation */
    private static final long rand_mask = (1L << 48) - 1;
	
	public PerlinNoise2D(long seed, double initialRange, double initialSize, int numOvertoneLayers){
		layers = new NoiseLayer2D[numOvertoneLayers];
		for(int i = 0; i < layers.length; i++){
			seed = scramble(seed);
			layers[i] = new NoiseLayer2D(seed,initialSize,initialRange);
			initialSize *= 0.5;
			initialRange *= 0.5;
		}
	}
	
	public double valueAt(double x, double y){
		double sum = 0;
		for(int i = 0; i < layers.length; i++){
			sum += layers[i].getValueAt(x, y);
		}
		return sum;
	}
	
	private static long scramble(long l){
		return ((l * rand_multiplier) + rand_addend) & rand_mask;
	}
}
