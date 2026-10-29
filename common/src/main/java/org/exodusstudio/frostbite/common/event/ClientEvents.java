package org.exodusstudio.frostbite.common.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientRawInputEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.CodexEntryToast;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.gui.CodexScreen;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.exodusstudio.frostbite.common.component.ArmourStatsData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.event.custom.*;
import org.exodusstudio.frostbite.common.item.armour.ArmourCleanliness;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.item.contract.ContractItem;
import org.exodusstudio.frostbite.common.item.contract.PartialContractItem;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SeriousAttackWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SpellTooltipable;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.network.ParryPayload;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.particle.options.TextParticleOption;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.util.PercentageAttribute;
import org.exodusstudio.frostbite.common.util.ScreenShakeHandler;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@SuppressWarnings("DataFlowIssue")
public class ClientEvents {
    public static final RandomSource random = RandomSource.create();

    public static void init() {
        CodexEntryUnlockedEvent.EVENT.register(ClientEvents::codexEntryUnlocked);
        PlayerHasEntryEvent.EVENT.register(ClientEvents::playerHasEntry);
        MovePlayerEvent.EVENT.register(ClientEvents::movePlayer);
        ClientGuiEvent.RENDER_POST.register(ClientEvents::inventoryContract);
        ClientRawInputEvent.KEY_PRESSED.register(ClientEvents::frog);
        ClientRawInputEvent.MOUSE_CLICKED_PRE.register(ClientEvents::staffControl);
        LivingTickEvent.EVENT.register(ClientEvents::hidePerfectParryFlash);
        TickEvent.PLAYER_PRE.register(ClientEvents::frog);
        TickEvent.PLAYER_POST.register(ClientEvents::ambience);
        TickEvent.PLAYER_POST.register(ClientEvents::snow);
        ClientTooltipEvent.ITEM.register(ClientEvents::contractTooltips);
        ClientTooltipEvent.ITEM.register(ClientEvents::spellTooltipable);
        ClientTooltipEvent.ITEM.register(ClientEvents::armourTooltips);
        LivingDamageEvent.Post.EVENT.register(ClientEvents::damageParticle);
        ClientTickEvent.CLIENT_POST.register(ClientEvents::codexOpen);
        ClientTickEvent.CLIENT_POST.register(ClientEvents::parryInput);
        ClientTickEvent.CLIENT_POST.register(ClientEvents::shader);
        ClientTickEvent.CLIENT_POST.register(ClientEvents::screenShakeTick);
        ClientTickEvent.CLIENT_POST.register(ClientEvents::screenShake);
    }

    public static void codexEntryUnlocked(CodexEntryUnlockedEvent event) {
        if (event.getPlayer().getUUID().equals(Minecraft.getInstance().player.getUUID()))
            Minecraft.getInstance().gui.toastManager().addToast(new CodexEntryToast(event.getEntry()));
    }

    public static void playerHasEntry(PlayerHasEntryEvent event) {
        event.hasEntry = CodexEntry.playerHasEntry(Minecraft.getInstance().player, event.getEntry());
    }

    public static void movePlayer(MovePlayerEvent event) {
        if (Minecraft.getInstance().player.getUUID().equals(event.getPlayer()))
            Minecraft.getInstance().player.addDeltaMovement(event.getSpeed());
    }

