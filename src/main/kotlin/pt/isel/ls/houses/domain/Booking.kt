package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class Booking(
    val bid: Int,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val userId: Int,
    val houseId: Int
)