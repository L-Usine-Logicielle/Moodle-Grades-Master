package fr.lusinelogicielle.mgm.mootse

import fr.lusinelogicielle.mgm.clients.PortainerApiClientImpl
import fr.lusinelogicielle.mgm.model.mootse.MootseDatabase
import fr.lusinelogicielle.mgm.model.mootse.MootseRunner
import fr.lusinelogicielle.mgm.model.mootse.MootseStack
import fr.lusinelogicielle.mgm.model.portainer.Stack
import fr.lusinelogicielle.mgm.service.portainer.mootse.MootseStackServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.springframework.http.HttpMethod
import org.springframework.test.util.ReflectionTestUtils

class MootseTests {
    private lateinit var portainerApiClient: PortainerApiClientImpl
    private lateinit var mootseStackService: MootseStackServiceImpl

    @BeforeEach
    fun setUp() {
        portainerApiClient = mock(PortainerApiClientImpl::class.java)
        mootseStackService = MootseStackServiceImpl(portainerApiClient)
        ReflectionTestUtils.setField(mootseStackService, "mailServer", "smtp.test.fr")
        ReflectionTestUtils.setField(mootseStackService, "mailPort", "587")
        ReflectionTestUtils.setField(mootseStackService, "mailUsername", "test-user")
        ReflectionTestUtils.setField(mootseStackService, "mailPassword", "test-password")
    }

    @Test
    fun `test getStackById`() {
        // Arrange
        val stackId = 62
        val responseJsonFile62 = "{\"StackFileContent\":\"version: '2'\\nservices:\\n  mootse-mariadb:\\n    container_name: mootse-db-stack-test22\\n    image: docker.io/library/mariadb:10.4\\n    restart: always\\n    environment:\\n      - MYSQL_ROOT_PASSWORD=mypass\\n    healthcheck:\\n      test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'\\n    networks:\\n      - mootse-net-test-22\\n  mootse-runner:\\n    container_name: mootse-runner-stack-test22\\n    image: ghcr.io/repo/moodle-grades-scraper:0.0.2\\n    depends_on:\\n      mootse-mariadb:\\n        condition: service_healthy\\n    restart: always\\n    networks:\\n      - mootse-net-test-22\\n    environment:\\n      - SCAN_INTERVAL=2555\\n      - MOOTSE_URL=https://moodle.test.fr\\n      - MOOTSE_USERNAME=NOM_UTILISATEUR\\n      - MOOTSE_PASSWORD=MOT_DE_PASSE\\n      - MAIL_USERNAME=no-reply@test.fr\\n      - MAIL_PASSWORD=PSNtaz7iNpFnZ5V\$\$@KrUH~5RFhbDw3jZW2V9m?Qk\\n      - MAIL_SERVER=smtp.ionos.fr\\n      - MAIL_PORT=587\\n      - MAIL_RECIPIENTS=\\n      - DISCORD_WEBHOOK_URL=\\n      - DB_HOST=mootse-db-stack-test22\\n      - DB_USER=root\\n      - DB_PASSWORD=mypass\\n      - DB_PORT=3306\\n      - PROMO=dbPromo\\n    healthcheck:\\n      test: 'python healthcheck.py'\\nnetworks:\\n  mootse-net-test-22:\"}"
        val responseJson62 = "{\"Id\":62,\"Name\":\"mootse-stack-test-22\",\"Type\":2,\"EndpointId\":2,\"SwarmId\":\"\",\"EntryPoint\":\"docker-compose.yml\",\"Env\":null,\"ResourceControl\":{\"Id\":98,\"ResourceId\":\"2_mootse-stack-test-22\",\"SubResourceIds\":[],\"Type\":6,\"UserAccesses\":[],\"TeamAccesses\":[],\"Public\":false,\"AdministratorsOnly\":true,\"System\":false},\"Status\":1,\"ProjectPath\":\"/data/compose/62\",\"CreationDate\":1692121421,\"CreatedBy\":\"lucas\",\"UpdateDate\":0,\"UpdatedBy\":\"\",\"AdditionalFiles\":null,\"AutoUpdate\":null,\"Option\":null,\"GitConfig\":null,\"FromAppTemplate\":false,\"Namespace\":\"\",\"IsComposeFormat\":false,\"Webhook\":\"\",\"SupportRelativePath\":false,\"FilesystemPath\":\"\"}\n"
        val mootseStack = MootseStack(
            name = "mootse-stack-test-22",
            network = "mootse-net-test-22",
            runner = MootseRunner(
                name = "mootse-runner-stack-test22",
                containerImage = "ghcr.io/repo/moodle-grades-scraper:0.0.2",
                url = "https://moodle.test.fr",
                username = "NOM_UTILISATEUR",
                password = "MOT_DE_PASSE",
                scanInterval = 2555,
            ),
            database = MootseDatabase(
                name = "mootse-db-stack-test22",
                containerImage = "docker.io/library/mariadb:10.4",
                password = "mypass",
            ),
        )
        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/62/file", null))
            .thenReturn(responseJsonFile62)

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/62", null))
            .thenReturn(responseJson62)

        // Act
        val result = mootseStackService.getStackById(stackId)

        // Assert
        assertEquals(mootseStack, result)
    }

