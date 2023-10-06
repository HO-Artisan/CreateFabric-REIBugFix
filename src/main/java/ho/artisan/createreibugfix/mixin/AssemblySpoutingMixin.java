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

import com.simibubi.create.compat.rei.category.sequencedAssembly.ReiSequencedAssemblySubCategory;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import ho.artisan.createreibugfix.utils.REICreateUtils;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(ReiSequencedAssemblySubCategory.AssemblySpouting.class)
@Environment(EnvType.CLIENT)
public class AssemblySpoutingMixin {

    @ModifyArg(method = "addFluidIngredients", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;"), remap = false)
    private int fixIndex(int index) {
        return 0;
    }

    @ModifyArgs(method = "addFluidIngredients", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/client/gui/widgets/Slot;entries(Ljava/util/Collection;)Lme/shedaniel/rei/api/client/gui/widgets/Slot;"))
    private void fluidIngredientToREI(Args args, SequencedRecipe<?> recipe, List<Widget> widgets, int x, int index, Point origin) {
        args.set(0, REICreateUtils.ingredientOf(recipe.getRecipe().getFluidIngredients().get(0)));
    }
}
