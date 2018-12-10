package nsl.sam.example

import nsl.sam.core.annotation.EnableSimpleAuthenticationMethods
import nsl.sam.method.basicauth.annotation.SimpleBasicAuthentication
import nsl.sam.method.token.annotation.SimpleTokenAuthentication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@Configuration
@EnableSimpleAuthenticationMethods(match="/file-user-info/**")
@SimpleBasicAuthentication(passwordsFilePath = "config/passwords.conf")
@SimpleTokenAuthentication(tokensFilePath = "config/tokens.conf")
class FileCredentialsConfiguration

@Configuration
@EnableSimpleAuthenticationMethods(match="/hardcoded-user-info/**")
@SimpleBasicAuthentication(users = ["hardcoded-demo-user:{noop}hardcoded-demo-password USER"])
@SimpleTokenAuthentication(tokens = ["TOKEN_HARDCODED_IN_ANNOTATION hardcoded-demo-user USER"])
class HardcodedCredentialsConfiguration

@Configuration
@EnableSimpleAuthenticationMethods(match="/environment-user-info/**")
@SimpleBasicAuthentication(usersEnvPrefix = "SMS_ENV_USER")
@SimpleTokenAuthentication(tokensEnvPrefix = "SMS_ENV_TOKEN")
class EnvironmentCredentialsConfiguration

@RestController
@SpringBootApplication
class KotlinSamExampleApplication {

    @GetMapping("/file-user-info")
    fun fileUsers(@AuthenticationPrincipal user: UserDetails?): String {
        return "Well done ${user?.username}! You are authorized basing on credentials stored in file. " +
                "Your roles are: ${user?.authorities}."
    }

    @GetMapping("/hardcoded-user-info")
    fun hardcodedUsers(@AuthenticationPrincipal user: UserDetails?): String {
        return "Well done ${user?.username}! You are authorized basing credentials hardcoded in annotation attribute. " +
                "Your roles are: ${user?.authorities}."
    }

    @GetMapping("/environment-user-info")
    fun environmentUsers(@AuthenticationPrincipal user: UserDetails?): String {
        return "Well done ${user?.username}! You are authorized basing on credentials get from environment variable. " +
                "Your roles are: ${user?.authorities}."
    }
}

fun main(args: Array<String>) {
    val bla : Int = 1
    runApplication<KotlinSamExampleApplication>(*args)
}
