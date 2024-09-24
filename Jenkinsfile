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
        AWS_ECR_ACCOUNT_URL = "876724398547.dkr.ecr.eu-north-1.amazonaws.com"
        AWS_ECR_REGISTRY = "${AWS_ECR_ACCOUNT_URL}/vikrantkatoch/calculator-app"

        KUBECONFIG = credentials('kubeconfig')   // Kubeconfig for Kubernetes cluster access
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
            post {
                failure {
                    emailext (
                        subject: "Job '${env.JOB_NAME}' (${env.BUILD_NUMBER}) - Quality Gate Failed",
                        body: """
                            <p>Hi Team,</p>
                            <p>The Jenkins build <b>${env.JOB_NAME}</b> with build number <b>${env.BUILD_NUMBER}</b> has failed due to the Quality Gate not passing.</p>
                            <p>Please review the SonarQube results and address any issues.</p>
                            <p>Regards,<br>DevOps Team</p>
                        """,
                        mimeType: 'text/html'
                    )
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

        stage('Deploy app to Kubernetes cluster using Helm Chart') {
            steps {
                withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBECONFIG')]) {
                    sh """
                        # create namespace if not exists
                        kubectl get namespace test-1 || kubectl create namespace test-1

                        # Run the helm upgrade command
                        helm upgrade --install calc-app ./Helm/calculator-app --namespace test-1 \
                            --set image.repository=${AWS_ECR_REGISTRY} \
                            --set image.tag=${env.BUILD_NUMBER}
                    """
                }
            }
        }

    }
}