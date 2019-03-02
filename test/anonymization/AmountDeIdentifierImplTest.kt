package anonymization

import initialization.DependencyBinder
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(BlockJUnit4ClassRunner::class)
class AmountDeIdentifierImplTest {
    private lateinit var amountDeIdentifier: DeIdentifier

    @Before
    fun setup() {
        amountDeIdentifier = DependencyBinder.amountDeIdentifier
    }

    @Test
    fun `should return NaN on blank string`() {
        val anonymisedText = amountDeIdentifier.getDeIdentifiedText("   ")
        assertEquals("NaN", anonymisedText)
    }

    @Test
    fun `should return NaN on empty string`() {
        val anonymisedText = amountDeIdentifier.getDeIdentifiedText("")
        assertEquals("NaN", anonymisedText)
    }

    @Test
    fun `should return a bucket number from 0 to bucket max`() {
        val testData = listOf(
            "0",
            "2", "-2",
            "8", "-8",
            "16", "-16",
            "32", "-32",
            "64", "-64",
            "128", "-128",
            "256", "-256",
            "512", "-512",
            "1024", "-1024",
            "2048", "-2048",
            "4096", "-4096",
            "8192", "-8192",
            "16384", "-16384",
            "32768", "-32768",
            "65536", "-65536",
            "131072", "-131072",
            "262144", "-262144",
            "524288", "-524288",
            "1048576", "-1048576",
            "2097152", "-2097152",
            "4194304", "-4194304"
        )

        testData.forEach {
            val bucketisedAmount = amountDeIdentifier.getDeIdentifiedText(it)
            assertTrue {
                bucketisedAmount.toDouble() <= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT
                        && bucketisedAmount.toDouble() >= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT * -1
            }
        }
    }

    @Test
    fun `should correctly work with untrimmed input strings`() {
        val testData = listOf(
            "0 ", "-0 ",
            " 2", "- 2", // - is denied
            "  8", "  -8",
            "16  ", "-16  ",
            " 32 ", " -32 ",
            "   64   ", "   -64   ",
            "                             128", "-                             128"
        )

        testData.forEach {
            val bucketisedAmount = amountDeIdentifier.getDeIdentifiedText(it)
            assertTrue {
                bucketisedAmount.toDouble() <= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT
                        && bucketisedAmount.toDouble() >= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT * -1
            }
        }
    }

    @Test
    fun `should work with localised divider signs`() {
        val testData = listOf(
            "1.241",
            "1,241",
            "-1,241",
            "-1.241"
        )

        testData.forEach {
            val bucketisedAmount = amountDeIdentifier.getDeIdentifiedText(it)
            assertTrue {
                bucketisedAmount.toDouble() <= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT
                        && bucketisedAmount.toDouble() >= AmountDeIdentifierImpl.MAXIMUM_BIN_COUNT * -1
            }
        }
    }

    @Test
    fun `buckets should be generated equally for each deIdentifier instance`() {
        val testData = listOf(
            "0",
            "2", "-2",
            "8", "-8",
            "16", "-16",
            "32", "-32",
            "64", "-64",
            "128", "-128",
            "256", "-256",
            "512", "-512",
            "1024", "-1024",
            "2048", "-2048",
            "4096", "-4096",
            "8192", "-8192",
            "16384", "-16384",
            "32768", "-32768",
            "65536", "-65536",
            "131072", "-131072",
            "262144", "-262144",
            "524288", "-524288",
            "1048576", "-1048576",
            "2097152", "-2097152",
            "4194304", "-4194304"
        )

        val buckets = mutableListOf<String>()
        testData.forEach {
            buckets.add(amountDeIdentifier.getDeIdentifiedText(it))
        }

        val lastBucketisedAmount = DependencyBinder.amountDeIdentifier.getDeIdentifiedText(testData.last())
        assertTrue { lastBucketisedAmount == buckets.last() }
    }
}