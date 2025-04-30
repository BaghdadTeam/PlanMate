package helpers.authentication

import org.baghdad.logic.model.entities.UserEntity
import org.baghdad.logic.model.entities.UserType

fun createUserHelper(userName: String = "itshaider", password : String= "password".md5WithSalt() ,
                     userType: UserType = UserType.Admin , name : String = "haider")
: UserEntity{
    return UserEntity(username = userName, hashedPassword = password, type = userType, name = name)

}