package de.blutmondgilde.ibocore.item.templates;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

import java.util.function.Supplier;

public class DefaultItemBlockTemplate extends BlockItem {
    public DefaultItemBlockTemplate(Supplier<? extends Block> block, Properties builder) {
        super(block.get(), builder);
    }
}
