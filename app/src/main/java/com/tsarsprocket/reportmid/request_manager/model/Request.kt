package com.tsarsprocket.reportmid.request_manager.model

abstract class Request<out K: RequestKey, out R: RequestResult>(val key: K): () -> R

