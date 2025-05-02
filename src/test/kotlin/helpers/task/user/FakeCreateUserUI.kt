package test.helpers

import org.baghdad.logic.model.entities.UserEntity

class FakeCreateUserUI : (UserEntity?) -> Unit {
    var invokedWith: UserEntity? = null
    override fun invoke(p1: UserEntity?) {
        invokedWith = p1
    }
}
