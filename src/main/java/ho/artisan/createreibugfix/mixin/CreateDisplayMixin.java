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

import com.simibubi.create.compat.rei.display.CreateDisplay;

import ho.artisan.createreibugfix.inject.CreateDisplayInject;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;

import net.minecraft.recipe.Recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CreateDisplay.class)
public abstract class CreateDisplayMixin {
    @ModifyArgs(method = "<init>(Lnet/minecraft/recipe/Recipe;Lme/shedaniel/rei/api/common/category/CategoryIdentifier;)V", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/compat/rei/display/CreateDisplay;<init>(Lnet/minecraft/recipe/Recipe;Lme/shedaniel/rei/api/common/category/CategoryIdentifier;Ljava/util/List;Ljava/util/List;)V"))
    private static void improveStupidDisplay(Args args, Recipe<?> recipe, CategoryIdentifier<CreateDisplay<Recipe<?>>> id) {
        CreateDisplayInject.improveStupidDisplay(args, recipe, id);
    }
}
