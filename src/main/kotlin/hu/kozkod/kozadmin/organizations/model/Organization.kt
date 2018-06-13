package hu.kozkod.kozadmin.organizations.model

import hu.kozkod.kozadmin.organizations.model.database.OrganizationAdministrators
import hu.kozkod.kozadmin.organizations.model.database.Organizations
import hu.kozkod.kozadmin.users.User
import hu.kozkod.kozadmin.users.UserRepresentation
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class Organization(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Organization>(Organizations)

    var name by Organizations.name
    var url by Organizations.url
    var status by Organizations.status
    var administrators by User via OrganizationAdministrators

    fun toRepresentation(): OrganizationRepresentation = OrganizationRepresentation(
            this.id.value,
            this.name,
            this.url,
            this.status,
            this.administrators.map {
                UserRepresentation(it.id.value, it.email, it.firstName, it.lastName)
            })
}