package cyano.mineralogy;

// DON'T FORGET TO UPDATE mcmod.info FILE!!!

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.grack.nanojson.JsonParserException;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cyano.mineralogy.blocks.Rock;
import cyano.mineralogy.worldgen.MineralogyWorldProvider;
import cyano.mineralogy.worldgen.OrePlacement;


// TODO:
// add ores, powders, and gems
// add crucible, metal alloys, and rock hammer
// add tutorial achievement tree
// add secret door and dry-wall
// add villager trades


@Mod(modid = Mineralogy.MODID, name=Mineralogy.NAME, version = Mineralogy.VERSION)
public class Mineralogy
{
    public static final String MODID = "mineralogy";
    public static final String NAME ="Mineralogy";
    public static final String VERSION = "0.1";
    /** stone block replacesments that are sedimentary */
    public static final List<Block> sedimentaryStones = new ArrayList<Block>();
    /** stone block replacesments that are metamorphic */
    public static final List<Block> metamorphicStones = new ArrayList<Block>();
    /** stone block replacesments that are igneous */
    public static final List<Block> igneousStones = new ArrayList<Block>();
    /** all blocks used in this mod (blockID,block)*/
    public static final Map<String,Block> mineralogyBlockRegistry = new HashMap<String,Block>();
    /** all ores that will be spawned via this mod*/
    public static final Map<String,OrePlacement> mineralogyOreSpawnRegistry = new HashMap<String,OrePlacement>();
    
    /** size of rock layers */
    public static double ROCK_LAYER_SIZE = 32; 
    /** size of mineral biomes */
    public static int GEOME_SIZE = 100; 

 //   public static OrePlacer orePlacementGenerator = null;
    
    public static Block testBlock;
    public static Item testItem;
    
    public final static String CONFIG_CATAGORY_ORES = "ores"; 
    
    private Set<Entry<String,Property>> oreProperties = null;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	// load config
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();
    	
    	// Blocks, Items, World-gen
    	addStoneType(RockType.IGNEOUS,"andesite",1.5,10,0,true,true,true,false);
    	addStoneType(RockType.IGNEOUS,"basalt",5,100,2,true,true,true,false);
    	addStoneType(RockType.IGNEOUS,"diorite",1.5,10,0,true,true,true,false);
    	addStoneType(RockType.IGNEOUS,"granite",3,15,1,true,true,true,false);
    	addStoneType(RockType.IGNEOUS,"pumice",0.75,1,0,false,true,true,false);
    	addStoneType(RockType.IGNEOUS,"rhyolite",1.5,10,0,true,true,true,false);
    	addStoneType(RockType.IGNEOUS,"pegmatite",1.5,10,0,true,true,true,false);
    	addStoneType(RockType.SEDIMENTARY,"shale",1.5,10,0,true,true,true,true);
    	addStoneType(RockType.SEDIMENTARY,"conglomerate",1.5,10,0,false,true,true,false);
    	addStoneType(RockType.SEDIMENTARY,"dolomite",3,15,1,true,true,true,false);
    	addStoneType(RockType.SEDIMENTARY,"limestone",1.5,10,0,true,true,true,true);
    	addStoneType(RockType.SEDIMENTARY,"gypsum",0.75,1,0,false,false,false,false); // TODO drops gypsum dust
    	addStoneType(RockType.SEDIMENTARY,"chert",1.5,10,1,false,false,false,false);  // TODO drops flint  
    	sedimentaryStones.add(Blocks.sandstone);
    	sedimentaryStones.add(Blocks.sand);
    	sedimentaryStones.add(Blocks.gravel);
    	addStoneType(RockType.METAMORPHIC,"slate",1.5,10,0,true,true,true,true);
    	addStoneType(RockType.METAMORPHIC,"schist",3,15,1,true,true,true,false);
    	addStoneType(RockType.METAMORPHIC,"gneiss",3,15,1,true,true,true,false);
    	