    public static void inventoryContract(Screen screen, GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Player player = Minecraft.getInstance().player;
        Font font = Minecraft.getInstance().font;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            int centerX = graphics.guiWidth() / 2;
            int centerY = graphics.guiHeight() / 2;
            int x = centerX + ((AbstractContainerScreen<?>) screen).imageWidth / 2;
            int y = centerY + ((AbstractContainerScreen<?>) screen).imageHeight / 2;
            if (Minecraft.getInstance().hasControlDown())
                renderArmourBonuses(player.getInventory().equipment, graphics, x, y, 150);
            else if (Minecraft.getInstance().hasShiftDown()) {
                List<FormattedCharSequence> l = font.split(Component.translatable("frostbite.inventory.armour_tooltip"), 100);
                Util.drawMultilineText(graphics, l, x + 3, y - font.lineHeight * l.size(), 0xFFFF6666);
            }

            Contract c = LivingContractInfo.getContract(player);
            if (c == null) return;
            List<ContractAttribute> attributes = c.allAttributes();
            if (attributes.isEmpty()) return;

            Map<FormattedCharSequence, Boolean> lines = new LinkedHashMap<>();
            for (ContractAttribute a : attributes) {
                lines.put(a.getSmallInfo(player, Either.right(c)).getVisualOrderText(), false);
                for (FormattedCharSequence s : font.split(a.getExtraInfo(player, Either.right(c), false), 150))
                    lines.put(s, true);
            }
            int i = -lines.size() / 2;
            for (FormattedCharSequence line : lines.keySet()) {
                graphics.text(font, line, lines.get(line) ? 16 : 0, centerY + i * font.lineHeight, 0xFFFFFFFF);
                i++;
            }
        }
    }

    public static void renderArmourBonuses(EntityEquipment equipment, GuiGraphicsExtractor gui, int x, int y, int w) {
        Font font = Minecraft.getInstance().font;

        Map<Holder<Attribute>, MobEffect.AttributeTemplate> totalAttributes = new LinkedHashMap<>();

        ArmourSet set = ArmourSet.getFullSet(equipment);
        if (set != null) {
            totalAttributes.putAll(set.attributes());
        }

        EquipmentSlot[] slots = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

        for (EquipmentSlot slot : slots) {
            ItemStack stack = equipment.get(slot);

            if (!stack.isEmpty() && stack.has(DataComponentTypeRegistry.ARMOUR_STATS.get())) {
                for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : stack.get(DataComponentTypeRegistry.ARMOUR_STATS.get()).getAttributes().entrySet()) {

                    if (totalAttributes.containsKey(entry.getKey()) && entry.getValue().operation() == totalAttributes.get(entry.getKey()).operation()) {
                        MobEffect.AttributeTemplate combined = new MobEffect.AttributeTemplate(
                                entry.getValue().id(),
                                totalAttributes.get(entry.getKey()).amount() + entry.getValue().amount(),
                                entry.getValue().operation()
                        );
                        totalAttributes.put(entry.getKey(), combined);
                    }
                    else totalAttributes.put(entry.getKey(), entry.getValue());
                }
            }
        }
        if (totalAttributes.isEmpty()) return;

        Map<Holder<Attribute>, MobEffect.AttributeTemplate> posAttributes = new LinkedHashMap<>();
        Map<Holder<Attribute>, MobEffect.AttributeTemplate> negativeAttributes = new LinkedHashMap<>();

        for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : totalAttributes.entrySet()) {
            if (entry.getValue().amount() > 0) posAttributes.put(entry.getKey(), entry.getValue());
            else negativeAttributes.put(entry.getKey(), entry.getValue());
        }

        List<Component> components = new ArrayList<>();
        attributesComponents(negativeAttributes, components, 0, true);
        attributesComponents(posAttributes, components, 0, true);

        components.addFirst(Component.translatable("frostbite.armour_bonuses").withStyle(ChatFormatting.GOLD));

        int h = components.size() * font.lineHeight + 16;
        gui.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("friends/toast_background"), x, y - h, w, h);

        int i = 0;
        for (Component component : components) {
            gui.text(font, component, x + 8, y + i + 8 - h, 0xFFFFFFFF);
            i += font.lineHeight;
        }
    }

    public static void comboCharge(GuiGraphicsExtractor graphics) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;
        if (Minecraft.getInstance().gui.hud.isHidden()) return;

        {
            int screenCenter = graphics.guiWidth() / 2;
            int barHeight = 16;
            int endY = graphics.guiHeight() - 16 - 3;
            int startY = endY + barHeight;

            for (int i = 0; i < 10; i++) {
                int x;
                ItemStack stack;

                if (i == 9) {
                    stack = player.getOffhandItem();
                    x = player.getMainArm().getOpposite() == HumanoidArm.LEFT ? screenCenter - 91 - 26 : screenCenter + 91 + 10;
                } else {
                    stack = player.getInventory().getItem(i);
                    x = screenCenter - 90 + i * 20 + 2;
                }

                if (!(stack.getItem() instanceof ComboWeapon comboWeapon)) continue;

                int charge = ComboWeapon.getCharge(stack);
                float chargeProgress = charge / (float) comboWeapon.chargeRequired();
                graphics.verticalLine(x, startY, endY, 0xFFFFFFFF);
                graphics.verticalLine(x, startY, (int) Mth.lerp(chargeProgress, startY, endY), 0xFF00FF00);

                if (!(comboWeapon instanceof SeriousAttackWeapon seriousAttackWeapon)) continue;

                int cooldown = (int) Math.min(seriousAttackWeapon.getCooldown(), SeriousAttackWeapon.secondsSinceLastUsed(stack, level.getGameTime()));
                float cooldownProgress = cooldown / seriousAttackWeapon.getCooldown();
                graphics.verticalLine(x + 14, startY, endY , 0xFFFFFFFF);
                graphics.verticalLine(x + 14, startY, (int) Mth.lerp(cooldownProgress, startY, endY), 0xFF3898f2);
            }
        }
    }

    public static void codexOpen(Minecraft minecraft) {
        while (KeyMappingRegistry.CODEX.consumeClick()) {
            minecraft.gui.setScreen(new CodexScreen());
        }
    }

    public static void parryInput(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player == null) return;
        while (KeyMappingRegistry.PARRY.consumeClick()) {
            if (!ParryManager.isParrying(player)) {
                NetworkManager.sendToServer(new ParryPayload(true));
            }
        }
    }

    public static void hidePerfectParryFlash(LivingTickEvent event) {
        if (!(event.getEntity() instanceof LivingEntity living) || !living.level().isClientSide()) return;
        if (DataHelper.getInt(living, ParryManager.PERFECT_FLASH) != 0) {
            living.hurtTime = 0;
            living.hurtDuration = 0;
            DataHelper.setData(living, ParryManager.PERFECT_FLASH, 0);
        }
    }

    public static EventResult frog(Minecraft minecraft, int action, KeyEvent keyEvent) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return EventResult.pass();

        if (Minecraft.getInstance().options.keyJump.matches(InputConstants.getKey(keyEvent)) &&
                player.getAttributes().hasAttribute(AttributeRegistry.JUMPS) &&
                player.getAttributeValue(AttributeRegistry.JUMPS) > 0 &&
                !player.isCreative()) {
            if (DataHelper.getInt(player, "jump_count") == -1) {
                DataHelper.setData(player, "jump_count", 0);
                return EventResult.pass();
            }
            DataHelper.setData(player, "jump_count", DataHelper.getInt(player, "jump_count") + 1);
            if (DataHelper.getInt(player, "jump_count") > player.getAttributeValue(AttributeRegistry.JUMPS)) return EventResult.pass();
            while (Minecraft.getInstance().options.keyJump.consumeClick()) {
                Vec3 d = player.getDeltaMovement();
                player.setDeltaMovement(d.x, 0, d.z);
                player.jumpFromGround();
            }
        }
        return EventResult.pass();
    }

    public static void frog(Player tickingPlayer) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (ParryManager.isParrying(player)) {
            Minecraft.getInstance().options.keyJump.setDown(false);
            if (player.getDeltaMovement().y > 0) player.setDeltaMovement(player.getDeltaMovement().x, 0, player.getDeltaMovement().z);
        }
        if (player.onGround()) DataHelper.setData(player, "jump_count", -1);
    }

