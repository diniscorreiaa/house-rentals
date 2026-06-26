package pt.isel.ls.houses.domain

data class House(
    val hid: Int,
    val title: String,
    val location: Int,
    val area: Double,
    val price: Double,
    val description: String,
    val owner: Int
)
