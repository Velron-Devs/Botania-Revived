package velrondevs.botania.client.integration.rrv;

import cc.cassian.rrv.api.client.RecipeScreenContext;
import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class BotaniaRrvRecipe implements ReliableClientRecipe {
	private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");

	private final BotaniaRrvRecipeType type;
	@Nullable
	private final Identifier id;
	private final Map<Integer, SlotContent> slots = new LinkedHashMap<>();
	private final List<SlotContent> ingredients = new ArrayList<>();
	private final List<SlotContent> results = new ArrayList<>();
	private final Set<Integer> backgrounds = new HashSet<>();
	private final List<Consumer<RecipeScreenContext>> renderers = new ArrayList<>();
	private int sortWeight;
	private String sortGroup = "";

	public BotaniaRrvRecipe(BotaniaRrvRecipeType type, @Nullable Identifier id) {
		this.type = type;
		this.id = id;
	}

	public BotaniaRrvRecipe input(int slot, SlotContent content) {
		SlotContent copy = SlotContent.of(content);
		slots.put(slot, copy);
		ingredients.add(copy);
		return this;
	}

	public BotaniaRrvRecipe inputs(List<Integer> slotIds, List<SlotContent> contents) {
		for (int i = 0; i < slotIds.size() && i < contents.size(); i++) {
			input(slotIds.get(i), contents.get(i));
		}
		return this;
	}

	public BotaniaRrvRecipe ringInputs(List<SlotContent> contents) {
		return inputs(type.layout().placeRing(contents.size()), contents);
	}

	public BotaniaRrvRecipe catalyst(int slot, SlotContent content) {
		slots.put(slot, SlotContent.of(content));
		return this;
	}

	public BotaniaRrvRecipe output(int slot, SlotContent content) {
		SlotContent copy = SlotContent.of(content);
		slots.put(slot, copy);
		results.add(copy);
		return this;
	}

	public BotaniaRrvRecipe background(int slot) {
		backgrounds.add(slot);
		return this;
	}

	public BotaniaRrvRecipe render(Consumer<RecipeScreenContext> renderer) {
		renderers.add(renderer);
		return this;
	}

	public BotaniaRrvRecipe sort(String group, int weight) {
		this.sortGroup = group;
		this.sortWeight = weight;
		return this;
	}

	public String sortGroup() {
		return sortGroup;
	}

	public int sortWeight() {
		return sortWeight;
	}

	@Override
	public ReliableClientRecipeType getType() {
		return type;
	}

	@Override
	public void bindSlots(RecipeViewMenu.SlotFillContext context) {
		for (Map.Entry<Integer, SlotContent> entry : slots.entrySet()) {
			context.bindSlot(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public List<SlotContent> getIngredients() {
		return ingredients;
	}

	@Override
	public List<SlotContent> getResults() {
		return results;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public void renderRecipe(RecipeScreenContext context) {
		for (int slot : backgrounds) {
			int[] pos = type.layout().position(slot);
			context.guiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_SPRITE, pos[0] - 1, pos[1] - 1, 18, 18);
		}
		for (Consumer<RecipeScreenContext> renderer : renderers) {
			renderer.accept(context);
		}
	}
}
