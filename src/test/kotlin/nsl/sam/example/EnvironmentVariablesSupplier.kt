package nsl.sam.example

import java.util.function.Supplier

class EnvironmentVariablesSupplier : Supplier<Map<String, String>> {
    override fun get(): Map<String, String> {
        return mapOf(
                "SMS_ENV_USER_1" to "environment-demo-user:{noop}environment-demo-password USER",
                "SMS_ENV_TOKEN_1" to "TOKEN_READ_FROM_ENVIRONMENT environment-demo-user USER"
        )
    }
}