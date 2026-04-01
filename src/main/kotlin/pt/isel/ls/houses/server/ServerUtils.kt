package pt.isel.ls.houses.server

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Status
import pt.isel.ls.houses.exceptions.HttpException

@OptIn(ExperimentalSerializationApi::class)
val filter = Filter { handler ->
    val wrapperHandler: HttpHandler = { request ->
        try {
            handler(request)
        } catch (e: HttpException) {
            Response(e.status).body(e.message)
        } catch (e: MissingFieldException) { // TODO: this is a hack, we probably should map it to our own errors
            Response(Status.BAD_REQUEST).body("Field ${e.missingFields} is required, but hasn't been passed")
        } catch (e: Exception) {
            Response(Status.INTERNAL_SERVER_ERROR).body("Internal error, contact the developers to report this situation")
        }
    }
    wrapperHandler
}