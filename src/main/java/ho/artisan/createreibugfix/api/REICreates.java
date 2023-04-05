/*
 * This file is licensed under the MIT License, part of Phoupraw's Common.
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

package ho.artisan.createreibugfix.api;

import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Pair;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class REICreates {
    @Contract(pure = true)
    public static EntryIngredient ingredientOf(@NotNull FluidIngredient fluidIngredient) {
        return EntryIngredients.of(VanillaEntryTypes.FLUID, CreateRecipeCategory.convertToREIFluids(fluidIngredient.getMatchingFluidStacks()));
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> ingredientsOf(@NotNull ProcessingRecipe<?> recipe) {
        return ingredientsOf(recipe.getIngredients(), recipe.getFluidIngredients());
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> ingredientsOf(@NotNull SequencedRecipe<?> recipe0) {
        ProcessingRecipe<?> recipe = recipe0.getRecipe();
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.addAll(recipe.getIngredients());
        ingredients.remove(0);
        return ingredientsOf(ingredients, recipe.getFluidIngredients());
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> ingredientsOf(@NotNull DefaultedList<Ingredient> ingredients, @NotNull DefaultedList<FluidIngredient> fluidIngredients) {
        List<EntryIngredient> list = new LinkedList<>();
        for (Pair<Ingredient, MutableInt> condensed : ItemHelper.condenseIngredients(ingredients)) {
            Collection<ItemConvertible> items = new LinkedList<>();
            for (ItemStack matchingStack : (condensed.getFirst()).getMatchingStacks()) {
                items.add(matchingStack.getItem());
            }
            list.add(EntryIngredients.ofItems(items, (condensed.getSecond()).getValue()));
        }
        Iterator<FluidIngredient> it = fluidIngredients.iterator();
        while (it.hasNext()) {
            list.add(ingredientOf(it.next()));
        }
        return list;
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> resultsOf(@NotNull ProcessingRecipe<?> recipe) {
        return resultsOf(recipe.getRollableResults(), recipe.getFluidResults());
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> resultsOf(@NotNull SequencedRecipe<?> recipe0) {
        ProcessingRecipe<?> recipe = recipe0.getRecipe();
        LinkedList<ProcessingOutput> rollableResults = new LinkedList<>(recipe.getRollableResults());
        rollableResults.remove(0);
        return resultsOf(rollableResults, recipe.getFluidResults());
    }

    @Contract(pure = true)
    @NotNull
    public static List<EntryIngredient> resultsOf(@NotNull List<ProcessingOutput> rollableResults, @NotNull DefaultedList<FluidStack> fluidResults) {
        List<EntryIngredient> list = new LinkedList<>();
        for (ProcessingOutput rollableResult : rollableResults) {
            list.add(EntryIngredients.of(rollableResult.getStack().copy()));
        }
        Iterator<FluidStack> it = fluidResults.iterator();
        while (it.hasNext()) {
            FluidStack fluidResult = it.next();
            list.add(EntryIngredients.of(dev.architectury.fluid.FluidStack.create(fluidResult.getFluid(), fluidResult.getAmount(), fluidResult.getTag())));
        }
        return list;
    }

    public static Slot slotOf(Point pos, Collection<EntryStack<?>> entries) {
        Slot slot = Widgets.createSlot(pos).entries(entries);
        if (entries.iterator().next().getValue() instanceof dev.architectury.fluid.FluidStack) {
            CreateRecipeCategory.setFluidRenderRatio(slot);
            CreateRecipeCategory.setFluidTooltip(slot);
        }
        return slot;
    }

    private REICreates() {
    }
}
