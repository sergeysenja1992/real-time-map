package pp.ua.ssenko.rsoket.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ConcurrentHashMap

fun uniqId() = "ID" + UUID.randomUUID().toString().replace("-", "")

val loggers = ConcurrentHashMap<Class<Any>, Logger>()
val loggerFactory: (Class<Any>) -> Logger = { LoggerFactory.getLogger(it) }
val Any.log: Logger get() = loggers.computeIfAbsent(this.javaClass, loggerFactory)

