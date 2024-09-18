pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        // NEXUS ARTIFACTORY
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.56.101:9081"
        NEXUS_REPOSITORY = "calculator-app"
        NEXUS_CREDENTIAL_ID = "nexus-creds"
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
                sh "mvn clean package -DskipTests"
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
                timeout(time: 2, unit: 'MINUTES') {
                    script {
                        def qualityGate = waitForQualityGate()

                        if (qualityGate.status != 'OK') {
                            echo "${qualityGate.status}"
                            echo "SonarQube Quality Gate failed"
                        }
                        else {
                            echo "${qualityGate.status}"
                            echo "SonarQube Quality Gates Passed"
                        }
                    }
                }
            }
        }

        stage('Upload to Nexus Artifactory') {
            steps {
                script {
                    filesByGlob = findFiles(glob: "target/*.war");
                    artifactPath = filesByGlob[0].path;

                    nexusArtifactUploader(
                        credentialsId: NEXUS_CREDENTIAL_ID,
                        nexusUrl: NEXUS_URL,
                        nexusVersion: NEXUS_VERSION,
                        protocol: NEXUS_PROTOCOL,
                        repository: NEXUS_REPOSITORY,
                        groupId: 'com.example',
                        version: '0.0.1',
                        artifacts: [
                            [artifactId: pom.artifactId,
                            classifier: '',
                            file: artifactPath,
                            type: 'war']
                        ]
                    )
            }
        }

    }
}