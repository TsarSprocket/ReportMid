package com.tsarsprocket.reportmid.app_api.request_manager

abstract class Request<out K: RequestKey, out R: RequestResult>(val key: K): () -> R

