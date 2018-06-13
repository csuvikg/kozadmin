package hu.kozkod.kozadmin.organizations.model.database

import hu.kozkod.kozadmin.organizations.model.StatusType
import org.jetbrains.exposed.dao.IntIdTable

object Organizations : IntIdTable() {
    val name = varchar("name", 256)
    val url = varchar("url", 64).uniqueIndex()
    val status = enumerationByName("status", 16, StatusType::class.java)
}