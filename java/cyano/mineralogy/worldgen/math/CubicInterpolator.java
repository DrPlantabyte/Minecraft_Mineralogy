package cyano.mineralogy.worldgen.math;

/**
 * This is a convenience class that provides multidimensional cubic 
 * interpolation methods. This class is abstract and not meant to be extended
 * @author Cyanobacterium
 */
public abstract class CubicInterpolator {
	
	
	/**
	 * Interpolate with cubic approximation for a point X on a grid. X 
	 * must lie between the X values of the yn1 and yp1 control points.
	 * @param x x coordinate to interpolate
	 * @param yn2 Y value at f(floor(x)-1)
	 * @param yn1 Y value at f(floor(x)-0)
	 * @param yp1 Y value at f(floor(x)+1)
	 * @param yp2 Y value at f(floor(x)+2)
	 * @return A cubic-interpolated value from the given control points.
	 */
	public static double interpolate(double x, double yn2, double yn1, double yp1, double yp2){
		return interpolate1d( x,  yn2,  yn1,  yp1,  yp2);
	}
	
	/**
	 * Interpolate with cubic approximation for a point X on a grid. X 
	 * must lie between the X values of the yn1 and yp1 control points.
	 * @param x x coordinate to interpolate
	 * @param yn2 Y value at f(floor(x)-1)
	 * @param yn1 Y value at f(floor(x)-0)
	 * @param yp1 Y value at f(floor(x)+1)
	 * @param yp2 Y value at f(floor(x)+2)
	 * @return A cubic-interpolated value from the given control points.
	 */
	public static double interpolate1d(double x, double yn2, double yn1, double yp1, double yp2){
		double w = x - Math.floor(x);
		if(w == 0 && x != 0) return yp1; // w should be 1, but the way this is calcluated it doesn't work right
		// prevent precision-loss artifacts
		if(w < 0.00000001){
			return yn1;
		}
		if(w > 0.9999999) {
			return yp1;
		}
		// adapted from http://www.paulinternet.nl/?page=bicubic
		double A = -0.5 * yn2 + 1.5 * yn1 - 1.5 * yp1 + 0.5 * yp2;
		double B = yn2 - 2.5 * yn1 + 2 * yp1 - 0.5 * yp2;
		double C = -0.5 * yn2 + 0.5 * yp1;
		double D = yn1;
		return A * w * w * w + B * w * w + C * w + D;
	}
	/**
	 * Returns the bi-cubic interpolation of the (x,y) coordinate inide 
	 * the provided grid of control points. (x,y) is assumed to be in the 
	 * center square of the unit grid.
	 * @param x x coordinate between local16[1][y] and local16[2][y]
	 * @param y y coordinate between local16[x][1] and local16[x][2]
	 * @param local16 Array [x][y] of the 4x4 grid around the coordinate
	 * @return Returns the bi-cubic interpolation of the (x,y) coordinate.
	 */
	public static double interpolate2d(double x, double y, double[][] local16){
		double[] section = new double[4];
		for(int i = 0; i < 4; i++){
			section[i] = interpolate1d(y,local16[i][0],local16[i][1],local16[i][2],local16[i][3]);
		}
		return interpolate1d(x,section[0],section[1],section[2],section[3]);
	}
	
	/**
	 * Performs a tri-cubic interpolation of the (x,y,z) coordinate near 
	 * the center of the provided unit grid of surrounding control points.
	 * @param x x coordinate in the middle of the array space
	 * @param y y coordinate in the middle of the array space
	 * @param z z coordinate in the middle of the array space
	 * @param local64 Array [x][y][z] of the 4x4x4 grid around the coordinate
	 * @return Returns the tri-cubic interpolation of the given coordinate.
	 */
	public static double interpolate3d(double x, double y, double z, double[][][] local64){
		double[] section = new double[4];
		for(int i = 0; i < 4; i++){
			section[i] = interpolate2d(y,z,local64[i]);
		}
		return interpolate1d(x,section[0],section[1],section[2],section[3]);
	}
	
	

	
	private static final int oneI24x8 = 1 << 8; 
	/** like interpolate1d(...), but using fixed-point numbers with 24 integer and 8 fractional bits */
	public static int interpolate1I24x8(int x, int yn2, int yn1, int yp1, int yp2){
		int w = x & 0xFF;
		// prevent precision-loss artifacts
		if(w  == 0){
			return yn1;
		}
		// adapted from http://www.paulinternet.nl/?page=bicubic
		int A = (3 * yn1 / 2) - (3 * yp1 / 2) + (yp2 >> 1) - (yn2 >> 1);
		int B = yn2 - (5 * yn1 / 2) + (yp1 << 1) - ( yp2 >> 1);
		int C = ( yp1 >> 1) - ( yn2 >> 1);
		int D = yn1;
		return A * w * w * w + B * w * w + C * w + D;
	}
	/** like interpolate2d(...), but using fixed-point numbers with 24 integer and 8 fractional bits */
	public static int interpolate2I24x8(int x, int y, int[][] local16){
		int[] section = new int[4];
		for(int i = 0; i < 4; i++){
			section[i] = interpolate1I24x8(y,local16[i][0],local16[i][1],local16[i][2],local16[i][3]);
		}
		return interpolate1I24x8(x,section[0],section[1],section[2],section[3]);
	}
	/** like interpolate3d(...), but using fixed-point numbers with 24 integer and 8 fractional bits */
	public static int interpolate3I24x8(int x, int y, int z, int[][][] local64){
		int[] section = new int[4];
		for(int i = 0; i < 4; i++){
			section[i] = interpolate2I24x8(y,z,local64[i]);
		}
		return interpolate1I24x8(x,section[0],section[1],section[2],section[3]);
	}
	

}
