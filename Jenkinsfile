pipeline {
    agent any

    tools {
        maven "Maven"
    }

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "192.168.56.101:9081"
        NEXUS_REPOSITORY = "calculator-app"
        NEXUS_CREDENTIAL_ID = "nexus-creds"
        NEXUS_CREDS = credentials('nexus-creds')

        AWS_CREDS_ID = 'aws-credentials'
        AWS_ACCOUNT_URL = "876724398547.dkr.ecr.eu-north-1.amazonaws.com"
        AWS_ECR_REGISTRY = "${AWS_ACCOUNT_URL}/vikrantkatoch/calculator-app"
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
                            [artifactId: 'CalculatorApp-SpringBoot',
                            classifier: '',
                            file: artifactPath,
                            type: 'war']
                        ]
                    )
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${AWS_ECR_REGISTRY}:${BUILD_NUMBER}",
                        "--build-arg NEXUS_USERNAME=${NEXUS_CREDS_USR} \
                        --build-arg NEXUS_PASSWORD=${NEXUS_CREDS_PSW} \
                        --build-arg NEXUS_URL=${NEXUS_URL} \
                        --build-arg NEXUS_REPOSITORY=${NEXUS_REPOSITORY} .")
                }
            }
        }

        stage('Push to AWS ECR') {
            steps {
                script {
                    docker.withRegistry("https://" + AWS_ECR_REGISTRY, "ecr:eu-north-1:" + AWS_CREDS_ID) {
                        dockerImage.push()
                    }
                }
            }
        }

    }
}