package velrondevs.botania.client.patchouli.component;

import net.minecraft.client.Minecraft;

import org.joml.Matrix3x2fStack;


import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.crafting.Ingredient;

import velrondevs.botania.xplat.BotaniaConfig;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;

import java.util.List;

public abstract class RotatingItemListComponentBase implements ICustomComponent {
	protected transient List<Ingredient> ingredients;
	protected transient int x, y;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX != -1 ? componentX : 17;
		this.y = componentY;
		this.ingredients = makeIngredients();
	}

	protected abstract List<Ingredient> makeIngredients();

	@Override
	public void extractRenderState(GuiGraphicsExtractor gui, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		int degreePerInput = (int) (360F / ingredients.size());
		int ticksElapsed = context.getTicksInBook();

		float currentDegree = BotaniaConfig.client().lexiconRotatingItems()
				? Minecraft.getInstance().hasShiftDown()
						? ticksElapsed
						: ticksElapsed + pticks
				: 0;

		for (Ingredient input : ingredients) {
			renderIngredientAtAngle(gui, context, currentDegree, input, mouseX, mouseY);

			currentDegree += degreePerInput;
		}
	}

	private void renderIngredientAtAngle(GuiGraphicsExtractor gui, IComponentRenderContext context, float angle, Ingredient ingredient, int mouseX, int mouseY) {
		Matrix3x2fStack ms = gui.pose();
		if (ingredient.isEmpty()) {
			return;
		}

		angle -= 90;
		int radius = 32;
		double xPos = x + Math.cos(angle * Math.PI / 180D) * radius + 32;
		double yPos = y + Math.sin(angle * Math.PI / 180D) * radius + 32;

		ms.pushMatrix();
		ms.translate((float) (xPos - (int) xPos), (float) (yPos - (int) yPos));
		context.renderIngredient(gui, (int) xPos, (int) yPos, mouseX, mouseY, ingredient);
		ms.popMatrix();
	}

}
