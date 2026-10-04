package velrondevs.botania.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.PrimedTnt;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.internal_caps.*;

import java.util.function.BiConsumer;
import java.util.function.Function;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaAttachments {
	public static final AttachmentType<EthicalComponent> TNT_ETHICAL = make(holder -> new EthicalComponent((PrimedTnt) holder));
	public static final AttachmentType<SpectralRailComponent> GHOST_RAIL = make(holder -> new SpectralRailComponent());
	public static final AttachmentType<ItemFlagsComponent> INTERNAL_ITEM = make(holder -> new ItemFlagsComponent());
	public static final AttachmentType<KeptItemsComponent> KEPT_ITEMS = make(holder -> new KeptItemsComponent());
	public static final AttachmentType<LooniumComponent> LOONIUM_DROP = make(holder -> new LooniumComponent());
	public static final AttachmentType<NarslimmusComponent> NARSLIMMUS = make(holder -> new NarslimmusComponent());
	public static final AttachmentType<TigerseyeComponent> TIGERSEYE = make(holder -> new TigerseyeComponent());

	private static <T extends SerializableComponent> AttachmentType<T> make(Function<IAttachmentHolder, T> factory) {
		return AttachmentType.builder(factory).serialize(new IAttachmentSerializer<CompoundTag, T>() {
			@Override
			public T read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider registries) {
				T component = factory.apply(holder);
				component.deserializeNBT(registries, tag);
				return component;
			}

			@Nullable
			@Override
			public CompoundTag write(T attachment, HolderLookup.Provider registries) {
				return attachment.serializeNBT(registries);
			}
		}).build();
	}

	public static void registerAttachments(BiConsumer<AttachmentType<?>, ResourceLocation> r) {
		r.accept(TNT_ETHICAL, prefix("tnt_ethical"));
		r.accept(GHOST_RAIL, prefix("ghost_rail"));
		r.accept(INTERNAL_ITEM, prefix("iitem"));
		r.accept(KEPT_ITEMS, prefix("kept_items"));
		r.accept(LOONIUM_DROP, prefix("loonium_drop"));
		r.accept(NARSLIMMUS, prefix("narslimmus"));
		r.accept(TIGERSEYE, prefix("tigerseye_pacified"));
	}

	private BotaniaAttachments() {}
}
