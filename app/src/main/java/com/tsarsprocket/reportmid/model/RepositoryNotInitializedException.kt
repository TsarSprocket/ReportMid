package com.tsarsprocket.reportmid.model

const val REPOSITORY_NOT_INITIALIZED_MESSAGE = "Repository not initialized"

class RepositoryNotInitializedException : RuntimeException {

    constructor() : super( REPOSITORY_NOT_INITIALIZED_MESSAGE )

    constructor( ex: Throwable ) : super( REPOSITORY_NOT_INITIALIZED_MESSAGE, ex )
}
