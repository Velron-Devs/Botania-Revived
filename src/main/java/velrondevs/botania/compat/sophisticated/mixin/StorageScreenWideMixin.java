package velrondevs.botania.compat.sophisticated.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageBackgroundProperties;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.compat.sophisticated.client.WideStorageLayout;

@Mixin(targets = "net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase", remap = false)
public abstract class StorageScreenWideMixin extends AbstractContainerScreen {
	private static final int HEIGHT_WITHOUT_STORAGE_SLOTS = 114;

	@Shadow
	protected StorageBackgroundProperties storageBackgroundProperties;

	@Shadow
	public abstract int getSlotsOnLine();

	private StorageScreenWideMixin(AbstractContainerMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@ModifyExpressionValue(method = "updateDimensionsAndSlotPositions", remap = false, at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, remap = false,
			target = "Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageBackgroundProperties;REGULAR_12_SLOT:Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageBackgroundProperties;"))
	private StorageBackgroundProperties botania$regular(StorageBackgroundProperties original) {
		return WideStorageLayout.applies((StorageContainerMenuBase<?>) getMenu()) ? WideStorageLayout.REGULAR : original;
	}

	@ModifyExpressionValue(method = "updateDimensionsAndSlotPositions", remap = false, at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, remap = false,
			target = "Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageBackgroundProperties;WIDER_12_SLOT:Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageBackgroundProperties;"))
	private StorageBackgroundProperties botania$wider(StorageBackgroundProperties original) {
		return WideStorageLayout.applies((StorageContainerMenuBase<?>) getMenu()) ? WideStorageLayout.WIDER : original;
	}

	@ModifyExpressionValue(method = {"updateDimensionsAndSlotPositions", "updateInventoryScrollPanel", "getNumberOfVisibleRows", "initUpgradeInventoryControls"},
			remap = false, at = @At(value = "INVOKE", remap = false,
					target = "Lnet/p3pp3rf1y/sophisticatedcore/common/gui/StorageContainerMenuBase;getNumberOfRows()I"))
	private int botania$rows(int original) {
		StorageContainerMenuBase<?> menu = (StorageContainerMenuBase<?>) getMenu();
		return WideStorageLayout.applies(menu) ? WideStorageLayout.rows(menu) : original;
	}

	@Inject(method = "drawInventoryBg", at = @At("HEAD"), cancellable = true, remap = false)
	private void botania$drawBackground(GuiGraphics graphics, int x, int y, ResourceLocation texture, CallbackInfo ci) {
		if (WideStorageLayout.drawBackground(graphics, x, y, imageWidth, imageHeight - HEIGHT_WITHOUT_STORAGE_SLOTS, texture)) {
			ci.cancel();
		}
	}

	@Inject(method = "drawSlotBg(Lnet/minecraft/client/gui/GuiGraphics;III)V", at = @At("HEAD"), cancellable = true, remap = false)
	private void botania$drawSlots(GuiGraphics graphics, int x, int y, int visibleSlotsCount, CallbackInfo ci) {
		if (WideStorageLayout.isWide(storageBackgroundProperties)) {
			WideStorageLayout.drawSlots(graphics, x, y, getSlotsOnLine(), visibleSlotsCount);
			ci.cancel();
		}
	}
}
