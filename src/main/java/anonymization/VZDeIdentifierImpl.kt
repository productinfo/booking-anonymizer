package anonymization

data class VZDeIdentifierImpl(
    private val delegate: EntityDeIdentifierDelegate
) : DeIdentifier by delegate
