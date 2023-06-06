package jasyncapicmp.cmp;

public enum ApiCompatibilityChange {
	CHANNEL_ADDED(true),
	CHANNEL_REMOVED(false),
	CHANNEL_ITEM_SUBSCRIBE_OPERATION_ID_CHANGED(false),
	CHANNEL_ITEM_SUBSCRIBE_MESSAGE_CONTENT_TYPE_CHANGED(false);

	private final boolean backwardCompatible;

	ApiCompatibilityChange(boolean backwardCompatible) {
		this.backwardCompatible = backwardCompatible;
	}

	public boolean isBackwardCompatible() {
		return backwardCompatible;
	}
}
