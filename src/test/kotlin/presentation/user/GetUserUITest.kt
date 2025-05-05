package org.baghdad.presentation.user

import io.mockk.*
import kotlin.test.*
import org.baghdad.logic.model.entities.*
import org.baghdad.logic.model.exceptions.user.*
import org.baghdad.logic.usecase.user.GetUserByUsernameUseCase
import org.baghdad.presentation.input.Reader
import org.baghdad.presentation.output.Viewer

class GetUserUITest {

    private lateinit var inputReader: Reader
    private lateinit var outputViewer: Viewer
    private lateinit var getUserByUsernameUseCase: GetUserByUsernameUseCase
    private lateinit var getUserUI: GetUserUI

    @BeforeTest
    fun setup() {
        getUserByUsernameUseCase = mockk()
        outputViewer = mockk(relaxed = true)
    }

    @Test
    fun `عند إدخال اسم مستخدم فارغ، يجب عرض رسالة تفيد بعدم العثور على المستخدم`() {
        // Given
        inputReader = mockk {
            every { readInput() } returns ""
        }
        getUserUI = GetUserUI(inputReader, outputViewer, getUserByUsernameUseCase)

        // When
        getUserUI.run()

        // Then
        verify {
            outputViewer.logMessage(match { it.contains("not found", ignoreCase = true) })
        }
    }

    @Test
    fun `عند إدخال اسم مستخدم غير صالح، يجب عرض رسالة خطأ توضح أن اسم المستخدم غير صالح`() {
        // Given
        val invalidUsername = "invalid"
        inputReader = mockk {
            every { readInput() } returns invalidUsername
        }
        every { getUserByUsernameUseCase.invoke(invalidUsername) } throws InvalidUsernameException("Username is invalid")

        getUserUI = GetUserUI(inputReader, outputViewer, getUserByUsernameUseCase)

        // When
        getUserUI.run()

        // Then
        verify {
            outputViewer.logError(match { it.contains("Invalid username", ignoreCase = true) })
        }
    }

    @Test
    fun `عند إدخال اسم مستخدم غير موجود، يجب عرض رسالة توضح أن المستخدم غير موجود`() {
        // Given
        val nonExistentUsername = "notExist"
        inputReader = mockk {
            every { readInput() } returns nonExistentUsername
        }
        every { getUserByUsernameUseCase.invoke(nonExistentUsername) } throws UserNotFoundException(nonExistentUsername)

        getUserUI = GetUserUI(inputReader, outputViewer, getUserByUsernameUseCase)

        // When
        getUserUI.run()

        // Then
        verify {
            outputViewer.logError(match { it.contains(nonExistentUsername) })
        }
    }

    @Test
    fun `عند إدخال اسم مستخدم موجود، يجب عرض رسالة تؤكد العثور عليه بنجاح`() {
        // Given
        val validUsername = "foundUser"
        val foundUser = UserEntity(name = "Ali", username = validUsername, hashedPassword = "secret", type = UserType.Mate)

        inputReader = mockk {
            every { readInput() } returns validUsername
        }
        every { getUserByUsernameUseCase.invoke(validUsername) } returns foundUser

        getUserUI = GetUserUI(inputReader, outputViewer, getUserByUsernameUseCase)

        // When
        getUserUI.run()

        // Then
        verify {
            outputViewer.logMessage(match { it.contains("Found: $validUsername") })
        }
    }
}
