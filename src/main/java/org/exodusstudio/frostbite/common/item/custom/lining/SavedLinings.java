package org.exodusstudio.frostbite.common.item.custom.lining;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class SavedLinings {
    private final HashMap<String, List<ItemStack>> playerLinings = new HashMap<>();

    public void setLiningsForPlayer(
            String playerUUID,
            ItemStack helmetLining,
            ItemStack chestplateLining,
            ItemStack leggingsLining,
            ItemStack bootsLining) {
        playerLinings.put(playerUUID, List.of(
                helmetLining,
                chestplateLining,
                leggingsLining,
                bootsLining));
    }

    public void setSpecificLiningForPlayer(String playerUUID, int index, ItemStack lining) {
        playerLinings.get(playerUUID).set(index, lining);
    }

    public List<ItemStack> getLiningsForPlayer(String playerUUID) {
        return playerLinings.get(playerUUID);
    }

    public int getLiningLevelForPlayer(String playerUUID) {
        List<ItemStack> linings = playerLinings.get(playerUUID);
        int sum = 0;
        for (ItemStack lining : linings) {
            if (!lining.isEmpty() && lining.getItem() instanceof LiningItem liningItem) {
                sum += liningItem.getLiningLevel();
            }
        }
        return sum;
    }

    public ItemStack getSpecificLiningForPlayer(String playerUUID, int index) {
        List<ItemStack> linings = playerLinings.get(playerUUID);
        if (linings != null && index >= 0 && index < linings.size()) {
            return linings.get(index);
        }
        return ItemStack.EMPTY;
    }

    public void load(ListTag listTag, RegistryAccess registryAccess, String playerUUID) {
        for (int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundtag = listTag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            ItemStack itemstack = ItemStack.parse(registryAccess, compoundtag).orElse(ItemStack.EMPTY);
            this.playerLinings.get(playerUUID).set(j, itemstack);
        }
    }

    public Tag save(ListTag listTag, RegistryAccess registryAccess, String playerUUID) {
        List<ItemStack> linings = playerLinings.get(playerUUID);
        for (int i = 0; i < linings.size(); ++i) {
            if (!(linings.get(i)).isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listTag.add((linings.get(i)).save(registryAccess, compoundtag));
            }
        }

        return listTag;
    }
}
