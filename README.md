# Injector
[![](https://jitpack.io/v/eugeneek/Injector.svg)](https://jitpack.io/#eugeneek/Injector)

Simple two-classes DI container with scopes. Written in Kotlin but Java-friendly. 

## How to use
Inherit `Scope` class to create scoped container for your instances. Create instances and `bind()` them to this scope. Open created scope and request instances whenever you need with object `Injector`. Close scope in right place to avoid memory leaks

### Create instances
For example, let's create scope, that will contains object `ResourceManager`. It responsible for providing text resources through  Android SDK's `Resources` class.

```kotlin
class MainScope(  
    private val context: Context  
): Scope() {  
  
    override fun init() {  
        // initialization
        val resourceManager = ResourceManager(context.resources)
        
        // binding
        bind(resourceManager)
    }  
}
```

### Open/close scope
 For `ResourceManager` it is important to has actual `Resources` object for accessing right resources (depends on Locale, screen orientation, etc.), so we can open and close scope in related lifecycle callback methods of our MainActivity:
```kotlin
...
override fun attachBaseContext(newBase: Context?) {  
    super.attachBaseContext(newBase)  
	
	val mainScope = MainScope(this)  
    Injector.openScope(mainScope, AppScope::class)  
}
...
override fun onDestroy() {  
    super.onDestroy()  
	
	Injector.closeScope(MainScope::class)
}

```
Here `AppScope::class` is a reference to a parent scope. All scopes should have a parent scope, excluding one root scope. Only one root scope can be opened without mentioning a parens scope: `Injector.openScope(appScope)`


### Request instances
Finally you can request bound instances whenever you need it:
```kotlin
// directly
val resourceManager: ResourceManager = get()
// or lazy
val resourceManager: ResourceManager by inject()
```
Even in other scopes:
```kotlin
class Mapper(resourceManager: ResourceManager)

class ChildScope: Scope() {  
      
    override fun init() {  
        // initialization
        val mapper = Mapper(
            resourceManager = get()
        )
        
        // binding
        bind(mapper)
    }  
}
```
### Add to project
Distributed by JitPack
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
} 
```
Step 2. Add the dependency
```
dependencies {
    implementation 'com.github.eugeneek:injector:1.0.1'
}
```