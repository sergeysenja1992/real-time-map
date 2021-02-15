package ua.pp.ssenko.chronostorm.domain.elements

import com.fasterxml.jackson.annotation.JsonTypeName
import ua.pp.ssenko.chronostorm.domain.MapObject

@JsonTypeName("md-icon-element")
class IconObject(id: String = "", val iconName: String, val iconSet: String): MapObject(id, iconName) {

}
