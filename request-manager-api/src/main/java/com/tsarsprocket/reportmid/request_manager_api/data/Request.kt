package com.tsarsprocket.reportmid.request_manager_api.data

abstract class Request<out K : RequestKey, out R : RequestResult>(val key: K) : suspend () -> R

