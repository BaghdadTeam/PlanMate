package test.helpers

class FakeGetUserUI : () -> Unit {
    var invoked = false
    override fun invoke() {
        invoked = true
    }
}
