package com.tsarsprocket.reportmid.model

const val MATCH_IS_NOT_IN_PROGRESS_MESSAGE = "Match is not in progress"

class MatchIsNotInProgressException: RuntimeException {
    constructor(): super( MATCH_IS_NOT_IN_PROGRESS_MESSAGE )
    constructor( ex: Throwable ): super( MATCH_IS_NOT_IN_PROGRESS_MESSAGE, ex )
}