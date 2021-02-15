package ua.pp.ssenko.chronostorm.domain

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeName
import pp.ua.ssenko.rsoket.domain.StorableEntity
import java.time.Instant
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.HashMap

class LocationMapInfo(
        var name: String,
        var previewImage: String,
        var lastUpdateTime: Instant = Instant.now()
): StorableEntity() {

}

class LocationMap(
    val id: String = UUID.randomUUID().toString(),
    @Volatile var name: String = "",
    @Volatile var order: Long = 0,
    var owner: String? = null
) {
    val customIcons: MutableMap<String, CustomIcon> = HashMap()
    val mapObjects: MutableMap<String, MapObject> = HashMap()
    var previewImage: String? = null

}

class CustomIcon {
    val id = UUID.randomUUID().toString()
    val defaultSize = Size()
    var previewUrl: String = ""
    var originUrl: String = ""
    var name: String = ""
}

class Position(@Volatile var top: String, @Volatile var left: String, @Volatile var rotate: Double = 1.0)

class Size(var width: String = "0px", var height: String = "0px", var scale: Double = 1.0)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
abstract class MapObject(var id: String = "", var name: String) {
    val size: Size = Size()
    val position: Position = Position("0px", "0px")
    var zIndex: Int = 10_000  // start from 10_000 and increment by 100
    var order: Int = 0
    val type: String
        get() = this.javaClass.getAnnotation(JsonTypeName::class.java).value
}
