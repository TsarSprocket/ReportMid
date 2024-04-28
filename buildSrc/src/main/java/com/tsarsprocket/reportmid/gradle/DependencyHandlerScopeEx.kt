package com.tsarsprocket.reportmid.gradle

import org.gradle.api.artifacts.Dependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.api(dependency: Dependency) = addDependency(ConfigTypes.API, dependency)

fun DependencyHandlerScope.api(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.API, dependencyProvider.get())

fun DependencyHandlerScope.debug(dependency: Dependency) = addDependency(ConfigTypes.DEBUG, dependency)

fun DependencyHandlerScope.debug(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.DEBUG, dependencyProvider.get())

fun DependencyHandlerScope.impl(dependency: Dependency) = addDependency(ConfigTypes.IMPL, dependency)

fun DependencyHandlerScope.impl(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.IMPL, dependencyProvider.get())

fun DependencyHandlerScope.kapt(dependency: Dependency) = addDependency(ConfigTypes.KAPT, dependency)

fun DependencyHandlerScope.kapt(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.KAPT, dependencyProvider.get())

fun DependencyHandlerScope.test(dependency: Dependency) = addDependency(ConfigTypes.TEST, dependency)

fun DependencyHandlerScope.test(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.TEST, dependencyProvider.get())

fun DependencyHandlerScope.androidTest(dep: Dependency) = addDependency(ConfigTypes.ANDROID_TEST, dep)

fun DependencyHandlerScope.androidTest(dependencyProvider: Provider<out Dependency>) = addDependency(ConfigTypes.ANDROID_TEST, dependencyProvider.get())

private fun DependencyHandlerScope.addDependency(configType: ConfigTypes, dependency: Dependency) {
    dependencies.add(configType.configName, dependency)
}