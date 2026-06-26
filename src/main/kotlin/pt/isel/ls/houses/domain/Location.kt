package pt.isel.ls.houses.domain

data class Location(
    val lid: Int,
    val name: String,
    val type: LocationType,
    val parentLocation: Int?
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Location) return false
        return name.equals(other.name, true) && type == other.type && parentLocation == other.parentLocation
    }

    override fun hashCode(): Int {
        var result = lid
        result = 31 * result + (parentLocation ?: 0)
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
