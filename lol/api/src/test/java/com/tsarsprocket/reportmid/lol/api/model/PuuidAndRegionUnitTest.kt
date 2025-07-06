package com.tsarsprocket.reportmid.lol.api.model

import android.os.Parcel
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@Suppress("MemberVisibilityCanBePrivate")
class PuuidAndRegionUnitTest {

    val testParcel: Parcel = mock()
    val testRegion: Region = Region.EUROPE_WEST

    /**
     * With this I explained the principles of unit-testing to my son
     */
    @Test
    fun `write puuid and region to parcel`() {
        val puuidAndRegion = PuuidAndRegion(Puuid(TEST_PUUID), testRegion)

        puuidAndRegion.writeToParcel(testParcel, TEST_FLAGS)

        verify(testParcel).writeString(eq(TEST_PUUID))
        verify(testParcel).writeInt(eq(testRegion.id))
    }

    @Test
    fun `puuid and region from parcel`() {
        whenever(testParcel.readString()).thenReturn(TEST_PUUID)
        whenever(testParcel.readInt()).thenReturn(testRegion.id)

        val puuidAndRegion = PuuidAndRegion.CREATOR.createFromParcel(testParcel)

        assert(puuidAndRegion.region == Region.EUROPE_WEST && puuidAndRegion.puuid == Puuid(TEST_PUUID))
    }

    companion object {
        const val TEST_FLAGS = 0
        const val TEST_PUUID = "test_puuid"
    }
}