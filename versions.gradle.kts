mapOf(
    "compile_sdk_version" to 36,
    "min_sdk_version" to 24,
    "target_sdk_version" to 36,
    "composeCompilerVersion" to "2.1.10",
).entries.forEach {
    project.extra.set(it.key, it.value)
}
