package org.exodusstudio.frostbite.common.item.lining;

import net.minecraft.world.ItemStackWithSlot;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.ArrayList;
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
        playerLinings.put(playerUUID, new ArrayList<>(List.of(
                helmetLining,
                chestplateLining,
                leggingsLining,
                bootsLining)));
    }

    public void setSpecificLiningForPlayer(String playerUUID, int index, ItemStack lining) {
        getLiningsForPlayerOrSetEmpty(playerUUID).set(index, lining);
    }

    public void setSpecificLiningForPlayer(String playerUUID, EquipmentSlot slot, ItemStack lining) {
        int index = switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };
        getLiningsForPlayerOrSetEmpty(playerUUID).set(index, lining);
    }

    public List<ItemStack> getLiningsForPlayerOrSetEmpty(String playerUUID) {
        List<ItemStack> linings = new ArrayList<>(List.of(ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY));
        playerLinings.putIfAbsent(playerUUID, linings);
        return playerLinings.get(playerUUID);
    }

    public int getLiningLevelForPlayer(String playerUUID) {
        List<ItemStack> linings = getLiningsForPlayerOrSetEmpty(playerUUID);
        int sum = 0;
        for (ItemStack lining : linings) {
            if (!lining.isEmpty() && lining.getItem() instanceof LiningItem liningItem) {
                sum += liningItem.getLiningLevel();
            }
        }
        return sum;
    }

    public ItemStack getSpecificLiningForPlayer(String playerUUID, int index) {
        List<ItemStack> linings = getLiningsForPlayerOrSetEmpty(playerUUID);
        if (linings != null && index >= 0 && index < linings.size()) {
            return linings.get(index);
        }
        return ItemStack.EMPTY;
    }

    public ItemStack getSpecificLiningForPlayer(String playerUUID, EquipmentSlot slot) {
        List<ItemStack> linings = getLiningsForPlayerOrSetEmpty(playerUUID);
        int index = switch (slot) {
            case HEAD -> 0;
            case CHEST -> 1;
            case LEGS -> 2;
            case FEET -> 3;
            default -> -1;
        };
        if (linings != null && index >= 0 && index < linings.size()) {
            return linings.get(index);
        }
        return ItemStack.EMPTY;
    }

    public void load(ValueInput.TypedInputList<ItemStackWithSlot> input, String playerUUID) {
        List<ItemStack> linings = new ArrayList<>();
        for (int i = 0; i < 4; i++) linings.add(ItemStack.EMPTY);

        for (ItemStackWithSlot itemstackwithslot : input) {
            linings.set(itemstackwithslot.slot(), itemstackwithslot.stack());
        }
        playerLinings.putIfAbsent(playerUUID, linings);
    }

    public void save(ValueOutput.TypedOutputList<ItemStackWithSlot> output, String playerUUID) {
        List<ItemStack> linings = playerLinings.get(playerUUID);
        for (int i = 0; i < linings.size(); ++i) {
            ItemStack itemstack = linings.get(i);
            if (!itemstack.isEmpty()) {
                output.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }
}
