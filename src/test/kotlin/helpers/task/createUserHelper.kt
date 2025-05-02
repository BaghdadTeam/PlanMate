package helpers.task


fun createUserHelper(
    username: String = "admin",
    password: String = "adminpass",
    role: Role = Role.ADMIN
): User {
    return User(username = username, password = password, role = role)
}