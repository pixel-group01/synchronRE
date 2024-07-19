pipeline
{
    agent any
    tools
    {
        jdk "JDK 17"
        maven "Maven 3.9.5"
    }

    stages
    {
        stage('Git checkout')
        {
            steps
            {
                git changelog: false, credentialsId: 'eed44918-7624-4bf2-bd56-86b31c94b68e', poll: false, url: 'https://github.com/pixel-group01/synchronRE.git'
            }
        }
        stage('Maven package')
        {
            steps
            {
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }
        }
        stage('OWASP Scan')
        {
            steps
            {
                dependencyCheck additionalArguments: '--scan ./ ', odcInstallation: 'OWASP DC'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
    }
}
