package org.exodusstudio.frostbite.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;

import static net.minecraft.util.Mth.floor;

public class AdvancedClockItem extends Item {
    private boolean used;

    public AdvancedClockItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemUtils.startUsingInstantly(level, player, usedHand);
        if (used) {
            float t = (level.getOverworldClockTime() / (20f * 60)) + 7;
            //if (t < 0) t += 24;
            String mShown = Integer.toString(floor((t - floor(t)) * 60));
            String hShown = Integer.toString(floor(t));
            if (mShown.length() == 1) mShown = "0" + mShown;
            if (hShown.length() == 1) hShown = "0" + hShown;
            player.sendOverlayMessage(Component.literal(String.format("It is currently: %sh%s", hShown, mShown)));
        }
        used = !used;
        return super.use(level, player, usedHand);
    }
}