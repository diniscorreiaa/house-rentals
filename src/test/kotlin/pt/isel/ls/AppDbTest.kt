package pt.isel.ls

import kotlin.test.Test
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Isto mostra alguns dos métodos de prepareStatement em java, mas é praticamente igual ao kotlin,
// depois vejam: https://www.geeksforgeeks.org/java/how-to-use-preparedstatement-in-java/

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

    // O teste está funcional, mas neste momento só dá para correr 1 vez, é preciso anular a inserção no final se não dá erro de duplicação.
    @Test
    fun `insert a new student`() {
        dataSource.setURL(jdbcDatabaseURL)

        dataSource.getConnection().use {
            val st = it.prepareStatement("INSERT INTO students(number, name, course) VALUES (?, ?, ?)")
            st.setInt(1, 99999)
            st.setString(2, "teste")
            st.setInt(3, 1)

            val result = st.executeUpdate()

            assertEquals(1, result) // Retorna nº de rows inseridas

            //TODO: Find way to remove the insertion after test execution, thought about DELETE operation but is it right to use another SQL operation for this?
        }
    }
}