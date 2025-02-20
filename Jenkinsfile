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
        CONFIG_FILE = "${DEPLOY_DIR}\\config\\application-dev.properties"
        NSSM_PATH = 'C:\\nssm\\nssm.exe'
        SERVICE_NAME = 'synchronreDev' // Nom du service Windows
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

                                 echo "Sauvegarde de l'ancien JAR si nécessaire..."
                                 def oldJarPath = "${DEPLOY_DIR}\\${JAR_NAME}"
                                 if (fileExists(oldJarPath)) {
                                     def backupPath = "${DEPLOY_DIR}\\backup_${JAR_NAME}_${new Date().format('yyyyMMddHHmmss')}"
                                     echo "Sauvegarde de l'ancien JAR vers ${backupPath}"
                                     bat "move /Y ${oldJarPath} ${backupPath}"
                                 }

                                 echo "Copie du nouveau JAR vers ${DEPLOY_DIR}"
                                 bat "copy /Y ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

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
                                     echo "Le service ${SERVICE_NAME} n'a pas démarré correctement. Restauration de l'ancien JAR..."

                                     def backupFiles = bat(script: "dir /B /O-D ${DEPLOY_DIR}\\backup_*.jar", returnStdout: true).trim().split("\n")
                                     if (backupFiles.length > 0) {
                                         def lastBackup = backupFiles[0].trim()
                                         bat "move /Y ${DEPLOY_DIR}\\${lastBackup} ${DEPLOY_DIR}\\${JAR_NAME}"
                                         echo "Ancien JAR restauré. Tentative de redémarrage du service..."
                                         bat "${NSSM_PATH} start ${SERVICE_NAME}"

                                         def restartSuccess = bat(script: "sc query ${SERVICE_NAME} | findstr /C:\"RUNNING\"", returnStatus: true) == 0
                                         if (!restartSuccess) {
                                             error "Impossible de restaurer et redémarrer le service ${SERVICE_NAME}."
                                         }
                                     } else {
                                         error "Aucun backup trouvé pour restaurer l'ancien JAR."
                                     }
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