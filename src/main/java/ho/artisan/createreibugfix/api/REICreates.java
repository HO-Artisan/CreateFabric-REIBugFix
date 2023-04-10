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

package ho.artisan.createreibugfix.api;

import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.content.contraptions.itemAssembly.SequencedRecipe;
import com.simibubi.create.content.contraptions.processing.ProcessingOutput;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Pair;
import dev.architectury.fluid.FluidStack;
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
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
/**
 和机械动力有关的REI工具方法。
 @since 1.0.0
 @author Phoupraw
 */
public final class REICreates {
    /**
     @see CreateRecipeCategory#convertToREIFluids(List)
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static EntryIngredient ingredientOf(@NotNull FluidIngredient fluidIngredient) {
        return EntryIngredients.of(VanillaEntryTypes.FLUID, CreateRecipeCategory.convertToREIFluids(fluidIngredient.getMatchingFluidStacks()));
    }

    /**
     @return 包含物品原料和流体原料的列表
     @see EntryIngredients#ofIngredients(List)
     @see #ingredientOf(FluidIngredient)
     @see Recipe#getIngredients()
     @see ProcessingRecipe#getFluidIngredients()
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> ingredientsOf(@NotNull ProcessingRecipe<?> recipe) {
        return ingredientsOf(recipe.getIngredients(), recipe.getFluidIngredients());
    }

    /**
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> ingredientsOf(@NotNull SequencedRecipe<?> recipe0) {
        var recipe = recipe0.getRecipe();
        DefaultedList<Ingredient> ingredients = DefaultedList.of();
        ingredients.addAll(recipe.getIngredients());
        ingredients.remove(0);
        return ingredientsOf(ingredients, recipe.getFluidIngredients());
    }

    /**
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> ingredientsOf(@NotNull DefaultedList<Ingredient> ingredients, @NotNull DefaultedList<FluidIngredient> fluidIngredients) {
        List<EntryIngredient> list = new LinkedList<>();
        for (Pair<Ingredient, MutableInt> condensed : ItemHelper.condenseIngredients(ingredients)) {
            Collection<ItemConvertible> items = new LinkedList<>();
            for (ItemStack matchingStack : condensed.getFirst().getMatchingStacks()) {
                items.add(matchingStack.getItem());
            }
            list.add(EntryIngredients.ofItems(items, condensed.getSecond().getValue()));
        }
        for (FluidIngredient fluidIngredient : fluidIngredients) {
            list.add(ingredientOf(fluidIngredient));
        }
        return list;
    }

    /**
     @return 包含 {@link ProcessingRecipe#getRollableResults()}和{@link ProcessingRecipe#getFluidResults()}。
     @see EntryIngredients#of(ItemStack)
     @see EntryIngredients#of(FluidStack)
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> resultsOf(@NotNull ProcessingRecipe<?> recipe) {
        return resultsOf(recipe.getRollableResults(), recipe.getFluidResults());
    }

    /**
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> resultsOf(@NotNull SequencedRecipe<?> recipe0) {
        var recipe = recipe0.getRecipe();
        var rollableResults = new LinkedList<>(recipe.getRollableResults());
        rollableResults.remove(0);
        return resultsOf(rollableResults, recipe.getFluidResults());
    }

    /**
     @since 1.0.0
     @author Phoupraw
     */
    @Contract(pure = true)
    public static @NotNull List<EntryIngredient> resultsOf(@NotNull List<ProcessingOutput> rollableResults, @NotNull DefaultedList<io.github.fabricators_of_create.porting_lib.util.FluidStack> fluidResults) {
        List<EntryIngredient> list = new LinkedList<>();
        for (ProcessingOutput rollableResult : rollableResults) {
            list.add(EntryIngredients.of(rollableResult.getStack().copy()));
        }
        for (io.github.fabricators_of_create.porting_lib.util.FluidStack fluidResult : fluidResults) {
            list.add(EntryIngredients.of(FluidStack.create(fluidResult.getFluid(), fluidResult.getAmount(), fluidResult.getTag())));
        }
        return list;
    }

    /**
     如果是流体：
     <ul>
     <li>如果不满一桶，则不会渲染完整一格，而是根据数量减少流体格高度，见{@link CreateRecipeCategory#setFluidRenderRatio}。
     <li>工具提示中以mB为单位，见{@link CreateRecipeCategory#setFluidTooltip}。
     </ul>
     如果是物品：与{@link Widgets#createSlot(Point)}完全相同。
     @param pos {@link Widgets#createSlot(Point)}的参数
     @param entries 可以为{@link EntryIngredient}。
     @see #ingredientsOf
     @see #resultsOf
     @since 1.0.0
     @author Phoupraw
     */
    public static Slot slotOf(Point pos, Collection<EntryStack<?>> entries) {
        var slot = Widgets.createSlot(pos).entries(entries);
        if (entries.iterator().next().getValue() instanceof FluidStack) {
            CreateRecipeCategory.setFluidRenderRatio(slot);
            CreateRecipeCategory.setFluidTooltip(slot);
        }
        return slot;
    }

    private REICreates() {

    }
}