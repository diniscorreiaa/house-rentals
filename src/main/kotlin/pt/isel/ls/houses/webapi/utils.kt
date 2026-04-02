package pt.isel.ls.houses.webapi

import org.http4k.core.Request
import pt.isel.ls.houses.exceptions.Unauthorized
import pt.isel.ls.houses.exceptions.WrongTypeForQuery
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun getToken(request: Request) = request.header("Authorization").let {
    it ?: throw Unauthorized()
    Uuid.parseOrNull(it) ?: throw Unauthorized()
}

data class Paging(val limit: Int?, val skip: Int?)

fun getLimitAndSkip(request: Request): Paging {
    var curr = "limit"
    try {
        val limit = request.query("limit")?.toInt().also { curr = "skip" }
        val skip = request.query("skip")?.toInt()
        return Paging(limit, skip)
    } catch (_: NumberFormatException) {
        throw WrongTypeForQuery(curr, "number")
    }
}
