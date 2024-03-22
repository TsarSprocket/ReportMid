mapOf(
    "compile_sdk_version" to 34,
    "min_sdk_version" to 24,
    "target_sdk_version" to 34,
    "composeCompilerVersion" to "1.5.10",
).entries.forEach {
    project.extra.set(it.key, it.value)
}
