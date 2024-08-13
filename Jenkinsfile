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
                    bat 'mvn package'
                }
            }
        }

        stage('deploiement') {
            steps {
                script {
                    // Copier le fichier jar généré dans le répertoire de déploiement
                    bat "copy ${BUILD_DIR}\\${JAR_NAME} ${DEPLOY_DIR}\\${JAR_NAME}"

                    // Démarrer le fichier jar dans une nouvelle fenêtre d'invite de commande
                    //bat "start cmd /k \"cd /d ${DEPLOY_DIR} && java -jar ${JAR_NAME}\""
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