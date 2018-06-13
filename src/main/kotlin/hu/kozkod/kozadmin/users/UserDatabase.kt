package hu.kozkod.kozadmin.users

import hu.kozkod.kozadmin.organizations.model.database.Organizations
import hu.kozkod.kozadmin.organizations.model.Organization
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction


object Users : IntIdTable() {
    val email = varchar("email", 128).uniqueIndex()
    val firstName = varchar("first_name", 64)
    val lastName = varchar("last_name", 64)
}

object UserOrganizations : Table(name = "users__organizations") {
    val user = reference("user", Users).primaryKey(0)
    val organization = reference("organization", Organizations.id).primaryKey(1)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var email by Users.email
    var firstName by Users.firstName
    var lastName by Users.lastName
    var organizations by Organization via UserOrganizations
}

fun listUsers() = transaction {
    logger.addLogger(StdOutSqlLogger)
    User.all()
}

fun getUserById(id: Int) = transaction {
    logger.addLogger(StdOutSqlLogger)
    User.find { Users.id eq id }
}
