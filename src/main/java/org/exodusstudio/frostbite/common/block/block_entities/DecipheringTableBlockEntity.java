package org.exodusstudio.frostbite.common.block.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.client.gui.scribing.DecipheringMenu;
import org.exodusstudio.frostbite.common.item.ThermalLensItem;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.jspecify.annotations.Nullable;

public class DecipheringTableBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private static final int[] SLOTS_FOR_UP = new int[]{0};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 1};
    private static final int[] SLOTS_FOR_SIDES = new int[]{1};
    private static final int DECIPHERING_TIME = 100;
    protected NonNullList<ItemStack> items;
    private int cookingTimer;
    private int cookingTotalTime = DECIPHERING_TIME;
    private boolean isDeciphering;
    private boolean wasDeciphering;
    protected final ContainerData dataAccess;

    public DecipheringTableBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(BlockEntityRegistry.DECIPHERING_TABLE.get(), worldPosition, blockState);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(int dataId) {
                return switch (dataId) {
                    case 0 -> DecipheringTableBlockEntity.this.cookingTimer;
                    case 1 -> DecipheringTableBlockEntity.this.cookingTotalTime;
                    default -> 0;
                };
            }

            public void set(int dataId, int value) {
                switch (dataId) {
                    case 0 -> DecipheringTableBlockEntity.this.cookingTimer = value;
                    case 1 -> DecipheringTableBlockEntity.this.cookingTotalTime = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);
        this.cookingTimer = input.getIntOr("cooking_time_spent", 0);
        this.cookingTotalTime = input.getIntOr("cooking_total_time", 0);
    }

    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("cooking_time_spent", this.cookingTimer);
        output.putInt("cooking_total_time", this.cookingTotalTime);
        ContainerHelper.saveAllItems(output, this.items);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.deciphering_table");
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, DecipheringTableBlockEntity entity) {
        boolean changed = false;

        ItemStack fragment = entity.items.get(0);
        ItemStack lens = entity.items.get(1);
        if (canDecipher(entity.items, lens, fragment)) {
            entity.wasDeciphering = entity.isDeciphering;
            entity.isDeciphering = true;

            entity.cookingTimer++;
            if (entity.cookingTimer == entity.cookingTotalTime) {
                entity.cookingTimer = 0;
                entity.cookingTotalTime = DECIPHERING_TIME;
                decipher(entity.items, fragment);
                changed = true;
            }
        } else {
            entity.cookingTimer = 0;
            entity.wasDeciphering = entity.isDeciphering;
            entity.isDeciphering = false;
        }

        if (entity.wasDeciphering != entity.isDeciphering) {
            changed = true;
            state = state.setValue(AbstractFurnaceBlock.LIT, entity.isDeciphering);
            level.setBlock(pos, state, 3);
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    private static boolean canDecipher(NonNullList<ItemStack> items, ItemStack lens, ItemStack fragment) {
        ItemStack resultItemStack = items.get(2);

        return lens.getItem() instanceof ThermalLensItem && fragment.getItem() instanceof ContractFragmentItem && resultItemStack.isEmpty();
    }

    private static void decipher(NonNullList<ItemStack> items, ItemStack inputItemStack) {
        ItemStack resultItemStack = items.get(2);
        if (resultItemStack.isEmpty()) {
            items.set(2, inputItemStack.copy());
        }

        inputItemStack.shrink(1);
    }

    public int[] getSlotsForFace(Direction direction) {
        if (direction == Direction.DOWN) {
            return SLOTS_FOR_DOWN;
        } else {
            return direction == Direction.UP ? SLOTS_FOR_UP : SLOTS_FOR_SIDES;
        }
    }

    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return this.canPlaceItem(slot, itemStack);
    }

    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return direction != Direction.DOWN || slot != 1 || itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET);
    }

    public int getContainerSize() {
        return this.items.size();
    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public void setItem(int slot, ItemStack itemStack) {
        this.setItem(slot, itemStack, false);
    }

    public void setItem(int slot, ItemStack itemStack, boolean insideTransaction) {
        this.items.set(slot, itemStack);
        itemStack.limitSize(this.getMaxStackSize(itemStack));
        if (slot == 1) {
            if (this.level instanceof ServerLevel) {
                if (!insideTransaction) {
                    this.cookingTotalTime = DECIPHERING_TIME;
                    this.cookingTimer = 0;
                    this.setChanged();
                }
            }
        }
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return new DecipheringMenu(i, inventory, this, dataAccess);
    }

    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0 -> {
                return itemStack.getItem() instanceof ThermalLensItem;
            }
            case 1 -> {
                return itemStack.getItem() instanceof ContractFragmentItem;
            }
        }
        return false;
    }
}
