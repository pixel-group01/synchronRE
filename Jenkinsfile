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

        stage('Deploiement') {
           steps {
                   script {
                    script {
                                echo "Vérification de l'espace disque disponible..."
                                def freeSpace = bat(script: "wmic logicaldisk where DeviceID='C:' get FreeSpace", returnStdout: true).trim()
                                if (freeSpace.toLong() < 500000000) { // Moins de 500 Mo libres
                                    error "Espace disque insuffisant pour le déploiement."
                                }

                                echo "Vérification des permissions du répertoire ${DEPLOY_DIR}..."
                                def hasWriteAccess = bat(script: "echo test > ${DEPLOY_DIR}\\write_test.tmp && del ${DEPLOY_DIR}\\write_test.tmp", returnStatus: true) == 0
                                if (!hasWriteAccess) {
                                    error "Permissions insuffisantes sur le répertoire ${DEPLOY_DIR}."
                                }

                                echo "Vérification de l'existence du service ${SERVICE_NAME}..."
                                def serviceExists = bat(script: "sc query ${SERVICE_NAME} | findstr /C:\"SERVICE_NAME\"", returnStatus: true) == 0

                                if (serviceExists) {
                                    echo "Service ${SERVICE_NAME} trouvé. Arrêt et suppression..."
                                    bat "${NSSM_PATH} stop ${SERVICE_NAME} || echo Service non démarré"
                                    sleep 5
                                    bat "${NSSM_PATH} remove ${SERVICE_NAME} confirm"
                                } else {
                                    echo "Service ${SERVICE_NAME} non trouvé, création d'un nouveau service."
                                }

                                echo "Vérification de l'existence du fichier JAR..."
                                def jarExists = fileExists("${BUILD_DIR}\\${JAR_NAME}")
                                if (!jarExists) {
                                    error "Fichier JAR introuvable : ${BUILD_DIR}\\${JAR_NAME}"
                                }

                                echo "Sauvegarde de l'ancien JAR avant déploiement..."
                                bat "if exist ${DEPLOY_DIR}\\${JAR_NAME} move ${DEPLOY_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\backup_${JAR_NAME}_%date:~6,4%%date:~3,2%%date:~0,2%.jar"

                                echo "Copie du JAR vers ${DEPLOY_DIR}"
                                bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

                                echo "Vérification si le port ${APP_PORT} est occupé..."
                                def portInUse = bat(script: "netstat -ano | findstr :${APP_PORT}", returnStatus: true) == 0
                                if (portInUse) {
                                    error "Le port ${APP_PORT} est déjà utilisé, arrêt du déploiement."
                                }

                                echo "Création du service ${SERVICE_NAME} avec NSSM..."
                                bat """
                                ${NSSM_PATH} install ${SERVICE_NAME} "${JAVA_HOME}\\bin\\java.exe" "-jar ${DEPLOY_DIR}\\${JAR_NAME}"
                                ${NSSM_PATH} set ${SERVICE_NAME} AppDirectory ${DEPLOY_DIR}
                                ${NSSM_PATH} set ${SERVICE_NAME} AppStdout ${DEPLOY_DIR}\\app.log
                                ${NSSM_PATH} set ${SERVICE_NAME} AppStderr ${DEPLOY_DIR}\\app.log
                                ${NSSM_PATH} set ${SERVICE_NAME} Start SERVICE_AUTO_START
                                ${NSSM_PATH} start ${SERVICE_NAME}
                                """

                                echo "Vérification de l'état du service après démarrage..."
                                def serviceStarted = bat(script: "sc query ${SERVICE_NAME} | findstr /C:\"RUNNING\"", returnStatus: true) == 0
                                if (!serviceStarted) {
                                    echo "Le service ${SERVICE_NAME} n'a pas démarré correctement. Restauration de l'ancienne version..."
                                    bat "move ${DEPLOY_DIR}\\backup_${JAR_NAME}_%date:~6,4%%date:~3,2%%date:~0,2%.jar ${DEPLOY_DIR}\\${JAR_NAME}"
                                    bat "${NSSM_PATH} start ${SERVICE_NAME}"
                                    error "Déploiement annulé et ancienne version restaurée."
                                }

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