package hu.kozkod.kozadmin.organizations.model.database

import hu.kozkod.kozadmin.users.Users
import org.jetbrains.exposed.sql.Table

object OrganizationAdministrators : Table(name = "organizations__administrators") {
    val organization = reference("organization", Organizations).primaryKey(0)
    val administrator = reference("administrator", Users).primaryKey(1)
}