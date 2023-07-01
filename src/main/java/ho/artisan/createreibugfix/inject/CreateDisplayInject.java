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

package ho.artisan.createreibugfix.inject;

import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.recipe.Recipe;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import ho.artisan.createreibugfix.utils.REICreateUtils;

import java.util.LinkedList;
import java.util.List;

@ApiStatus.Internal
public final class CreateDisplayInject {
    /**
     Add the multi-item output and fluid input and output of {@link ProcessingRecipe} and {@link SequencedAssemblyRecipe} to the input and output of {@link me.shedaniel.rei.api.common.display.Display}.
     @param args {@link CreateDisplay#CreateDisplay(Recipe, CategoryIdentifier, List, List)}
     @param recipe0 {@link CreateDisplay#CreateDisplay(Recipe, CategoryIdentifier)} of {@code recipe}
     @param id {@link CreateDisplay#CreateDisplay(Recipe, CategoryIdentifier)} of parameter {@code id}
     */
    public static void improveStupidDisplay(Args args, Recipe<?> recipe0, CategoryIdentifier<CreateDisplay<Recipe<?>>> id) {
        if (recipe0 instanceof ProcessingRecipe<?> recipe) {
            args.set(2, REICreateUtils.ingredientsOf(recipe));
            args.set(3, REICreateUtils.resultsOf(recipe));
        } else if (recipe0 instanceof SequencedAssemblyRecipe recipe) {
            List<EntryIngredient> inputs = new LinkedList<>(EntryIngredients.ofIngredients(recipe.getIngredients()));
            List<EntryIngredient> outputs = new LinkedList<>();
            for (SequencedRecipe<?> sequencedRecipe : recipe.getSequence()) {
                inputs.addAll(REICreateUtils.ingredientsOf(sequencedRecipe));
                outputs.addAll(REICreateUtils.resultsOf(sequencedRecipe));
            }
            for (ProcessingOutput output : SequencedAssemblyRecipeInject.Interface.getResultPool(recipe)) {
                outputs.add(EntryIngredients.of(output.getStack()));
            }
            args.set(2, inputs);
            args.set(3, outputs);
        }
    }

    private CreateDisplayInject() {

    }
}
