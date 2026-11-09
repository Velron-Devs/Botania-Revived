package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageBackgroundProperties;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;

import velrondevs.botania.common.lib.LibMisc;

import java.util.Optional;

public final class WideStorageLayout {
	public static final int COLUMNS = 18;
	private static final int MIN_SLOTS = 82;
	private static final int TEXTURE_WIDTH = 512;
	private static final int TEXTURE_HEIGHT = 256;
	private static final int SLOT = 18;
	private static final int SLOTS_X = 7;
	private static final int SLOTS_Y = 17;
	private static final int SLOT_CHUNK_ROWS = 12;
	private static final int TOP_MAX = 150;
	private static final int BOTTOM = 97;
	private static final int PANEL_WIDTH = COLUMNS * SLOT + 14;

	private static final Identifier BACKGROUND = texture("storage_wide");
	private static final Identifier BACKGROUND_SCROLL = texture("storage_wide_scroll");
	private static final Identifier SLOTS = texture("storage_wide_slots");

	public static final StorageBackgroundProperties REGULAR = new StorageBackgroundProperties(COLUMNS, (PANEL_WIDTH - 177) / 2, BACKGROUND);
	public static final StorageBackgroundProperties WIDER = new StorageBackgroundProperties(COLUMNS, (PANEL_WIDTH + 6 - 177) / 2, BACKGROUND_SCROLL);

	private WideStorageLayout() {}

	private static Identifier texture(String name) {
		return Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, "textures/gui/" + name + ".png");
	}

	public static boolean applies(StorageContainerMenuBase<?> menu) {
		return menu.getNumberOfStorageInventorySlots() >= MIN_SLOTS && isBotania(menu);
	}

	public static int rows(StorageContainerMenuBase<?> menu) {
		int perLine = Math.max(1, COLUMNS - menu.getColumnsTaken());
		return (menu.getNumberOfStorageInventorySlots() + perLine - 1) / perLine;
	}

	public static boolean isWide(StorageBackgroundProperties properties) {
		return properties == REGULAR || properties == WIDER;
	}

	private static boolean isBotania(StorageContainerMenuBase<?> menu) {
		Optional<BlockPos> pos = menu.getBlockPosition();
		Level level = Minecraft.getInstance().level;
		if (pos.isPresent() && level != null) {
			Identifier block = BuiltInRegistries.BLOCK.getKey(level.getBlockState(pos.get()).getBlock());
			if (block.getNamespace().equals(LibMisc.MOD_ID)) {
				return true;
			}
		}
		ItemStack stack = menu.getStorageWrapper().getWrappedStorageStack();
		return !stack.isEmpty() && BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals(LibMisc.MOD_ID);
	}

	public static boolean drawBackground(GuiGraphicsExtractor graphics, int x, int y, int width, int storageHeight, Identifier texture) {
		if (!texture.equals(BACKGROUND) && !texture.equals(BACKGROUND_SCROLL)) {
			return false;
		}
		int top = Math.min(storageHeight / 2, TOP_MAX);
		int stretch = 0;
		graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0, 0, width, SLOTS_Y + top, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		if (storageHeight / 2 > TOP_MAX) {
			stretch = (storageHeight / 2 - TOP_MAX) * 2;
			graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y + SLOTS_Y + top, 0, SLOTS_Y, width, stretch, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		}
		graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y + stretch + SLOTS_Y + top, 0, TEXTURE_HEIGHT - (BOTTOM + top), width, BOTTOM + top, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		return true;
	}

	public static void drawSlots(GuiGraphicsExtractor graphics, int x, int y, int slotsOnLine, int count) {
		if (slotsOnLine <= 0 || count <= 0) {
			return;
		}
		int left = x + SLOTS_X;
		int top = y + SLOTS_Y;
		int fullRows = count / slotsOnLine;
		int rest = count % slotsOnLine;
		int row = 0;
		while (row < fullRows) {
			int chunk = Math.min(SLOT_CHUNK_ROWS, fullRows - row);
			graphics.blit(RenderPipelines.GUI_TEXTURED, SLOTS, left, top + row * SLOT, 0, 0, slotsOnLine * SLOT, chunk * SLOT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
			row += chunk;
		}
		if (rest > 0) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, SLOTS, left, top + fullRows * SLOT, 0, 0, rest * SLOT, SLOT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
		}
	}
}
