package pt.isel.ls.houses.webapi.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/////////// Input ///////////
@Serializable
data class CreateBookingIn(val hid: Int, val startDate: LocalDateTime, val endDate: LocalDateTime)


/////////// Output ///////////
@Serializable
data class CreateBookingOut(val bid: Int) // TODO: Não está explicito no enunciado que ao criar booking deve retornar o id, eu coloquei porque nos outros dominios pede e eu assumi que era para fazer - perguntar à prof

@Serializable
data class GetBookingOut(val startDate: LocalDateTime, val endDate: LocalDateTime, val uid: Int, val hid: Int) // TODO: Faz sentido o client receber uid e hid?