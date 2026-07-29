package org.exodusstudio.frostbite.common.entity.custom.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.util.helpers.CodecHelper;

import java.util.UUID;

public class PseudoEntity {
    public Vec3 speed;
    public AABB aabb;
    public UUID owner;
    public final long startTick;

    public PseudoEntity(Vec3 speed, AABB aabb, UUID owner, long startTick) {
        this.speed = speed;
        this.aabb = aabb;
        this.owner = owner;
        this.startTick = startTick;
    }

    public static final Codec<PseudoEntity> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("speed").forGetter(pseudoEntity -> pseudoEntity.speed),
            CodecHelper.AABB_CODEC.fieldOf("aabb").forGetter(pseudoEntity -> pseudoEntity.aabb),
            CodecHelper.UUID_CODEC.fieldOf("owner").forGetter(pseudoEntity -> pseudoEntity.owner),
            Codec.LONG.fieldOf("start_tick").forGetter(pseudoEntity -> pseudoEntity.startTick)
    ).apply(instance, PseudoEntity::new));

    public static final StreamCodec<ByteBuf, PseudoEntity> STREAM_CODEC = StreamCodec.of(
            PseudoEntity::toBuffer,
            PseudoEntity::fromBuffer
    );

    public static void toBuffer(ByteBuf buffer, PseudoEntity pseudoEntity) {
        buffer.writeDouble(pseudoEntity.speed.x);
        buffer.writeDouble(pseudoEntity.speed.y);
        buffer.writeDouble(pseudoEntity.speed.z);
        buffer.writeDouble(pseudoEntity.aabb.minX);
        buffer.writeDouble(pseudoEntity.aabb.minY);
        buffer.writeDouble(pseudoEntity.aabb.minZ);
        buffer.writeDouble(pseudoEntity.aabb.maxX);
        buffer.writeDouble(pseudoEntity.aabb.maxY);
        buffer.writeDouble(pseudoEntity.aabb.maxZ);
        Utf8String.write(buffer, pseudoEntity.owner.toString(), 32767);
        buffer.writeLong(pseudoEntity.startTick);
    }

    public static PseudoEntity fromBuffer(ByteBuf buffer) {
        Vec3 speed = new Vec3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        AABB aabb = new AABB(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        UUID owner = UUID.fromString(Utf8String.read(buffer, 32767));
        long startTick = buffer.readLong();
        return new PseudoEntity(speed, aabb, owner, startTick);
    }

    public record PseudoEntityContext(PseudoEntity pseudoEntity, ServerLevel level, Entity owner, long ticksAlive) {}
}
