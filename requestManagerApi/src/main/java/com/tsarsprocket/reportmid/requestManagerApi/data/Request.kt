package com.tsarsprocket.reportmid.requestManagerApi.data

abstract class Request<out K : RequestKey, out R : RequestResult>(val key: K) : suspend () -> R

