package nsl.sam.example

import nsl.sam.envvar.SteeredEnvironmentVariablesAccessor
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class KotlinSamExampleApplicationTests {

    companion object {
        const val FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
                "Well done demo-user! You are authorized basing on credentials stored in file. Your roles are: [ROLE_USER]."

        const val HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
                "Well done hardcoded-demo-user! You are authorized basing credentials hardcoded in annotation attribute. Your roles are: [ROLE_USER]."

        const val ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY =
                "Well done environment-demo-user! You are authorized basing on credentials get from environment variable. Your roles are: [ROLE_USER]."

        @BeforeClass
        @JvmStatic
        fun setUpEnvironmentVariablesProvider() {
            System.setProperty(
                    SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME,
                    EnvironmentVariablesSupplier::class.qualifiedName
            )
        }

        @AfterClass
        @JvmStatic
        fun cleanUpEnvironmentVariablesProvider() {
            System.clearProperty(SteeredEnvironmentVariablesAccessor.SUPPLIER_PROPERTY_NAME)
        }

    }

    @Autowired
    private lateinit var mvc: MockMvc

    @Test
    fun unauthorizedWhenNoCredentialsAtAll() {
        val mvcResult = mvc.perform(get("/file-user-info")).andReturn()

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.UNAUTHORIZED.value(), mvcResult.response.status
        )
    }

	@Test
	fun authorizedWithUserFromFileWhenFileUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/file-user-info").with(httpBasic(
                        "demo-user", "demo-password"
                ))
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
	}

	@Test
	fun authorizedWithTokenFromFileWhenFileUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/file-user-info")
                        .header(
                                "Authorization",
                                "Bearer TOKEN_STORED_IN_FILE"
                        )
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                FILE_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
	}

    @Test
    fun authorizedWithHardcodedUserWhenHardcodedUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/hardcoded-user-info").with(httpBasic(
                        "hardcoded-demo-user", "hardcoded-demo-password"
                ))
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
    }

    @Test
    fun authorizedWithHardcodedTokenWhenHardcodedUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/hardcoded-user-info").header(
                        "Authorization",
                        "Bearer TOKEN_HARDCODED_IN_ANNOTATION"
                )
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                HARDCODED_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
    }

    @Test
    fun authorizedWithEnvironmentUserWhenEnvironmentUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/environment-user-info").with(httpBasic(
                        "environment-demo-user", "environment-demo-password"
                ))
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
    }

    @Test
    fun authorizedWithEnvironmentTokenWhenEnvironmentUserInfoEndpointCalled() {
        val mvcResult = mvc.perform(
                get("/environment-user-info").header(
                        "Authorization",
                        "Bearer TOKEN_READ_FROM_ENVIRONMENT"
                )
        ).andReturn()

        println(mvcResult.response.contentAsString)

        assertEquals(
                "Response status different from expected value.",
                HttpStatus.OK.value(), mvcResult.response.status
        )

        assertEquals(
                "Response body doesn't match the expected value",
                ENVIRONMENT_USER_INFO_ENDPOINT_EXPECTED_RESPONSE_BODY,
                mvcResult.response.contentAsString
        )
    }
}
