package org.exodusstudio.frostbite.common.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
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
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.CodexEntryToast;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.gui.CodexScreen;
import org.exodusstudio.frostbite.common.component.ArmourStatsData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.event.custom.CodexEntryUnlockedEvent;
import org.exodusstudio.frostbite.common.event.custom.MovePlayerEvent;
import org.exodusstudio.frostbite.common.event.custom.PlayerHasEntryEvent;
import org.exodusstudio.frostbite.common.item.armour.ArmourCleanliness;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.item.contract.ContractItem;
import org.exodusstudio.frostbite.common.item.contract.PartialContractItem;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SeriousAttackWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SpellTooltipable;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.particle.options.TextParticleOption;
import org.exodusstudio.frostbite.common.registry.*;
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
@EventBusSubscriber(modid = Frostbite.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {
    public static final RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void codexEntryUnlocked(CodexEntryUnlockedEvent event) {
        if (event.getPlayer().getUUID().equals(Minecraft.getInstance().player.getUUID()))
            Minecraft.getInstance().gui.toastManager().addToast(new CodexEntryToast(event.getEntry()));
    }

    @SubscribeEvent
    public static void playerHasEntry(PlayerHasEntryEvent event) {
        event.hasEntry = CodexEntry.playerHasEntry(Minecraft.getInstance().player, event.getEntry());
    }

    @SubscribeEvent
    public static void movePlayer(MovePlayerEvent event) {
        if (Minecraft.getInstance().player.getUUID().equals(event.getPlayer()))
            Minecraft.getInstance().player.addDeltaMovement(event.getSpeed());
    }

    @SubscribeEvent
    public static void inventoryContract(ScreenEvent.Render.Post event) {
        Player player = Minecraft.getInstance().player;
        Font font = Minecraft.getInstance().font;
        Level level = Minecraft.getInstance().level;
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        if (player == null || level == null) return;

        if (event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof CreativeModeInventoryScreen) {
            int centerX = graphics.guiWidth() / 2;
            int centerY = graphics.guiHeight() / 2;
            int x = centerX + ((AbstractContainerScreen<?>) event.getScreen()).getImageWidth() / 2;
            int y = centerY + ((AbstractContainerScreen<?>) event.getScreen()).getImageHeight() / 2;
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

            if (!stack.isEmpty() && stack.has(DataComponentTypeRegistry.ARMOUR_STATS)) {
                for (Map.Entry<Holder<Attribute>, MobEffect.AttributeTemplate> entry : stack.get(DataComponentTypeRegistry.ARMOUR_STATS).getAttributes().entrySet()) {

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

        List<Either<FormattedText, TooltipComponent>> components = new ArrayList<>();
        attributesComponents(negativeAttributes, components, 0, true);
        attributesComponents(posAttributes, components, 0, true);

        components.addFirst(Either.left(Component.translatable("frostbite.armour_bonuses").withStyle(ChatFormatting.GOLD)));

        int h = components.size() * font.lineHeight + 16;
        gui.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("friends/toast_background"), x, y - h, w, h);

        int i = 0;
        for (Either<FormattedText, TooltipComponent> component : components) {
            gui.text(font, (Component) component.left().orElseThrow(), x + 8, y + i + 8 - h, 0xFFFFFFFF);
            i += font.lineHeight;
        }
    }

    @SubscribeEvent
    public static void comboCharge(RenderGuiLayerEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        if (player == null || level == null) return;
        if (Minecraft.getInstance().gui.hud.isHidden()) return;

        if (event.getName().equals(Identifier.withDefaultNamespace("hotbar"))) {
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

    @SubscribeEvent
    public static void codexOpen(InputEvent.Key event) {
        if (KeyMappingRegistry.CODEX.isActiveAndMatches(InputConstants.getKey(event.getKeyEvent()))) {
            while (KeyMappingRegistry.CODEX.consumeClick()) {
                Minecraft.getInstance().gui.setScreen(new CodexScreen());
            }
        }
    }

    @SubscribeEvent
    public static void frog(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (Minecraft.getInstance().options.keyJump.isActiveAndMatches(InputConstants.getKey(event.getKeyEvent())) &&
                player.getAttributes().hasAttribute(AttributeRegistry.JUMPS) &&
                player.getAttributeValue(AttributeRegistry.JUMPS) > 0 &&
                !player.isCreative()) {
            if (DataHelper.getInt(player, "jump_count") == -1) {
                DataHelper.setData(player, "jump_count", 0);
                return;
            }
            DataHelper.setData(player, "jump_count", DataHelper.getInt(player, "jump_count") + 1);
            if (DataHelper.getInt(player, "jump_count") > player.getAttributeValue(AttributeRegistry.JUMPS)) return;
            while (Minecraft.getInstance().options.keyJump.consumeClick()) {
                Vec3 d = player.getDeltaMovement();
                player.setDeltaMovement(d.x, 0, d.z);
                player.jumpFromGround();
            }
        }
    }

    @SubscribeEvent
    public static void frog(PlayerTickEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (player.onGround()) DataHelper.setData(player, "jump_count", -1);
    }

//    @SubscribeEvent
//    public static void illusionsStart(RenderPlayerEvent.Pre event) {
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

//    @SubscribeEvent
//    public static void illusionsRender(RenderPlayerEvent.Post event) {
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

    @SubscribeEvent
    public static void contractTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.getItem() instanceof ContractFragmentItem) {
            ContractAttribute a = ContractAttribute.getAttribute(stack);
            if (a == null) return;
            event.getTooltipElements().add(1, Either.left(a.getSmallInfo(player, Either.left(stack))));
            if (Minecraft.getInstance().hasShiftDown()) event.getTooltipElements().add(2, Either.left(a.getExtraInfo(player, Either.left(stack), true)));
            event.getTooltipElements().remove(Either.left(Component.literal("frostbite:contract_fragment_" + a.id).withStyle(ChatFormatting.DARK_GRAY)));
        }

        Contract c = Contract.getContract(stack);
        if (c == null) return;
        List<ContractAttribute> attributes = c.allAttributes();
        if (attributes.isEmpty()) return;

        for (ContractAttribute a : c.allAttributes().reversed()) {
            event.getTooltipElements().add(1, Either.left(a.getSmallInfo(player, Either.left(stack))));
            if (Minecraft.getInstance().hasShiftDown()) event.getTooltipElements().add(2, Either.left(a.getExtraInfo(player, Either.left(stack), true)));
        }

        if (stack.getItem() instanceof ContractItem || stack.getItem() instanceof PartialContractItem) return;
        if (!c.hasAttribute(ContractAttributes.CORROSION)) return;

        int ticksSinceStart = Math.toIntExact(level.getGameTime() - DataHelper.getInt(stack, "corrosion_start"));
        float halfEvery = ContractAttribute.getStat(c, ContractAttributes.CORROSION) * 60;
        event.getTooltipElements().add(Minecraft.getInstance().hasShiftDown() ? 17 : 9, Either.left(Component.translatable("contract.corrosion.reduction", Math.round(100f *
                Util.getLog2Reduction(ticksSinceStart, halfEvery)), "%").withStyle(ChatFormatting.YELLOW)));
    }

    @SubscribeEvent
    public static void spellTooltipable(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.getItem() instanceof SpellTooltipable s && stack.has(DataComponentTypeRegistry.MODE)) {
            int i = 1;
            for (String str : s.getModes()) {
                String mode = stack.get(DataComponentTypeRegistry.MODE).mode();
                MutableComponent c = Component.translatable("frostbite.modes." + str);
                if (mode.equals(str)) c = c.withStyle(ChatFormatting.BOLD).withStyle(s.selectedColour());
                else c = c.withStyle(s.regularColour());
                event.getTooltipElements().add(i, Either.left(c));
                i++;
            }
            event.getTooltipElements().add(i, Either.left(Component.translatable("frostbite.modes.left_click").withStyle(ChatFormatting.GRAY)));
            event.getTooltipElements().add(i, Either.left(Component.translatable("frostbite.modes.right_click").withStyle(ChatFormatting.GRAY)));
        }
    }

    @SubscribeEvent
    public static void armourTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        if (player == null || level == null) return;

        if (stack.has(DataComponentTypeRegistry.ARMOUR_SET)) {
            ArmourSet set = stack.get(DataComponentTypeRegistry.ARMOUR_SET).set();
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes = set.attributes();
            int pieces = set.getPiecesWithSet(player.getInventory().equipment);

            attributesComponents(attributes, event.getTooltipElements(), 1, pieces == 4);
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
            event.getTooltipElements().add(1, Either.left(name));
        }

        if (stack.has(DataComponentTypeRegistry.ARMOUR_STATS)) {
            ArmourStatsData data = stack.get(DataComponentTypeRegistry.ARMOUR_STATS);
            ArmourCleanliness cleanliness = data.cleanliness();
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes = data.getAttributes();

            MutableComponent first = (MutableComponent) event.getTooltipElements().removeFirst().left().orElseThrow();
            Component name = Component.translatable("frostbite.cleanliness." + cleanliness.name().toLowerCase())
                    .withStyle(cleanliness.formatting).withStyle(ChatFormatting.ITALIC).append(" ").append(first);

            event.getTooltipElements().addFirst(Either.left(name));

            attributesComponents(attributes, event.getTooltipElements(), 1, true);
        }
    }

    private static void attributesComponents(Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes, List<Either<FormattedText, TooltipComponent>> list, int offset, boolean shouldColour) {
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

            list.add(i, Either.left(c));
            i++;
        }
    }

    @SubscribeEvent
    public static void staffControl(InputEvent.MouseButton.Pre event) {
        if (event.getButton() == 1) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (itemInHand.getItem() instanceof ModeWeapon staff && !player.getCooldowns().isOnCooldown(itemInHand)) {
                    staff.attack(player.level(), player);
                    player.getCooldowns().addCooldown(itemInHand, 20);
                    event.setCanceled(true);
                    ClientPacketDistributor.sendToServer(new StaffPayload(new StaffPayload.StaffInfo(itemInHand.get(DataComponentTypeRegistry.MODE.get()).mode(), player.getUUID())));
                }
            }
        }
    }

    @SubscribeEvent
    public static void ambience(PlayerTickEvent.Post event) {
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.AMBIENCES) {
            if (ambience.shouldPlay.apply(event.getEntity().level()) && event.getEntity().level() instanceof ClientLevel level) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(event.getEntity(), ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
            }
        }
    }

    @SubscribeEvent
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

    @SubscribeEvent
    public static void snow(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = event.getEntity().level();
        RandomSource random = player.getRandom();
        if (level.isClientSide() && isFrostbite(level)) {
            WeatherInfo weatherInfo = DataHelper.getWeatherInfo(level);
            float r = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 30, 20), 15);
            float offset = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 10, 5), 2);
            float s = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 0.025f, 0.1f), 0.1f);
            float d = Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 0.05f, 0.1f), 0.1f);
            int count = (int) Mth.lerp(weatherInfo.whiteoutLevel,
                    Mth.lerp(weatherInfo.blizzardLevel, 125, 100), 75);
            for (int i = 0; i < count; i++) {
                double d0 = player.getX() + player.getLookAngle().normalize().x * offset + (0.5D - random.nextDouble()) * r;
                double d1 = player.getY() + player.getLookAngle().normalize().y * offset + (0.5D - random.nextDouble()) * r / 2f + r / 4f;
                double d2 = player.getZ() + player.getLookAngle().normalize().z * offset + (0.5D - random.nextDouble()) * r;

                level.addAlwaysVisibleParticle(
                        random.nextFloat() < 0.01 ? ParticleTypes.END_ROD : ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(),
                        d0, d1, d2,
                        (random.nextDouble()) * d + s,
                        (-random.nextDouble()) * d - s,
                        (random.nextDouble()) * d + s);
            }
        }
    }
}
