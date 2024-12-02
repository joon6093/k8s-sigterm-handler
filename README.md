# Sigterm Handler for Spring applications in Kubernetes
In Kubernetes, Pods are gracefully terminated by sending a SIGTERM signal to their containers. However, in Java-based Spring Boot applications, the default behavior sets the exit code to 143. As a result, Kubernetes marks the Pod as "Error," even though the termination was intentional and graceful. This can lead to unnecessary alerts and operational confusion.

The Sigterm Handler ensures that SIGTERM signals are correctly processed, allowing your Spring application to exit cleanly with a 0 exit code, ensuring the Pod is marked as "Completed" rather than "Error" in Kubernetes.

## ⚙️ Requirements
To use the Sigterm Handler, ensure your project meets the following requirements.

- **Operating System:** Verified to work on Linux, macOS, and Windows. (Uses non-standard APIs for direct signal handling, which may exhibit different behavior across JVMs and OSs.)
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
    implementation 'com.github.joon6093:k8s-sigterm-handler:1.2.2'
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
    <version>1.2.2</version>  
</dependency>
```
## 🔧 Configuration
To customize the Sigterm Handler, configure the following options in your Spring Boot configuration file.

#### Options
- **enabled:** Set whether the handler is enabled or disabled. (default: true)
- **exit-code:** Set the exit code for graceful application termination. (default: 0)
- **termination-message-path:** Set the file path where the termination message should be written. (default: not set)
- **termination-message:** Set the content of the termination message written to the specified path. (default: SIGTERM signal received. Application has been terminated successfully.)
  
#### YAML (application.yml)
```
kubernetes:
  sigterm-handler:
    enabled: true
    exit-code: 0
    termination-message-path: /dev/termination-log
    termination-message: SIGTERM signal received. Application has been terminated successfully.
```

#### Properties (application.properties)
```
kubernetes.sigterm-handler.enabled=true
kubernetes.sigterm-handler.exit-code=0
kubernetes.sigterm-handler.termination-message-path=/dev/termination-log
kubernetes.sigterm-handler.termination-message=SIGTERM signal received. Application has been terminated successfully.
```

## 📄 Log
When the Sigterm Handler is initialized, a startup log displays the exit code, termination message path, and termination message.
```
2024-11-30T08:10:12.628+09:00 INFO --- i.j.handler.SigtermHandlerConfiguration: Sigterm handler initialized with exitCode: 0, terminationMessagePath: /dev/termination-log, terminationMessage: 'SIGTERM signal received...'
```

Log output indicating that the SIGTERM signal was received and the Sigterm handler has been triggered to manage the shutdown process.
```
2024-11-30T08:10:17.677+09:00 INFO --- i.j.handler.ApplicationTerminator: Received SIGTERM signal. Initiating termination handler.
```

## ✨ Effect
Using the Sigterm Handler, after the Pod receives a SIGTERM signal and shuts down gracefully, you can run commands like "kubectl get pod" in Kubernetes to verify that the application terminated cleanly.
```
state:
  terminated:
    containerID: containerd://7935f0bcfd27b2d01f900029746261e0cdad8bcdbd5a72d01f901
    exitCode: 0
    finishedAt: "2024-11-29T20:22:26Z"
    message: SIGTERM signal received. Application has been terminated successfully.
    reason: Completed
    startedAt: "2024-11-29T20:20:42Z"
```

## ✏️ Note
- Although designed for Kubernetes, the Sigterm Handler can also be used in Docker or other environments needing signal handling.
- If you encounter any issues or have suggestions, feel free to open an issue in the repository. feedback is always welcome! 😊
- If you found this project helpful or interesting, please consider giving it a star on GitHub! ⭐

## 🗓️ Release
- [Version 1.0.0](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.0.0) - Released on 2024/11/23
- [Version 1.0.1](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.0.1) - Released on 2024/11/24
- [Version 1.0.2](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.0.2) - Released on 2024/11/24
- [Version 1.0.3](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.0.3) - Released on 2024/11/25
- [Version 1.1.0](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.1.0) - Released on 2024/11/26
- [Version 1.2.0](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.2.0) - Released on 2024/11/28
- [Version 1.2.1](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.2.1) - Released on 2024/11/29
- [Version 1.2.2](https://github.com/joon6093/k8s-sigterm-handler/releases/tag/1.2.2) - Released on 2024/12/02
