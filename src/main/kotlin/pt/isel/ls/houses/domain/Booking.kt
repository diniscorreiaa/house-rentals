package pt.isel.ls.houses.domain

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Serializable
data class Booking(
    val idBooking: Int, // unique identifier for the booking
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val userId: Int,
    val houseId: Int
)