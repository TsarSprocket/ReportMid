package com.tsarsprocket.reportmid.lazy_proxy_ksp.processor

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.FunctionKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier.INTERNAL
import com.google.devtools.ksp.validate
import com.tsarsprocket.reportmid.lazy_proxy_ksp.annotation.LazyProxy
import java.io.BufferedWriter
import java.util.LinkedList

class LazyProxyProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        return resolver.getSymbolsWithAnnotation(LazyProxy::class.qualifiedName.orEmpty()).filterIsInstance<KSClassDeclaration>().onEach { rootClassDeclaration ->
            val className = rootClassDeclaration.simpleName.asString()
            val fieldName = className.startLowercase()
            val dependencies = Dependencies(true, rootClassDeclaration.containingFile!!)
            val packageName = rootClassDeclaration.packageName.asString()
            val lazyProxyClassName = className + LAZY_PROXY_SUFFIX
            val producerName = "${fieldName}Producer"
            val isInternal = rootClassDeclaration.modifiers.contains(INTERNAL)

            codeGenerator.createNewFile(dependencies, packageName, lazyProxyClassName).bufferedWriter().use { writer ->
                writer.write(
                    """
                        package $packageName

                        ${if(isInternal) "internal " else ""}class $lazyProxyClassName($producerName: () -> $className) : $className {

                            private val $fieldName by lazy(${producerName})
                    """.trimIndent() + "\n"
                )

                val classDeclarations = LinkedList<KSClassDeclaration>().apply { add(rootClassDeclaration) }

                while(classDeclarations.isNotEmpty()) {
                    val theClassDeclaration = classDeclarations.pop()
                    classDeclarations.addAll(theClassDeclaration.superTypes.map { it.resolve().declaration }.filterIsInstance<KSClassDeclaration>())
                    theClassDeclaration.declarations.forEach { it.accept(FieldVisitor(writer = writer, fieldName = fieldName, logger = logger), Unit) }
                }

                writer.write("\n}\n")
            }
        }.filterNot { it.validate() }.toList()
    }

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
            } else {
                logger.warn("Cannot proxy extension property, skipping", property)
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
            } else {
                logger.warn("Cannot proxy this function", function)
            }
        }

        private fun KSTypeReference.resolveToName() = resolve().declaration.qualifiedName?.asString().orEmpty()
    }

    private data class FuncArg(val name: String, val type: String)

    private fun String.startLowercase(): String = if(isNotEmpty()) this[0].lowercase() + this.substring(1) else ""

    private companion object {
        const val LAZY_PROXY_SUFFIX = "LazyProxy"
    }
}
