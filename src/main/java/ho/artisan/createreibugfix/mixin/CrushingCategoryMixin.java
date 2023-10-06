/*
 * This file is licensed under the MIT License, part of Create's Delight.
 * Copyright (c) 2021~2023 Phoupraw
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ho.artisan.createreibugfix.mixin;

import com.simibubi.create.compat.rei.category.CrushingCategory;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;

import ho.artisan.createreibugfix.inject.CrushingCategoryInject;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.objectweb.asm.Opcodes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(CrushingCategory.class)
@Environment(EnvType.CLIENT)
public class CrushingCategoryMixin {
    @ModifyVariable(method = "setupDisplay(Lcom/simibubi/create/compat/rei/display/CreateDisplay;Lme/shedaniel/math/Rectangle;)Ljava/util/List;", at = @At(value = "STORE", opcode = Opcodes.ISTORE), remap = false)
    private int addSize(int size, CreateDisplay<AbstractCrushingRecipe> display, Rectangle bounds) {
        if (display.getRecipe().getFluidResults().isEmpty()) return size;
        return size + 1;
    }

    @Inject(method = "setupDisplay(Lcom/simibubi/create/compat/rei/display/CreateDisplay;Lme/shedaniel/math/Rectangle;)Ljava/util/List;", at = @At(value = "NEW", target = "com/simibubi/create/compat/rei/category/animations/AnimatedCrushingWheels", remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void drawFluidSlot(CreateDisplay<AbstractCrushingRecipe> display, Rectangle bounds, CallbackInfoReturnable<List<Widget>> cir, Point origin, List<Widget> widgets, List<ProcessingOutput> results) {
        CrushingCategoryInject.drawFluidSlot((CrushingCategory) (Object) this, display, bounds, origin, widgets);
    }
}
