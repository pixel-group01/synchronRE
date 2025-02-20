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
        SERVICE_NAME = 'synchronreTest' // Nom du service Windows
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

//                        echo "Vérification de l'existence du service ${SERVICE_NAME}..."
//                        bat """
//                        sc query ${SERVICE_NAME} > nul 2>&1
//                        if %ERRORLEVEL% EQU 1060 (
//                            echo "Le service ${SERVICE_NAME} n'existe pas. Il sera créé."
//                        ) else (
//                            echo "Le service ${SERVICE_NAME} existe déjà. Arrêt et suppression..."
//                            sc stop ${SERVICE_NAME} || echo "Le service ${SERVICE_NAME} est déjà arrêté ou ne peut pas être arrêté."
//                            timeout /t 5 > nul
//                            sc delete ${SERVICE_NAME} || echo "Le service ${SERVICE_NAME} ne peut pas être supprimé."
//                            timeout /t 5 > nul
//                            echo "Service ${SERVICE_NAME} supprimé."
//                        )
//                        """

                       echo "Création du service ${SERVICE_NAME} avec NSSM..."
                       bat """
                       ${NSSM_PATH} install ${SERVICE_NAME} "${JAVA_HOME}\\bin\\java.exe" "-jar ${DEPLOY_DIR}\\${JAR_NAME}"
                       ${NSSM_PATH} set ${SERVICE_NAME} AppDirectory ${DEPLOY_DIR}
                       ${NSSM_PATH} set ${SERVICE_NAME} AppStdout ${DEPLOY_DIR}\\app.log
                       ${NSSM_PATH} set ${SERVICE_NAME} AppStderr ${DEPLOY_DIR}\\app.log
                       ${NSSM_PATH} set ${SERVICE_NAME} Start SERVICE_AUTO_START
                       ${NSSM_PATH} start ${SERVICE_NAME}
                       """

                       echo "Service ${SERVICE_NAME} installé et démarré avec succès."
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