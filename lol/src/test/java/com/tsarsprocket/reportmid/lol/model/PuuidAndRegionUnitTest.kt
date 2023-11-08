package com.tsarsprocket.reportmid.lol.model

import android.os.Parcel
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.eq
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PuuidAndRegionUnitTest {

    @Test
    fun testWriteToParcel() {
        // готовим мок-объекты
        val testParcel: Parcel = Mockito.mock(Parcel::class.java)
        val testRegion: Region = Mockito.mock()
        val testFlafs = 0

        // создаём реальный тестируемый объект
        val puuidAndRegion = PuuidAndRegion(TEST_PUUID, testRegion)

        // Предопределяем поведение мок-объектов (только необходимое)
        whenever(testRegion.tag).thenReturn(TEST_REGION_TAG)

        // Замускаем тестируемый метод
        puuidAndRegion.writeToParcel(testParcel, testFlafs)

        verify(testParcel, times(1)).writeString(eq(TEST_PUUID))
        verify(testParcel, atLeastOnce()).writeString(eq(TEST_REGION_TAG))
    }

    companion object {
        const val TEST_PUUID = "test_puuid"
        const val TEST_REGION_TAG = "test_region_tag"
    }
}