package fr.lusinelogicielle.mgm.utils

import fr.lusinelogicielle.mgm.model.mootse.MootseDatabase
import fr.lusinelogicielle.mgm.model.mootse.MootseRunner
import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilsTests {
    @Test
    fun `test splitBeforeAndAfterCharacter when character is in the middle`() {
        // Arrange
        val input = "Hello-World"
        val character = '-'

        // Act
        val result = splitBeforeAndAfterCharacter(input, character)

        // Assert
        assertEquals("Hello", result.first)
        assertEquals("World", result.second)
    }

    @Test
    fun `test splitBeforeAndAfterCharacter when character is at the beginning`() {
        // Arrange
        val input = "-Hello"
        val character = '-'

        // Act
        val result = splitBeforeAndAfterCharacter(input, character)

        // Assert
        assertEquals("", result.first)
        assertEquals("Hello", result.second)
    }

    @Test
    fun `test splitBeforeAndAfterCharacter when character is at the end`() {
        // Arrange
        val input = "World-"
        val character = '-'

        // Act
        val result = splitBeforeAndAfterCharacter(input, character)

        // Assert
        assertEquals("World", result.first)
        assertEquals("", result.second)
    }

    @Test
    fun `test splitBeforeAndAfterCharacter when character is not in the string`() {
        // Arrange
        val input = "HelloWorld"
        val character = '-'

        // Act
        val result = splitBeforeAndAfterCharacter(input, character)

        // Assert
        assertEquals("HelloWorld", result.first)
        assertEquals("", result.second)
    }

    @Test
    fun `test forge Docker compose`() {
        // Arrange
        val mailServer = "smtp.test.fr"
        val mailPort = "587"
        val mailUsername = "test"
        val mailPassword = "testPassword"
        val mootseStack = MootseStack(
            name = "mootse-stack-test-2",
            network = "mootse-net-test-2",
            runner = MootseRunner(
                name = "mootse-runner-stack-test2",
                containerImage = "ghcr.io/repo/moodle-grades-scraper:0.0.2",
                url = "https://moodle.test.fr",
                username = "NOM_UTILISATEUR",
                password = "MOT_DE_PASSE",
                scanInterval = 25,
            ),
            database = MootseDatabase(
                name = "mootse-db-stack-test2",
                containerImage = "docker.io/library/mariadb:10.4",
                password = "mypass",
            ),
        )
        val expectedResult = """
        version: '2'
        services:
          mootse-mariadb:
            container_name: mootse-db-stack-test2
            image: docker.io/library/mariadb:10.4
            restart: always
            environment:
              - MYSQL_ROOT_PASSWORD=mypass
            healthcheck:
              test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'
            networks:
              - mootse-net-test-2
          mootse-runner:
            container_name: mootse-runner-stack-test2
            image: ghcr.io/repo/moodle-grades-scraper:0.0.2
            depends_on:
              mootse-mariadb:
                condition: service_healthy
            restart: always
            networks:
              - mootse-net-test-2
            environment:
              - SCAN_INTERVAL=25
              - MOOTSE_URL=https://moodle.test.fr
              - MOOTSE_USERNAME=NOM_UTILISATEUR
              - MOOTSE_PASSWORD=MOT_DE_PASSE
              - MAIL_USERNAME=test
              - MAIL_PASSWORD=testPassword
              - MAIL_SERVER=smtp.test.fr
              - MAIL_PORT=587
              - MAIL_RECIPIENTS=
              - DISCORD_WEBHOOK_URL=
              - DB_HOST=mootse-db-stack-test2
              - DB_USER=root
              - DB_PASSWORD=mypass
              - DB_PORT=3306
              - PROMO=dbPromo
            healthcheck:
              test: 'python healthcheck.py'
        networks:
          mootse-net-test-2:
        """.trimIndent()

        // Act
        val result = forgeDockerComposeFile(
            mootseStack = mootseStack,
            mailServer = mailServer,
            mailPort = mailPort,
            mailUsername = mailUsername,
            mailPassword = mailPassword,
        )

        // Assert
        assertEquals(expectedResult, result)
    }
}
