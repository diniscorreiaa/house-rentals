package pt.isel.ls.houses.webapi

import org.http4k.core.Request
import pt.isel.ls.houses.data.memory.DataMem
import pt.isel.ls.houses.data.memory.UserDataMem
import pt.isel.ls.houses.exceptions.Unauthorized
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun verifyAndGetAuth(request: Request, data: DataMem) =
    request.header("Authorization")?.let {
        data.getUserByToken(Uuid.parse(it))
    } ?: throw Unauthorized()