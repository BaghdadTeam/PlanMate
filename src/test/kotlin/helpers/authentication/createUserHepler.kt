package helpers.authentication

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.utils.md5WithSalt

fun createUserHelper(
    userName: String = "itshaider",
    userType: UserType = UserType.Admin, name: String = "haider"
)
        : UserEntity {
    return UserEntity(username = userName, type = userType, name = name)

}