package velrondevs.botania.integration.kubejs;

import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.type.TypeInfo;
import dev.latvian.mods.rhino.util.HideFromJS;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.common.crafting.BotanicalBreweryRecipe;
import velrondevs.botania.common.crafting.ElvenTradeRecipe;
import velrondevs.botania.common.crafting.ManaInfusionRecipe;
import velrondevs.botania.common.crafting.PetalsRecipe;
import velrondevs.botania.common.crafting.PureDaisyRecipe;
import velrondevs.botania.common.crafting.RecipeTerraPlate;
import velrondevs.botania.common.crafting.RunicAltarRecipe;
import velrondevs.botania.common.crafting.StateIngredientHelper;
import velrondevs.botania.common.lib.BotaniaTags;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class BotaniaKubeRecipe<T extends BotaniaKubeRecipe<T>> {
	private static final TypeInfo ITEM_STACK = TypeInfo.of(ItemStack.class);
	private static final TypeInfo ITEM_STACKS = ITEM_STACK.asArray();
	private static final TypeInfo SIZED_INGREDIENTS = TypeInfo.of(SizedIngredient.class).asArray();
	private static final TypeInfo BLOCK_STATE = TypeInfo.of(BlockState.class);

	private final String type;
	private final SourceLine sourceLine;
	@Nullable
	private ResourceLocation id;
	private boolean failed;

	protected BotaniaKubeRecipe(Context cx, String type) {
		this.type = type;
		this.sourceLine = SourceLine.of(cx);
	}

	@SuppressWarnings("unchecked")
	protected T self() {
		return (T) this;
	}

	public T id(Object id) {
		ResourceLocation key = guard("id", () -> ID.kjs(id));
		if (key != null) {
			this.id = key;
		}
		return self();
	}

	@HideFromJS
	@Nullable
	public ResourceLocation getId() {
		return id;
	}

	@HideFromJS
	public boolean isFailed() {
		return failed;
	}

	@HideFromJS
	public String getType() {
		return type;
	}

	@HideFromJS
	public String describe() {
		return "Botania " + type.replace('_', ' ') + " recipe" + (id == null ? "" : " " + id);
	}

	@HideFromJS
	public void error(String message, Throwable error) {
		ScriptType.SERVER.console.error("[Botania] " + message, sourceLine, error, null);
	}

	@HideFromJS
	public abstract String autoIdPath();

	@HideFromJS
	public abstract Recipe<?> build();

	protected <V> V guard(String what, Supplier<V> supplier) {
		try {
			return supplier.get();
		} catch (Exception e) {
			failed = true;
			error("Invalid " + what + " in " + describe(), e);
			return null;
		}
	}

	protected static IllegalStateException missing(String what) {
		return new IllegalStateException("Missing " + what);
	}

	protected static String path(ItemStack stack) {
		return BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
	}

	protected static ItemStack item(Context cx, Object o) {
		ItemStack stack = (ItemStack) cx.jsToJava(o, ITEM_STACK);
		if (stack == null || stack.isEmpty()) {
			throw new IllegalArgumentException("Empty item " + o);
		}
		return stack;
	}

	protected static ItemStack[] items(Context cx, Object o) {
		ItemStack[] stacks = (ItemStack[]) cx.jsToJava(o, ITEM_STACKS);
		if (stacks == null || stacks.length == 0) {
			throw new IllegalArgumentException("No items");
		}
		for (ItemStack stack : stacks) {
			if (stack == null || stack.isEmpty()) {
				throw new IllegalArgumentException("Empty item in " + o);
			}
		}
		return stacks;
	}

	protected static Ingredient[] ingredients(Context cx, Object o) {
		SizedIngredient[] sized = (SizedIngredient[]) cx.jsToJava(o, SIZED_INGREDIENTS);
		if (sized == null || sized.length == 0) {
			throw new IllegalArgumentException("No ingredients");
		}
		List<Ingredient> list = new ArrayList<>();
		for (SizedIngredient s : sized) {
			if (s == null || s.ingredient().isEmpty()) {
				throw new IllegalArgumentException("Empty ingredient in " + o);
			}
			for (int i = 0; i < Math.max(1, s.count()); i++) {
				list.add(s.ingredient());
			}
		}
		return list.toArray(Ingredient[]::new);
	}

	protected static Ingredient ingredient(Context cx, Object o) {
		Ingredient[] list = ingredients(cx, o);
		if (list.length != 1) {
			throw new IllegalArgumentException("Expected a single ingredient with count 1, got " + list.length);
		}
		return list[0];
	}

	protected static BlockState blockState(Context cx, Object o) {
		BlockState state = (BlockState) cx.jsToJava(o, BLOCK_STATE);
		if (state == null || state.isAir()) {
			throw new IllegalArgumentException("Empty block state " + o);
		}
		return state;
	}

	protected static StateIngredient stateIngredient(Object o) {
		if (o instanceof StateIngredient s) {
			return s;
		}
		if (o instanceof BlockState state) {
			return StateIngredientHelper.of(state);
		}
		if (o instanceof Block block) {
			return StateIngredientHelper.of(block);
		}
		if (o instanceof Iterable<?> iterable) {
			List<StateIngredient> list = new ArrayList<>();
			for (Object e : iterable) {
				list.add(stateIngredient(e));
			}
			return compound(list);
		}
		if (o != null && o.getClass().isArray()) {
			List<StateIngredient> list = new ArrayList<>();
			for (int i = 0; i < Array.getLength(o); i++) {
				list.add(stateIngredient(Array.get(o, i)));
			}
			return compound(list);
		}
		if (o instanceof CharSequence cs) {
			String s = cs.toString().trim();
			if (s.startsWith("#")) {
				return StateIngredientHelper.of(ResourceLocation.parse(s.substring(1)));
			}
			if (s.contains("[")) {
				try {
					return StateIngredientHelper.of(BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), s, false).blockState());
				} catch (Exception e) {
					throw new IllegalArgumentException("Invalid block state " + s, e);
				}
			}
			ResourceLocation key = ResourceLocation.parse(s);
			Block block = BuiltInRegistries.BLOCK.getOptional(key).orElse(Blocks.AIR);
			if (block == Blocks.AIR) {
				throw new IllegalArgumentException("Unknown block " + key);
			}
			return StateIngredientHelper.of(block);
		}
		throw new IllegalArgumentException("Cannot convert " + o + " to a block state ingredient");
	}

	private static StateIngredient compound(List<StateIngredient> list) {
		if (list.isEmpty()) {
			throw new IllegalArgumentException("Empty block state ingredient list");
		}
		return list.size() == 1 ? list.get(0) : StateIngredientHelper.compound(list);
	}

	public static class RunicAltar extends BotaniaKubeRecipe<RunicAltar> {
		private final ItemStack output;
		private final Ingredient[] inputs;
		private int mana = -1;

		public RunicAltar(Context cx, Object output, Object inputs) {
			super(cx, "runic_altar");
			this.output = guard("output", () -> item(cx, output));
			this.inputs = guard("inputs", () -> ingredients(cx, inputs));
		}

		public RunicAltar mana(int mana) {
			this.mana = mana;
			return this;
		}

		@Override
		public String autoIdPath() {
			return path(output);
		}

		@Override
		public Recipe<?> build() {
			if (mana < 0) {
				throw missing("mana, call .mana(n)");
			}
			return new RunicAltarRecipe(output, mana, inputs);
		}
	}

	public static class PetalApothecary extends BotaniaKubeRecipe<PetalApothecary> {
		private final ItemStack output;
		private final Ingredient[] inputs;
		private Ingredient reagent;

		public PetalApothecary(Context cx, Object output, Object inputs) {
			super(cx, "petal_apothecary");
			this.output = guard("output", () -> item(cx, output));
			this.inputs = guard("inputs", () -> ingredients(cx, inputs));
		}

		public PetalApothecary reagent(Context cx, Object reagent) {
			Ingredient value = guard("reagent", () -> ingredient(cx, reagent));
			if (value != null) {
				this.reagent = value;
			}
			return this;
		}

		@Override
		public String autoIdPath() {
			return path(output);
		}

		@Override
		public Recipe<?> build() {
			Ingredient r = reagent == null ? Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT) : reagent;
			return new PetalsRecipe(output, r, inputs);
		}
	}

	public static class ManaInfusion extends BotaniaKubeRecipe<ManaInfusion> {
		private final ItemStack output;
		private final Ingredient input;
		private int mana = -1;
		@Nullable
		private StateIngredient catalyst;
		@Nullable
		private String group;

		public ManaInfusion(Context cx, Object output, Object input) {
			super(cx, "mana_infusion");
			this.output = guard("output", () -> item(cx, output));
			this.input = guard("input", () -> ingredient(cx, input));
		}

		public ManaInfusion mana(int mana) {
			this.mana = mana;
			return this;
		}

		public ManaInfusion catalyst(Object catalyst) {
			StateIngredient value = guard("catalyst", () -> stateIngredient(catalyst));
			if (value != null) {
				this.catalyst = value;
			}
			return this;
		}

		public ManaInfusion group(String group) {
			this.group = group;
			return this;
		}

		@Override
		public String autoIdPath() {
			return path(output);
		}

		@Override
		public Recipe<?> build() {
			if (mana < 0) {
				throw missing("mana, call .mana(n)");
			}
			return new ManaInfusionRecipe(output, input, mana, group, catalyst);
		}
	}

	public static class ElvenTrade extends BotaniaKubeRecipe<ElvenTrade> {
		private final ItemStack[] outputs;
		private final Ingredient[] inputs;

		public ElvenTrade(Context cx, Object outputs, Object inputs) {
			super(cx, "elven_trade");
			this.outputs = guard("outputs", () -> items(cx, outputs));
			this.inputs = guard("inputs", () -> ingredients(cx, inputs));
		}

		@Override
		public String autoIdPath() {
			return path(outputs[0]);
		}

		@Override
		public Recipe<?> build() {
			return new ElvenTradeRecipe(outputs, inputs);
		}
	}

	public static class TerraPlate extends BotaniaKubeRecipe<TerraPlate> {
		private final ItemStack output;
		private final Ingredient[] inputs;
		private int mana = -1;

		public TerraPlate(Context cx, Object output, Object inputs) {
			super(cx, "terra_plate");
			this.output = guard("output", () -> item(cx, output));
			this.inputs = guard("inputs", () -> ingredients(cx, inputs));
		}

		public TerraPlate mana(int mana) {
			this.mana = mana;
			return this;
		}

		@Override
		public String autoIdPath() {
			return path(output);
		}

		@Override
		public Recipe<?> build() {
			if (mana < 0) {
				throw missing("mana, call .mana(n)");
			}
			return new RecipeTerraPlate(mana, NonNullList.of(Ingredient.EMPTY, inputs), output);
		}
	}

	public static class BrewRecipe extends BotaniaKubeRecipe<BrewRecipe> {
		private final ResourceLocation brewId;
		private final Brew brew;
		private final Ingredient[] inputs;

		public BrewRecipe(Context cx, Object brew, Object inputs) {
			super(cx, "brew");
			this.brewId = guard("brew", () -> ID.mc(brew));
			this.brew = brewId == null ? null : guard("brew", () -> {
				var registry = BotaniaAPI.instance().getBrewRegistry();
				if (registry == null || !registry.containsKey(brewId)) {
					throw new IllegalArgumentException("Unknown brew " + brewId);
				}
				return registry.get(brewId);
			});
			this.inputs = guard("inputs", () -> ingredients(cx, inputs));
		}

		@Override
		public String autoIdPath() {
			return brewId.getPath();
		}

		@Override
		public Recipe<?> build() {
			return new BotanicalBreweryRecipe(brew, inputs);
		}
	}

	public static class PureDaisy extends BotaniaKubeRecipe<PureDaisy> {
		private final StateIngredient input;
		private final BlockState output;
		private int time = PureDaisyRecipe.DEFAULT_TIME;

		public PureDaisy(Context cx, Object input, Object output) {
			super(cx, "pure_daisy");
			this.input = guard("input", () -> stateIngredient(input));
			this.output = guard("output", () -> blockState(cx, output));
		}

		public PureDaisy time(int time) {
			this.time = time;
			return this;
		}

		@Override
		public String autoIdPath() {
			return BuiltInRegistries.BLOCK.getKey(output.getBlock()).getPath();
		}

		@Override
		public Recipe<?> build() {
			if (time < 0) {
				throw new IllegalStateException("Time must not be negative");
			}
			return new PureDaisyRecipe(input, output, time, Optional.empty());
		}
	}
}
