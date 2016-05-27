package cyano.mineralogy.patching;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Chris on 5/26/2016.
 */
public class UpdateBlock extends Block {

	private final IBlockState updateTo;
	public UpdateBlock(IBlockState replacement) {
		super(Material.ROCK);
		this.setTickRandomly(true);
		this.updateTo = replacement;
	}
	@Override
	public void updateTick(World w, BlockPos pos, IBlockState state, Random rand){
		final int minX;
		final int minY;
		final int minZ;
		minX = pos.getX() & ~0x0F;
		minY = pos.getY() & ~0x0F;
		minZ = pos.getZ() & ~0x0F;
		final int maxX = minX | 0x0F;
		final int maxY = minY | 0x0F;
		final int maxZ = minZ | 0x0F;
		for(int y = minY; y <= maxY; y++) {
			for (int z = minZ; z <= maxZ; z++) {
				for (int x = minX; x <= maxX; x++) {
					// replace blocks
					BlockPos target = new BlockPos(x,y,z);
					if(w.getBlockState(target).getBlock() == this){
						w.setBlockState(target,updateTo);
					}
				}
			}
		}
	}
}
