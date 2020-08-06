package de.blutmondgilde.ibocore.item.builder;

import de.blutmondgilde.ibocore.item.ItemList;
import de.blutmondgilde.ibocore.item.templates.DefaultItemTemplate;
import de.blutmondgilde.ibocore.lang.LangFileGenerator;
import de.blutmondgilde.ibocore.util.Ref;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

import java.util.function.Supplier;

public class ItemBuilder {
    private final String registryName;
    private String modId = Ref.MOD_ID;
    Supplier<? extends Item> itemClass;
    private boolean hasDefaultClass = true;
    private Item.Properties properties = new Item.Properties();

    /**
     * A Simple {@link ItemBuilder} to create Items.
     *
     * @param registryName of the Item you're trying to create.
     */
    public ItemBuilder(final String registryName) {
        this.registryName = registryName;
    }

    /**
     * Creates a new {@link ItemBuilder} for a Item with the given Registry Name
     *
     * @param registryName the Item gonna have after build
     * @return the new ItemBuilder
     */
    public static ItemBuilder create(final String registryName) {
        return new ItemBuilder(registryName);
    }

    /**
     * Sets the Mod Id of the Item to the Given Parameter
     *
     * @param modId the Item should be registered to
     * @return the {@link ItemBuilder}
     */
    public ItemBuilder setModId(final String modId) {
        this.modId = modId;
        return this;
    }

    /**
     * Changes the Item Class to the given supplier
     * <p>
     * Only works of you don't use {@link ItemBuilder#setCreativeTab}!<br>
     * Only works of you don't use {@link ItemBuilder#setMaxStackSize}!<br>
     * Only works of you don't use {@link ItemBuilder#setRarity}!
     *
     * @param sup with the Item Class
     * @return the {@link ItemBuilder}
     */
    public ItemBuilder setItemClass(final Supplier<? extends Item> sup) {
        hasDefaultClass = false;
        this.itemClass = sup;
        return this;
    }

    /**
     * Sets the ItemGroup of the Item to the given parameter
     * <p>
     * Only works of you don't use {@link ItemBuilder#setItemClass}!
     *
     * @param itemGroup the Item should be in
     * @return the {@link ItemBuilder}
     */
    public ItemBuilder setCreativeTab(final ItemGroup itemGroup) {
        this.properties = this.properties.group(itemGroup);
        return this;
    }

    /**
     * Sets the maximum stack size of your Item
     *
     * @param maxStackSize of the Item
     * @return the {@link ItemBuilder}
     */
    public ItemBuilder setMaxStackSize(final int maxStackSize) {
        this.properties = this.properties.maxStackSize(maxStackSize);
        return this;
    }

    /**
     * Sets the {@link Rarity} of your Item
     *
     * @param rarity of the Item
     * @return the {@link ItemBuilder}
     */
    public ItemBuilder setRarity(final Rarity rarity) {
        this.properties = this.properties.rarity(rarity);
        return this;
    }

    /**
     * Build Method to save and register the Item.
     */
    public void build() {
        if (hasDefaultClass) {
            ItemList.registerItem(this.modId, this.registryName, () -> new DefaultItemTemplate(this.properties));
        } else {
            ItemList.registerItem(this.modId, this.registryName, this.itemClass);
        }

        LangFileGenerator.addString(this.modId, "item." + this.modId + "." + this.registryName);
    }
}
