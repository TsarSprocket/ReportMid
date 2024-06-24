package com.tsarsprocket.reportmid.gradle

import org.gradle.api.artifacts.Dependency
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderConvertible
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.api(dependency: Dependency) = addDependency(ConfigTypes.API, dependency)

fun DependencyHandlerScope.api(dependencyProvider: Provider<out Dependency>) = api(dependencyProvider.get())

fun DependencyHandlerScope.api(providerConvertible: ProviderConvertible<out Dependency>) = api(providerConvertible.asProvider())

fun DependencyHandlerScope.debug(dependency: Dependency) = addDependency(ConfigTypes.DEBUG, dependency)

fun DependencyHandlerScope.debug(dependencyProvider: Provider<out Dependency>) = debug(dependencyProvider.get())

fun DependencyHandlerScope.debug(providerConvertible: ProviderConvertible<out Dependency>) = debug(providerConvertible.asProvider())

fun DependencyHandlerScope.impl(dependency: Dependency) = addDependency(ConfigTypes.IMPL, dependency)

fun DependencyHandlerScope.impl(dependencyProvider: Provider<out Dependency>) = impl(dependencyProvider.get())

fun DependencyHandlerScope.impl(providerConvertible: ProviderConvertible<out Dependency>) = impl(providerConvertible.asProvider())

fun DependencyHandlerScope.kapt(dependency: Dependency) = addDependency(ConfigTypes.KAPT, dependency)

fun DependencyHandlerScope.kapt(dependencyProvider: Provider<out Dependency>) = kapt(dependencyProvider.get())

fun DependencyHandlerScope.kapt(providerConvertible: ProviderConvertible<out Dependency>) = kapt(providerConvertible.asProvider())

fun DependencyHandlerScope.ksp(dependency: Dependency) = addDependency(ConfigTypes.KSP, dependency)

fun DependencyHandlerScope.ksp(dependencyProvider: Provider<out Dependency>) = ksp(dependencyProvider.get())

fun DependencyHandlerScope.ksp(providerConvertible: ProviderConvertible<out Dependency>) = ksp(providerConvertible.asProvider())

fun DependencyHandlerScope.test(dependency: Dependency) = addDependency(ConfigTypes.TEST, dependency)

fun DependencyHandlerScope.test(dependencyProvider: Provider<out Dependency>) = test(dependencyProvider.get())

fun DependencyHandlerScope.test(providerConvertible: ProviderConvertible<out Dependency>) = test(providerConvertible.asProvider())

fun DependencyHandlerScope.androidTest(dep: Dependency) = addDependency(ConfigTypes.ANDROID_TEST, dep)

fun DependencyHandlerScope.androidTest(dependencyProvider: Provider<out Dependency>) = androidTest(dependencyProvider.get())

fun DependencyHandlerScope.androidTest(providerConvertible: ProviderConvertible<out Dependency>) = androidTest(providerConvertible.asProvider())

private fun DependencyHandlerScope.addDependency(configType: ConfigTypes, dependency: Dependency) {
    dependencies.add(configType.configName, dependency)
}