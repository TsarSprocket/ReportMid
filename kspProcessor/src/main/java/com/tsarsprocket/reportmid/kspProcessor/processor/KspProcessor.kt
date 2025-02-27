package com.tsarsprocket.reportmid.kspProcessor.processor

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueArgument
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier.INTERNAL
import com.google.devtools.ksp.symbol.Variance.CONTRAVARIANT
import com.google.devtools.ksp.symbol.Variance.COVARIANT
import com.google.devtools.ksp.validate
import com.tsarsprocket.reportmid.kspApi.annotation.Capability
import com.tsarsprocket.reportmid.kspApi.annotation.Intent
import com.tsarsprocket.reportmid.kspApi.annotation.LazyProxy
import com.tsarsprocket.reportmid.kspApi.annotation.Reducer
import com.tsarsprocket.reportmid.kspApi.helper.FindTheOnlyOne
import com.tsarsprocket.reportmid.kspProcessor.util.BriefProcessee
import com.tsarsprocket.reportmid.kspProcessor.util.NameInfo
import com.tsarsprocket.reportmid.kspProcessor.util.ReducerData
import com.tsarsprocket.reportmid.kspProcessor.util.ViewIntentData
import com.tsarsprocket.reportmid.kspProcessor.util.doOnEachAnnotated
import java.io.BufferedWriter
import java.util.LinkedList

