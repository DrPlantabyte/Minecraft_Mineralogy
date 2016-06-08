package cyano.mineralogy;

// DON'T FORGET TO UPDATE mcmod.info FILE!!!

import cyano.mineralogy.blocks.*;
import cyano.mineralogy.items.*;
import cyano.mineralogy.patching.PatchHandler;
import cyano.mineralogy.worldgen.OreSpawner;
import cyano.mineralogy.worldgen.StoneReplacer;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.*;



@Mod(modid = Mineralogy.MODID, name=Mineralogy.NAME, version = Mineralogy.VERSION,
		acceptedMinecraftVersions = "[1.9.4,)")
public class Mineralogy
{
    public static final String MODID = "mineralogy";
    public static final String NAME ="Mineralogy";
    public static final String VERSION = "3.1.1";
    /** stone block replacesments that are sedimentary */
    public static final List<Block> sedimentaryStones = new ArrayList<Block>();
    /** stone block replacesments that are metamorphic */
    public static final List<Block> metamorphicStones = new ArrayList<Block>();
    /** stone block replacesments that are igneous */
    public static final List<Block> igneousStones = new ArrayList<Block>();
	/** all blocks used in this mod (blockID,block)*/
	public static final Map<String,Block> mineralogyBlockRegistry = new HashMap<String,Block>();
	/** all items used in this mod (blockID,block)*/
	public static final Map<String,Item> mineralogyItemRegistry = new HashMap<String,Item>();
    
    /** size of rock layers */
    public static double ROCK_LAYER_NOISE = 32; 
    /** size of mineral biomes */
    public static int GEOME_SIZE = 100; 
    /** thickness of rock layers */
    public static int GEOM_LAYER_THICKNESS = 8;

	public static boolean SMELTABLE_GRAVEL = true;

	public static boolean DROP_COBBLESTONE = false;

	public static boolean PATCH_UPDATE = true;
    
 //   public static OrePlacer orePlacementGenerator = null;

    public static Block blockChert;

    public static Block blockGypsum;

    public static Block blockPumice;
    
    public static Item gypsumPowder;
    
    public static Item sulphurPowder;
    
    public static Item phosphorousPowder;
    
    public static Item nitratePowder; // aka "saltpeter"
    
    public static Item mineralFertilizer;
    
    public static Block[] drywall = new Block[16];

    

	// add other blocks and recipes
	private static final String[] colorSuffixes = {"black","red","green","brown","blue","purple","cyan",
			"silver","gray","pink","lime","yellow","light_blue","magenta","orange","white"};

