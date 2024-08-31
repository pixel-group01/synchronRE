pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }

    environment {
        GIT_REPO_URL = 'https://github.com/pixel-group01/synchronRE.git'
        BRANCH = 'main'
        BUILD_DIR = 'target'
        JAR_NAME = 'synchronRE.jar'
        DEPLOY_DIR = 'C:\\Users\\Administrator\\Desktop\\synchronRE\\nsia-group\\Prod\\front'
        CONFIG_FILE = "${DEPLOY_DIR}\\config\\application-test.properties"
    }

    stages {
        stage('parametrage') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO_URL}"
            }
        }

        stage('construction du jar') {
            steps {
                script {
                    bat 'mvn clean compile -e package'
                }
            }
        }

//         stage('verification du port') {
//             steps {
//                 script {
//                     // Lire le port depuis le fichier application-test.properties
//                     def port = readFile(file: "${CONFIG_FILE}")
//                         .split('\\n')
//                         .find { it.startsWith('server.port=') }
//                         ?.replace('server.port=', '')
//                         ?.trim()
//
//                     if (port) {
//                         // Vérifier si le port est occupé jjjjjj
//                         bat "netstat -ano | findstr :${port}"
//
//                         // Si le port est occupé, arrêter le processus
//                         bat """
//                         for /f "tokens=5" %%a in ('netstat -ano ^| findstr :${port}') do taskkill /PID %%a /F
//                         """
//                     } else {
//                         error "Le port n'a pas pu être trouvé dans ${CONFIG_FILE}"
//                     }
//                 }
//             }
//         }

//         stage('deploiement') {
//             steps {
//                 script {
//                     // Copier le fichier jar généré dans le répertoire de déploiement
//                     bat "copy ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"
//                     // Démarrer le fichier jar dans le répertoire de déploiement
//                     bat "cd /d ${DEPLOY_DIR} && java -jar ${JAR_NAME} > app.log 2>&1"
//                 }
//             }
//         }
    }

    post {
        success {
            echo "Build and deployment of ${JAR_NAME} completed successfully."
        }
        failure {
            echo "Build or deployment of ${JAR_NAME} failed."
        }
    }
}