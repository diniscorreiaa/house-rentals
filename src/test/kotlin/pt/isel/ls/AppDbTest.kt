package pt.isel.ls

import kotlin.test.Test
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppDbTest {

    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")

    @Test
    fun `read all students`() {
        dataSource.setURL(jdbcDatabaseURL) // TODO: Find way to not repeat this in every test

        dataSource.getConnection().use {
            val st = it.prepareStatement("SELECT * FROM students")
            val rs = st.executeQuery()
            val courses = mutableListOf<Int>()
            val numbers = mutableListOf<Int>()
            val names = mutableListOf<String>()
            while (rs.next()) {
                courses.add(rs.getInt("course"))
                numbers.add(rs.getInt("number"))
                names.add(rs.getString("name"))
            }
            // Verifica que não está vazio
            assertTrue(courses.isNotEmpty())
            assertTrue(numbers.isNotEmpty())
            assertTrue(names.isNotEmpty())

            // Verifica os dados
            assertTrue(courses.contains(1))
            assertTrue(numbers.contains(12345))
            assertTrue(numbers.contains(12346))
            assertTrue(names.contains("Alice"))
            assertTrue(names.contains("Bob"))
        }
    }

}