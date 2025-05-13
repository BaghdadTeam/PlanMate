package helpers.authentication

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType
import org.baghdad.logic.utils.md5WithSalt
import java.util.UUID

fun createUserHelper(

    userName: String = "itshaider", password: String = "password".md5WithSalt(),
    userType: UserType = UserType.Admin, name: String = "haider",id: UUID = UUID.randomUUID(),
)
        : UserEntity {
    return UserEntity(id = id,username = userName,type = userType, name = name)

}