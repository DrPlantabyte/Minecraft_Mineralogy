package cyano.mineralogy.blocks;

import java.util.Collection;
import java.util.HashMap;




import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class MetaBlockTest extends net.minecraft.block.Block {

	IBlockState[] states = new IBlockState[4];
	
	public MetaBlockTest() {
		super(net.minecraft.block.material.Material.cloth);
		for(int i = 0; i < states.length; i++){
			states[i] = new TestBlockState(this,i);
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int m){
		return states[m];
	}
	
	public static class TestBlockState implements IBlockState{
		
		final ImmutableMap<String,String> metaDataMap;
		
		final Block block;
		public TestBlockState(Block srcBlock, int value){
			HashMap<String,String> m = new HashMap<String, String>();
			block = srcBlock;
			if(value == 0){
				m.put("variants", "normal");
			} else {
				m.put("variants", "variant_"+value);
			}
			metaDataMap = ImmutableMap.copyOf(m);
		}
		

		@Override
		public IBlockState cycleProperty(IProperty arg0) {
			// cycling not supported in this implementation
			return this;
		}

		@Override
		public Block getBlock() {
			return block;
		}

		@Override
		public ImmutableMap getProperties() {
			return metaDataMap;
		}

		@Override
		public Collection getPropertyNames() {
			return metaDataMap.keySet();
		}

		@Override
		public Comparable getValue(IProperty arg0) {
			return metaDataMap.get(arg0.getName());
		}

		@Override
		public IBlockState withProperty(IProperty arg0, Comparable arg1) {
			// don't know what this method is supposed to do
			return this;
		}
		
	}

}
