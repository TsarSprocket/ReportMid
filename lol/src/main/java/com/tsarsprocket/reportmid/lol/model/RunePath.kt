package com.tsarsprocket.reportmid.lol.model

class RunePath(
    val id: Int,
    val key: String,
    val name: String,
    val iconPath: String,
) {

    private val myRunes = ArrayList<Rune>()
    val runes = myRunes.toList()

    fun createRune(
        id: Int,
        key: String,
        name: String,
        slotNo: Int,
        iconPath: String
    ): Rune {
        return RuneImpl(id, key, name, slotNo, iconPath)
            .also { myRunes.add(it) }
    }

    private inner class RuneImpl(
        id: Int,
        key: String,
        name: String,
        slotNo: Int,
        iconPath: String
    ) : Rune(id, key, name, slotNo, iconPath) {

        override val runePath: RunePath = this@RunePath
    }

    override fun equals(other: Any?): Boolean = if(other is RunePath) id == other.id else false

    override fun hashCode() = id
}

