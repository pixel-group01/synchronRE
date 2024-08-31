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
                    bat 'mvn -e package'
                }
            }
        }

        stage('Vérification du port') {
            steps {
                script {
                    // Lire le port depuis le fichier application-test.properties
                    def port = readFile(file: "${CONFIG_FILE}")
                        .split('\\n')
                        .find { it.startsWith('server.port=') }
                        ?.replace('server.port=', '')
                        ?.trim()

                    if (port) {
                        // Vérifier si le port est occupé
                        def portOccupied = bat(script: "netstat -ano | findstr :${port}", returnStatus: true) == 0

                        if (portOccupied) {
                            // Si le port est occupé, arrêter le processus
                            bat """
                            for /f "tokens=5" %%a in ('netstat -ano ^| findstr :${port}') do (
                                echo Arrêt du processus avec le PID %%a
                                taskkill /PID %%a /F
                            )
                            """
                        } else {
                            echo "Le port ${port} est libre."
                        }
                    } else {
                        error "Le port n'a pas pu être trouvé dans ${CONFIG_FILE}"
                    }
                }
            }
        }

        stage('Déploiement') {
            steps {
                script {
                    // Copier le fichier JAR généré dans le répertoire de déploiement
                    bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"
                    // Démarrer le fichier JAR dans le répertoire de déploiement
                    bat "cd /d ${DEPLOY_DIR} && java -jar ${JAR_NAME} > app.log 2>&1"
                }
            }
        }
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