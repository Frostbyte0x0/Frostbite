package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.exodusstudio.frostbite.common.mixinterfaces.AttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({Entity.class, Level.class, ChunkAccess.class})
public abstract class AttachmentHolderMixin implements AttachmentHolder {
}
