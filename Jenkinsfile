pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }

    environment {
        GIT_REPO_URL = 'https://github.com/pixel-group01/synchronRE.git'
        BRANCH = 'Comptes'
        BUILD_DIR = 'target'
        JAR_NAME = 'synchronRE.jar'
        DEPLOY_DIR = 'C:\\Users\\Administrator\\Desktop\\synchronRE\\nsia-group\\Test\\back'
        CONFIG_FILE = "${DEPLOY_DIR}\\config\\application-test.properties"
    }

    stages {
        stage('Parametrage') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO_URL}"
            }
        }

        stage('Construction du JAR') {
            steps {
                script {
                    bat 'mvn clean package -e'
                }
            }
        }

        stage('Verification du port') {
            steps {
                script {
                    def port = null
                    try {
                        def configContent = readFile(file: "${CONFIG_FILE}")
                        echo "Contenu du fichier de config:\n${configContent}"
                        port = configContent
                            .split('[\\r\\n]+')
                            .find { it.startsWith('server.port=') }
                            ?.replace('server.port=', '')
                            ?.trim()

                        if (!port?.isInteger()) {
                            error "Le port extrait (${port}) n'est pas valide."
                        }
                    } catch (Exception e) {
                        error "Impossible de lire ${CONFIG_FILE} : ${e.message}"
                    }

                    if (port) {
                        echo "Vérification du port : ${port}"
                        def portOccupied = bat(script: "netstat -ano | findstr LISTENING | findstr :${port}", returnStatus: true) == 0

                        if (portOccupied) {
                            echo "Le port ${port} est occupé, arrêt du processus..."
                            bat """
                            for /f "tokens=5" %%a in ('netstat -ano ^| findstr LISTENING ^| findstr :${port}') do (
                                echo Arrêt du processus avec le PID %%a
                                taskkill /PID %%a /F
                            )
                            """
                            sleep time: 10, unit: 'SECONDS'  // Délai plus long pour éviter les conflits
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
                    echo "Copie du JAR vers ${DEPLOY_DIR}"
                    bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

                    echo "Démarrage de l'application..."
                 // bat "cd /d ${DEPLOY_DIR} && java -jar ${JAR_NAME} > app.log 2>&1"
                    bat """
                    cd /d ${DEPLOY_DIR}
                    start /B java -jar ${JAR_NAME} >> app.log 2>&1
                    powershell -Command "(Get-WmiObject Win32_Process -Filter 'commandline like \\\"%${JAR_NAME}%\\\"').ProcessId > app.pid"
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Build et déploiement de ${JAR_NAME} réussis."
        }
        failure {
            echo "Échec du build ou du déploiement de ${JAR_NAME}."
        }
    }
}
