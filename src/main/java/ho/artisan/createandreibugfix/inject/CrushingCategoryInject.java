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

package ho.artisan.createandreibugfix.inject;

import com.simibubi.create.compat.rei.category.CreateRecipeCategory;
import com.simibubi.create.compat.rei.category.CrushingCategory;
import com.simibubi.create.compat.rei.display.CreateDisplay;
import com.simibubi.create.content.contraptions.components.crusher.AbstractCrushingRecipe;
import ho.artisan.createandreibugfix.api.REICreates;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.util.collection.DefaultedList;

import java.util.List;

public interface CrushingCategoryInject {
    static void drawFluidSlot(CrushingCategory category, CreateDisplay<AbstractCrushingRecipe> display, Rectangle bounds, Point origin, List<Widget> widgets) {
        DefaultedList<FluidStack> fluidResults = display.getRecipe().getFluidResults();
        if (fluidResults.isEmpty()) return;
        int size = display.getRecipe().getRollableResults().size() + 1;
        int outputIndex = size - 1;
        int offset = -size * 19 / 2;
        widgets.add(REICreates.slotOf(new Point((origin.x + category.getDisplayWidth(display) / 2 + offset + 19 * outputIndex) + 1, origin.y + 78 + 1), List.of(EntryStacks.of(CreateRecipeCategory.convertToREIFluid(fluidResults.get(0)))))
                .markOutput());
    }
}
