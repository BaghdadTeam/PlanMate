package helpers.authentication

import org.baghdad.logic.entities.UserEntity
import org.baghdad.logic.entities.UserType
import org.baghdad.utils.passwordutils.md5WithSalt

fun createUserHelper(userName: String = "itshaider", password : String= "password".md5WithSalt() ,
                     userType: UserType = UserType.Admin , name : String = "haider")
: UserEntity{
    return UserEntity(username = userName, hashedPassword = password, type = userType, name = name)

}