package pt.isel.ls.houses.domain

import kotlinx.datetime.LocalDate

data class Booking(
    val bid: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val userId: Int,
    val houseId: Int
)
