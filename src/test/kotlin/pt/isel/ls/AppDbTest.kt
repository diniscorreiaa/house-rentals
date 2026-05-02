package pt.isel.ls

import org.junit.Before
import org.postgresql.ds.PGSimpleDataSource
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// Isto mostra alguns dos métodos de prepareStatement em java, mas é praticamente igual ao kotlin,
// depois vejam: https://www.geeksforgeeks.org/java/how-to-use-preparedstatement-in-java/
@Ignore
class AppDbTest {

    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL")

    @Before
    fun setUrl() {
        dataSource.setURL(jdbcDatabaseURL)
    }

    @Test
    fun `read all students`() {
        dataSource.connection.use {
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

    @Test
    fun `insert a new student`() {
        dataSource.connection.use {
            it.autoCommit = false
            val st = it.prepareStatement("INSERT INTO students(number, name, course) VALUES (?, ?, ?)")
            st.setInt(1, 99999)
            st.setString(2, "teste")
            st.setInt(3, 1)

            val result = st.executeUpdate()

            val courses = mutableListOf<Int>()
            val numbers = mutableListOf<Int>()
            val names = mutableListOf<String>()

            val st2 = it.prepareStatement("SELECT * FROM students")
            val rs = st2.executeQuery()

            while (rs.next()) {
                courses.add(rs.getInt("course"))
                numbers.add(rs.getInt("number"))
                names.add(rs.getString("name"))
            }

            assertEquals(1, result) // Retorna nº de rows inseridas

            assertTrue(names.contains("teste"))
            assertTrue(courses.contains(1))
            assertTrue(numbers.contains(99999))

            it.rollback()
            it.autoCommit = true
        }
    }

    @Test
    fun `update student`() {
        dataSource.connection.use {
            it.autoCommit = false
            val st = it.prepareStatement("UPDATE students SET name = ? WHERE number = ?")
            st.setString(1, "Alice -updated-")
            st.setInt(2, 12345)

            val result = st.executeUpdate()

            val courses = mutableListOf<Int>()
            val numbers = mutableListOf<Int>()
            val names = mutableListOf<String>()

            val st2 = it.prepareStatement("SELECT * FROM students")
            val rs = st2.executeQuery()

            while (rs.next()) {
                courses.add(rs.getInt("course"))
                numbers.add(rs.getInt("number"))
                names.add(rs.getString("name"))
            }

            assertEquals(1, result)

            assertTrue(names.contains("Alice -updated-"))
            assertTrue(courses.contains(1))
            assertTrue(numbers.contains(12345))

            it.rollback()
            it.autoCommit = true
        }
    }

    @Test
    fun `delete student`() {
        dataSource.connection.use {
            it.autoCommit = false
            val st = it.prepareStatement("DELETE FROM students WHERE number = ?")
            st.setInt(1, 12345)

            val result = st.executeUpdate()

            val numbers = mutableListOf<Int>()
            val names = mutableListOf<String>()

            val st2 = it.prepareStatement("SELECT * FROM students")
            val rs = st2.executeQuery()

            while (rs.next()) {
                numbers.add(rs.getInt("number"))
                names.add(rs.getString("name"))
            }

            assertEquals(1, result)

            assertTrue(names.none { name -> name == "Alice" })
            assertTrue(numbers.none { number -> number == 12345 })

            it.rollback()
            it.autoCommit = true
        }
    }
}