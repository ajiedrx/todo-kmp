package com.adr.todo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform