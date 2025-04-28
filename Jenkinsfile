pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE_NAME = 'ramachandrampm/financeme-image'
        SSH_CREDENTIALS = 'ec2-ssh-key'
        EC2_IP = '54.167.8.65'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', credentialsId: 'github-creds', url: 'https://github.com/NIKHILMPM/finance-me-project.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKERHUB_CREDENTIALS}") {
                        sh """
                            docker build -t ${IMAGE_NAME}:latest .
                            docker push ${IMAGE_NAME}:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent (credentials: ["${SSH_CREDENTIALS}"]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker pull ${IMAGE_NAME}:latest"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker stop financeme-container || true"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker rm financeme-container || true"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker run -d --name financeme-container -p 8081:8080 ${IMAGE_NAME}:latest"
                    """
                }
            }
        }
    }
}
