package pt.isel.ls.houses.webapi.dtos

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/////////// Input ///////////
@Serializable
data class CreateBookingIn(val hid: Int, val startDate: String, val endDate: String) // Usa-se string para receber as datas de forma a validar se a data vem no formato válido, se a data viesse com o formato inválido ia ser lançado logo uma exceção se usássemos o tipo LocalDate.

@Serializable
data class UpdateBookingIn(val startDate: String, val endDate: String)

/////////// Output ///////////
@Serializable
data class CreateBookingOut(val bid: Int) // TODO: Não está explicito no enunciado que ao criar booking deve retornar o id, eu coloquei porque nos outros dominios pede e eu assumi que era para fazer - perguntar à prof

@Serializable
data class GetBookingOut(
    val bid: Int,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val uid: Int,
    val hid: Int
)

@Serializable
data class UpdateBookingOut(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val uid: Int,
    val hid: Int
)

@Serializable
data class UserList(
    val name: String,
    val email: String,
    val numberOfBookedDays: Int
)