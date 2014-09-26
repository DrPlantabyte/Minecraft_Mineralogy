package cyano.mineralogy.worldgen;

import net.minecraft.block.Block;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import cpw.mods.fml.common.registry.GameRegistry;

import java.util.logging.Level;
import java.util.logging.Logger;

import cyano.mineralogy.Mineralogy;
import cyano.mineralogy.RockType;

/**
 * The OrePlacement class defines the rules for ore placement by Mineralogy
 * @author Cyanobacterium
 *
 */
public class OrePlacement {

	/** The block to spawn for this ore */
	public final Block block;
	/** The number of ore deposits per chunk. If 0, then use rarity instead */
	public final int frequency;
	/** The average number of chunks per ore deposits. If 0, use frequency instead */
	public final int rarity;
	/** number of blocks per deposit */
	public final int size;
	/** Minimum Y value for ore to appear */
	public final int minSpawnHeight;
	/** Maximum Y value for ore to appear */
	public final int maxSpawnHeight;
	public final RockType geologyType; 
	/**
	 * Creates an object describing the rules for placing this ore deposit
	 * @param ore The block to spawn for this ore 
	 * @param depositsPerChunk The number of ore deposits per chunk. If less than 1, 
	 * then the inverse is the number of chunks per ore deposit
	 * @param blocksPerDeposit number of blocks per deposit
	 * @param minY Minimum Y value for ore to appear
	 * @param maxY Maximum Y value for ore to appear
	 */
	public OrePlacement(Block ore, float depositsPerChunk, int blocksPerDeposit, int minY, int maxY){
		this.block = ore;
		this.frequency = (int)depositsPerChunk;
		this.rarity = (int)(1.0 / depositsPerChunk);
		this.size = blocksPerDeposit;
		this.minSpawnHeight = minY;
		this.maxSpawnHeight = maxY;
		this.geologyType = RockType.ANY;
	}/**
	 * Creates an object describing the rules for placing this ore deposit
	 * @param ore The block to spawn for this ore 
	 * @param depositsPerChunk The number of ore deposits per chunk. If less than 1, 
	 * then the inverse is the number of chunks per ore deposite
	 * @param blocksPerDeposit number of blocks per deposit
	 * @param minY Minimum Y value for ore to appear
	 * @param maxY Maximum Y value for ore to appear
	 * @param geologyPlacement If set to a rock type other than ANY, then this ore 
	 * will only appear in specific types of rock formations 
	 */
	public OrePlacement(Block ore, float depositsPerChunk, int blocksPerDeposit, int minY, int maxY, RockType geologyPlacement){
		this.block = ore;
		this.frequency = (int)depositsPerChunk;
		this.rarity = (int)(1.0 / depositsPerChunk);
		this.size = blocksPerDeposit;
		this.minSpawnHeight = minY;
		this.maxSpawnHeight = maxY;
		this.geologyType = RockType.ANY;
	}
	public static OrePlacement parseJSON(String json) throws JsonParserException, NumberFormatException, IllegalArgumentException{
		JsonObject input = JsonParser.object().from(json);
		String blockString = input.getString("BlockID"); 
		if(blockString == null || blockString.isEmpty()){
			throw new IllegalArgumentException("OrePlacement JSON string "+json+" is missing required member variable \"BlockID\"");
		}
		if(blockString.contains(":") == false){
			blockString = "minecraft:"+blockString;
		}
		String blockID = blockString.substring(blockString.indexOf(':')+1, blockString.length());
		String modID = blockString.substring(0,blockString.indexOf(':'));
		
		float frequency = input.getFloat("Count");
		if(frequency <= 0.0f || input.containsKey("Count") == false){
			throw new IllegalArgumentException("OrePlacement JSON string "+json+" must have member variable \"Count\" set to a positive number.");
		}
		int size = input.getInt("Size");
		if(size <= 0 || input.containsKey("Size") == false){
			throw new IllegalArgumentException("OrePlacement JSON string "+json+" must have member variable \"Size\" set to a positive integer.");
		}
		int minY = input.getInt("MinHeight");
		if(minY < 0 || input.containsKey("MinHeight") == false){
			throw new IllegalArgumentException("OrePlacement JSON string "+json+" must have member variable \"MinHeight\" set to a positive integer or zero.");
		}
		int maxY = input.getInt("MaxHeight");
		if(maxY <= minY ){
			throw new IllegalArgumentException("OrePlacement JSON string "+json+" must have member variable \"MaxHeight\" set to a positive integer that is greator than value of \"MinHeight\".");
		}
		Block block = GameRegistry.findBlock(modID, blockID);
		if(block == null){
			throw new IllegalArgumentException("Block "+modID+":"+blockID+" does not exist in block registry.");
		}
		if(input.containsKey("RockType") == false){
			return new OrePlacement(block,frequency,size,minY,maxY);
		}else{
			String rockTypeStr = input.getString("RockType");
			RockType type = RockType.valueOf(rockTypeStr);
			if(rockTypeStr == null || rockTypeStr.isEmpty()){
				throw new IllegalArgumentException("OrePlacement JSON string "+json+" is missing required member variable \"RockType\"");
			}
			return new OrePlacement(block,frequency,size,minY,maxY,type);
		}
	}
}
