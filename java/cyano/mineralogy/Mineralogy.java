package cyano.mineralogy;

// DON'T FORGET TO UPDATE mcmod.info FILE!!!

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import scala.actors.threadpool.Arrays;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import cyano.mineralogy.blocks.*;
import cyano.mineralogy.items.*;
import cyano.mineralogy.worldgen.*;




@Mod(modid = Mineralogy.MODID, name=Mineralogy.NAME, version = Mineralogy.VERSION)
public class Mineralogy
{
    public static final String MODID = "mineralogy";
    public static final String NAME ="Mineralogy";
    public static final String VERSION = "2.1";
    /** stone block replacesments that are sedimentary */
    public static final List<Block> sedimentaryStones = new ArrayList<Block>();
    /** stone block replacesments that are metamorphic */
    public static final List<Block> metamorphicStones = new ArrayList<Block>();
    /** stone block replacesments that are igneous */
    public static final List<Block> igneousStones = new ArrayList<Block>();
    /** all blocks used in this mod (blockID,block)*/
    public static final Map<String,Block> mineralogyBlockRegistry = new HashMap<String,Block>();
    
    /** size of rock layers */
    public static double ROCK_LAYER_SIZE = 32; 
    /** size of mineral biomes */
    public static int GEOME_SIZE = 100; 

 //   public static OrePlacer orePlacementGenerator = null;

    public static Block blockChert;
    
    public static Block blockGypsum;
    
    public static Item gypsumPowder;
    
    public static Item sulphurPowder;
    
    public static Item phosphorousPowder;
    
    public static Item nitratePowder; // aka "saltpeter"
    
    public static Item mineralFertilizer;
    
    public static Block[] drywall = new Block[16];
    
    public final static String CONFIG_CATAGORY_ORES = "ores"; 
    

	// add other blocks and recipes
	private static final String[] colorSuffixes = {"black","red","green","brown","blue","purple","cyan",
			"silver","gray","pink","lime","yellow","light_blue","magenta","orange","white"};
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	// load config
    //	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
    //	config.load();
    	
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
    	sedimentaryStones.add(Blocks.sandstone);
    	blockGypsum = new Gypsum();
    	GameRegistry.registerBlock(blockGypsum, "gypsum");
    	mineralogyBlockRegistry.put("gypsum", blockGypsum);
    	blockChert = new Chert();
    	GameRegistry.registerBlock(blockChert,"chert");
    	mineralogyBlockRegistry.put("chert", blockChert);
    	sedimentaryStones.add(blockChert);
    	addStoneType(RockType.METAMORPHIC,"slate",1.5,10,0,true,true,true,true);
    	addStoneType(RockType.METAMORPHIC,"schist",3,15,1,true,true,true,false);
    	addStoneType(RockType.METAMORPHIC,"gneiss",3,15,1,true,true,true,false);
    	
    	// add recipe to make cobblestone and stone
    // TODO: Forge update:	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Blocks.cobblestone,4),"stone","stone",Blocks.gravel,Blocks.gravel));
    	GameRegistry.addSmelting(Blocks.gravel, new ItemStack(Blocks.stone,1), 0.1f);
    	
