package pt.isel.ls.houses.webapi.dtos

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/////////// Input ///////////
@Serializable
data class CreateBookingIn(val hid: Int, val startDate: LocalDate, val endDate: LocalDate)

@Serializable
data class UpdateBookingIn(val startDate: LocalDate, val endDate: LocalDate)

/////////// Output ///////////
@Serializable
data class CreateBookingOut(val bid: Int) // TODO: Não está explicito no enunciado que ao criar booking deve retornar o id, eu coloquei porque nos outros dominios pede e eu assumi que era para fazer - perguntar à prof

@Serializable
data class GetBookingOut(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val uid: Int,
    val hid: Int
) // TODO: Faz sentido o client receber uid e hid?

@Serializable
data class UpdateBookingOut(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val uid: Int,
    val hid: Int
)