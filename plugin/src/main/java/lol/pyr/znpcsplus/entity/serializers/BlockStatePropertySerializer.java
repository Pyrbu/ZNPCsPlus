package lol.pyr.znpcsplus.entity.serializers;

import lol.pyr.znpcsplus.entity.PropertySerializer;
import lol.pyr.znpcsplus.util.BlockState;

public class BlockStatePropertySerializer implements PropertySerializer<BlockState> {
    @Override
    public String serialize(BlockState property) {
        return String.valueOf(property.getGlobalId());
    }

    @Override
    public BlockState deserialize(String property) {
        try {
            int id = Integer.parseInt(property);
            return new BlockState(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BlockState(0);
    }

    @Override
    public Class<BlockState> getTypeClass() {
        return BlockState.class;
    }
}
