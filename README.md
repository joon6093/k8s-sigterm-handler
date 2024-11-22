# Sigterm Handler for Spring applications in Kubernetes
In Kubernetes, Pods are gracefully terminated by sending a SIGTERM signal to their containers. However, in Java-based Spring Boot applications, the default behavior sets the exit code to 143. As a result, Kubernetes marks the Pod as "Error," even though the termination was intentional and graceful. This can lead to unnecessary alerts and operational confusion.

The Sigterm Handler ensures that SIGTERM signals are correctly processed, allowing your Spring application to exit cleanly with a 0 exit code, ensuring the Pod is marked as "Succeeded" rather than "Error" in Kubernetes.

## ⚙️ Requirements
To use the Sigterm Handler, ensure your project meets the following requirements.

- **Spring Boot:** 3.0.0 or higher  
- **Java:** 17 or higher

## 📦 Dependency
To integrate the Sigterm Handler into your project, follow the steps below based on your build tool.

#### Gradle (build.gradle)
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {  
    implementation 'com.github.joon6093:k8s-sigterm-handler:1.0.0'
}
```
#### Maven (pom.xml)
```
<repositories>  
    <repository>  
        <id>jitpack.io</id>  
        <url>https://jitpack.io</url>  
    </repository>  
</repositories>

<dependency>  
    <groupId>com.github.joon6093</groupId>  
    <artifactId>k8s-sigterm-handle</artifactId>  
    <version>1.0.0</version>  
</dependency>
```

## ✏️ Note
- While designed primarily for Kubernetes environments, the Sigterm Handler is flexible and can be used in other scenarios requiring precise signal handling.
- If you encounter any issues or have feature requests, feel free to open an issue in the project repository. Contributions and feedback are always welcome!
- If you found this project helpful or interesting, please consider giving it a star on GitHub! ⭐

## 🗓️ Release
- [Version 1.0.0](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.0.0) - Released on 2024/11/23

