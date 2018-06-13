package hu.kozkod.kozadmin.organizations

import hu.kozkod.kozadmin.organizations.model.Organization
import hu.kozkod.kozadmin.organizations.model.OrganizationCreateCommand
import hu.kozkod.kozadmin.organizations.model.OrganizationRepresentation
import hu.kozkod.kozadmin.organizations.model.StatusType
import hu.kozkod.kozadmin.users.User
import hu.kozkod.kozadmin.users.UserOrganizations
import hu.kozkod.kozadmin.users.UserRepresentation
import hu.kozkod.kozadmin.users.Users
import org.jetbrains.exposed.dao.EntityNotFoundException
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class OrganizationService {
    fun getById(id: Int): OrganizationRepresentation? = transaction {
        try {
            Organization[id].toRepresentation()
        } catch (e: EntityNotFoundException) {
            // todo log
            null
        }
    }

    fun listAll() = transaction {
        Organization.all().map {
            it.toRepresentation()
        }
    }

    fun create(organization: OrganizationCreateCommand): OrganizationRepresentation {
        val newOrganization = transaction {
            Organization.new {
                name = organization.name
                url = organization.url
                status = StatusType.PENDING
            }
        }

        transaction {
            newOrganization.administrators = SizedCollection(
                    organization.administratorIds.mapNotNull {
                        User.findById(it)
                    })
            newOrganization.administrators.forEach {
                if (newOrganization !in it.organizations) {
                    it.organizations = SizedCollection(it.organizations.plus(newOrganization))
                }
            }
        }

        return transaction { newOrganization.toRepresentation() }
    }

    fun getAdmins(id: Int): List<UserRepresentation> = transaction {
        Organization[id].administrators.map { UserRepresentation(it.id.value, it.email, it.firstName, it.lastName) }
    }

    fun getUsers(id: Int): List<UserRepresentation>? =
            transaction {
                try {
                    Organization[id]
                    User.wrapRows(Users.innerJoin(UserOrganizations)
                            .slice(Users.columns)
                            .select { UserOrganizations.organization eq id })
                            .map { UserRepresentation(it.id.value, it.email, it.firstName, it.lastName) }
                } catch (e: EntityNotFoundException) {
                    // todo log
                    null
                }
            }
}