////    public static void illusionsStart(RenderPlayerEvent.Pre event) {
//        Entity e = Minecraft.getInstance().level.getEntity(((UUIDState) event.getRenderState()).frostbite$getUUID());
//        if (e instanceof Player player &&
//                LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.ILLUSIONS) &&
//                event.getRenderState() instanceof AvatarRenderState) {
//            if (DataHelper.getBoolean(player, "is_rendering_illusions")) {
//                event.setCanceled(true);
//            } else {
//                DataHelper.setData(player, "is_rendering_illusions", true);
//            }
//        }
//    }

////    public static void illusionsRender(RenderPlayerEvent.Post event) {
//        Entity e = Minecraft.getInstance().level.getEntity(((UUIDState) event.getRenderState()).frostbite$getUUID());
//        if (e instanceof Player player &&
//                LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.ILLUSIONS) &&
//                event.getRenderState() instanceof AvatarRenderState a) {
//            List<String> uuids = new ArrayList<>(List.of(DataHelper.getString(player, "illusion_UUIDs").split(";")));
//            List<String> toRemove = new ArrayList<>();
//            for (String stringUUID : uuids) {
//                Entity illusionMaybe = Minecraft.getInstance().level.getEntity(UUID.fromString(stringUUID));
//                if (illusionMaybe instanceof PlayerIllusionEntity illusion) {
//                    PoseStack s = event.getPoseStack();
//                    s.pushPose();
//                    s.translate(player.getPosition(event.getPartialTick()).subtract(illusion.getPosition(event.getPartialTick())));
//                    event.getRenderer().submit(a, s, event.getSubmitNodeCollector(), Minecraft.getInstance().levelRenderer.levelRenderState.cameraRenderState);
//                    s.popPose();
//                } else {
//                    toRemove.add(stringUUID);
//                }
//            }
//            uuids.removeAll(toRemove);
//            StringBuilder remaining = new StringBuilder();
//            uuids.forEach(s -> remaining.append(s).append(";"));
//            DataHelper.setData(player, "illusion_UUIDs", remaining.toString());
//            DataHelper.setData(player, "is_rendering_illusions", false);
//        }
//    }

    public static void contractTooltips(ItemStack stack, List<Component> lines, Item.TooltipContext context, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.getItem() instanceof ContractFragmentItem) {
            ContractAttribute a = ContractAttribute.getAttribute(stack);
            if (a == null) return;
            lines.add(1, a.getSmallInfo(player, Either.left(stack)));
            if (Minecraft.getInstance().hasShiftDown()) lines.add(2, a.getExtraInfo(player, Either.left(stack), true));
            lines.remove(Component.literal("frostbite:contract_fragment_" + a.id).withStyle(ChatFormatting.DARK_GRAY));
        }

        Contract c = Contract.getContract(stack);
        if (c == null) return;
        List<ContractAttribute> attributes = c.allAttributes();
        if (attributes.isEmpty()) return;

        for (ContractAttribute a : c.allAttributes().reversed()) {
            lines.add(1, a.getSmallInfo(player, Either.left(stack)));
            if (Minecraft.getInstance().hasShiftDown()) lines.add(2, a.getExtraInfo(player, Either.left(stack), true));
        }

        if (stack.getItem() instanceof ContractItem || stack.getItem() instanceof PartialContractItem) return;
        if (!c.hasAttribute(ContractAttributes.CORROSION)) return;

        int ticksSinceStart = Math.toIntExact(level.getGameTime() - DataHelper.getInt(stack, "corrosion_start"));
        float halfEvery = ContractAttribute.getStat(c, ContractAttributes.CORROSION) * 60;
        int tooltipIndex = Math.min(Minecraft.getInstance().hasShiftDown() ? 17 : 9, lines.size());
        lines.add(tooltipIndex, Component.translatable("contract.corrosion.reduction", Math.round(100f *
                Util.getLog2Reduction(ticksSinceStart, halfEvery)), "%").withStyle(ChatFormatting.YELLOW));
    }

    public static void spellTooltipable(ItemStack stack, List<Component> lines, Item.TooltipContext context, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.getItem() instanceof SpellTooltipable s && stack.has(DataComponentTypeRegistry.MODE.get())) {
            int i = 1;
            if (stack.getItem() instanceof ModeWeapon) {
                lines.add(i++, Component.translatable(
                        "frostbite.staff.uses", ModeWeapon.getUses(stack), ModeWeapon.MAX_USES)
                        .withStyle(ChatFormatting.GRAY));
            }
            for (String str : s.getModes()) {
                String mode = stack.get(DataComponentTypeRegistry.MODE.get()).mode();
                MutableComponent c = Component.translatable("frostbite.modes." + str);
                if (mode.equals(str)) c = c.withStyle(ChatFormatting.BOLD).withStyle(s.selectedColour());
                else c = c.withStyle(s.regularColour());
                lines.add(i, c);
                i++;
            }
            lines.add(i, Component.translatable("frostbite.modes.left_click").withStyle(ChatFormatting.GRAY));
            lines.add(i, Component.translatable("frostbite.modes.right_click").withStyle(ChatFormatting.GRAY));
        }
    }

    public static void armourTooltips(ItemStack stack, List<Component> lines, Item.TooltipContext context, TooltipFlag flag) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.has(DataComponentTypeRegistry.ARMOUR_SET.get())) {
            ArmourSet set = stack.get(DataComponentTypeRegistry.ARMOUR_SET.get()).set();
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes = set.attributes();
            int pieces = set.getPiecesWithSet(player.getInventory().equipment);

            attributesComponents(attributes, lines, 1, pieces == 4);
            MutableComponent setPieces = Component.literal(" (")
                    .append(String.valueOf(pieces))
                    .append("/4)");
            MutableComponent name = Component.translatable("frostbite.armour_sets." + set.id())
                    .append(" ")
                    .append(Component.translatable("frostbite.armour_set"));
            if (pieces != 4) setPieces.withStyle(ChatFormatting.GRAY);
            if (stack.has(DataComponents.CUSTOM_NAME)) {
                name.withStyle(stack.get(DataComponents.CUSTOM_NAME).getStyle());
                if (pieces == 4) setPieces.withStyle(stack.get(DataComponents.CUSTOM_NAME).getStyle());
            }
            name.append(setPieces);
            lines.add(1, name);
        }

        if (stack.has(DataComponentTypeRegistry.ARMOUR_STATS.get())) {
            ArmourStatsData data = stack.get(DataComponentTypeRegistry.ARMOUR_STATS.get());
            ArmourCleanliness cleanliness = data.cleanliness();
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes = data.getAttributes();

            Component first = lines.removeFirst();
            Component name = Component.translatable("frostbite.cleanliness." + cleanliness.name().toLowerCase())
                    .withStyle(cleanliness.formatting).withStyle(ChatFormatting.ITALIC).append(" ").append(first);

            lines.addFirst(name);

            attributesComponents(attributes, lines, 1, true);
        }
    }

    private static void attributesComponents(Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes, List<Component> list, int offset, boolean shouldColour) {
        int i = offset;
        for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : attributes.entrySet()) {
            Holder<Attribute> attribute = entry.getKey();
            AttributeModifier modifier = entry.getValue().create(0);

            MutableComponent c = Component.literal(modifier.amount() > 0 ? "  +" : "  ");
            if (attribute.value() instanceof PercentageAttribute || attribute.value().equals(Attributes.ATTACK_SPEED.value()))
                c.append(String.format("%.0f", modifier.amount() * 100)).append("%");
            else if (attribute.value().equals(AttributeRegistry.POISON.value())) c.append(String.format("%.0f", modifier.amount())).append("s");
            else c.append(String.format("%.0f", modifier.amount()));

            c.append(" ");
            c.append(Component.translatable(attribute.value().getDescriptionId()));
            if (shouldColour) c.withStyle(modifier.amount() > 0 ? ChatFormatting.DARK_GREEN : ChatFormatting.DARK_RED);
            else c.withStyle(ChatFormatting.GRAY);

            list.add(i, c);
            i++;
        }
    }

    public static EventResult staffControl(Minecraft minecraft, MouseButtonInfo buttonInfo, int action) {
//        if (event.getAction() != GLFW.GLFW_PRESS) return EventResult.pass(); TODO: translate to arch
        if (buttonInfo.button() != 0 && buttonInfo.button() != 1) return EventResult.pass();

        Player player = Minecraft.getInstance().player;
        if (player == null || Minecraft.getInstance().gui.screen() != null) return EventResult.pass();

        ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(itemInHand.getItem() instanceof ModeWeapon staff)) return EventResult.pass();

        if (buttonInfo.button() == 0) {
            staff.cycleMode(itemInHand);
            NetworkManager.sendToServer(new StaffPayload(new StaffPayload.StaffInfo(
                    itemInHand.get(DataComponentTypeRegistry.MODE.get()).mode(), player.getUUID(), false)));
            return EventResult.interruptTrue();
        } else if (!player.getCooldowns().isOnCooldown(itemInHand)) {
            boolean used = staff.attack(player.level(), player);
            player.getCooldowns().addCooldown(itemInHand, 20);
            if (used) {
                NetworkManager.sendToServer(new StaffPayload(new StaffPayload.StaffInfo(
                        itemInHand.get(DataComponentTypeRegistry.MODE.get()).mode(), player.getUUID(), true)));
            }
            return EventResult.interruptTrue();
        }
        return EventResult.pass();
    }