    	// add recipe to make cobblestone
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone,4),"stone","stone",Blocks.gravel,Blocks.gravel));
    	GameRegistry.addSmelting(Blocks.gravel, new ItemStack(Blocks.stone,1), 0.1f);
    	
    	// add custom ore generator
    //	orePlacementGenerator = new OrePlacer();
    //	GameRegistry.registerWorldGenerator(orePlacementGenerator, 100);
    	
    	
    	// register ores
    	/*
    	if(config.hasCategory(CONFIG_CATAGORY_ORES) == false){
    		config.addCustomCategoryComment(CONFIG_CATAGORY_ORES, 
    				"Ore generation is stored as a JSON entry for each ore that you want to spawn, taking the following \nformat:\n" +
    				"S:XXX={\"BlockID\":\"<mod id>:<block name>\",\"Size\":#,\"Count\":#,\"MinHeight\":#,\"MaxHeight\":#},\n\t\"RockType\":\"ANY|IGNEOUS|SEDIMENTARY|METAMORPHIC\"}\n" +
    				"Where XXX can be any name you want, <mod id> is the source mod for this block (use minecraft for \nblocks that are part of the original Minecraft game(, " +
    				"<block name> is the blockID string for the block \nyou want to spawn in the ore deposit, " +
    				"# is a number, and ANY|IGNEOUS|SEDIMENTARY|METAMORPHIC is the \ngeological formation that you want the ore to spawn in (usually ANY)" +
    				"For example, the Minecraft default \nspawn rules for gold is as follows:\n" +
    				"S:oreGold={\"BlockID\":\"minecraft:gold_ore\",\"Size\":9,\"Count\":2,\"MinHeight\":0,\"MaxHeight\":32},\"RockType\":\"ANY\"}\n" +
    				"And if you wanted glowstone to spawn like ore in the Overworld in volcanic rock layers, you could add \nthe following:\n" +
    				"S:glowstone={\"BlockID\":\"minecraft:glowstone\",\"Size\":16,\"Count\":1,\"MinHeight\":0,\"MaxHeight\":20},\"RockType\":\"IGNEOUS\"}");
    		config.get(CONFIG_CATAGORY_ORES, "oreCoal", "{\"BlockID\":\"minecraft:coal_ore\",\"Size\":17,\"Count\":20,\"MinHeight\":0,\"MaxHeight\":128,\"RockType\":\"ANY\"}", "coal ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreIron", "{\"BlockID\":\"minecraft:iron_ore\",\"Size\":9,\"Count\":20,\"MinHeight\":0,\"MaxHeight\":64},\"RockType\":\"ANY\"}", "iron ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreGold", "{\"BlockID\":\"minecraft:gold_ore\",\"Size\":9,\"Count\":2,\"MinHeight\":0,\"MaxHeight\":32},\"RockType\":\"ANY\"}", "gold ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreRedstone", "{\"BlockID\":\"minecraft:redstone_ore\",\"Size\":8,\"Count\":8,\"MinHeight\":0,\"MaxHeight\":16,\"RockType\":\"ANY\"}", "redstone ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreDiamond", "{\"BlockID\":\"minecraft:diamond_ore\",\"Size\":8,\"Count\":1,\"MinHeight\":0,\"MaxHeight\":16,\"RockType\":\"ANY\"}", "diamond ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreLapis", "{\"BlockID\":\"minecraft:lapis_ore\",\"Size\":7,\"Count\":1,\"MinHeight\":0,\"MaxHeight\":32,\"RockType\":\"ANY\"}", "lapis ore spawn setting");
    		config.get(CONFIG_CATAGORY_ORES, "oreEmerald", "{\"BlockID\":\"minecraft:emerald_ore\",\"Size\":1,\"Count\":8,\"MinHeight\":4,\"MaxHeight\":32,\"RockType\":\"METAMORPHIC\"}", "emerald ore spawn setting (now based on geology rather than biome)");
    		config.get(CONFIG_CATAGORY_ORES, "clay", "{\"BlockID\":\"minecraft:clay\",\"Size\":16,\"Count\":1,\"MinHeight\":32,\"MaxHeight\":64,\"RockType\":\"SEDIMENTARY\"}", "spawn clay in sedimentary layers");
    	}
    	ConfigCategory ores = config.getCategory(CONFIG_CATAGORY_ORES);
    	oreProperties = ores.entrySet();
    	//*/
    	
   	/*
    	final String blockName = "derp";
    	testBlock = new Block(Material.leaves){};// super-lame anonymous class
    	testBlock.setBlockName(Mineralogy.MODID +"_"+ blockName);
    	testBlock.setBlockTextureName(Mineralogy.MODID +":"+ blockName);
    	testBlock.setCreativeTab(CreativeTabs.tabMisc);
    	GameRegistry.registerBlock(testBlock, blockName);
    	
    	final String itemName = "derpper";
    	testItem = new Item(){};// another super-lame anonymous class
    	testItem.setUnlocalizedName(Mineralogy.MODID +"_"+ itemName);
    	testItem.setTextureName(Mineralogy.MODID +":"+ itemName);
    	testItem.setCreativeTab(CreativeTabs.tabMisc);
    	GameRegistry.registerItem(testItem, itemName);
    	*/
    	config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// event registration, tile entities, renderers
    	
    	// register custom chunk generation
    	DimensionManager.unregisterProviderType(0);
    	DimensionManager.registerProviderType(0, MineralogyWorldProvider.class, true);
    }
    

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
		// addons to other mods
    	/*
    	// add ores from config file (which may include blocks from other mods)
    	for(Entry<String,Property> entry : oreProperties){
    		String depositName = entry.getKey();
    		Property p = entry.getValue();
    		Logger.getLogger(MODID).log(Level.INFO, "Adding ore deposit named '"+depositName+"'");
    		try {
				OrePlacement deposit = OrePlacement.parseJSON(p.getString());
				Mineralogy.addOreSpawn(depositName,deposit);
			} catch (NumberFormatException e) {
				Logger.getLogger(MODID).log(Level.SEVERE, "Failed to generate OrePlacement instance from JSON object "+p.getString(), e);
			} catch (IllegalArgumentException e) {
				Logger.getLogger(MODID).log(Level.SEVERE, "Failed to generate OrePlacement instance from JSON object "+p.getString(), e);
			} catch (JsonParserException e) {
				Logger.getLogger(MODID).log(Level.SEVERE, "Failed to generate OrePlacement instance from JSON object "+p.getString(), e);
			}
    	}
    	//*/
    	
    	/*
    	System.out.println("Ore Dictionary Registry:");
    	for(String s : OreDictionary.getOreNames()){
    		System.out.print(s+":");
    		for(ItemStack o : OreDictionary.getOres(s)){
    			System.out.print(" "+o.getItem().getUnlocalizedName()+"#"+o.getItemDamage());
    		}
    		System.out.println();
    	}
    	//*/
    }
    /**
     * Registers a new ore to spawn in the world.  
     * @param spawnKey A unique name for this ore spawn
     * @param deposit An OrePlacement object defining the rules for the spawning of this ore.
     */
    public static void addOreSpawn(String spawnKey, OrePlacement deposit) {
    	mineralogyOreSpawnRegistry.put(spawnKey, deposit);
	}

	/**
     * 
     * @param type Igneous, sedimentary, or metamorphic
     * @param name id-name of the block
     * @param hardness How hard (time duration) the block is to pick. For reference, dirt is 0.5, stone is 1.5, ores are 3, and obsidian is 50
     * @param blastResistance how resistant the block is to explosions. For reference, dirt is 0, stone is 10, and blast-proof materials are 2000
     * @param toolHardnessLevel 0 for wood tools, 1 for stone, 2 for iron, 3 for diamond
     * @param isStoneEquivalent if true, use in recipes requiring stone
     * @param isCobblestoneEquivalent if true, use in recipes requiring cobblestone
     * @param hasSmooth if true, then XXX_smooth exists
     * @param hasBricks if true, then XXX_brick exists
     */
    private static void addStoneType(RockType type, String name,double hardness,double blastResistance,int toolHardnessLevel, boolean isStoneEquivalent, boolean isCobblestoneEquivalent, boolean hasSmooth, boolean hasBricks){
    	Block b = new Rock();
    	b.setBlockName(Mineralogy.MODID +"_"+ name);
    	b.setBlockTextureName(Mineralogy.MODID +":"+ name);
    	b.setCreativeTab(CreativeTabs.tabBlock);
    	b.setHardness((float)hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
    	b.setResistance((float)blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
    	b.setStepSound(Block.soundTypePiston); // sound for stone
    	b.setHarvestLevel("pickaxe", toolHardnessLevel);
    	GameRegistry.registerBlock(b, name); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
    	mineralogyBlockRegistry.put(name, b);
    	switch(type){
	    	case IGNEOUS:
	    		igneousStones.add(b);
	    		break;
	    	case METAMORPHIC:
	    		metamorphicStones.add(b);
	    		break;
	    	case SEDIMENTARY:
	    		sedimentaryStones.add(b);
	    		break;
	    	case ANY:
	    		sedimentaryStones.add(b);
	    		metamorphicStones.add(b);
	    		igneousStones.add(b);
	    		break;
    	}
    	if(isStoneEquivalent){
    		OreDictionary.registerOre("stone", b);
    	}
    	if(isStoneEquivalent){
    		OreDictionary.registerOre("cobblestone", b);
    	}
    	if(hasSmooth){
    		String smoothName = name + "_smooth";
    		Block b2 = new Rock();
        	b2.setBlockName(Mineralogy.MODID +"_"+ smoothName);
        	b2.setBlockTextureName(Mineralogy.MODID +":"+ smoothName);
        	b2.setCreativeTab(CreativeTabs.tabBlock);
        	b2.setHardness((float)hardness); 
        	b2.setResistance((float)blastResistance); 
        	b2.setStepSound(Block.soundTypePiston); 
        	b2.setHarvestLevel("pickaxe", toolHardnessLevel);
        	GameRegistry.registerBlock(b2, smoothName); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
        	GameRegistry.addRecipe(new ItemStack(b2,4), "xx","xx",'x',new ItemStack(b));
        	mineralogyBlockRegistry.put(smoothName, b2);

        	if(hasBricks){
        		String brickName = name + "_brick";
        		Block b3 = new Rock();
            	b3.setBlockName(Mineralogy.MODID +"_"+ brickName);
            	b3.setBlockTextureName(Mineralogy.MODID +":"+ brickName);
            	b3.setCreativeTab(CreativeTabs.tabBlock);
            	b3.setHardness((float)hardness*2f); 
            	b3.setResistance((float)blastResistance*1.5f); 
            	b3.setStepSound(Block.soundTypePiston); 
            	b3.setHarvestLevel("pickaxe", toolHardnessLevel);
            	GameRegistry.registerBlock(b3, brickName); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
            	GameRegistry.addRecipe(new ItemStack(b3,4), "xx","xx",'x',new ItemStack(b2));
            	mineralogyBlockRegistry.put(brickName, b3);
        	}
    	}
    }
    
}