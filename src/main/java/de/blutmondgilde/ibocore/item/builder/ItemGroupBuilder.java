package de.blutmondgilde.ibocore.item.builder;

import de.blutmondgilde.ibocore.lang.LangFileGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupBuilder {
    private final String label;
    private ItemStack iconItem = new ItemStack(Items.AIR);
    private boolean hasSearchBar = false;

    /**
     * Creates a new {@link ItemGroupBuilder} with the given parameter
     *
     * @param label of the generated {@link ItemGroup}
     */
    public ItemGroupBuilder(final String label) {
        this.label = label;
    }

    /**
     * Creates a new {@link ItemGroupBuilder} with the given parameter
     *
     * @param label of the generated {@link ItemGroup}
     * @return a new {@link ItemGroupBuilder}
     */
    public static ItemGroupBuilder create(final String label) {
        return new ItemGroupBuilder(label);
    }

    /**
     * Set the Icon for the generated {@link ItemGroup}
     *
     * @param item to display as Icon
     * @return the {@link ItemGroupBuilder}
     */
    public ItemGroupBuilder setIcon(Item item) {
        this.iconItem = new ItemStack(item);

        return this;
    }

    /**
     * Set the Icon for the generated {@link ItemGroup}
     *
     * @param item to display as Icon
     * @return the {@link ItemGroupBuilder}
     */
    public ItemGroupBuilder setIcon(ItemStack item) {
        this.iconItem = item;
        return this;
    }

    /**
     * Creates a Search Bar in the generated {@link ItemGroup}
     *
     * @return the {@link ItemGroupBuilder}
     */
    public ItemGroupBuilder hasSearchBar() {
        this.hasSearchBar = true;
        return this;
    }

    /**
     * Builds the configured {@link ItemGroup}
     *
     * @return a new {@link ItemGroup}
     */
    public ItemGroup build() {
        LangFileGenerator.addString("itemGroup." + label);

        return new ItemGroup(this.label) {
            @Override
            public ItemStack createIcon() {
                return iconItem;
            }

            @Override
            public boolean hasSearchBar() {
                return hasSearchBar;
            }
        };
    }
}
