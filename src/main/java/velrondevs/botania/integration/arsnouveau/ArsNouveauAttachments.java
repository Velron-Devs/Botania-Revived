package velrondevs.botania.integration.arsnouveau;

import com.mojang.serialization.Codec;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import velrondevs.botania.common.lib.LibMisc;

import java.util.function.Supplier;

final class ArsNouveauAttachments {
	static final DeferredRegister<AttachmentType<?>> REGISTER = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LibMisc.MOD_ID);

	static final Supplier<AttachmentType<Double>> MANA_REMAINDER = REGISTER.register("ars_mana_remainder",
			() -> AttachmentType.builder(() -> 0.0).serialize(Codec.DOUBLE).build());

	private ArsNouveauAttachments() {}
}
