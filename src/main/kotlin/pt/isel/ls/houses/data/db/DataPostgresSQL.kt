package pt.isel.ls.houses.data.db

import kotlinx.datetime.LocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.domain.Location
import pt.isel.ls.houses.domain.LocationType
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.repository.DataRepository
import kotlin.uuid.Uuid

class DataPostgresSQL : DataRepository {

    private val dataSource = PGSimpleDataSource().apply { setUrl(System.getenv("JDBC_DATABASE_URL")) }

    override val user = UserDataPostgresql(dataSource)
    override val location = LocationDataPostgresql(dataSource)
    override val house = HouseDataPostgresql(dataSource)
    override val booking = BookingDataPostgresql(dataSource)
}