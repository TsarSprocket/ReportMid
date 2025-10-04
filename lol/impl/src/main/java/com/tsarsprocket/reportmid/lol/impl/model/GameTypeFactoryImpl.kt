package com.tsarsprocket.reportmid.lol.impl.model

import android.content.Context
import android.util.Log
import com.tsarsprocket.reportmid.baseApi.di.AppContext
import com.tsarsprocket.reportmid.lol.api.model.GAME_MODE_CLASSIC
import com.tsarsprocket.reportmid.lol.api.model.GAME_TYPE_MATCHED
import com.tsarsprocket.reportmid.lol.api.model.GameType
import com.tsarsprocket.reportmid.lol.api.model.GameTypeFactory
import com.tsarsprocket.reportmid.lol.api.model.QUEUE_ID_NORMAL_DRAFT
import com.tsarsprocket.reportmid.lol.api.model.QUEUE_ID_SOLOQ
import com.tsarsprocket.reportmid.lol.impl.R
import javax.inject.Inject

internal class GameTypeFactoryImpl @Inject constructor(
    @AppContext appContext: Context,
) : GameTypeFactory {

    private val gameTypeDescriptions: Map<String?, Map<String?, Map<Int?, Map<Int?, String>>>> = mutableMapOf<String?, MutableMap<String?, MutableMap<Int?, MutableMap<Int?, String>>>>().apply {
        PATTERNS.forEach { pattern ->
            getOrPut(pattern.gameMode) { mutableMapOf() }
                .getOrPut(pattern.gameType) { mutableMapOf() }
                .getOrPut(pattern.mapId) { mutableMapOf() }[pattern.queueId] = appContext.getString(pattern.description)
        }
    }

    override fun getGameType(gameMode: String, gameType: String, mapId: Int, queueId: Int): GameType {
        // For game type discovery
        Log.d("GAME_TYPE", "Mage mode: $gameMode, type: $gameType, map: $mapId, queue: $queueId")

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

        val PATTERNS = listOf(
            TypePattern(GAME_MODE_CLASSIC, GAME_TYPE_MATCHED, null, QUEUE_ID_NORMAL_DRAFT, R.string.lol_api_game_type_name_normal_draft),
            TypePattern(GAME_MODE_CLASSIC, GAME_TYPE_MATCHED, null, QUEUE_ID_SOLOQ, R.string.lol_api_game_type_name_soloq),
            // TODO: Add more patterns

            // Ensures it always returns something
            TypePattern(null, null, null, null, R.string.lol_api_game_type_name_unknown),
        )
    }
}

private fun <K, V, M> Map<K?, M>.evaluateWithFallback(key: K, evaluator: M.() -> V?): V? = this[key].let { it?.evaluator() ?: this[null]?.evaluator() }
