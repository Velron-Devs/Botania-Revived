package velrondevs.botania.client.integration.rrv;

import cc.cassian.rrv.api.recipe.ReliableClientRecipeType;
import cc.cassian.rrv.common.recipe.inventory.RecipeViewMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class BotaniaRrvRecipeType implements ReliableClientRecipeType {
	private final Identifier id;
	private final String titleKey;
	private final Supplier<ItemLike> icon;
	private final int width;
	private final int height;
	private final BotaniaRrvLayout layout;
	private final Supplier<List<ItemLike>> workstations;

	public BotaniaRrvRecipeType(Identifier id, String titleKey, Supplier<ItemLike> icon, int width, int height,
			BotaniaRrvLayout layout, Supplier<List<ItemLike>> workstations) {
		this.id = id;
		this.titleKey = titleKey;
		this.icon = icon;
		this.width = width;
		this.height = height;
		this.layout = layout;
		this.workstations = workstations;
	}

	@SafeVarargs
	public static Supplier<List<ItemLike>> stations(Supplier<ItemLike>... items) {
		return () -> Arrays.stream(items).map(Supplier::get).toList();
	}

	public BotaniaRrvLayout layout() {
		return layout;
	}

	@Override
	public Component getDisplayName() {
		return Component.translatable(titleKey);
	}

	@Override
	public int getDisplayWidth() {
		return width;
	}

	@Override
	public int getDisplayHeight() {
		return height;
	}

	@Override
	public Identifier getGuiTexture() {
		return null;
	}

	@Override
	public int getSlotCount() {
		return layout.size();
	}

	@Override
	public void placeSlots(RecipeViewMenu.SlotDefinition definition) {
		definition.setHighlightWithoutContents(false);
		for (int i = 0; i < layout.size(); i++) {
			int[] pos = layout.position(i);
			definition.addItemSlot(i, pos[0], pos[1]);
		}
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(icon.get());
	}

	@Override
	public List<ItemStack> getCraftReferences() {
		List<ItemStack> list = new ArrayList<>();
		for (ItemLike item : workstations.get()) {
			list.add(new ItemStack(item));
		}
		return list;
	}
}
