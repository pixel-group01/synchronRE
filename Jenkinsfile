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
        NSSM_PATH = 'C:\\nssm\\nssm.exe'
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
                    echo "Nettoyage et construction du JAR..."
                    bat "mvn clean package -e -DargLine=\"-Xmx1024m -Xms512m\""
                }
            }
        }

        stage('Déploiement') {
            steps {
                    script {
                        echo "Copie du JAR vers ${DEPLOY_DIR}"
                        bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

                        echo "Vérification et gestion du service synchronreTest..."
                        bat """
                        sc query synchronreTest | findstr /I /C:"SERVICE_NAME" > nul
                        if %ERRORLEVEL% == 0 (
                            echo "Arrêt et suppression du service existant..."
                            sc stop synchronreTest
                            ping -n 6 127.0.0.1 > nul
                            sc delete synchronreTest
                            ping -n 6 127.0.0.1 > nul
                        ) else (
                            echo "Le service n'existe pas, pas besoin de le supprimer."
                        )
                        """

                        echo "Installation du service Windows avec NSSM..."
                        bat """
                        ${NSSM_PATH} install synchronreTest "${JAVA_HOME}\\bin\\java.exe" "-jar ${DEPLOY_DIR}\\${JAR_NAME}"
                        ${NSSM_PATH} set synchronreTest AppDirectory ${DEPLOY_DIR}
                        ${NSSM_PATH} set synchronreTest AppStdout ${DEPLOY_DIR}\\app.log
                        ${NSSM_PATH} set synchronreTest AppStderr ${DEPLOY_DIR}\\app.log
                        ${NSSM_PATH} set synchronreTest Start SERVICE_AUTO_START
                        ${NSSM_PATH} start synchronreTest
                        """

                        echo "Service synchronreTest installé et démarré avec succès."
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