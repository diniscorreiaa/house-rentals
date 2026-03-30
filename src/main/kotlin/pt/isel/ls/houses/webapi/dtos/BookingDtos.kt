package pt.isel.ls.houses.webapi.dtos

import kotlinx.datetime.LocalDate

/////////// Input ///////////

data class CreateBookingIn(val hid: Int, val startDate: LocalDate, val endDate: LocalDate)

/////////// Output ///////////

data class CreateBookingOut(val bid: Int) // TODO: Não está explicito no enunciado que ao criar booking deve retornar o id, eu coloquei porque nos outros dominios pede e eu assumi que era para fazer - perguntar à prof

data class GetBookingOut(val startDate: LocalDate, val endDate: LocalDate, val uid: Int, val hid: Int) // TODO: Faz sentido o client receber uid e hid?