//    public static EventResult staffControl(Minecraft minecraft, MouseButtonInfo buttonInfo, int action) {
//        if (buttonInfo.button() == InputConstants.MOUSE_BUTTON_RIGHT) {
//            Player player = Minecraft.getInstance().player;
//            if (player != null) {
//                ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
//                if (itemInHand.getItem() instanceof ModeWeapon staff && !player.getCooldowns().isOnCooldown(itemInHand)) {
//                    staff.attack(player.level(), player);
//                    player.getCooldowns().addCooldown(itemInHand, 20);
//                    NetworkManager.sendToServer(new StaffPayload(new StaffPayload.StaffInfo(itemInHand.get(DataComponentTypeRegistry.MODE.get()).mode(), player.getUUID())));
//                    return EventResult.interruptFalse();
//                }
//            }
//        }
//        return EventResult.pass();
//    }

    public static void ambience(Player player) {
        if (Minecraft.getInstance().level == null) return;
        WeatherInfo weatherInfo = DataHelper.getWeatherInfo(Minecraft.getInstance().level);
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.AMBIENCES) {
            if (ambience.shouldPlay.apply(player.level()) && player.level() instanceof ClientLevel level) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(player, ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
            }
        }
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.SNOW_AMBIENCE) {
            if (ambience.shouldPlay.apply(player.level()) && player.level() instanceof ClientLevel level) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(player, ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
            }
        }
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.BLIZZARD_AMBIENCE) {
            if (ambience.shouldPlay.apply(player.level()) && player.level() instanceof ClientLevel level  && weatherInfo.isBlizzarding) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(player, ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
            }
        }
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.WHITEOUT_AMBIENCE) {
            if (ambience.shouldPlay.apply(player.level()) && player.level() instanceof ClientLevel level && weatherInfo.isWhiteouting) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(player, ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
            }
        }
    }

    public static void damageParticle(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof Player && event.getEntity() instanceof LivingEntity target && Minecraft.getInstance().level != null) {
            Vector3f speed = new Vector3f(
                    random.nextFloat(),
                    Math.abs(random.nextFloat()),
                    random.nextFloat())
                    .normalize(0.1f);

            Minecraft.getInstance().level.addParticle(
                    TextParticleOption.create(ParticleRegistry.DAMAGE_PARTICLE.get(),
                            String.format("%.1f", event.getInflictedDamage()),
                            0xFFFFFF
                    ),
                    target.getRandomX(0.7F),
                    target.getRandomY() + 1,
                    target.getRandomZ(0.7F),
                    speed.x(),
                    speed.y(),
                    speed.z());

            Frostbite.LOGGER.debug(String.valueOf(event.getInflictedDamage()));
        }
    }

    public static void snow(Player player) {
        Level level = player.level();
        RandomSource random = player.getRandom();
        if (level.isClientSide() && isFrostbite(level)) {
            WeatherInfo weatherInfo = DataHelper.getWeatherInfo(level);
            float r = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 30, 20), 15);
            float offset = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 10, 6), 2);
            float s = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 0.025f, 0.05f), 0.1f);
            float d = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 0.075f, 0.1f), 0.3f);
            int count = (int) Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 50, 100), 200);
            for (int i = 0; i < count; i++) {
                double d0 = player.getX() + player.getLookAngle().normalize().x * offset + (0.5D - random.nextDouble()) * r;
                double d1 = player.getY() + player.getLookAngle().normalize().y * offset + (0.5D - random.nextDouble()) * r / 2f + r / 4f;
                double d2 = player.getZ() + player.getLookAngle().normalize().z * offset + (0.5D - random.nextDouble()) * r;

                level.addAlwaysVisibleParticle(
                        random.nextFloat() < Mth.lerp(weatherInfo.whiteoutLevel,
                                Mth.lerp(weatherInfo.blizzardLevel, 0.01, 0.02), 0.05)
                                ? ParticleTypes.END_ROD : ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(),
                        d0, d1, d2,
                        (random.nextDouble()) * d + s,
                        (-random.nextDouble()) * d - s,
                        (random.nextDouble()) * d + s);
            }
        }
    }

    public static void shader(Minecraft minecraft) {
//        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.tickCount > 5)
//            Minecraft.getInstance().gameRenderer.setPostEffect(
//                    Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "custom"));
    }

    public static void screenShakeTick(Minecraft minecraft) {
        ScreenShakeHandler.tick();
    }

    static int screenShakeTicks = 0;
    public static void screenShake(Minecraft client) {
        Minecraft minecraft =  Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        WeatherInfo weatherInfo = DataHelper.getWeatherInfo(minecraft.level);
        if (minecraft.level.canSeeSky(player.blockPosition()) && minecraft.level.dimension() == Util.FROSTBITE_KEY) {
            screenShakeTicks++;
            ScreenShakeHandler.triggerShake(Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 0, 0.25), 0.5));
            if (screenShakeTicks >= 10) {
                screenShakeTicks = 0;
            }
        }
    }
}
