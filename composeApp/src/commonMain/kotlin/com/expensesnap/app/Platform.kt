package com.expensesnap.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform