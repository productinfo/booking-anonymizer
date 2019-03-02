package anonymization

class AmountDeIdentifierImpl(private val delegate: EntityDeIdentifierDelegate) : DeIdentifier by delegate {

    override fun getDeIdentifiedText(text: String?): String {
        if (text.isNullOrBlank()) {
            return "NaN"
        }
        val amount = getAmountFromText(text)
        return when (amount) {
            Double.NaN -> "NaN"
            else -> getDeIdentifiedAmount(amount)
        }
    }

    private fun getDeIdentifiedAmount(amount: Double?): String {
        return when (amount) {
            0.0, null -> "0"
            else -> {
                val amountSign = if (amount < 0) "-" else ""
                val unsignedAmount = when {
                    amount < 0 -> amount * -1.0
                    else -> amount
                }

                var binCounter = 1
                while (Math.pow(2.0, binCounter.toDouble()) < unsignedAmount && binCounter < MAXIMUM_BIN_COUNT) {
                    binCounter++
                }
                amountSign + binCounter
            }
        }
    }

    private fun getAmountFromText(text: String): Double {
        return try {
            text.trim(' ').toDouble()
        } catch (e: NumberFormatException) {
            Double.NaN
        }
    }

    companion object {
        const val MAXIMUM_BIN_COUNT = 20
    }
}