    	// add items
    	gypsumPowder = new GypsumDust();
    	GameRegistry.registerItem(gypsumPowder, GypsumDust.itemName);
    	// TODO: Forge update:	OreDictionary.registerOre(GypsumDust.dictionaryName, gypsumPowder);
    	sulphurPowder = new SulfurDust();
    	GameRegistry.registerItem(sulphurPowder, SulfurDust.itemName);
    	// TODO: Forge update:	OreDictionary.registerOre(SulfurDust.dictionaryName, sulphurPowder);
    	phosphorousPowder = new PhosphoriteDust();
    	GameRegistry.registerItem(phosphorousPowder, PhosphoriteDust.itemName);
    	// TODO: Forge update:	OreDictionary.registerOre(PhosphoriteDust.dictionaryName, phosphorousPowder);
    	nitratePowder = new NitrateDust();
    	GameRegistry.registerItem(nitratePowder, NitrateDust.itemName);
    	// TODO: Forge update:	OreDictionary.registerOre(NitrateDust.dictionaryName, nitratePowder);
    	mineralFertilizer = new MineralFertilizer();
    	GameRegistry.registerItem(mineralFertilizer, MineralFertilizer.itemName);
    	// TODO: Forge update:	OreDictionary.registerOre(MineralFertilizer.dictionaryName, mineralFertilizer);
    	
    	
    	// register ores
    	// TODO: Forge update:	load config
    	addOre("sulfur_ore","oreSulfur",sulphurPowder,1,4,0, 
    			16,//config.getInt("sulphur_ore.minY", "Mineralogy Ores", 16, 1, 255, "Minimum ore spawn height"),
    			64,//config.getInt("sulphur_ore.maxY", "Mineralogy Ores", 64, 1, 255, "Maximum ore spawn height"),
    			1,//config.getInt("sulphur_ore.frequency", "Mineralogy Ores", 1, 0, 63, "Number of ore deposits per chunk"),
    			16);//config.getInt("sulphur_ore.quantity", "Mineralogy Ores", 16, 0, 63, "Size of ore deposit"));
    	addOre("phosphorous_ore","orePhosphorous",phosphorousPowder,1,4,0, 
    			16,//config.getInt("phosphorous_ore.minY", "Mineralogy Ores", 16, 1, 255, "Minimum ore spawn height"),
    			64,//config.getInt("phosphorous_ore.maxY", "Mineralogy Ores", 64, 1, 255, "Maximum ore spawn height"),
    			1,//config.getInt("phosphorous_ore.frequency", "Mineralogy Ores", 1, 0, 63, "Number of ore deposits per chunk"),
    			16);//config.getInt("phosphorous_ore.quantity", "Mineralogy Ores", 16, 0, 63, "Size of ore deposit"));
    	addOre("nitrate_ore","oreNitrate",nitratePowder,1,4,0, 
    			16,//config.getInt("nitrate_ore.minY", "Mineralogy Ores", 16, 1, 255, "Minimum ore spawn height"),
    			64,//config.getInt("nitrate_ore.maxY", "Mineralogy Ores", 64, 1, 255, "Maximum ore spawn height"),
    			1,//config.getInt("nitrate_ore.frequency", "Mineralogy Ores", 1, 0, 63, "Number of ore deposits per chunk"),
    			16);//config.getInt("nitrate_ore.quantity", "Mineralogy Ores", 16, 0, 63, "Size of ore deposit"));
    	
    	
    	
    	
    	for(int i = 0; i < 16; i++){
    		drywall[i] = new DryWall(colorSuffixes[i]);
    		GameRegistry.registerBlock(drywall[i], "drywall_"+colorSuffixes[i]);
    		// TODO: Forge update:	OreDictionary.registerOre("drywall", drywall[i]);
    	}
    	