internal class KspProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val capabilityAnnotationClassName = Capability::class.qualifiedName.orEmpty()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("KSP Processor is invoked")

        val viewIntents = mutableListOf<ViewIntentData>()

        val viewIntentDeclarations = resolver.doOnEachAnnotated<Intent> { declaration -> viewIntents += extractViewIntentData(declaration) }

        val reducers = mutableListOf<ReducerData>()

        val reducerDeclarations = resolver.doOnEachAnnotated<Reducer> { declaration -> reducers += extractReducers(declaration, viewIntents) }

        val lazyProxyDeclarations = resolver.doOnEachAnnotated<LazyProxy> { declaration -> defineLazyProxy(declaration) }

        val capabilityDeclarations = resolver.doOnEachAnnotated<Capability> { declaration -> defineCapability(declaration, reducers) }

        return (viewIntentDeclarations + reducerDeclarations + lazyProxyDeclarations + capabilityDeclarations).filterNot { it.validate() }.toList()
    }

    private fun defineCapability(capabilityClassDeclaration: KSClassDeclaration, reducers: MutableList<ReducerData>) {
        capabilityClassDeclaration.annotations.fold(object {
            var capability: KSAnnotation? = null
            val others = mutableListOf<KSAnnotation>()

            operator fun component1() = capability
            operator fun component2() = others
        }) { acc, annotation ->
            acc.apply {
                if(annotation.annotationType.resolve().declaration.qualifiedName?.asString().orEmpty() == capabilityAnnotationClassName) {
                    capability = annotation
                } else {
                    others += annotation
                }
            }
        }.takeIf { it.capability != null }
            ?.let { (capabilityAnnotation, otherAnnotations) ->
                fun KSValueArgument.toStringList() = (this.value as? List<Any?>)?.filterIsInstance<KSType>()?.map { it.resolveToName() }.orEmpty()

                val packageName = capabilityClassDeclaration.packageName.asString()
                val fileDependency = Dependencies(true, capabilityClassDeclaration.containingFile!!)

                val capabilityName = capabilityClassDeclaration.simpleName
                val capabilityFQName = capabilityClassDeclaration.qualifiedName?.asString().orEmpty()
                val componentName = capabilityName.asString() + COMPONENT_POSTFIX
                val provisionModuleName = capabilityName.asString() + PROVISION_MODULE_POSTFIX
                val isInternal = capabilityClassDeclaration.modifiers.contains(INTERNAL)

                var api = NameInfo(NO_API_PROVIDED, NO_API_PROVIDED)
                val exportBindings = mutableSetOf<String>()
                val modules = mutableListOf<String>()
                var dependencies: List<NameInfo> = emptyList()
                capabilityAnnotation?.arguments?.forEach { argument ->
                    when(argument.name?.asString()) {
                        CAPABILITY_ARGUMENT_API_NAME -> api = (argument.value as KSType).let { NameInfo(it.declaration.simpleName.asString(), it.resolveToName()) }
                        CAPABILITY_ARGUMENT_EXPORT_BINDINGS_NAME -> exportBindings.addAll(argument.toStringList())
                        CAPABILITY_ARGUMENT_MODULES_NAME -> modules.addAll(argument.toStringList())
                        CAPABILITY_ARGUMENT_DEPENDENCIES_NAME -> {
                            dependencies = (argument.value as? List<Any?>)?.filterIsInstance<KSType>()?.map { type ->
                                NameInfo(type.declaration.simpleName.asString(), type.resolveToName())
                            }.orEmpty()
                        }
                    }
                }
                val otherAnnotationsText =
                    otherAnnotations.joinToString(separator = SPACE) { annotation -> "@${annotation.annotationType.resolveToName()}" } // ToDo: If required, support annotations with arguments

                val reducerModules = reducers.filter { it.capability == null || it.capability == capabilityFQName }.mapNotNull { data ->
                    if(data.isAssigned) {
                        logger.error("Ambiguous capability matching for reducer ${data.declaration.qualifiedName?.asString()}")
                        null
                    } else {
                        data.isAssigned = true
                        generateReducerModuleFile(
                            packageName = packageName,
                            reducerData = data,
                            capabilityName = capabilityName.asString(),
                        )
                    }
                }

                if(reducerModules.isNotEmpty()) {
                    modules += reducerModules
                    exportBindings += REDUCER_BINDING_CLASS_NAME
                }

                generateComponentFile(
                    fileDependency = fileDependency,
                    packageName = packageName,
                    componentName = componentName,
                    otherAnnotationsText = otherAnnotationsText,
                    modules = modules,
                    dependencies = dependencies,
                    isInternal = isInternal,
                    capabilityClassDeclaration = capabilityClassDeclaration,
                    api = api,
                    exportBindings = exportBindings,
                )

                val lowercasedComponentName = componentName.startLowercase()

                generateProvisioningModuleFile(
                    fileDependency = fileDependency,
                    packageName = packageName,
                    provisionModuleName = provisionModuleName,
                    api = api,
                    lowercasedComponentName = lowercasedComponentName,
                    componentName = componentName,
                    dependencies = dependencies,
                )

                generateComponentAccessorFile(
                    fileDependency = fileDependency,
                    packageName = packageName,
                    componentName = componentName,
                    provisionModuleName = provisionModuleName,
                    lowercasedComponentName = lowercasedComponentName,
                )
            }
    }

    private fun defineLazyProxy(sourceClassDeclaration: KSClassDeclaration) {
        val className = sourceClassDeclaration.simpleName.asString()
        val dependencies = Dependencies(true, sourceClassDeclaration.containingFile!!)
        val packageName = sourceClassDeclaration.packageName.asString()
        val isInternal = sourceClassDeclaration.modifiers.contains(INTERNAL)

        val lazyProxyClassName = className + LAZY_PROXY_SUFFIX
        val fieldName = className.startLowercase()
        val producerName = "${fieldName}Producer"

        codeGenerator.createNewFile(dependencies, packageName, lazyProxyClassName).bufferedWriter().use { writer ->
            writer.write(
                """
                        package $packageName

                        ${if(isInternal) "internal " else ""}class $lazyProxyClassName($producerName: () -> $className) : $className {

                            private val $fieldName by lazy(${producerName})
                    """.trimIndent() + NEW_LINE
            )

            val classDeclarations = LinkedList<KSClassDeclaration>().apply { add(sourceClassDeclaration) }

            while(classDeclarations.isNotEmpty()) {
                val theClassDeclaration = classDeclarations.pop()
                classDeclarations.addAll(theClassDeclaration.superTypes.map { it.resolve().declaration }.filterIsInstance<KSClassDeclaration>())
                theClassDeclaration.declarations.forEach { declaration ->
                    declaration.accept(FieldVisitor(writer = writer, fieldName = fieldName, logger = logger), Unit)
                }
            }

            writer.write(
                """
                    
                    fun forceInitialize() {
                        $fieldName
                    }
                }$NEW_LINE
                """.trimIndent()
            )
        }
    }

    private fun extractViewIntentData(declaration: KSClassDeclaration): ViewIntentData = declaration.annotations.find { ksAnnotation ->
        ksAnnotation.annotationType.resolve().declaration.qualifiedName?.asString() == Intent::class.qualifiedName
    }?.let { ksAnnotation ->
        val reducerType = (ksAnnotation.arguments.find { it.name?.asString() == Intent::reducer.name }?.value as? KSType)?.takeIf { it.resolveToName() != FindTheOnlyOne::class.qualifiedName }
        ViewIntentData(declaration, reducerType?.resolveToName(), declaration.containingFile!!)
    } ?: throw IllegalStateException("ViewIntent annotation not found") // which is rather unlikely

    private fun extractReducers(declaration: KSClassDeclaration, implicitIntents: MutableList<ViewIntentData>): ReducerData {
        val reducerName = declaration.qualifiedName?.asString()

        return declaration.annotations.find { ksAnnotation ->
            ksAnnotation.annotationType.resolve().declaration.qualifiedName?.asString() == Reducer::class.qualifiedName
        }?.let { ksAnnotation ->
            val explicitIntents = mutableListOf<BriefProcessee>()
            var capability: String? = null

            ksAnnotation.arguments.onEach { annotation ->
                when(annotation.name?.asString()) {
                    Reducer::viewIntents.name -> {
                        explicitIntents.addAll((annotation.value as List<*>).filterIsInstance<KSType>().map { BriefProcessee(it.declaration.simpleName.asString(), it.resolveToName()) })
                    }

                    Reducer::capability.name -> capability = (annotation.value as KSType).resolveToName().takeIf { it != FindTheOnlyOne::class.qualifiedName }
                }
            }

            val (combinedIntents, viewIntentFiles) = object {
                val intents = mutableSetOf<BriefProcessee>()
                val files = mutableSetOf<KSFile>()

                operator fun component1() = intents.toList()
                operator fun component2() = files.toList()
            }.apply {
                explicitIntents.onEach { intent ->
                    if(!intents.contains(intent)) intents.add(intent) else logger.warn("Duplicate view intent $intent in reducer ${declaration.qualifiedName}")
                }

                implicitIntents.onEach { intent ->
                    when {
                        intent.fqReducerName == null && !intent.isAssigned ||
                                intent.fqReducerName == reducerName && !intents.any { declaration.qualifiedName?.asString() == intent.declaration.qualifiedName?.asString() } -> {
                            intents.add(BriefProcessee(intent.declaration.simpleName.asString(), intent.declaration.qualifiedName?.asString()!!))
                            files.add(intent.file)
                            intent.isAssigned = true
                        }

                        intent.fqReducerName == null && intent.isAssigned -> {
                            logger.error("View intent ${intent.declaration.qualifiedName?.asString()} is assigned to multiple reducers")
                        }

                        intent.fqReducerName == reducerName && intents.any { it.fullName == intent.declaration.qualifiedName?.asString() } -> {
                            logger.warn("View intent ${intent.declaration.qualifiedName?.asString()} is assigned to reducer $reducerName both explicitly and implicitly")
                        }
                    }
                }
            }

            ReducerData(declaration, combinedIntents, capability, viewIntentFiles)
        } ?: throw IllegalStateException("ViewIntent annotation not found") // which is rather unlikely
    }

    private fun generateComponentAccessorFile(
        fileDependency: Dependencies,
        packageName: String,
        componentName: String,
        provisionModuleName: String,
        lowercasedComponentName: String
    ) {
        codeGenerator.createNewFile(fileDependency, packageName, COMPONENT_ACCESSOR_FILE_NAME).bufferedWriter().use { writer ->
            writer.write(
                """
                        package $packageName
                        
                        internal val component: $componentName
                            get() = $provisionModuleName.$lowercasedComponentName
                    """.trimIndent() + NEW_LINE
            )
        }
    }

    private fun generateComponentFile(
        fileDependency: Dependencies,
        packageName: String,
        componentName: String,
        otherAnnotationsText: String,
        modules: List<String>,
        dependencies: List<NameInfo>,
        isInternal: Boolean,
        capabilityClassDeclaration: KSClassDeclaration,
        api: NameInfo,
        exportBindings: Set<String>
    ) {
        codeGenerator.createNewFile(fileDependency, packageName, componentName).bufferedWriter().use { writer ->
            writer.write(
                """
                    package $packageName
                    
                    import dagger.*
                    import ${LazyProxy::class.qualifiedName.orEmpty()}
                    
                    @LazyProxy
                    $otherAnnotationsText
                    @Component(
                        modules = [ ${modules.joinToString { "$it::class" }} ],
                        dependencies = [ ${dependencies.joinToString { "${it.qualifiedName}::class" }} ]
                    )
                    ${if(isInternal) "internal " else EMPTY_STRING}interface $componentName : ${
                    capabilityClassDeclaration.simpleName.asString()
                }, ${api.qualifiedName}${exportBindings.takeIf { it.isNotEmpty() }?.joinToString(prefix = ", ").orEmpty()} {
                    
                        @Component.Factory
                        interface Factory {
                            fun create(${dependencies.joinToString { "${it.shortName.startLowercase()}: ${it.qualifiedName}" }}): $componentName
                        }
                    }
                    """.trimIndent() + NEW_LINE
            )
        }
    }

    private fun generateProvisioningModuleFile(
        fileDependency: Dependencies,
        packageName: String,
        provisionModuleName: String,
        api: NameInfo,
        lowercasedComponentName: String,
        componentName: String,
        dependencies: List<NameInfo>
    ) {
        codeGenerator.createNewFile(fileDependency, packageName, provisionModuleName).bufferedWriter().use { writer ->
            writer.write(
                """
                    package $packageName

                    import com.tsarsprocket.reportmid.baseApi.di.AppScope
                    import com.tsarsprocket.reportmid.baseApi.di.BindingExport
                    import dagger.Binds
                    import dagger.Module
                    import dagger.Provides
                    import dagger.multibindings.IntoSet
                    import javax.inject.Provider

                    @Module
                    interface $provisionModuleName {
                    
                        @Binds
                        @IntoSet
                        @AppScope
                        @BindingExport
                        fun bindExports(api: ${api.qualifiedName}): Any
                        
                        companion object {
                        
                            internal lateinit var $lowercasedComponentName: $componentName
                                private set
                                
                            @Provides
                            @AppScope
                            fun provide${api.shortName}(${dependencies.joinToString { "${it.shortName.startLowercase()}: Provider<${it.qualifiedName}>" }}): ${api.qualifiedName} {
                                return try {
                                    $lowercasedComponentName
                                } catch(_: UninitializedPropertyAccessException) {
                                    $componentName$LAZY_PROXY_SUFFIX {
                                        Dagger$componentName.factory().create(${dependencies.joinToString { "${it.shortName.startLowercase()}.get()" }})
                                    }.also {
                                        $lowercasedComponentName = it
                                    }
                                }
                            }
                        }
                    }
                    """.trimIndent() + NEW_LINE
            )
        }
    }

    private fun generateReducerModuleFile(packageName: String, reducerData: ReducerData, capabilityName: String): String {
        return "${capabilityName}${reducerData.declaration.simpleName.asString()}$BINDING_MODULE_POSTFIX".also { moduleName ->
            val reducerName = reducerData.declaration.qualifiedName?.asString()!!

            codeGenerator.createNewFile(
                dependencies = Dependencies(true, reducerData.declaration.containingFile!!, *reducerData.viewIntentFiles.toTypedArray()),
                packageName = packageName,
                fileName = moduleName,
            )
                .bufferedWriter()
                .use { writer ->
                    writer.write(
                        """
                        |package $packageName
                        |
                        |import com.tsarsprocket.reportmid.baseApi.di.PerApi
                        |import com.tsarsprocket.reportmid.viewStateApi.di.ViewIntentKey
                        |import com.tsarsprocket.reportmid.viewStateApi.reducer.ViewStateReducer
                        |import dagger.Binds
                        |import dagger.Module
                        |import dagger.multibindings.IntoMap
                        |
                        |@Module
                        |internal interface $moduleName {${reducerData.viewIntents.joinToString(separator = EMPTY_STRING) { viewIntent -> generateSingleModuleDeclaration(viewIntent, reducerName) }}
                        |}
                        |""".trimMargin()
                    )
                }
        }
    }

    private fun generateSingleModuleDeclaration(viewIntent: BriefProcessee, reducerName: String): String {
        return NEW_LINE + NEW_LINE + """|       @Binds
                                        |       @PerApi
                                        |       @IntoMap
                                        |       @ViewIntentKey(${viewIntent.fullName}::class)
                                        |       fun bindTo${viewIntent.shortName}(reducer: $reducerName): ViewStateReducer""".trimMargin()
    }

    private fun KSTypeReference.resolveToName(recursionLevel: Int = MAX_RECURSION_DEPTH): String {
        if(recursionLevel < 0) throw MaximumRecursionLevelReachedException()

        return resolve().resolveToName(recursionLevel)
    }

    private fun KSType.resolveToName(recursionLevel: Int = MAX_RECURSION_DEPTH): String {
        val name = this.declaration.qualifiedName?.asString().orEmpty()
        val args = this.arguments.map { typeArgument ->
            typeArgument.variance.let { if(it in setOf(COVARIANT, CONTRAVARIANT)) "${it.label} " else it.label } +
                    typeArgument.type?.resolveToName(recursionLevel - 1).orEmpty()
        }
        return if(args.isNotEmpty()) "$name<${args.joinToString()}>" else name
    }

    private fun String.startLowercase(): String = if(isNotEmpty()) this[0].lowercase() + this.substring(1) else ""

    inner class FieldVisitor(
        private val writer: BufferedWriter,
        private val fieldName: String,
        private val logger: KSPLogger,
    ) : KSVisitorVoid() {

        override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: Unit) {
            if(property.extensionReceiver == null && property.isAbstract()) {
                val propertyName = property.simpleName.asString()
                val propertyType = property.type.resolveToName()

                writer.write(
                    "\n" + """
                    |    override val $propertyName: $propertyType
                    |        get() = $fieldName.$propertyName
                    """.trimMargin()
                )

                if(property.isMutable) {
                    writer.write(
                        """
                        |        set(value) { $fieldName.$propertyName = value }
                        """.trimMargin()
                    )
                }

                writer.write(NEW_LINE)
            } else {
                logger.warn("Cannot proxy extension property $property, skipping", property)
            }
        }

        override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
            if(function.functionKind == FunctionKind.MEMBER && function.extensionReceiver == null && function.isAbstract) {
                val functionName = function.simpleName.asString()
                val args = function.parameters.map {
                    FuncArg(it.name?.asString().orEmpty(), it.type.resolveToName())
                }
                val returnType = function.returnType?.resolveToName()
                writer.write(
                    "\n" + """
                    |    override fun $functionName(${args.joinToString { "${it.name}: ${it.type}" }}): $returnType {
                    |        return $fieldName.$functionName(${args.joinToString { it.name }})
                    |    }
                    """.trimMargin()
                )

                writer.write(NEW_LINE)
            } else {
                logger.warn("Cannot proxy function $function, skipping", function)
            }
        }
    }

    private data class FuncArg(val name: String, val type: String)

    private companion object {
        const val BINDING_MODULE_POSTFIX = "BindingModule"
        const val CAPABILITY_ARGUMENT_API_NAME = "api"
        const val CAPABILITY_ARGUMENT_EXPORT_BINDINGS_NAME = "exportBindings"
        const val CAPABILITY_ARGUMENT_MODULES_NAME = "modules"
        const val CAPABILITY_ARGUMENT_DEPENDENCIES_NAME = "dependencies"
        const val COMPONENT_ACCESSOR_FILE_NAME = "component"
        const val COMPONENT_POSTFIX = "Component"
        const val EMPTY_STRING = ""
        const val LAZY_PROXY_SUFFIX = "LazyProxy"
        const val MAX_RECURSION_DEPTH = 1000
        const val NEW_LINE = "\n"
        const val NO_API_PROVIDED = "<no_api_provided>"
        const val PROVISION_MODULE_POSTFIX = "ProvisionModule"
        const val REDUCER_BINDING_CLASS_NAME = "com.tsarsprocket.reportmid.viewStateApi.di.ReducerBinding"
        const val SPACE = " "
    }
}
