package pt.isel.ls.houses.data.db

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.houses.domain.User
import pt.isel.ls.houses.exceptions.InternalError
import pt.isel.ls.houses.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserDataPostgresql(val dataSource: PGSimpleDataSource): UserRepository { // TODO: FALTA setUrl()!!!

    // Deu bastante jeito: https://www.jooq.org/doc/latest/manual/sql-building/sql-statements/insert-statement/insert-returning/
    // Baseei-me na resposta do Thiago Silva pois a resposta no topo implica imports: https://www.devmedia.com.br/forum/postgresql-insert-returning-id-resolvido/569757

    override fun createUser(name: String, email: String): User {
        dataSource.connection.use {
            val st = it.prepareStatement("INSERT INTO \"user\" (\"UserName\", \"Email\") VALUES (?, ?) RETURNING \"IdUser\", \"Token\"")

            st.setString(1, name)
            st.setString(2, email)

            val rs = st.executeQuery() // A expressão RETURNING é detetada como uma query pois estamos a receber dados da base de dados
            if (!rs.next()) throw InternalError() // Avança para a 1a linha// TODO: Podemos lançar exceções daqui?
            return User(
                rs.getInt(1),
                name,
                email,
                Uuid.parse(rs.getString(2))
            )
        }
    }

    override fun getUser(uid: Int): User? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM \"user\" WHERE \"IdUser\" = ?")
            st.setInt(1, uid)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return User(
                uid,
                rs.getString("UserName"),
                rs.getString("Email"),
                Uuid.parse(rs.getString("Token"))
            )
        }
    }

    override fun getUserByEmail(email: String): User? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM \"user\" WHERE \"Email\" = ?")
            st.setString(1, email)

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return User(
                rs.getInt("IdUser"),
                rs.getString("UserName"),
                email,
                Uuid.parse(rs.getString("Token"))
            )
        }
    }

    override fun getUserByToken(token: Uuid): User? {
        dataSource.connection.use {
            val st = it.prepareStatement("SELECT * FROM \"user\" WHERE \"Token\" = ?::uuid") // Cast explícito (sem ele a comparação não funciona): https://www.gotoquiz.com/web-coding/programming/java-programming/convert-between-java-enums-and-postgresql-enums/
            st.setString(1, token.toString())

            val rs = st.executeQuery()
            if (!rs.next()) return null

            return User(
                rs.getInt("IdUser"),
                rs.getString("UserName"),
                rs.getString("Email"),
                token
            )
        }
    }
}