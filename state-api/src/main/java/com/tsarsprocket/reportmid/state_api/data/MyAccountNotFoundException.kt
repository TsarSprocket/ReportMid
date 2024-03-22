package com.tsarsprocket.reportmid.state_api.data

class MyAccountNotFoundException(details: String = "no details") : RuntimeException("My account not found: $details")