package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.core.staticdata.ReforgedRune

const val RES_NAME_PREFIX_RUNE= "rune"

class RuneModel( val repository: Repository, private val reforgedRune: ReforgedRune ) {
    val name = reforgedRune.name
    val key = reforgedRune.key
    val imagePath = reforgedRune.image
    val longDesc = reforgedRune.longDescription
    val slot = reforgedRune.slot
    val runePath = Repository.getRunePath( reforgedRune.path.id )
    val iconResId = getIconResId( key )

    private fun getIconResId( key: String? ): Int {
        val splitPath = imagePath.split( '/', '.' )
        val resKey = splitPath[ splitPath.size - 2 ]
        val resName = "${RES_NAME_PREFIX_RUNE}_${resKey.toLowerCase()}"
        return repository.context.resources.getIdentifier( resName, "drawable", repository.context.packageName )
    }
}
