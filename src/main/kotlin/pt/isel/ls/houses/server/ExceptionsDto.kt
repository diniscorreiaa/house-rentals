package pt.isel.ls.houses.server

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionsDto(val uri: String, val message: String)