package com.eugeneek.injector

import java.util.*
import kotlin.reflect.KClass


abstract class Scope {
    private val instances = mutableMapOf<KClass<*>, MutableMap<String, Any>>()
    private var parentScope: Scope? = null
    internal val childScopes = HashSet<String>()
    internal val isRoot: Boolean
        get() = parentScope == null

    abstract fun init()

    inline fun <reified T: Any> bind(instance: T, name: String? = T::class.qualifiedName) {
        bind(T::class, instance, name)
    }

    @JvmOverloads
    fun <T: Any> bind(type: KClass<*>, instance: T, name: String? = type.qualifiedName) {
        requireNotNull(name) { "You must provide not null name to bind instance of this class" }

        val namedInstances = instances[type] ?: mutableMapOf()
        namedInstances[name] = instance

        instances[type] = namedInstances
    }

    internal fun setParentScope(parentScope: Scope) {
        this.parentScope = parentScope
    }

    internal fun addChildScopeName(childScopeName: String) {
        childScopes.add(childScopeName)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> getInstance(type: KClass<*>, name: String): T? =
        instances[type]?.get(name) as? T ?: parentScope?.getInstance(type, name)
}