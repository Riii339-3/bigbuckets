package com.williambl.bigbuckets.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.williambl.bigbuckets.BigBucketItem;
import com.williambl.bigbuckets.client.platform.ClientServices;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Modified heavily from LemmaEOF's custom durability bar
 * <a href="https://github.com/Boundarybreaker/ShulkerCharm/blob/master/src/main/java/space/bbkr/shulkercharm/mixin/MixinItemRenderer.java">source</a>
 */
@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Unique
    private void bigBuckets$drawFluidBar(
            GuiGraphics gui,
            TextureAtlasSprite sprite,
            int color,
            int x,
            int y,
            int width
    ) {
        if (width <= 0) {
            return;
        }

        Matrix4f matrix = gui.pose().last().pose();

        float u0 = sprite.getU0();
        float v0 = sprite.getV0();

        float u1 = sprite.getU(width);
        float v1 = sprite.getV(1);

        float a = ((color >> 24) & 255) / 255.0F;
        float r = ((color >> 16) & 255) / 255.0F;
        float g = ((color >> 8) & 255) / 255.0F;
        float b = (color & 255) / 255.0F;

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        float z = 200.0F;

        BufferBuilder builder = Tesselator.getInstance().getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        builder.vertex(matrix, x, y + 1, z)
                .uv(u0, v1)
                .color(r, g, b, a)
                .endVertex();

        builder.vertex(matrix, x + width, y + 1, z)
                .uv(u1, v1)
                .color(r, g, b, a)
                .endVertex();

        builder.vertex(matrix, x + width, y, z)
                .uv(u1, v0)
                .color(r, g, b, a)
                .endVertex();

        builder.vertex(matrix, x, y, z)
                .uv(u0, v0)
                .color(r, g, b, a)
                .endVertex();

        BufferUploader.drawWithShader(builder.end());
    }
    @Inject(
            method = "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At("TAIL")
    )
    private void bigBuckets$renderBigBucketBar(
            Font font,
            ItemStack stack,
            int x,
            int y,
            String amount,
            CallbackInfo ci
    ) {
        if (!(stack.getItem() instanceof BigBucketItem item)) {
            return;
        }

        if (!item.shouldShowBar(stack)) {
            return;
        }

        var data = item.getBucketStorageData(stack);

        float progress = (float)data.fullness() / (float)data.capacity();
        int barWidth = Math.max(0, Math.min(13, (int)(13 * progress)));

        GuiGraphics gui = (GuiGraphics)(Object)this;

        gui.fill(
                x + 2,
                y + 13,
                x + 15,
                y + 15,
                0xFF000000
        );

        var fluidRenderer = ClientServices.FLUIDS;

        TextureAtlasSprite sprite =
                fluidRenderer.getSprite(
                        data.fluid(),
                        data.data().orElse(null)
                );

        int color =
                fluidRenderer.getColor(
                        data.fluid(),
                        data.data().orElse(null)
                );

        bigBuckets$drawFluidBar(
                gui,
                sprite,
                color,
                x + 2,
                y + 13,
                barWidth
        );
    }
}