    	// TODO: Forge update:	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(drywall[7],3),"pgp","pgp","pgp",'p',Items.paper,'g',GypsumDust.dictionaryName));
    	ItemStack[] input = new ItemStack[9];
    	for(int i = 0; i < 9; i++){
    		if((i%3)== 1){
    			input[i] = new ItemStack(gypsumPowder);
    		} else {
    			input[i] = new ItemStack(Items.paper);
    		}
    	}
    	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapedRecipes(3, 3, input, new ItemStack(drywall[7],3)));
    	
    	// TODO: Forge update:	
    	/*
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[0] ,1),"drywall","dyeBlack"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[1] ,1),"drywall","dyeRed"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[2] ,1),"drywall","dyeGreen"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[3] ,1),"drywall","dyeBrown"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[4] ,1),"drywall","dyeBlue"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[5] ,1),"drywall","dyePurple"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[6] ,1),"drywall","dyeCyan"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[7] ,1),"drywall","dyeLightGray"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[8] ,1),"drywall","dyeGray"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[9] ,1),"drywall","dyePink"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[10],1),"drywall","dyeLime"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[11],1),"drywall","dyeYellow"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[12],1),"drywall","dyeLightBlue"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[13],1),"drywall","dyeMagenta"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[14],1),"drywall","dyeOrange"));
    	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(drywall[15],1),"drywall","dyeWhite"));
    	*/
    	for(int i = 0; i < 16; i++){
    		for(int j = 0; j < 16; j++){
    			if(i == j) continue;
	    		List<ItemStack> recipeInputs = new ArrayList<ItemStack>(2);
	    		recipeInputs.add(new ItemStack(drywall[j]));
	    		recipeInputs.add(new ItemStack(Items.dye,1,i));
	        	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapelessRecipes(new ItemStack(drywall[i]), recipeInputs));
    		}
    	}
    	
    	// TODO: Forge update:	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.gunpowder,4),new ItemStack(Items.coal,1,1),NitrateDust.dictionaryName,SulfurDust.dictionaryName));
    	// TODO: Forge update:	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.gunpowder,4),Items.sugar,NitrateDust.dictionaryName,SulfurDust.dictionaryName));
    	// TODO: Forge update:	GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(mineralFertilizer,1),NitrateDust.dictionaryName,PhosphoriteDust.dictionaryName));
    	List<ItemStack> recipeInputs = new ArrayList<ItemStack>(3);
		recipeInputs.add(new ItemStack(Items.coal,1,1));
		recipeInputs.add(new ItemStack(nitratePowder));
		recipeInputs.add(new ItemStack(sulphurPowder));
    	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapelessRecipes(new ItemStack(Items.gunpowder,4), recipeInputs));
    	recipeInputs = new ArrayList<ItemStack>(3);
		recipeInputs.add(new ItemStack(Items.sugar));
		recipeInputs.add(new ItemStack(nitratePowder));
		recipeInputs.add(new ItemStack(sulphurPowder));
    	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapelessRecipes(new ItemStack(Items.gunpowder,4), recipeInputs));
    	recipeInputs = new ArrayList<ItemStack>(2);
		recipeInputs.add(new ItemStack(nitratePowder));
		recipeInputs.add(new ItemStack(phosphorousPowder));
    	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapelessRecipes(new ItemStack(mineralFertilizer,1), recipeInputs));
    	
    //	config.save();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// event registration, tile entities, renderers
    	
    	// register custom chunk generation
    	// TODO: Forge update:	
    	/*
    	DimensionManager.unregisterProviderType(0);
    	DimensionManager.registerProviderType(0, MineralogyWorldProvider.class, true);
    	*/
    	GameRegistry.registerWorldGenerator(new StoneReplacer(), 10);
    	
    	// register item rendering for blocks
    	if(event.getSide().isClient()){
    		registerItemRenders();
    	}
    }
    
    
    private void registerItemRenders(){
    	
    	for(String name : mineralogyBlockRegistry.keySet()){
    		Block b = Mineralogy.mineralogyBlockRegistry.get(name);
    		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    				.register(net.minecraft.item.Item.getItemFromBlock(b), 0, 
    						new ModelResourceLocation(Mineralogy.MODID+":"+name, "inventory"));
    	}
    	for(int i = 0; i < 16; i++){
    		// TODO: Forge update:	OreDictionary.registerOre("drywall", drywall[i]);
    		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
    				.register(net.minecraft.item.Item.getItemFromBlock(drywall[i]), 0, 
    						new ModelResourceLocation(Mineralogy.MODID+":drywall_"+colorSuffixes[i], "inventory"));
    	}
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(gypsumPowder, 0, 
				new ModelResourceLocation(Mineralogy.MODID+":"+GypsumDust.itemName, "inventory"));
        
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(sulphurPowder, 0, 
			new ModelResourceLocation(Mineralogy.MODID+":"+SulfurDust.itemName, "inventory"));
        
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(phosphorousPowder, 0, 
			new ModelResourceLocation(Mineralogy.MODID+":"+PhosphoriteDust.itemName, "inventory"));
        
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(nitratePowder, 0, 
			new ModelResourceLocation(Mineralogy.MODID+":"+NitrateDust.itemName, "inventory"));
        
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
		.register(mineralFertilizer, 0, 
			new ModelResourceLocation(Mineralogy.MODID+":"+MineralFertilizer.itemName, "inventory"));
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    	
		// addons to other mods
    	
    	
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
    

    private static int oreWeightCount = 20;
    
    private static void addOre(String oreName, String oreDictionaryName, Item oreDropItem, int numMin, int numMax, int pickLevel,
    		int minY, int maxY, int spawnFrequency, int spawnQuantity){
    	String oreBlockName = Mineralogy.MODID+"_"+oreName;
    	Block oreBlock = new Ore(oreName,oreDropItem,numMin,numMax,pickLevel);
    	GameRegistry.registerBlock(oreBlock, oreName); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
    	mineralogyBlockRegistry.put(oreName, oreBlock);
    	// TODO: Forge update:	OreDictionary.registerOre(oreDictionaryName, oreBlock);
    	GameRegistry.registerWorldGenerator(new OreSpawner(oreBlock,minY,maxY,spawnFrequency,spawnQuantity, (oreWeightCount * 25214903917L)+11L), oreWeightCount++);
    	
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
    	Block b = new Rock(isStoneEquivalent,(float)hardness,(float)blastResistance,toolHardnessLevel,Block.soundTypePiston);
    	b.setUnlocalizedName(Mineralogy.MODID +"_"+ name);
    	b.setCreativeTab(CreativeTabs.tabBlock);
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
    		// TODO: Forge update:	OreDictionary.registerOre("stone", b);
    		List<ItemStack> recipeInputs = new ArrayList<ItemStack>(4);
    		recipeInputs.add(new ItemStack(Blocks.gravel));
    		recipeInputs.add(new ItemStack(Blocks.gravel));
    		recipeInputs.add(new ItemStack(b));
    		recipeInputs.add(new ItemStack(b));
        	GameRegistry.addRecipe(new net.minecraft.item.crafting.ShapelessRecipes(new ItemStack(Blocks.cobblestone,4), recipeInputs));
    	}
    	if(isCobblestoneEquivalent){
    		// TODO: Forge update:	OreDictionary.registerOre("cobblestone", b);
    		GameRegistry.addSmelting(b, new ItemStack(Blocks.stone,1), 0.1f);
    		addCobblestoneRecipes(b);
    	}
    	if(hasSmooth){
    		String smoothName = name + "_smooth";
    		Block b2 = new Rock(false,(float)hardness,(float)blastResistance,toolHardnessLevel,Block.soundTypePiston);
        	b2.setUnlocalizedName(Mineralogy.MODID +"_"+ smoothName);
        	b2.setCreativeTab(CreativeTabs.tabBlock);
        	GameRegistry.registerBlock(b2, smoothName); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
        	GameRegistry.addRecipe(new ItemStack(b2,4), "xx","xx",'x',new ItemStack(b));
        	mineralogyBlockRegistry.put(smoothName, b2);

        	if(hasBricks){
        		String brickName = name + "_brick";
        		Block b3 = new Rock(false,(float)hardness*2,(float)blastResistance*1.5f,toolHardnessLevel,Block.soundTypePiston);
            	b3.setUnlocalizedName(Mineralogy.MODID +"_"+ brickName);
            	b3.setCreativeTab(CreativeTabs.tabBlock);
            	GameRegistry.registerBlock(b3, brickName); // MUST REGISTER BLOCK WITH GAME BEFORE DOING ANYTHING ELSE WITH IT!!!
            	GameRegistry.addRecipe(new ItemStack(b3,4), "xx","xx",'x',new ItemStack(b2));
            	mineralogyBlockRegistry.put(brickName, b3);
        	}
    	}
    }

	private static void addCobblestoneRecipes(Block block) {
		// TODO: Forge update:	remove and use ore dictionary
		ItemStack c = new ItemStack(block);
		GameRegistry.addRecipe(new ItemStack(Items.stone_axe), "cc","cs"," s",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_axe), "cc","sc","s ",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), "cc"," s"," s",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_hoe), "cc","s ","s ",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_pickaxe), "ccc"," s "," s ",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_shovel), " c "," s "," s ",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Items.stone_sword), " c "," c "," s ",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Blocks.furnace), "ccc","c c","ccc",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.brewing_stand), " b ","ccc",'c',c,'b',Items.blaze_rod);
		GameRegistry.addRecipe(new ItemStack(Blocks.cobblestone_wall,6,1), "ccc","ccc",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.dropper), "ccc","c c","crc",'c',c,'r',Items.redstone);
		GameRegistry.addRecipe(new ItemStack(Blocks.dispenser), "ccc","cbc","crc",'c',c,'r',Items.redstone,'b',Items.bow);
		GameRegistry.addRecipe(new ItemStack(Blocks.lever), "s","c",'c',c,'s',Items.stick);
		GameRegistry.addRecipe(new ItemStack(Blocks.piston), "www","cic","crc",'c',c,'r',Items.redstone,'i',Items.iron_ingot,'w',Blocks.planks);
		GameRegistry.addRecipe(new ItemStack(Blocks.stone_slab,6,3), "ccc",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.stone_stairs,4,1), "c  ","cc ","ccc",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.stone_stairs,4,1), "  c"," cc","ccc",'c',c);
		
	}
    
	private static void addStoneRecipes(Block block) {
		// TODO: Forge update:	remove and use ore dictionary
		ItemStack c = new ItemStack(block);
		GameRegistry.addRecipe(new ItemStack(Blocks.stone_button), "c",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.stone_pressure_plate), "cc",'c',c);
		GameRegistry.addRecipe(new ItemStack(Blocks.powered_comparator), " r ","rqr","ccc",'c',c,'r',Blocks.redstone_torch,'q',Items.quartz);
		GameRegistry.addRecipe(new ItemStack(Blocks.powered_repeater), "rqr","ccc",'c',c,'r',Blocks.redstone_torch,'q',Items.redstone);
		
	}
}