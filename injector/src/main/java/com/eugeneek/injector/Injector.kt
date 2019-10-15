package com.eugeneek.injector

import java.util.*
import kotlin.reflect.KClass


object Injector {
    private val scopes = HashMap<String, Scope>()

    @JvmStatic
    @JvmOverloads
    fun openScope(scope: Scope, parentScopeClass: KClass<*>? = null) {
        val scopeName = scope::class.qualifiedName ?: throw IllegalArgumentException(
            "Scope class should not be local or anonymous object")

        if (parentScopeClass == null) {
            for (openedScope in scopes) {
                if (openedScope.value.isRoot && openedScope.key != scopeName) {
                    throw IllegalArgumentException("Only one root scope can be opened. Use root scope name " +
                            "'${openedScope.key}' or other opened scope name as 'parentScopeName' parameter")
                }

            }
        } else {
            val parentScope = scopes[parentScopeClass.qualifiedName] ?: throw IllegalArgumentException(
                "Parent scope with name '${parentScopeClass.qualifiedName}' is not opened or was closed")

            parentScope.addChildScopeName(scopeName)
            scope.setParentScope(parentScope)
        }

        scope.init()
        scopes[scopeName] = scope
    }

    @JvmStatic
    fun closeScope(scopeClass: KClass<*>) {
        val scopeName = scopeClass.qualifiedName ?: throw IllegalStateException(
            "Tried to close invalid scope class: $scopeClass")

        closeScopeByName(scopeName)
    }

    private fun closeScopeByName(scopeName: String) {
        val scope = scopes[scopeName]
        if (scope != null) {
            for (childScope in scope.childScopes) {
                closeScopeByName(childScope)
            }
            scopes.remove(scopeName)
        }
    }

    inline fun <reified T> inject(name: String? = T::class.qualifiedName): Lazy<T> = lazy {
        get<T>(name)
    }

    inline fun <reified T> get(name: String? = T::class.qualifiedName): T =
        get(T::class, name)

    @JvmStatic
    @JvmOverloads
    fun <T> get(type: KClass<*>, name: String? = type.qualifiedName): T {
        if (name == null) {
            throw IllegalArgumentException("You need to provide not null 'name' parameter to get instance of this class")
        }

        for (scope in scopes.values) {
            scope.getInstance<T>(type, name)?.let { return it }
        }

        val namedErrorAddition = if (name != type.qualifiedName) {
            " and name \"$name\""
        } else {
            ". Check that you provided unnamed instance for this type"
        }

        throw IllegalArgumentException("No instances bound for $type class$namedErrorAddition")
    }
}