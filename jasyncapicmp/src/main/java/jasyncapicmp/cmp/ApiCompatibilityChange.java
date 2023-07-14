package jasyncapicmp.cmp;

/**
 * BACKWARD compatibility means that consumers using the new schema can read data produced with the last schema.
 * FORWARD compatibility means that data produced with a new schema can be read by consumers using the last schema,
 * even though they may not be able to use the full capabilities of the new schema.
 * FULL compatibility means schemas are both backward and forward compatible.
 * (see also <a href="https://docs.confluent.io/platform/current/schema-registry/fundamentals/schema-evolution.html">here</a>)
 */
public enum ApiCompatibilityChange {
	CHANNEL_ADDED(true, true),
	CHANNEL_REMOVED(false, false),
	OPERATION_OPERATION_ID_CHANGED(false, false),
	MESSAGE_MESSAGE_ID_CHANGED(false, false),
	MESSAGE_CONTENT_TYPE_CHANGED(false, false),
	MESSAGE_SCHEMA_FORMAT_CHANGED(false, false),
	SCHEMA_TYPE_CHANGED(false, false),
	SCHEMA_PROPERTY_ADDED(true, true),
	SCHEMA_PROPERTY_REMOVED(true, true),
	SCHEMA_PROPERTY_REQUIRED_ADDED(false, true),
	SCHEMA_PROPERTY_REQUIRED_REMOVED(true, false),
	SCHEMA_MIN_LENGTH_INCREASED(false, true),
	SCHEMA_MIN_LENGTH_DECREASED(true, false),
	SCHEMA_MAX_LENGTH_INCREASED(true, false),
	SCHEMA_MAX_LENGTH_DECREASED(false, true);

	private final boolean backwardCompatible;
	private final boolean forwardCompatible;

	ApiCompatibilityChange(boolean backwardCompatible, boolean forwardCompatible) {
		this.backwardCompatible = backwardCompatible;
		this.forwardCompatible = forwardCompatible;
	}

	public boolean isBackwardCompatible() {
		return backwardCompatible;
	}

	public boolean isForwardCompatible() {
		return forwardCompatible;
	}
}
