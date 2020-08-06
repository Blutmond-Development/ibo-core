package de.blutmondgilde.ibocore.block.builder;

import de.blutmondgilde.ibocore.block.BlockList;
import de.blutmondgilde.ibocore.block.templates.DefaultBlockTemplate;
import de.blutmondgilde.ibocore.item.builder.ItemBuilder;
import de.blutmondgilde.ibocore.util.Ref;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraftforge.common.ToolType;

import java.util.function.Supplier;

public class BlockBuilder {
    private final String registryName;
    private String modId = Ref.MOD_ID;
    Supplier<? extends Item> itemClass;
    private boolean hasDefaultClass = true;
    Material material;
    private boolean notPushable = false;
    private boolean isNotSolid = false;
    private boolean isReplaceable = false;
    private MaterialColor color = MaterialColor.PURPLE;
    private boolean requiresTool = false;
    private ToolType harvestToolTypes = null;
    private int harvestLevel = -1;
    private float hardness = 0;
    private float resistance = 0;
    Supplier<? extends Block> blockClass;
    private Item.Properties properties = new Item.Properties();

    public BlockBuilder(String registryName) {
        this.registryName = registryName;
    }

    public static BlockBuilder create(String registryName) {
        return new BlockBuilder(registryName);
    }

    /**
     * Sets the ItemGroup of the Item to the given parameter
     * <p>
     * Only works of you don't use {@link ItemBuilder#setItemClass}!
     *
     * @param itemGroup the Item should be in
     * @return the {@link BlockBuilder}
     */
    public BlockBuilder setCreativeTab(final ItemGroup itemGroup) {
        this.properties = this.properties.group(itemGroup);
        return this;
    }

    /**
     * Sets the maximum stack size of your Item
     *
     * @param maxStackSize of the Item
     * @return the {@link BlockBuilder}
     */
    public BlockBuilder setMaxStackSize(final int maxStackSize) {
        this.properties = this.properties.maxStackSize(maxStackSize);
        return this;
    }

    /**
     * Sets the {@link Rarity} of your Item
     *
     * @param rarity of the Item
     * @return the {@link BlockBuilder}
     */
    public BlockBuilder setRarity(final Rarity rarity) {
        this.properties = this.properties.rarity(rarity);
        return this;
    }


    public BlockBuilder setBlockClass(final Supplier<? extends Block> blockClass) {
        this.blockClass = blockClass;
        this.hasDefaultClass = false;
        return this;
    }

    public BlockBuilder setModId(final String modId) {
        this.modId = modId;
        return this;
    }

    public BlockBuilder notPushable() {
        this.notPushable = true;
        return this;
    }

    public BlockBuilder isNotSolid() {
        this.isNotSolid = true;
        return this;
    }

    public BlockBuilder isReplaceable() {
        this.isReplaceable = true;
        return this;
    }

    public BlockBuilder setMaterialColor(final MaterialColor color) {
        this.color = color;
        return this;
    }

    public BlockBuilder requiresTool() {
        this.requiresTool = true;
        return this;
    }

    public BlockBuilder addRequiredToolType(final ToolType toolType) {
        this.harvestToolTypes = toolType;
        return this;
    }

    public BlockBuilder setHarvestLevel(final int level) {
        this.harvestLevel = level;
        return this;
    }

    public BlockBuilder setHardnessAndResistance(final float hardness, final float ressistance) {
        this.hardness = hardness;
        this.resistance = ressistance;
        return this;
    }

    public BlockBuilder setHardnessAndResistance(final float hardnessAndResistance) {
        return setHardnessAndResistance(hardnessAndResistance, hardnessAndResistance);
    }

    public void build() {
        if (this.hasDefaultClass) {
            Material.Builder matBuilder = new Material.Builder(this.color);
            if (this.notPushable) {
                matBuilder.doesNotBlockMovement();
            }

            if (this.isNotSolid) {
                matBuilder.notSolid();
            }

            if (this.isReplaceable) {
                matBuilder.replaceable();
            }

            AbstractBlock.Properties blockProbs = AbstractBlock.Properties.create(matBuilder.build());

            if (this.requiresTool) {
                blockProbs.setRequiresTool();
            }

            if (this.harvestToolTypes != null) {
                blockProbs.harvestTool(this.harvestToolTypes);
            }

            if (this.harvestLevel > -1) {
                blockProbs.harvestLevel(this.harvestLevel);
            }

            if (this.hardness > 0) {
                blockProbs.hardnessAndResistance(this.hardness, this.resistance);
            }

            BlockList.registerBlock(this.modId, this.registryName, () -> new DefaultBlockTemplate(blockProbs), this.properties);
        } else {
            BlockList.registerBlock(this.modId, this.registryName, this.blockClass, this.properties);
        }
    }
}
