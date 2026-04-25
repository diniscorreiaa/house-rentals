package pt.isel.ls.houses.data.db

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.Booking
import pt.isel.ls.houses.domain.House
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.BookingRepository
import java.sql.Date

class BookingDataPostgresql(val dataSource: PGSimpleDataSource) : BookingRepository {
    override fun createBooking(
        hid: Int,
        userId: Int,
        startDate: LocalDate,
        endDate: LocalDate
    ): Booking {
        dataSource.connection.use {
            val st =
                it.prepareStatement("INSERT INTO BOOKING (\"StartDate\", \"EndDate\", \"IdUser\", \"IdHouse\") VALUES (?, ?, ?, ?) RETURNING \"IdBooking\"")

            st.setDate(1, Date.valueOf(startDate.toJavaLocalDate())) // TODO: Provavelmente isto terá de ser melhorado
            st.setDate(2, Date.valueOf(endDate.toJavaLocalDate()))
            st.setInt(3, userId)
            st.setInt(4, hid)

            val rs = st.executeQuery()
            if (!rs.next()) throw InternalError() // TODO: Erros nesta camada?

            return Booking(
                rs.getInt(1),
                startDate,
                endDate,
                userId,
                hid
            )
        }
    }

    override fun getBooking(bid: Int): Booking? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM BOOKING WHERE \"IdBooking\" = ?")

            st.setInt(1, bid)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return Booking(
                bid,
                rs.getDate("StartDate").toLocalDate().toKotlinLocalDate(),
                rs.getDate("EndDate").toLocalDate().toKotlinLocalDate(),
                rs.getInt("IdUser"),
                rs.getInt("IdHouse")
            )
        }
    }

    override fun getBookings(
        hid: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?,
    ): List<Booking> {
        dataSource.connection.use {
            var query = "SELECT * FROM BOOKING WHERE \"IdHouse\" = ? AND \"StartDate\" >= ? AND \"EndDate\" <= ?"
            if (limit != null) query += " ORDER BY \"StartDate\" LIMIT ? OFFSET ?"

            val st = it.prepareStatement(query)

            st.setInt(1, hid)
            st.setDate(2, Date.valueOf(startDate.toJavaLocalDate()))
            st.setDate(3, Date.valueOf(endDate.toJavaLocalDate()))
            if (limit != null) {
                st.setInt(4, limit)
                st.setInt(5, skip ?: 0)
            }

            val rs = st.executeQuery()

            val bookings = mutableListOf<Booking>()

            while (rs.next()) {
                bookings.add(
                    Booking(
                        rs.getInt("IdBooking"),
                        rs.getDate("StartDate").toLocalDate().toKotlinLocalDate(),
                        rs.getDate("EndDate").toLocalDate().toKotlinLocalDate(),
                        rs.getInt("IdUser"),
                        hid
                    )
                )
            }

            return bookings
        }
    }

    override fun getAvailableHouses(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int?,
        skip: Int?
    ): List<House> {
        dataSource.connection.use {
            var query = """
                SELECT H.* 
                FROM HOUSE AS H
                LEFT OUTER JOIN BOOKING AS B ON H."IdHouse" = B."IdHouse" AND "StartDate" <= ? AND "EndDate" >= ? 
                WHERE B."IdHouse" IS NULL
                """
            if (limit != null) query += " ORDER BY H.\"IdHouse\" LIMIT ? OFFSET ?"

            val st = it.prepareStatement(query)

            st.setDate(1, Date.valueOf(endDate.toJavaLocalDate()))
            st.setDate(2, Date.valueOf(startDate.toJavaLocalDate()))
            if (limit != null) {
                st.setInt(3, limit)
                st.setInt(4, skip ?: 0)
            }

            val rs = st.executeQuery()

            val houses = mutableListOf<House>()

            while (rs.next()) {
                houses.add(
                    House(
                        rs.getInt("IdHouse"),
                        rs.getString("Title"),
                        rs.getInt("Location"),
                        rs.getDouble("Area"),
                        rs.getDouble("Price"),
                        rs.getString("Description")
                    )
                )
            }

            return houses
        }
    }
}