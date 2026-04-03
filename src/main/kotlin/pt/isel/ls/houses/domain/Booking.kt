package pt.isel.ls.houses.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Booking(
    val bid: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val userId: Int,
    val houseId: Int
)