	private List<String> igneousWhitelist = new ArrayList<String>();
	private List<String> igneousBlacklist = new ArrayList<String>();
	private List<String> sedimentaryWhitelist = new ArrayList<String>();
	private List<String> sedimentaryBlacklist  = new ArrayList<String>();
	private List<String> metamorphicWhitelist = new ArrayList<String>();
	private List<String> metamorphicBlacklist  = new ArrayList<String>();
	
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {



    	// load config
    	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    	config.load();

		PATCH_UPDATE = config.getBoolean("patch_world", "options", PATCH_UPDATE,
				"If true, then the world will be patched to fix compatibility-breaking " +
				"changes to this mod by adding-back mock-ups of old obsolete blocks and " +
				"then replacing obsolete blocks with newer blocks.");

    	SMELTABLE_GRAVEL = config.getBoolean("SMELTABLE_GRAVEL", "options", SMELTABLE_GRAVEL,
   "If true, then gravel can be smelted into generic stone");
   		DROP_COBBLESTONE = config.getBoolean("DROP_COBBLESTONE", "options", DROP_COBBLESTONE,
   "If true, then rock blocks will drop cobblestone instead of themselves");
    	GEOME_SIZE = config.getInt("GEOME_SIZE", "world-gen", GEOME_SIZE, 4, Short.MAX_VALUE, 
   "Making this value larger increases the size of regions of igneous, \n"
 + "sedimentary, and metamorphic rocks");
    	ROCK_LAYER_NOISE = (double)config.getFloat("ROCK_LAYER_NOISE", "world-gen", (float)ROCK_LAYER_NOISE, 1.0f, (float)Short.MAX_VALUE, 
   "Changing this value will change the 'waviness' of the layers.");
    	GEOM_LAYER_THICKNESS = config.getInt("ROCK_LAYER_THICKNESS", "world-gen",GEOM_LAYER_THICKNESS, 1, 255, 
   "Changing this value will change the height of individual layers.");

    	igneousBlacklist.addAll(asList(config.getString("igneous_blacklist", "world-gen", "", "Ban blocks from spawning in rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));
    	sedimentaryBlacklist.addAll(asList(config.getString("sedimentary_blacklist", "world-gen", "", "Ban blocks from spawning in rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));
    	metamorphicBlacklist.addAll(asList(config.getString("metamorphic_blacklist", "world-gen", "", "Ban blocks from spawning in rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));
    	
    	igneousWhitelist.addAll(asList(config.getString("igneous_whitelist", "world-gen", "", "Adds blocks to rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));
    	sedimentaryWhitelist.addAll(asList(config.getString("sedimentary_whitelist", "world-gen", "", "Adds blocks to rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));
    	metamorphicWhitelist.addAll(asList(config.getString("metamorphic_whitelist", "world-gen", "", "Adds blocks to rock layers (format is mod:block as a semicolin (;) delimited list)"),";"));




		// Blocks, Items, World-gen

		// Rocks
		addStoneType(RockType.IGNEOUS,"andesite",1.5,10,0);
		addStoneType(RockType.IGNEOUS,"basalt",5,100,2);
		addStoneType(RockType.IGNEOUS,"diorite",1.5,10,0);
		addStoneType(RockType.IGNEOUS,"granite",3,15,1);
		addStoneType(RockType.IGNEOUS,"rhyolite",1.5,10,0);
		addStoneType(RockType.IGNEOUS,"pegmatite",1.5,10,0);

		addStoneType(RockType.SEDIMENTARY,"shale",1.5,10,0);
		addStoneType(RockType.SEDIMENTARY,"conglomerate",1.5,10,0);
		addStoneType(RockType.SEDIMENTARY,"dolomite",3,15,1);
		addStoneType(RockType.SEDIMENTARY,"limestone",1.5,10,0);

		addStoneType(RockType.METAMORPHIC,"slate",1.5,10,0);
		addStoneType(RockType.METAMORPHIC,"schist",3,15,1);
		addStoneType(RockType.METAMORPHIC,"gneiss",3,15,1);
		addStoneType(RockType.SEDIMENTARY,"marble",1.5,10,0);
		addStoneType(RockType.METAMORPHIC,"phyllite",1.5,10,0);
		addStoneType(RockType.METAMORPHIC,"amphibolite",3,15,1);



		// add items
		gypsumPowder = registerItem(new GypsumDust(),GypsumDust.itemName).setCreativeTab(CreativeTabs.MATERIALS);
		OreDictionary.registerOre(GypsumDust.dictionaryName, gypsumPowder);

		sulphurPowder = registerItem(new SulfurDust(), SulfurDust.itemName).setCreativeTab(CreativeTabs.MATERIALS);
		OreDictionary.registerOre("dustSulfur", sulphurPowder);
		OreDictionary.registerOre("sulfur", sulphurPowder);
		OreDictionary.registerOre("sulphur", sulphurPowder);
		OreDictionary.registerOre("dustSulphur", sulphurPowder);

		phosphorousPowder = registerItem(new PhosphoriteDust(), PhosphoriteDust.itemName).setCreativeTab(CreativeTabs.MATERIALS);
		OreDictionary.registerOre(PhosphoriteDust.dictionaryName, phosphorousPowder);

		nitratePowder = registerItem(new NitrateDust(), NitrateDust.itemName).setCreativeTab(CreativeTabs.MATERIALS);
		OreDictionary.registerOre(NitrateDust.dictionaryName, nitratePowder);

		mineralFertilizer = registerItem(new MineralFertilizer(), MineralFertilizer.itemName).setCreativeTab(CreativeTabs.MATERIALS);
		OreDictionary.registerOre(MineralFertilizer.dictionaryName, mineralFertilizer);


		// other blocks
		sedimentaryStones.add(Blocks.SANDSTONE);
		blockChert = registerBlock(new Chert(),"chert");
		sedimentaryStones.add(blockChert);
		OreDictionary.registerOre("cobblestone",blockChert);
		blockGypsum = registerBlock(new Gypsum(),"gypsum");
		sedimentaryStones.add(blockGypsum);
		blockPumice = registerBlock(new Rock(false,0.5F,5F,0, SoundType.GROUND),"pumice");
		igneousStones.add(blockGypsum);

		// register ores
		Block s = addOre("sulfur_ore","oreSulfur",sulphurPowder,1,4,0,
				config.getInt("sulphur_ore.minY", "ores", 16, 1, 255, "Minimum ore spawn height"),
				config.getInt("sulphur_ore.maxY", "ores", 64, 1, 255, "Maximum ore spawn height"),
				config.getFloat("sulphur_ore.frequency", "ores", 1, 0, 63, "Number of ore deposits per chunk"),
				config.getInt("sulphur_ore.quantity", "ores", 16, 0, 63, "Size of ore deposit"));
		OreDictionary.registerOre("oreSulphur",s); // Damn English and its multiple spellings. There better not be people out there spelling is "sulphre"
		addOre("phosphorous_ore","orePhosphorous",phosphorousPowder,1,4,0,
				config.getInt("phosphorous_ore.minY", "ores", 16, 1, 255, "Minimum ore spawn height"),
				config.getInt("phosphorous_ore.maxY", "ores", 64, 1, 255, "Maximum ore spawn height"),
				config.getFloat("phosphorous_ore.frequency", "ores", 1, 0, 63, "Number of ore deposits per chunk"),
				config.getInt("phosphorous_ore.quantity", "ores", 16, 0, 63, "Size of ore deposit"));
		addOre("nitrate_ore","oreNitrate",nitratePowder,1,4,0,
				config.getInt("nitrate_ore.minY", "ores", 16, 1, 255, "Minimum ore spawn height"),
				config.getInt("nitrate_ore.maxY", "ores", 64, 1, 255, "Maximum ore spawn height"),
				config.getFloat("nitrate_ore.frequency", "ores", 1, 0, 63, "Number of ore deposits per chunk"),
				config.getInt("nitrate_ore.quantity", "ores", 16, 0, 63, "Size of ore deposit"));

		config.save();


		for(int i = 0; i < 16; i++){
			drywall[i] = registerBlock(new DryWall(colorSuffixes[i]), "drywall_"+colorSuffixes[i]);
			OreDictionary.registerOre("drywall", drywall[i]);
		}

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(drywall[7],3),"pgp","pgp","pgp",'p',Items.PAPER,'g',GypsumDust.dictionaryName));



    }
    
    private static List<String> asList(String list, String delimiter){
    	String[] a = list.split(delimiter);
    	return Arrays.asList(a);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// recipes
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 0] ,1),"drywall","dyeBlack"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 1] ,1),"drywall","dyeRed"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 2] ,1),"drywall","dyeGreen"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 3] ,1),"drywall","dyeBrown"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 4] ,1),"drywall","dyeBlue"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 5] ,1),"drywall","dyePurple"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 6] ,1),"drywall","dyeCyan"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 7] ,1),"drywall","dyeLightGray"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 8] ,1),"drywall","dyeGray"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[ 9] ,1),"drywall","dyePink"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[10],1),"drywall","dyeLime"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[11],1),"drywall","dyeYellow"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[12],1),"drywall","dyeLightBlue"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[13],1),"drywall","dyeMagenta"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[14],1),"drywall","dyeOrange"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[15],1),"drywall","dyeWhite"));


		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER,4),new ItemStack(Items.COAL,1,1),NitrateDust.dictionaryName,"dustSulfur"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER,4),"dustCarbon",NitrateDust.dictionaryName,"dustSulfur"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.GUNPOWDER,4),Items.SUGAR,NitrateDust.dictionaryName,"dustSulfur"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(mineralFertilizer,1),NitrateDust.dictionaryName,PhosphoriteDust.dictionaryName));

		// recipe modifications
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_AXE),"xx","xy"," y", 'x',"stone",'y',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_HOE),"xx"," y"," y", 'x',"stone",'y',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_PICKAXE),"xxx"," y "," y ", 'x',"stone",'y',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_SHOVEL),"x","y","y", 'x',"stone",'y',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.STONE_SWORD),"x","x","y", 'x',"stone",'y',"stickWood"));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.FURNACE),"xxx","x x","xxx", 'x',"stone"));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.COBBLESTONE,4), "stone","stone",Blocks.GRAVEL, Blocks.GRAVEL));

		if(SMELTABLE_GRAVEL)GameRegistry.addSmelting(Blocks.GRAVEL, new ItemStack(Blocks.STONE), 0.1F);

		// remove default stone slab recipe (interferes with rock slab recipes)
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		List<IRecipe> removeList = new ArrayList<>();
		for(IRecipe r : recipes){
			ItemStack item = r.getRecipeOutput();
			if(item == null || item.getItem() == null) continue;
			if(item.getItem() == Item.getItemFromBlock(Blocks.STONE_SLAB) && item.getItemDamage() == 0){
				// is recipe for stone slab, bookmark for removal
				removeList.add(r);
			}
		}
		CraftingManager.getInstance().getRecipeList().removeAll(removeList);
		// less generic stone slab recipe
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Blocks.STONE_SLAB,6,0),"xxx", 'x',Blocks.STONE));

		// initialize legacy updater
		PatchHandler.getInstance().init(PATCH_UPDATE);

		// event registration, tile entities
    	// (none)

    	// register custom chunk generation
    	GameRegistry.registerWorldGenerator(new StoneReplacer(), 10);

    	// register renderers
    	if(event.getSide().isClient()){
    		registerItemRenders();
    	}

    }
    
    
    private void registerItemRenders(){


		for(String name : mineralogyItemRegistry.keySet()){
			Item i = Mineralogy.mineralogyItemRegistry.get(name);
    		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    				.register(i, 0, new ModelResourceLocation(Mineralogy.MODID+":"+name, "inventory"));
    	}

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

		// addons to other mods
    	
    	// process black-lists and white-lists
    	for(String id : igneousWhitelist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		igneousStones.add(b);
    	}
    	for(String id : metamorphicWhitelist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		metamorphicStones.add(b);
    	}
    	for(String id : sedimentaryWhitelist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		sedimentaryStones.add(b);
    	}
    	for(String id : igneousBlacklist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		igneousStones.remove(b);
    	}
    	for(String id : metamorphicBlacklist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		metamorphicStones.remove(b);
    	}
    	for(String id : sedimentaryBlacklist){
    		Block b = getBlock(id);
    		if(b == null) continue;
    		sedimentaryStones.remove(b);
    	}
    	
    	
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
    

    private static Block getBlock(String id){
    	return Block.getBlockFromName(id);
    }
    
    private static int oreWeightCount = 20;
    
    private static Block addOre(String oreName, String oreDictionaryName, Item oreDropItem, int numMin, int numMax, int pickLevel,
    		int minY, int maxY, float spawnFrequency, int spawnQuantity){
    	String oreBlockName = Mineralogy.MODID+"."+oreName;
    	Block oreBlock = new Ore(oreName,oreDropItem,numMin,numMax,pickLevel).setUnlocalizedName(oreBlockName);
    	registerBlock(oreBlock, oreName);
    	OreDictionary.registerOre(oreDictionaryName, oreBlock);
    	GameRegistry.registerWorldGenerator(new OreSpawner(oreBlock,minY,maxY,spawnFrequency,spawnQuantity, (oreWeightCount * 25214903917L)+11L), oreWeightCount++);
    	return oreBlock;
    }


	private static String formatName(String s){
		StringBuilder sb = new StringBuilder();
		String[] words = s.split("_");
		boolean first = true;
		//for(int i = 0; i < words.length; i++){
		for(int i = words.length-1; i < words.length; i++){
			if(!first) sb.append(" ");
			first = false;
			sb.append(words[i].substring(0,1).toUpperCase()).append(words[i].substring(1));
		}
		return sb.toString();
	}

	private static Block registerBlock(Block b, String name){
		GameRegistry.register(b.setRegistryName(MODID,name));
		b.setUnlocalizedName(MODID+"."+name);
		registerItem(new ItemBlock(b),name);
		mineralogyBlockRegistry.put(name,b);
		//FMLLog.info("%s: added %s=%s",MODID,b.getUnlocalizedName(),formatName(name));
		return b;
	}

	private static Item registerItem(Item i, String name){
		GameRegistry.register(i.setRegistryName(MODID,name));
		mineralogyItemRegistry.put(name,i);
		i.setUnlocalizedName(MODID+"."+name);
		//if(!(i instanceof ItemBlock))FMLLog.info("%s: added %s=%s",MODID,i.getUnlocalizedName(),formatName(name));
		return i;
	}

	/**
     * 
     * @param type Igneous, sedimentary, or metamorphic
     * @param name id-name of the block
     * @param hardness How hard (time duration) the block is to pick. For reference, dirt is 0.5, stone is 1.5, ores are 3, and obsidian is 50
     * @param blastResistance how resistant the block is to explosions. For reference, dirt is 0, stone is 10, and blast-proof materials are 2000
     * @param toolHardnessLevel 0 for wood tools, 1 for stone, 2 for iron, 3 for diamond
     */
    private static void addStoneType(RockType type, String name,double hardness,double blastResistance,int toolHardnessLevel){
		final Block rock, rockStairs, rockSlab;
		final Block brick, brickStairs, brickSlab;
		final Block smooth, smoothStairs, smoothSlab;
		final Block smoothBrick, smoothBrickStairs, smoothBrickSlab;
    	rock = registerBlock(new Rock(true, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name);
    	switch(type){
	    	case IGNEOUS:
	    		igneousStones.add(rock);
	    		break;
	    	case METAMORPHIC:
	    		metamorphicStones.add(rock);
	    		break;
	    	case SEDIMENTARY:
	    		sedimentaryStones.add(rock);
	    		break;
	    	case ANY:
	    		sedimentaryStones.add(rock);
	    		metamorphicStones.add(rock);
	    		igneousStones.add(rock);
	    		break;
    	}
		OreDictionary.registerOre("stone",rock);
		GameRegistry.addSmelting(rock, new ItemStack(Blocks.STONE),0.1F);

		rockStairs = registerBlock(new RockStairs(rock, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_stairs");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rockStairs,4), "x  ","xx ", "xxx", 'x', rock));
		rockSlab = registerBlock(new RockSlab((float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_slab");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(rockSlab,6), "xxx", 'x', rock));

		brick = registerBlock(new Rock(false, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_brick");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(brick,4), "xx", "xx", 'x', rock));
		brickStairs = registerBlock(new RockStairs(rock, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_brick_stairs");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(brickStairs,4), "x  ","xx ", "xxx", 'x', brick));
		brickSlab = registerBlock(new RockSlab((float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_brick_slab");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(brickSlab,6), "xxx", 'x', brick));

		smooth = registerBlock(new Rock(false, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth");
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(smooth,1), rock, "sand"));
		smoothStairs = registerBlock(new RockStairs(rock, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth_stairs");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smoothStairs,4), "x  ","xx ", "xxx", 'x', smooth));
		smoothSlab = registerBlock(new RockSlab((float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth_slab");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smoothSlab,6), "xxx", 'x', smooth));

		smoothBrick = registerBlock(new Rock(false, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth_brick");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smoothBrick,4), "xx", "xx", 'x', smooth));
		smoothBrickStairs = registerBlock(new RockStairs(rock, (float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth_brick_stairs");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smoothBrickStairs,4), "x  ","xx ", "xxx", 'x', smoothBrick));
		smoothBrickSlab = registerBlock(new RockSlab((float)hardness,(float)blastResistance,toolHardnessLevel, SoundType.STONE),name+"_smooth_brick_slab");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(smoothBrickSlab,6), "xxx", 'x', smoothBrick));


    }


}