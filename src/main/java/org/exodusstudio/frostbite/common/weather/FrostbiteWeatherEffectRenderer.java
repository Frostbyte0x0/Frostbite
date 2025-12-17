package org.exodusstudio.frostbite.common.weather;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;

import java.util.ArrayList;
import java.util.List;

public class FrostbiteWeatherEffectRenderer {
    private static final Identifier SNOW_LOCATION =
            Identifier.withDefaultNamespace("textures/environment/snow.png");
    private float uPos;
    private float lastTime;
    private final float[] columnSizeX = new float[1024];
    private final float[] columnSizeZ = new float[1024];

    public FrostbiteWeatherEffectRenderer() {
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                float f = (float)(j - 16);
                float f1 = (float)(i - 16);
                float f2 = Mth.length(f, f1);
                this.columnSizeX[i * 32 + j] = -f1 / f2;
                this.columnSizeZ[i * 32 + j] = f / f2;
            }
        }
    }

    public void render(Level level, MultiBufferSource bufferSource, int ticks, float partialTick, Vec3 cameraPosition) {
        if (((ClientLevel) level).effects().renderSnowAndRain((ClientLevel) level, ticks, partialTick,
                cameraPosition.x, cameraPosition.y, cameraPosition.z))
            return;
      
        assert Minecraft.getInstance().level != null;

        uPos += (Minecraft.getInstance().level.getGameTime() + partialTick - lastTime) *
                Mth.lerp(Frostbite.weatherInfo.getBlizzardLevel(partialTick), 0.02f, 0.2f);
        lastTime = (float) Minecraft.getInstance().level.getGameTime() + partialTick;
        int r = 10;
        List<ColumnInstance> list1 = new ArrayList<>();
        this.collectColumnInstances(level, ticks, partialTick, cameraPosition, r, list1);
        if (!list1.isEmpty()) {
            this.render(bufferSource, cameraPosition, r, list1);
        }
    }

    private void collectColumnInstances(
            Level level,
            int ticks,
            float partialTick,
            Vec3 cameraPosition,
            int radius,
            List<ColumnInstance> snowColumnInstances
    ) {
        int i = Mth.floor(cameraPosition.x);
        int j = Mth.floor(cameraPosition.y);
        int k = Mth.floor(cameraPosition.z);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        RandomSource randomsource = RandomSource.create();

        for (int l = k - radius; l <= k + radius; l++) {
            for (int i1 = i - radius; i1 <= i + radius; i1++) {
                int j1 = level.getHeight(Heightmap.Types.MOTION_BLOCKING, i1, l);
                int k1 = Math.max(j - radius, j1);
                int l1 = Math.max(j + radius, j1);
                if (l1 - k1 != 0) {
                    int i2 = i1 * i1 * 3121 + i1 * 45238971 ^ l * l * 418711 + l * 13761;
                    randomsource.setSeed(i2);
                    int j2 = Math.max(j, j1);
                    int k2 = LevelRenderer.getLightColor(level, blockpos$mutableblockpos.set(i1, j2, l));
                    snowColumnInstances.add(
                            this.createSnowColumnInstance(randomsource, ticks, i1, k1, l1, l, k2, partialTick));
                }
            }
        }
    }

    private void render(
            MultiBufferSource bufferSource,
            Vec3 cameraPosition,
            int radius,
            List<ColumnInstance> snowColumnInstances
    ) {
        if (!snowColumnInstances.isEmpty()) {
            RenderType rendertype1 = RenderTypes.weather(SNOW_LOCATION, Minecraft.useShaderTransparency());
            this.renderInstances(bufferSource.getBuffer(rendertype1), snowColumnInstances, cameraPosition,
                    radius);
        }
    }

    private ColumnInstance createSnowColumnInstance(
            RandomSource random, int ticks, int x, int bottomY, int topY, int z, int lightCoords, float partialTick
    ) {
        float f = ticks + partialTick;
        float f1 = random.nextFloat();
        float f2 = (float)(random.nextDouble() + (f * random.nextDouble() * 0.001f));
        float f3 = -((float)(ticks & 511) + partialTick) / 512f;
        int i = LightTexture.pack((LightTexture.block(lightCoords) * 3 + 15) / 4,
                (LightTexture.sky(lightCoords) * 3 + 15) / 4);
        return new ColumnInstance(x, z, bottomY, topY, uPos + f1, f3 + f2, i);
    }

    private void renderInstances(
            VertexConsumer buffer, List<ColumnInstance> columnInstances,
            Vec3 cameraPosition, int radius
    ) {
        for (ColumnInstance weathereffectrenderer$columninstance : columnInstances) {
            float f = (float)((double)weathereffectrenderer$columninstance.x + 0.5 - cameraPosition.x);
            float f1 = (float)((double)weathereffectrenderer$columninstance.z + 0.5 - cameraPosition.z);
            float f2 = (float)Mth.lengthSquared(f, f1);
            float f3 = Mth.lerp(f2 / (radius * radius), 0.8f, 0.5F);
            int i = ARGB.white(f3);
            int j = (weathereffectrenderer$columninstance.z - Mth.floor(cameraPosition.z) + 16) * 32
                    + weathereffectrenderer$columninstance.x
                    - Mth.floor(cameraPosition.x)
                    + 16;
            float f4 = this.columnSizeX[j] / 2.0F;
            float f5 = this.columnSizeZ[j] / 2.0F;
            float f6 = f - f4;
            float f7 = f + f4;
            float f8 = (float)((double)weathereffectrenderer$columninstance.topY - cameraPosition.y);
            float f9 = (float)((double)weathereffectrenderer$columninstance.bottomY - cameraPosition.y);
            float f10 = f1 - f5;
            float f11 = f1 + f5;
            float f12 = weathereffectrenderer$columninstance.uOffset + 0.0F;
            float f13 = weathereffectrenderer$columninstance.uOffset + 1.0F;
            float f14 = (float)weathereffectrenderer$columninstance.bottomY * 0.25F + weathereffectrenderer$columninstance.vOffset;
            float f15 = (float)weathereffectrenderer$columninstance.topY * 0.25F + weathereffectrenderer$columninstance.vOffset;
            buffer.addVertex(f6, f8, f10).setUv(f12, f14).setColor(i).setLight(weathereffectrenderer$columninstance.lightCoords);
            buffer.addVertex(f7, f8, f11).setUv(f13, f14).setColor(i).setLight(weathereffectrenderer$columninstance.lightCoords);
            buffer.addVertex(f7, f9, f11).setUv(f13, f15).setColor(i).setLight(weathereffectrenderer$columninstance.lightCoords);
            buffer.addVertex(f6, f9, f10).setUv(f12, f15).setColor(i).setLight(weathereffectrenderer$columninstance.lightCoords);
        }
    }

    record ColumnInstance(int x, int z, int bottomY, int topY, float uOffset, float vOffset, int lightCoords) {
    }
}