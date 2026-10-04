package velrondevs.botania.common.impl.corporea;

import velrondevs.botania.api.corporea.CorporeaHelper;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class DefaultCorporeaMatchers {
	public static void init() {
		CorporeaHelper.instance().registerRequestMatcher(prefix("string"), CorporeaStringMatcher.class, CorporeaStringMatcher::createFromNBT);
		CorporeaHelper.instance().registerRequestMatcher(prefix("item_stack"), CorporeaItemStackMatcher.class, CorporeaItemStackMatcher::createFromNBT);
	}

	private DefaultCorporeaMatchers() {}
}
