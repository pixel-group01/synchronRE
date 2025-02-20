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
        DEPLOY_DIR = 'C:\\Users\\Administrator\\Desktop\\synchronRE\\nsia-group\\Dev\\back'
        NSSM_PATH = 'C:\\nssm\\nssm.exe'
        SERVICE_NAME = 'synchronreDev'
    }

    stages {
        stage('Clonage du dépôt') {
            steps {
                git branch: BRANCH, url: GIT_REPO_URL
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
                    echo "Vérification de l'existence du service ${SERVICE_NAME}..."
                    def serviceExists = bat(script: "sc query ${SERVICE_NAME} | findstr /C:\"SERVICE_NAME\"", returnStatus: true) == 0

                    if (serviceExists) {
                        echo "Service ${SERVICE_NAME} trouvé. Arrêt et suppression..."
                        bat "${NSSM_PATH} stop ${SERVICE_NAME} || echo Service non démarré"
                        sleep 5
                        bat "${NSSM_PATH} remove ${SERVICE_NAME} confirm"
                    }

                    echo "Vérification de l'existence du fichier JAR..."
                    if (!fileExists("${BUILD_DIR}\\${JAR_NAME}")) {
                        error "Fichier JAR introuvable : ${BUILD_DIR}\\${JAR_NAME}"
                    }

                    echo "Sauvegarde et remplacement du JAR..."
                    def oldJarPath = "${DEPLOY_DIR}\\${JAR_NAME}"
                    if (fileExists(oldJarPath)) {
                        def backupPath = "${DEPLOY_DIR}\\backup_${JAR_NAME}_$(date +%Y%m%d%H%M%S)"
                        bat "move /Y ${oldJarPath} ${backupPath}"
                    }
                    bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

                    echo "Installation et démarrage du service avec NSSM..."
                    bat """
                        ${NSSM_PATH} install ${SERVICE_NAME} "${JAVA_HOME}\\bin\\java.exe" "-jar ${DEPLOY_DIR}\\${JAR_NAME}"
                        ${NSSM_PATH} set ${SERVICE_NAME} AppDirectory ${DEPLOY_DIR}
                        ${NSSM_PATH} set ${SERVICE_NAME} AppStdout ${DEPLOY_DIR}\\app.log
                        ${NSSM_PATH} set ${SERVICE_NAME} AppStderr ${DEPLOY_DIR}\\app.log
                        ${NSSM_PATH} set ${SERVICE_NAME} Start SERVICE_AUTO_START
                        ${NSSM_PATH} start ${SERVICE_NAME}
                    """

                    echo "Vérification de l'état du service..."
                    if (bat(script: "sc query ${SERVICE_NAME} | findstr /C:\"RUNNING\"", returnStatus: true) != 0) {
                        error "Le service ${SERVICE_NAME} n'a pas démarré correctement."
                    }
                    echo "Service ${SERVICE_NAME} installé et démarré avec succès."
                }
            }
        }
    }

    post {
        success {
            echo "Build et déploiement réussis."
        }
        failure {
            echo "Échec du build ou du déploiement."
        }
    }
}