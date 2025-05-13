package helpers.authentication

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import java.util.*

fun createUserHelper(

    userName: String = "itshaider",
    userType: UserType = UserType.Admin, name: String = "haider", id: UUID = UUID.randomUUID(),
)
        : UserEntity {
    return UserEntity(id = id,username = userName,type = userType, name = name)

}