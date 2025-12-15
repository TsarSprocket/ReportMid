package com.tsarsprocket.reportmid.dataDragonApi.data


class ItemIsUnknownException(val itemId: Int) : RuntimeException("Item with id=$itemId is unknown")