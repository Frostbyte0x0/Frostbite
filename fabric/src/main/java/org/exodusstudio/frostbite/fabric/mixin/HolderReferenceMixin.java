package org.exodusstudio.frostbite.fabric.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> {
    @Shadow private @Nullable ResourceKey<T> key;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Holder.Reference<?> other)) return false;
        ResourceKey<?> otherKey = ((HolderReferenceMixin<?>) (Object) other).key;
        return otherKey != null && this.key != null && otherKey == this.key;
    }

    @Override
    public int hashCode() {
        return this.key == null ? super.hashCode() : this.key.hashCode();
    }
}
