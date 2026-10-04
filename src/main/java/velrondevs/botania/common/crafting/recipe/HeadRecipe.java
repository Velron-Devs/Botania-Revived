package velrondevs.botania.common.crafting.recipe;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.response.MinecraftTexturesPayload;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.RunicAltarRecipe;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeadRecipe extends RunicAltarRecipe {
	private static final Pattern PROFILE_PATTERN = Pattern.compile(
			"(?<base64>[A-Za-z0-9+/]{100,}={0,2})" +
					"|(?<url>(?=\\S{50,})https?://(?!bugs|education|feedback)\\w+\\.(?:minecraft\\.net|mojang\\.com)/\\S+)" +
					"|(?<hash>[0-9a-f]{64})");
	public static final String TEXTURE_URL_BASE = "https://textures.minecraft.net/texture/";
	private static final Supplier<Gson> gson = Suppliers.memoize(() -> new GsonBuilder()
			.registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create());
	private static final GameProfile PROFILE_VALID_RESULT = new GameProfile(Util.NIL_UUID, "valid");
	private static final LoadingCache<String, UUID> GENERATED_UUID_CACHE = CacheBuilder.newBuilder()
			.expireAfterAccess(1, TimeUnit.MINUTES).build(
					new CacheLoader<>() {
						@NotNull
						@Override
						public UUID load(@NotNull String key) {
							return UUID.nameUUIDFromBytes(key.getBytes(StandardCharsets.UTF_8));
						}
					});

	public HeadRecipe(ItemStack output, int mana, Ingredient... inputs) {
		super(output, mana, inputs);
	}

	@Override
	public boolean matches(RecipeInput inv, @NotNull Level world) {
		boolean matches = super.matches(inv, world);
		boolean foundName = false;

		if (matches) {
			for (int i = 0; i < inv.size(); i++) {
				ItemStack stack = inv.getItem(i);
				if (stack.isEmpty()) {
					break;
				}

				if (stack.is(Items.NAME_TAG)) {
					if (foundName || !stack.has(DataComponents.CUSTOM_NAME)
							|| !StringUtil.isValidPlayerName(stack.getHoverName().getString())) {
						return false;
					}
					foundName = true;
				} else if (stack.is(Items.WRITTEN_BOOK)) {
					if (foundName || parseProfileFromBook(stack, true) == null) {
						return false;
					}
					foundName = true;
				}
			}
		}

		return matches;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull RecipeInput inv, @NotNull HolderLookup.Provider registries) {
		ItemStack stack = getResultItem(registries).copy();
		for (int i = 0; i < inv.size(); i++) {
			ItemStack ingr = inv.getItem(i);
			if (ingr.is(Items.NAME_TAG)) {
				stack.set(DataComponents.PROFILE, new ResolvableProfile(Optional.of(ingr.getHoverName().getString()), Optional.empty(), new PropertyMap()));
				break;
			}
			if (ingr.is(Items.WRITTEN_BOOK)) {
				GameProfile profile = parseProfileFromBook(ingr, false);
				if (profile != null) {
					stack.set(DataComponents.PROFILE, new ResolvableProfile(profile));
				}
				break;
			}
		}
		return stack;
	}

	private GameProfile parseProfileFromBook(ItemStack stack, boolean validateOnly) {
		WrittenBookContent content = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
		if (content == null) {
			return null;
		}
		String name = content.title().raw();
		if (name.isBlank() || !StringUtil.isValidPlayerName(name)) {
			return null;
		}

		List<Filterable<Component>> pages = content.pages();

		int maxPages = Math.min(2, pages.size());
		for (int i = 0; i < maxPages; ++i) {
			String pageText = pages.get(i).raw().getString();

			Matcher matcher = PROFILE_PATTERN.matcher(pageText);
			if (matcher.matches()) {

				String textureUrl;
				String hash, base64, url;
				if ((hash = matcher.group("hash")) != null) {

					textureUrl = TEXTURE_URL_BASE + hash;
				} else if ((url = matcher.group("url")) != null) {

					try {

						URL validUrl = new URL(url);
						textureUrl = validUrl.toString();
					} catch (Exception e) {
						return null;
					}
				} else if ((base64 = matcher.group("base64")) != null) {

					try {
						final String json = new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
						MinecraftTexturesPayload result = gson.get().fromJson(json, MinecraftTexturesPayload.class);
						MinecraftProfileTexture skinTexture = result.textures().get(MinecraftProfileTexture.Type.SKIN);
						String skinTextureUrl = skinTexture.getUrl();
						if (!PROFILE_PATTERN.matcher(skinTextureUrl).matches()) {
							return null;
						}
						URL validUrl = new URL(skinTextureUrl);
						textureUrl = validUrl.toString();
					} catch (Exception e) {
						return null;
					}
				} else {
					return null;
				}
				if (validateOnly) {
					return PROFILE_VALID_RESULT;
				}

				String profileTextureJson = "{textures:{SKIN:{url:\"%s\"}}}".formatted(textureUrl);
				String propertyBase64 = Base64.getEncoder().encodeToString(profileTextureJson.getBytes(StandardCharsets.UTF_8));
				var profile = new GameProfile(GENERATED_UUID_CACHE.getUnchecked(propertyBase64), name);
				profile.getProperties().put("textures", new Property("textures", propertyBase64));
				return profile;
			}
		}
		return null;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.RUNE_HEAD_SERIALIZER;
	}

	public static class Serializer extends RunicAltarRecipe.Serializer<HeadRecipe> {
		public Serializer() {
			super(HeadRecipe::new);
		}
	}

}