    @Test
    fun `test getAllStacks`() {
        // Arrange
        val mootseStacks1 = MootseStack(
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
        val mootseStacks2 = MootseStack(
            name = "mootse-stack-test-22",
            network = "mootse-net-test-22",
            runner = MootseRunner(
                name = "mootse-runner-stack-test22",
                containerImage = "ghcr.io/repo/moodle-grades-scraper:0.0.2",
                url = "https://moodle.test.fr",
                username = "NOM_UTILISATEUR",
                password = "MOT_DE_PASSE",
                scanInterval = 2555,
            ),
            database = MootseDatabase(
                name = "mootse-db-stack-test22",
                containerImage = "docker.io/library/mariadb:10.4",
                password = "mypass",
            ),
        )
        val mootseStacksList = listOf(mootseStacks1, mootseStacks2)

        val responseJsonFile61 = "{\"StackFileContent\":\"version: '2'\\nservices:\\n  mootse-mariadb:\\n    container_name: mootse-db-stack-test2\\n    image: docker.io/library/mariadb:10.4\\n    restart: always\\n    environment:\\n      - MYSQL_ROOT_PASSWORD=mypass\\n    healthcheck:\\n      test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'\\n    networks:\\n      - mootse-net-test-2\\n  mootse-runner:\\n    container_name: mootse-runner-stack-test2\\n    image: ghcr.io/repo/moodle-grades-scraper:0.0.2\\n    depends_on:\\n      mootse-mariadb:\\n        condition: service_healthy\\n    restart: always\\n    networks:\\n      - mootse-net-test-2\\n    environment:\\n      - SCAN_INTERVAL=25\\n      - MOOTSE_URL=https://moodle.test.fr\\n      - MOOTSE_USERNAME=NOM_UTILISATEUR\\n      - MOOTSE_PASSWORD=MOT_DE_PASSE\\n      - MAIL_USERNAME=no-reply@test.fr\\n      - MAIL_PASSWORD=PSNtaz7iNpFnZ5V\$\$@KrUH~5RFhbDw3jZW2V9m?Qk\\n      - MAIL_SERVER=smtp.ionos.fr\\n      - MAIL_PORT=587\\n      - MAIL_RECIPIENTS=\\n      - DISCORD_WEBHOOK_URL=\\n      - DB_HOST=mootse-db-stack-test2\\n      - DB_USER=root\\n      - DB_PASSWORD=mypass\\n      - DB_PORT=3306\\n      - PROMO=dbPromo\\n    healthcheck:\\n      test: 'python healthcheck.py'\\nnetworks:\\n  mootse-net-test-2:\"}\n"
        val responseJson61 = "{\"Id\":61,\"Name\":\"mootse-stack-test-2\",\"Type\":2,\"EndpointId\":2,\"SwarmId\":\"\",\"EntryPoint\":\"docker-compose.yml\",\"Env\":null,\"ResourceControl\":{\"Id\":97,\"ResourceId\":\"2_mootse-stack-test-2\",\"SubResourceIds\":[],\"Type\":6,\"UserAccesses\":[],\"TeamAccesses\":[],\"Public\":false,\"AdministratorsOnly\":true,\"System\":false},\"Status\":1,\"ProjectPath\":\"/data/compose/61\",\"CreationDate\":1692121299,\"CreatedBy\":\"lucas\",\"UpdateDate\":0,\"UpdatedBy\":\"\",\"AdditionalFiles\":null,\"AutoUpdate\":null,\"Option\":null,\"GitConfig\":null,\"FromAppTemplate\":false,\"Namespace\":\"\",\"IsComposeFormat\":false,\"Webhook\":\"\",\"SupportRelativePath\":false,\"FilesystemPath\":\"\"}\n"
        val responseJsonFile62 = "{\"StackFileContent\":\"version: '2'\\nservices:\\n  mootse-mariadb:\\n    container_name: mootse-db-stack-test22\\n    image: docker.io/library/mariadb:10.4\\n    restart: always\\n    environment:\\n      - MYSQL_ROOT_PASSWORD=mypass\\n    healthcheck:\\n      test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'\\n    networks:\\n      - mootse-net-test-22\\n  mootse-runner:\\n    container_name: mootse-runner-stack-test22\\n    image: ghcr.io/repo/moodle-grades-scraper:0.0.2\\n    depends_on:\\n      mootse-mariadb:\\n        condition: service_healthy\\n    restart: always\\n    networks:\\n      - mootse-net-test-22\\n    environment:\\n      - SCAN_INTERVAL=2555\\n      - MOOTSE_URL=https://moodle.test.fr\\n      - MOOTSE_USERNAME=NOM_UTILISATEUR\\n      - MOOTSE_PASSWORD=MOT_DE_PASSE\\n      - MAIL_USERNAME=no-reply@test.fr\\n      - MAIL_PASSWORD=PSNtaz7iNpFnZ5V\$\$@KrUH~5RFhbDw3jZW2V9m?Qk\\n      - MAIL_SERVER=smtp.ionos.fr\\n      - MAIL_PORT=587\\n      - MAIL_RECIPIENTS=\\n      - DISCORD_WEBHOOK_URL=\\n      - DB_HOST=mootse-db-stack-test22\\n      - DB_USER=root\\n      - DB_PASSWORD=mypass\\n      - DB_PORT=3306\\n      - PROMO=dbPromo\\n    healthcheck:\\n      test: 'python healthcheck.py'\\nnetworks:\\n  mootse-net-test-22:\"}"
        val responseJson62 = "{\"Id\":62,\"Name\":\"mootse-stack-test-22\",\"Type\":2,\"EndpointId\":2,\"SwarmId\":\"\",\"EntryPoint\":\"docker-compose.yml\",\"Env\":null,\"ResourceControl\":{\"Id\":98,\"ResourceId\":\"2_mootse-stack-test-22\",\"SubResourceIds\":[],\"Type\":6,\"UserAccesses\":[],\"TeamAccesses\":[],\"Public\":false,\"AdministratorsOnly\":true,\"System\":false},\"Status\":1,\"ProjectPath\":\"/data/compose/62\",\"CreationDate\":1692121421,\"CreatedBy\":\"lucas\",\"UpdateDate\":0,\"UpdatedBy\":\"\",\"AdditionalFiles\":null,\"AutoUpdate\":null,\"Option\":null,\"GitConfig\":null,\"FromAppTemplate\":false,\"Namespace\":\"\",\"IsComposeFormat\":false,\"Webhook\":\"\",\"SupportRelativePath\":false,\"FilesystemPath\":\"\"}\n"
        val responseJson = """
        [
            {
                "Id": 4,
                "Name": "proxy",
                "Type": 2,
                "EndpointId": 2,
                "SwarmId": "",
                "EntryPoint": "docker-compose.yml",
                "Env": [],
                "ResourceControl": {
                    "Id": 6,
                    "ResourceId": "2_proxy",
                    "SubResourceIds": [],
                    "Type": 6,
                    "UserAccesses": [],
                    "TeamAccesses": [],
                    "Public": false,
                    "AdministratorsOnly": true,
                    "System": false
                },
                "Status": 1,
                "ProjectPath": "/data/compose/4",
                "CreationDate": 1657650416,
                "CreatedBy": "lucas",
                "UpdateDate": 0,
                "UpdatedBy": "",
                "AdditionalFiles": null,
                "AutoUpdate": null,
                "Option": null,
                "GitConfig": null,
                "FromAppTemplate": false,
                "Namespace": "",
                "IsComposeFormat": false,
                "Webhook": "",
                "SupportRelativePath": false,
                "FilesystemPath": ""
            },
            {
                "Id": 5,
                "Name": "ddns-cloud",
                "Type": 2,
                "EndpointId": 2,
                "SwarmId": "",
                "EntryPoint": "docker-compose.yml",
                "Env": [
                    {
                        "name": "API_KEY",
                        "value": "KqWFqihzce8VmyqUpJ6qr0GXaVvolPgyjAXrlLYg"
                    },
                    {
                        "name": "API_ZONE",
                        "value": "test.fr"
                    },
                    {
                        "name": "API_SUBDOMAIN",
                        "value": "cloud"
                    }
                ],
                "ResourceControl": {
                    "Id": 8,
                    "ResourceId": "2_ddns-cloud",
                    "SubResourceIds": [],
                    "Type": 6,
                    "UserAccesses": [],
                    "TeamAccesses": [],
                    "Public": false,
                    "AdministratorsOnly": true,
                    "System": false
                },
                "Status": 1,
                "ProjectPath": "/data/compose/5",
                "CreationDate": 1657844924,
                "CreatedBy": "lucas",
                "UpdateDate": 1659484011,
                "UpdatedBy": "lucas",
                "AdditionalFiles": null,
                "AutoUpdate": null,
                "Option": null,
                "GitConfig": null,
                "FromAppTemplate": false,
                "Namespace": "",
                "IsComposeFormat": false,
                "Webhook": "",
                "SupportRelativePath": false,
                "FilesystemPath": ""
            },
            {
                "Id": 61,
                "Name": "mootse-stack-test-2",
                "Type": 2,
                "EndpointId": 2,
                "SwarmId": "",
                "EntryPoint": "docker-compose.yml",
                "Env": null,
                "ResourceControl": {
                    "Id": 97,
                    "ResourceId": "2_mootse-stack-test-2",
                    "SubResourceIds": [],
                    "Type": 6,
                    "UserAccesses": [],
                    "TeamAccesses": [],
                    "Public": false,
                    "AdministratorsOnly": true,
                    "System": false
                },
                "Status": 1,
                "ProjectPath": "/data/compose/61",
                "CreationDate": 1692121299,
                "CreatedBy": "lucas",
                "UpdateDate": 0,
                "UpdatedBy": "",
                "AdditionalFiles": null,
                "AutoUpdate": null,
                "Option": null,
                "GitConfig": null,
                "FromAppTemplate": false,
                "Namespace": "",
                "IsComposeFormat": false,
                "Webhook": "",
                "SupportRelativePath": false,
                "FilesystemPath": ""
            },
            {
                "Id": 62,
                "Name": "mootse-stack-test-22",
                "Type": 2,
                "EndpointId": 2,
                "SwarmId": "",
                "EntryPoint": "docker-compose.yml",
                "Env": null,
                "ResourceControl": {
                    "Id": 98,
                    "ResourceId": "2_mootse-stack-test-22",
                    "SubResourceIds": [],
                    "Type": 6,
                    "UserAccesses": [],
                    "TeamAccesses": [],
                    "Public": false,
                    "AdministratorsOnly": true,
                    "System": false
                },
                "Status": 1,
                "ProjectPath": "/data/compose/62",
                "CreationDate": 1692121421,
                "CreatedBy": "lucas",
                "UpdateDate": 0,
                "UpdatedBy": "",
                "AdditionalFiles": null,
                "AutoUpdate": null,
                "Option": null,
                "GitConfig": null,
                "FromAppTemplate": false,
                "Namespace": "",
                "IsComposeFormat": false,
                "Webhook": "",
                "SupportRelativePath": false,
                "FilesystemPath": ""
            }
        ]
        """.trimIndent()

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks", null))
            .thenReturn(responseJson)

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/61/file", null))
            .thenReturn(responseJsonFile61)

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/61", null))
            .thenReturn(responseJson61)

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/62/file", null))
            .thenReturn(responseJsonFile62)

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.GET, "/api/stacks/62", null))
            .thenReturn(responseJson62)

