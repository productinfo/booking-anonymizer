package anonymization

data class NameDeIdentifier(
    private val delegate: EntityDeIdentifierDelegate
) : DeIdentifier by delegate
