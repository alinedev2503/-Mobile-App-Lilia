package com.example

import com.example.data.web3.Web3RewardsManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class Web3RewardsRobolectricTest {

    @Test
    fun `test initial web3 wallet has valid initial polygon state and transactions`() {
        val initialTxs = Web3RewardsManager.getInitialTransactions()
        assertTrue(initialTxs.isNotEmpty())

        val firstTx = initialTxs.first()
        assertTrue(firstTx.txHash.startsWith("0x"))
        assertEquals(66, firstTx.txHash.length) // 0x + 64 hex chars (SHA-256)
        assertTrue(firstTx.blockNumber > 0)
    }

    @Test
    fun `test cryptographic sha256 tx hash generation is deterministic and valid format`() {
        val fixedNonce = 123456789L
        val hash1 = Web3RewardsManager.generateTxHash("Meal_Verification_123", fixedNonce)
        val hash2 = Web3RewardsManager.generateTxHash("Meal_Verification_123", fixedNonce)

        assertTrue(hash1.startsWith("0x"))
        assertEquals(hash1, hash2)
        assertEquals(66, hash1.length)
    }

    @Test
    fun `test available marketplace perks have valid redemption costs and partners`() {
        val perks = Web3RewardsManager.getAvailablePerks()
        assertTrue(perks.size >= 4)

        val firstPerk = perks.first()
        assertTrue(firstPerk.tokenCost > 0)
        assertNotNull(firstPerk.partnerBrand)
        assertTrue(firstPerk.voucherCode.isNotEmpty())
    }
}
