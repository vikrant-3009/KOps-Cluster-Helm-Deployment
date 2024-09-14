pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                script {
                    git branch: 'master',
                        credentialsId: 'GitHub-Token',
                        url: 'https://github.com/vikrant-3009/kubernetes-assignment-1.git'
                }
            }
        }

        stage('Build Code') {
            steps {
                sh "mvn clean package"
            }
        }

        stage('Test Code') {
            steps {
                sh "mvn test"
            }

            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }
}