        // Act
        val result = mootseStackService.getAllStacks()

        // Assert
        assertEquals(mootseStacksList, result)
    }

    @Test
    fun `test createStack`() {
        // Arrange
        val mootseStack = MootseStack(
            name = "mootse-stack-test-22",
            network = "mootse-net-test-22",
            runner = MootseRunner(
                name = "mootse-runner-stack-test22",
                containerImage = "ghcr.io/repo/moodle-grades-scraper:0.0.2",
                url = "https://moodle.test.fr",
                username = "NOM_UTILISATEUR",
                password = "MOT_DE_PASSE",
                scanInterval = 2555,
            ),
            database = MootseDatabase(
                name = "mootse-db-stack-test22",
                containerImage = "docker.io/library/mariadb:10.4",
                password = "mypass",
            ),
        )

        val stackJson = """
        {
            "fromAppTemplate": false,
            "name": "mootse-stack-test-22",
            "stackFileContent": "version: '2'\nservices:\n  mootse-mariadb:\n    container_name: mootse-db-stack-test22\n    image: docker.io/library/mariadb:10.4\n    restart: always\n    environment:\n      - MYSQL_ROOT_PASSWORD=mypass\n    healthcheck:\n      test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'\n    networks:\n      - mootse-net-test-22\n  mootse-runner:\n    container_name: mootse-runner-stack-test22\n    image: ghcr.io/repo/moodle-grades-scraper:0.0.2\n    depends_on:\n      mootse-mariadb:\n        condition: service_healthy\n    restart: always\n    networks:\n      - mootse-net-test-22\n    environment:\n      - SCAN_INTERVAL=2555\n      - MOOTSE_URL=https://moodle.test.fr\n      - MOOTSE_USERNAME=NOM_UTILISATEUR\n      - MOOTSE_PASSWORD=MOT_DE_PASSE\n      - MAIL_USERNAME=test-user\n      - MAIL_PASSWORD=test-password\n      - MAIL_SERVER=smtp.test.fr\n      - MAIL_PORT=587\n      - MAIL_RECIPIENTS=\n      - DISCORD_WEBHOOK_URL=\n      - DB_HOST=mootse-db-stack-test22\n      - DB_USER=root\n      - DB_PASSWORD=mypass\n      - DB_PORT=3306\n      - PROMO=dbPromo\n      - MOOTSE_MASTER_URL=http://10.243.103.188:8090\n    healthcheck:\n      test: 'python healthcheck.py'\nnetworks:\n  mootse-net-test-22:"
        }
        """.trimIndent()

        val stackMock = """
        {
            "Id": 11,
            "Name": "mootse-stack-test-22",
            "Type": 2,
            "EndpointId": 2,
            "SwarmId": "",
            "EntryPoint": "docker-compose.yml",
            "Env": null,
            "ResourceControl": {
                "Id": 50,
                "ResourceId": "2_test-mootse",
                "SubResourceIds": [],
                "Type": 6,
                "UserAccesses": [],
                "TeamAccesses": [],
                "Public": false,
                "AdministratorsOnly": true,
                "System": false
            },
            "Status": 1,
            "ProjectPath": "/data/compose/11",
            "CreationDate": 1690922520,
            "CreatedBy": "lucas",
            "UpdateDate": 0,
            "UpdatedBy": "",
            "AdditionalFiles": null,
            "AutoUpdate": null,
            "Option": null,
            "GitConfig": null,
            "FromAppTemplate": false,
            "Namespace": "",
            "IsComposeFormat": false,
            "Webhook": ""
        }
        """.trimIndent()

        val stack = Stack(
            id = 11,
            name = "mootse-stack-test-22",
            type = 2,
            endpointId = 2,
        )

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.POST, "/api/stacks?type=2&method=string&endpointId=2", stackJson))
            .thenReturn(stackMock)

        // Act
        val result = mootseStackService.createStack(mootseStack)

        // Assert
        assertEquals(stack, result)
    }

    @Test
    fun `test updateStack`() {
        // Arrange
        val mootseStack = MootseStack(
            name = "mootse-stack-test-22",
            network = "mootse-net-test-22",
            runner = MootseRunner(
                name = "mootse-runner-stack-test22",
                containerImage = "ghcr.io/repo/moodle-grades-scraper:0.0.2",
                url = "https://moodle.test.fr",
                username = "NOM_UTILISATEUR",
                password = "MOT_DE_PASSE",
                scanInterval = 2555,
                mootseMasterUrl = "http://10.243.103.188:8090",
            ),
            database = MootseDatabase(
                name = "mootse-db-stack-test22",
                containerImage = "docker.io/library/mariadb:10.4",
                password = "mypass",
            ),
        )

        val stack = Stack(
            id = 62,
            name = "mootse-stack-test-22",
            type = 2,
            endpointId = 2,
        )

        val responseJson = """
        {
            "Id": 62,
            "Name": "mootse-stack-test-22",
            "Type": 2,
            "EndpointId": 2,
            "SwarmId": "",
            "EntryPoint": "docker-compose.yml",
            "Env": null,
            "ResourceControl": null,
            "Status": 1,
            "ProjectPath": "/data/compose/11",
            "CreationDate": 1690922520,
            "CreatedBy": "user",
            "UpdateDate": 1690922790,
            "UpdatedBy": "user",
            "AdditionalFiles": null,
            "AutoUpdate": null,
            "Option": null,
            "GitConfig": null,
            "FromAppTemplate": false,
            "Namespace": "",
            "IsComposeFormat": false,
            "Webhook": ""
        }
        """.trimIndent()

        val requestBodyJson = """
        {
            "prune": false,
            "pullImage": true,
            "stackFileContent": "version: '2'\nservices:\n  mootse-mariadb:\n    container_name: mootse-db-stack-test22\n    image: docker.io/library/mariadb:10.4\n    restart: always\n    environment:\n      - MYSQL_ROOT_PASSWORD=mypass\n    healthcheck:\n      test: '/usr/local/bin/healthcheck.sh --su-mysql --connect --innodb_initialized'\n    networks:\n      - mootse-net-test-22\n  mootse-runner:\n    container_name: mootse-runner-stack-test22\n    image: ghcr.io/repo/moodle-grades-scraper:0.0.2\n    depends_on:\n      mootse-mariadb:\n        condition: service_healthy\n    restart: always\n    networks:\n      - mootse-net-test-22\n    environment:\n      - SCAN_INTERVAL=2555\n      - MOOTSE_URL=https://moodle.test.fr\n      - MOOTSE_USERNAME=NOM_UTILISATEUR\n      - MOOTSE_PASSWORD=MOT_DE_PASSE\n      - MAIL_USERNAME=test-user\n      - MAIL_PASSWORD=test-password\n      - MAIL_SERVER=smtp.test.fr\n      - MAIL_PORT=587\n      - MAIL_RECIPIENTS=\n      - DISCORD_WEBHOOK_URL=\n      - DB_HOST=mootse-db-stack-test22\n      - DB_USER=root\n      - DB_PASSWORD=mypass\n      - DB_PORT=3306\n      - PROMO=dbPromo\n      - MOOTSE_MASTER_URL=http://10.243.103.188:8090\n    healthcheck:\n      test: 'python healthcheck.py'\nnetworks:\n  mootse-net-test-22:"
        }
        """.trimIndent()

        Mockito.`when`(portainerApiClient.makeRequest(HttpMethod.PUT, "/api/stacks/62?endpointId=2", requestBodyJson))
            .thenReturn(responseJson)

        // Act
        val result = mootseStackService.updateStack(62, mootseStack)

        // Assert
        assertEquals(stack, result)
    }
}
