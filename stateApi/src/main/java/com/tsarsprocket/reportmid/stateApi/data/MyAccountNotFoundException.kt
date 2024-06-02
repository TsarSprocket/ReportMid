package com.tsarsprocket.reportmid.stateApi.data

class MyAccountNotFoundException(details: String = "no details") : RuntimeException("My account not found: $details")