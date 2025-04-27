import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*
import org.junit.jupiter.api.Test

class ArchitectureTest {

    private val basePackage = "org.baghdad" // <<< CHANGE THIS to your base package
    private val importedClasses: JavaClasses = ClassFileImporter()
        .importPackages(basePackage)

    @Test
    fun `logic layer should not depend on data or ui layers`() {
        noClasses()
            .that().resideInAPackage("$basePackage.logic..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                "$basePackage.data..",
                "$basePackage.presentation.."
            )
            .check(importedClasses)
    }

    @Test
    fun `data layer should only depend on logic layer`() {
        classes()
            .that().resideInAPackage("$basePackage.data..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$basePackage.data..",
                "$basePackage.logic..",
                "java..",
                "kotlin.."
            )
            .allowEmptyShould(true)
            .check(importedClasses)
    }

    @Test
    fun `presentation layer should only depend on logic layer`() {
        classes()
            .that().resideInAPackage("$basePackage.presentation..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "$basePackage.presentation..",
                "$basePackage.logic..",
                "java..",
                "kotlin.."
            )
            .allowEmptyShould(true)
            .check(importedClasses)
    }

    @Test
    fun `repositories should reside in data repositories package`() {
        classes()
            .that().haveSimpleNameEndingWith("Repository")
            .should().resideInAPackage("$basePackage.logic.repositories..")
            .check(importedClasses)
    }

    @Test
    fun `entities should reside in logic entities package`() {
        classes()
            .that().haveSimpleNameEndingWith("Entity")
            .should().resideInAPackage("$basePackage.logic.entities..")
            .check(importedClasses)
    }
}