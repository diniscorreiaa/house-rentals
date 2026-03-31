package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate

@Serializable
data class Booking(
    val bid: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val userId: Int,
    val houseId: Int
)