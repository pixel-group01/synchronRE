pipeline {
    agent any

    tools {
        jdk 'JAVA_HOME'
        maven 'MAVEN_HOME'
    }

    environment {
        GIT_REPO_URL = 'https://github.com/pixel-group01/synchronRE.git'
        BRANCH = 'main'
        DEPLOY_DIR = 'target'
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
                    // Démarre la nouvelle instance en arrière-plan
                   // bat "start java -jar ${DEPLOY_DIR}\\jenkins1.jar"
                    bat "java -jar ${DEPLOY_DIR}\\jenkins1.jar"
                }
            }
        }
    }

    post {
        success {
            echo 'Build and deployment of jenkins1 completed successfully.'
        }
        failure {
            echo 'Build or deployment of jenkins1 failed.'
        }
    }
}