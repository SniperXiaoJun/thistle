# thistle 6.4
* Java common library for private use
* https://github.com/shepherdviolet/thistle

### Import dependencies from maven repository

```gradle

repositories {
    maven { url 'https://jitpack.io' }
}
dependencies {
    compile 'com.github.shepherdviolet:thistle:6.4'
}

```

### Import dependencies from local repository

```gradle

repositories {
    //replace by your path
    maven { url 'file:C:/m2repository/repository' }
}

```