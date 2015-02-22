package cyano.mineralogy.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class RockSlab extends net.minecraft.block.Block{

	public static final PropertyEnum POSITION = PropertyEnum.create("position", SlabType.class);
	
	public RockSlab(Material mat, float hardness,float blastResistance,int toolHardnessLevel,SoundType sound) {
		super(mat);
		this.setHardness((float)hardness); // dirt is 0.5, grass is 0.6, stone is 1.5,iron ore is 3, obsidian is 50
		this.setResistance((float)blastResistance); // dirt is 0, iron ore is 5, stone is 10, obsidian is 2000
		this.setStepSound(sound); // sound for stone
		this.setHarvestLevel("pickaxe", toolHardnessLevel);
		this.setCreativeTab(CreativeTabs.tabDecorations);
		this.setDefaultState(this.blockState.getBaseState()
	    		.withProperty(POSITION, SlabType.BOTTOM));
		this.useNeighborBrightness = true;
	}
	
	@Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { POSITION});
    }
	
	@Override
    public void setBlockBoundsBasedOnState(final IBlockAccess world, final BlockPos coord) {
        final SlabType slab = (SlabType) world.getBlockState(coord).getValue(POSITION);
        
        float x1 = 0;
        float x2 = 1;
        float y1 ,y2;
        float z1 = 0;
        float z2 = 1;
        switch(slab){
        case BOTTOM:
        	y1 = 0;
        	y2 = 0.5f;
        	break;
        case TOP:
        	y1 = 0.5f;
        	y2 = 1;
        	break;
        default:
        	y1 = 0;
        	y2 = 1;
        	break;
        }
        this.setBlockBounds(x1, y1, z1, x2, y2, z2);
    }
	@Override
    public void addCollisionBoxesToList(final World world, final BlockPos coord, 
    		final IBlockState bs, final AxisAlignedBB box, final List collisionBoxList, 
    		final Entity entity) {
		final SlabType slab = (SlabType) bs.getValue(POSITION);
		
		float x1 = 0;
        float x2 = 1;
        float y1 ,y2;
        float z1 = 0;
        float z2 = 1;
        switch(slab){
        case BOTTOM:
        	y1 = 0;
        	y2 = 0.5f;
        	break;
        case TOP:
        	y1 = 0.5f;
        	y2 = 1;
        	break;
        default:
        	y1 = 0;
        	y2 = 1;
        	break;
        }
        this.setBlockBounds(x1, y1, z1, x2, y2, z2);
        super.addCollisionBoxesToList(world, coord, bs, box, collisionBoxList, entity);
	}
	
	@Override
    protected boolean canSilkHarvest() {
        return false;
    }
	
	
	/**
	 * Override of default block behavior
	 */
    @Override
    public Item getItemDropped(final IBlockState state, final Random prng, final int i) {
        return Item.getItemFromBlock(this);
    }
    /**
	 * Override of default block behavior
	 */
    @Override
    public int quantityDropped(final IBlockState state, final int bonus, final Random prng){
    	final SlabType slab = (SlabType) state.getValue(POSITION);
    	if(slab == SlabType.DOUBLE){
    		// double slab
    		return 2;
    	} else {
    		// single slab
    		return 1;
    	}
    }
    

    @Override
    public IBlockState onBlockPlaced(final World world, final BlockPos state, final EnumFacing facing, 
    		final float partialX, final float partialY, final float partialZ, 
    		final int i, final EntityLivingBase player) {
        final IBlockState bs = super.onBlockPlaced(world, state, facing, partialX, partialY, partialZ, i, player);
        if (facing == EnumFacing.DOWN || (facing != EnumFacing.UP && partialY > 0.5)) {
            return bs.withProperty(POSITION, SlabType.TOP);
        } else {
        	return bs.withProperty(POSITION, SlabType.BOTTOM);
        }
    }
    
    @Override
    public boolean onBlockActivated(final World w, final BlockPos coord, final IBlockState bs, 
    		final EntityPlayer player, final EnumFacing facing, final float f1, final float f2, 
    		final float f3) {
        if(player.isSneaking() == false 
        		&& bs.getBlock() ==  Block.getBlockFromItem(player.getCurrentEquippedItem().getItem())){
        	// turn into double-slab
        	if((bs.getValue(POSITION) == SlabType.BOTTOM && facing == EnumFacing.UP)
        			|| (bs.getValue(POSITION) == SlabType.TOP && facing == EnumFacing.DOWN)){
	        	w.setBlockState(coord, bs.withProperty(POSITION, SlabType.DOUBLE));
	        	if(player.capabilities.isCreativeMode) return true;
	        	player.getCurrentEquippedItem().stackSize--;
	        	if(player.getCurrentEquippedItem().stackSize == 0){
	        		player.destroyCurrentEquippedItem();
	        	}
	        	return true;
        	}
        }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess world, final BlockPos coord, final EnumFacing facing) {
        final IBlockState blockState = world.getBlockState(coord);
        
        // logic to decide whether to render the top and bottom faces
        if(blockState.getBlock() == this && ((facing == EnumFacing.UP && blockState.getValue(POSITION) == SlabType.BOTTOM) 
        		|| (facing == EnumFacing.DOWN && blockState.getValue(POSITION) == SlabType.TOP))){
        	return true;
        }
        // treat all other faces as normal block
        return super.shouldSideBeRendered(world, coord, facing);
    }
    
    /**
	 * Override of default block behavior
	 */
	@Override
    public void onBlockAdded(final World world, final BlockPos coord, final IBlockState state) {
        
    }
    
	
	
	
	/**
	 * Override of default block behavior
	 */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    /**
	 * Override of default block behavior
	 */
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    /**
	 * Override of default block behavior
	 */
    @Override
    public boolean isPassable(final IBlockAccess world, final BlockPos coord) {
        return false;
    }
    
    
    /**
	 * Metadata 
	 */
    @Override
    public int getMetaFromState(final IBlockState bs) {
        return ((SlabType)bs.getValue(POSITION)).getMetaDataValue();
    }

    /**
	 * Metadata 
	 */
    @Override
    public IBlockState getStateFromMeta(int meta){
    	switch(meta){
			case 1:
				return this.getDefaultState().withProperty(POSITION, SlabType.TOP);
			case 2:
				return this.getDefaultState().withProperty(POSITION, SlabType.DOUBLE);
    		default:
    			return this.getDefaultState().withProperty(POSITION, SlabType.BOTTOM);
    	}
    	
    }
	
    
    public enum SlabType implements IStringSerializable{
    	BOTTOM("BOTTOM",0,"bottom"),TOP("TOP",1,"top"),DOUBLE("DOUBLE",2,"double");
    	
    	private final String name;
    	
    	private final int metaValue;
        
        private SlabType(final String enumName, final int data, final String name) {
            this.name = name;
            this.metaValue = data;
        }
        
        public int getMetaDataValue(){
        	return metaValue;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
        
        @Override
        public String getName() {
            return this.name;
        }
    }
}
