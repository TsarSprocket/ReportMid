package com.tsarsprocket.reportmid.lol.api.model

import android.os.Parcel
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class PuuidAndRegionUnitTest {

    /**
     * With this I explained the principles of unit-testing to my son
     */
    @Test
    fun testWriteToParcel() {
        // готовим мок-объекты
        val testParcel: Parcel = Mockito.mock(Parcel::class.java)
        val testRegion: Region = Mockito.mock()
        val testFlags = 0

        // создаём реальный тестируемый объект
        val puuidAndRegion = PuuidAndRegion(Puuid(TEST_PUUID), testRegion)

        // Замускаем тестируемый метод
        puuidAndRegion.writeToParcel(testParcel, testFlags)

        verify(testParcel, times(1)).writeString(eq(TEST_PUUID))
        verify(testParcel, atLeastOnce()).writeString(eq(TEST_REGION_TAG))
    }

    companion object {
        const val TEST_PUUID = "test_puuid"
        const val TEST_REGION_TAG = "test_region_tag"
    }
}