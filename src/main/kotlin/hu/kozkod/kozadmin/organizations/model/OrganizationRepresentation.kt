package hu.kozkod.kozadmin.organizations.model

import hu.kozkod.kozadmin.users.UserRepresentation

data class OrganizationRepresentation(val id: Int,
                                      val name: String,
                                      val url: String,
                                      val status: StatusType,
                                      val administrators: List<UserRepresentation>)
