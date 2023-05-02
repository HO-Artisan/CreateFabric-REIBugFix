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

import com.simibubi.create.compat.rei.category.BasinCategory;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.contraptions.processing.BasinRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.utility.Pair;
import ho.artisan.createreibugfix.utils.REICreateUtils;
import ho.artisan.createreibugfix.utils.StringUtils;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Restriction(conflict = @Condition(StringUtils.createsdelight_modid))
@Mixin(BasinCategory.class)
@Environment(EnvType.CLIENT)
public class BasinCategoryMixin {
    private final ThreadLocal<FluidIngredient> fluidIngredient = new ThreadLocal<>();

    @Inject(method = "addWidgets", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/common/util/EntryIngredients;of(Ldev/architectury/fluid/FluidStack;)Lme/shedaniel/rei/api/common/entry/EntryIngredient;", ordinal = 0, remap = false), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void captureVars(CreateDisplay<BasinRecipe> display, List<Widget> widgets, Point origin, CallbackInfo ci, BasinRecipe recipe, DefaultedList<FluidIngredient> fluidIngredients, List<Pair<Ingredient, MutableInt>> ingredients, List<ItemStack> itemOutputs, DefaultedList<io.github.fabricators_of_create.porting_lib.util.FluidStack> fluidOutputs, int size, int xOffset, int yOffset, int i, int j) {
        fluidIngredient.set(fluidIngredients.get(j));
    }

    @Redirect(method = "addWidgets", at = @At(value = "INVOKE", target = "Lme/shedaniel/rei/api/common/util/EntryIngredients;of(Ldev/architectury/fluid/FluidStack;)Lme/shedaniel/rei/api/common/entry/EntryIngredient;", ordinal = 0, remap = false), remap = false)
    private EntryIngredient fixFluidIngredient(dev.architectury.fluid.FluidStack stack) {
        return REICreateUtils.ingredientOf(fluidIngredient.get());
    }
}
