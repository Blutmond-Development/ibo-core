package de.blutmondgilde.ibocore.item;

import de.blutmondgilde.ibocore.item.builder.ItemGroupBuilder;
import net.minecraft.item.ItemGroup;

import java.util.HashMap;

public class ItemGroupList {
    private static final HashMap<String, ItemGroup> ITEMGROUPS = new HashMap<>();


    public static ItemGroup register(final String label) {
        final ItemGroup group = ItemGroupBuilder.create(label).build();
        ITEMGROUPS.put(label, group);
        return group;
    }

    public static ItemGroup getItemGroup(final String label) {
        //returns generated ItemGroups
        if (ITEMGROUPS.containsKey(label)) return ITEMGROUPS.get(label);

        //returns vanilla ItemGroups
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.getTabLabel().equalsIgnoreCase(label)) {
                return group;
            }
        }

        //Generate a new ItemGroup
        return register(label);
    }
}
