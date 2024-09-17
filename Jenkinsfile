pipeline {
    agent any

    tools {
        maven "Maven"
    }

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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('SonarQube Quality Gate Check') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

    }
}