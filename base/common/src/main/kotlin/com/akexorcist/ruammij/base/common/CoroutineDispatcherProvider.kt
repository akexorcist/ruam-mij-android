package com.akexorcist.ruammij.base.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface CoroutineDispatcherProvider {
    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher
}

class DefaultCoroutineDispatcherProvider : CoroutineDispatcherProvider {
    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun io(): CoroutineDispatcher = Dispatchers.IO
}
