package com.tsarsprocket.reportmid.model

import com.merakianalytics.orianna.types.common.GameMode
import com.merakianalytics.orianna.types.common.GameType
import com.merakianalytics.orianna.types.common.Queue
import com.tsarsprocket.reportmid.R

typealias GameMap = com.merakianalytics.orianna.types.common.Map

enum class GameTypeModel(
    val gameType: GameType?,
    val queue: Queue?,
    val gameMode: GameMode?,
    val gameMap: GameMap?,
    val titleResId: Int
) {
    CUSTOM_BLIND_SR( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.SUMMONERS_RIFT, R.string.gamemode_title_custom_blind_sr ),
    CUSTOM_BLIND_HA( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.HOWLING_ABYSS, R.string.gamemode_title_custom_blind_ha ),
    CUSTOM_DRAFT_SR( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.SUMMONERS_RIFT, R.string.gamemode_title_custom_draft_sr ),
    CUSTOM_DRAFT_HA( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.HOWLING_ABYSS, R.string.gamemode_title_custom_draft_ha ),
    CUSTOM_RANDOM_SR( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.SUMMONERS_RIFT, R.string.gamemode_title_custom_random_sr ),
    CUSTOM_RANDOM_HA( GameType.CUSTOM_GAME, Queue.ALL_RANDOM, null, GameMap.HOWLING_ABYSS, R.string.gamemode_title_custom_random_ha ),

    NORMAL_DRAFT( GameType.MATCHED_GAME, Queue.NORMAL, null, null, R.string.gamemode_title_normal_draft ),
    ARAM( GameType.MATCHED_GAME, Queue.ARAM, null, null, R.string.gamemode_title_aram ),

//    ARAM(null, null, GameMode.ARAM, null, R.string.gamemode_title_aram),
    ARSR(null, null, GameMode.ARSR, null, R.string.gamemode_title_arsr),
    ASCENSION(null, null, GameMode.ASCENSION, null, R.string.gamemode_title_ascension),
    ASSASSINATE(null, null, GameMode.ASSASSINATE, null, R.string.gamemode_title_assassinate),
    CLASSIC(null, null, GameMode.CLASSIC, null, R.string.gamemode_title_classic),
    DARKSTAR(null, null, GameMode.DARKSTAR, null, R.string.gamemode_title_darkstar),
    DOOMBOTSTEEMO(null, null, GameMode.DOOMBOTSTEEMO, null, R.string.gamemode_title_doombotsteemo),
    FIRSTBLOOD(null, null, GameMode.FIRSTBLOOD, null, R.string.gamemode_title_firstblood),
    KINGPORO(null, null, GameMode.KINGPORO, null, R.string.gamemode_title_kingporo),
    ODIN(null, null, GameMode.ODIN, null, R.string.gamemode_title_odin),
    ONEFORALL(null, null, GameMode.ONEFORALL, null, R.string.gamemode_title_oneforall),
    OVERCHARGE(null, null, GameMode.OVERCHARGE, null, R.string.gamemode_title_overcharge),
    PRACTICETOOL(null, null, GameMode.PRACTICETOOL, null, R.string.gamemode_title_practicetool),
    PROJECT(null, null, GameMode.PROJECT, null, R.string.gamemode_title_project),
    SIEGE(null, null, GameMode.SIEGE, null, R.string.gamemode_title_siege),
    SNOWURF(null, null, GameMode.SNOWURF, null, R.string.gamemode_title_snowurf),
    STARTGUARDIAN(null, null, GameMode.STARTGUARDIAN, null, R.string.gamemode_title_startguardian),
    TUTORIAL(null, null, GameMode.TUTORIAL, null, R.string.gamemode_title_tutorial),
    TUTORIAL_MODULE_1(null, null, GameMode.TUTORIAL_MODULE_1, null, R.string.gamemode_title_tutorial_module_1),
    TUTORIAL_MODULE_2(null, null, GameMode.TUTORIAL_MODULE_2, null, R.string.gamemode_title_tutorial_module_2),
    TUTORIAL_MODULE_3(null, null, GameMode.TUTORIAL_MODULE_3, null, R.string.gamemode_title_tutorial_module_3),
    URF(null, null, GameMode.URF, null, R.string.gamemode_title_urf),

    UNKNOWN(null, null, null, null, R.string.gamemode_title_unknown);

    companion object {

        private val gtRegistry: MutableMap<GameType?,MutableMap<Queue?,MutableMap<GameMode?,MutableMap<GameMap?,GameTypeModel>>>>

/*
                Map<GameType?,Map<Queue?,Map<GameMode?,Map<GameMap?,GameTypeModel>>>> = mapOf (
            GameType.CUSTOM_GAME to mapOf<Queue?,Map<GameMode?,Map<GameMap?,GameTypeModel>>>(
                Queue.BLIND_PICK to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        GameMap.SUMMONERS_RIFT to CUSTOM_RANDOM_SR,
                        GameMap.HOWLING_ABYSS to CUSTOM_RANDOM_HA
                    )
                ),
                Queue.NORMAL to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        GameMap.SUMMONERS_RIFT to CUSTOM_RANDOM_SR,
                        GameMap.HOWLING_ABYSS to CUSTOM_RANDOM_HA
                    )
                ),
                Queue.ALL_RANDOM to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        GameMap.SUMMONERS_RIFT to CUSTOM_RANDOM_SR,
                        GameMap.HOWLING_ABYSS to CUSTOM_RANDOM_HA
                    )
                )
            ),
            GameType.MATCHED_GAME to mapOf<Queue?,Map<GameMode?,Map<GameMap?,GameTypeModel>>>(
                Queue.NORMAL to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        null to NORMAL_DRAFT
                    )
                ),
                Queue.ARAM to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        null to ARAM
                    )
                )
            ),
            null to mapOf<Queue?,Map<GameMode?,Map<GameMap?,GameTypeModel>>>(
                null to mapOf<GameMode?,Map<GameMap?,GameTypeModel>>(
                    null to mapOf<GameMap?,GameTypeModel>(
                        null to UNKNOWN
                    )
                )
            )
        )
*/

        init {
            gtRegistry = HashMap<GameType?,MutableMap<Queue?,MutableMap<GameMode?,MutableMap<GameMap?,GameTypeModel>>>>()

            for( gtm in GameTypeModel.values() ) {
                val typeMap = gtRegistry[ gtm.gameType ]?:
                    ( HashMap<Queue?,MutableMap<GameMode?,MutableMap<GameMap?,GameTypeModel>>>().also { gtRegistry[ gtm.gameType ] = it } )
                val queueMap = typeMap[ gtm.queue ]?:
                    ( HashMap<GameMode?,MutableMap<GameMap?,GameTypeModel>>().also { typeMap[ gtm.queue ] = it } )
                val modeMap = queueMap[ gtm.gameMode ]?:
                    ( HashMap<GameMap?,GameTypeModel>().also { queueMap[ gtm.gameMode ] = it } )
                modeMap[ gtm.gameMap ] = gtm
            }
        }

        fun by( gameType: GameType? = null, queue: Queue? = null, gameMode: GameMode? = null, gameMap: GameMap? = null ) =
            fallbackCall( gameType, { gt -> byGameType( gtRegistry, gt, queue, gameMode, gameMap ) } )

        private fun byGameType( map: Map<GameType?, Map<Queue?, Map<GameMode?, Map<GameMap?, GameTypeModel>>>>, gameType: GameType?, queue: Queue?, gameMode: GameMode?, gameMap: GameMap? ) =
            fallbackCall( queue, { q -> byQueue( map[ gameType ], q, gameMode, gameMap ) } )

        private fun byQueue( map: Map<Queue?, Map<GameMode?, Map<GameMap?, GameTypeModel>>>?, queue: Queue?, gameMode: GameMode?, gameMap: GameMap? ) =
            fallbackCall( gameMode, { mode -> byMode( map!![ queue ], mode, gameMap ) }  )

        private fun byMode( map: Map<GameMode?, Map<GameMap?, GameTypeModel>>?, gameMode: GameMode?, gameMap: GameMap? ) =
            fallbackCall( gameMap, { gm -> byMap( map!![ gameMode ], gm ) } )

        private fun byMap( map: Map<GameMap?, GameTypeModel>?, gameMap: GameMap? ) =
            map!![ gameMap ]?: throw GameTypeModelNotFoundException()

        inline fun<T,R> fallbackCall( arg: T?, lambda: ( T? ) -> R ) =
            try {
                try {
                    lambda( arg )
                } catch( ex: GameTypeModelNotFoundException ) {
                    if( arg != null ) {
                        lambda( null )
                    } else throw ex
                }
            } catch( ex: NullPointerException ) {
                throw GameTypeModelNotFoundException()
            }

        val byExtrrnalId = GameTypeModel.values().map { it.gameMode to it }.toMap()
    }

    class GameTypeModelNotFoundException: RuntimeException( "Game type model not found" )
}

