package com.tsarsprocket.reportmid.model

import com.tsarsprocket.reportmid.RIOTIconProvider

class RunePathModel(
    val id: Int,
    val key: String,
    val name: String,
    iconPath: String,
    private val iconProvider: RIOTIconProvider,
    ) {

    private val myRunes = ArrayList<RuneModel>()
    val runes = myRunes.toList()
    val icon by lazy { iconProvider.getRunePathIcon(iconPath).cache() }

    fun createRune(id: Int, key: String, name: String, slotNo: Int, iconPath: String, iconProvider: RIOTIconProvider): RuneModel =
        RuneModelImpl(id, key, name,slotNo, iconPath, iconProvider)
            .also { myRunes.add(it) }

    private inner class RuneModelImpl(id: Int, key: String, name: String, slotNo: Int, iconPath: String, iconProvider: RIOTIconProvider) :
        RuneModel(id, key, name, slotNo, iconPath, iconProvider, ) {

        override val runePath: RunePathModel = this@RunePathModel
    }

    override fun equals(other: Any?): Boolean = if (other is RunePathModel) id == other.id else false
}

