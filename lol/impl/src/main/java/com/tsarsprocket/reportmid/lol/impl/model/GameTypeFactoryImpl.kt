package com.tsarsprocket.reportmid.lol.impl.model

import android.content.Context
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.lol.api.model.GameType
import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.lol.impl.R
import javax.inject.Inject

internal class GameTypeFactoryImpl @Inject constructor(
    @AppContext appContext: Context,
) : GameTypeFactory {

    private val gameTypeDescriptions: Map<String?, Map<String?, Map<String?, Map<Int?, String>>>> = mutableMapOf<String?, MutableMap<String?, MutableMap<String?, MutableMap<Int?, String>>>>().apply {
        PATTERNS.forEach { pattern ->
            getOrPut(pattern.gameMode) { mutableMapOf() }
                .getOrPut(pattern.gameType) { mutableMapOf() }
                .getOrPut(pattern.mapId) { mutableMapOf() }[pattern.queueId] = appContext.getString(pattern.description)
        }
    }

    override fun getGameType(gameMode: String, gameType: String, mapId: String, queueId: Int): GameType {
        return GameType(
            gameMode = gameMode,
            gameType = gameType,
            mapId = mapId,
            queueId = queueId,
            name = gameTypeDescriptions.evaluateWithFallback(gameMode) {
                evaluateWithFallback(gameType) {
                    evaluateWithFallback(mapId) {
                        evaluateWithFallback(queueId) { this }
                    }
                }
            }!!
        )
    }

    private companion object {
        const val GAME_TYPE_CUSTOM = "CUSTOM_GAME"
        const val GAME_TYPE_MATCHED = "MATCHED_GAME"
        const val GAME_TYPE_TUTORIAL = "TUTORIAL_GAME"
        const val QUEUE_TYPE_ARAM = "ARAM"
        const val QUEUE_TYPE_NORMAL = "NORMAL"

        val PATTERNS = listOf(
            TypePattern(GAME_TYPE_MATCHED, QUEUE_TYPE_ARAM, null, null, R.string.lol_impl_game_type_name_aram),
            TypePattern(GAME_TYPE_MATCHED, QUEUE_TYPE_NORMAL, null, null, R.string.lol_impl_game_type_name_normal_draft),
            // TODO: Add more patterns

            // Ensures it always returns something
            TypePattern(null, null, null, null, R.string.lol_impl_game_type_name_unknown),
        )
    }
}

private fun <K, V, M> Map<K?, M>.evaluateWithFallback(key: K, evaluator: M.() -> V?): V? = this[key].let { if(it != null) it.evaluator() else this[null]?.evaluator() }
