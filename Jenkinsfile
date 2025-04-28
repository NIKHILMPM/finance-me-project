pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE = 'ramachandrampm/financeme-image'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', credentialsId: 'github-creds', url: 'https://github.com/NIKHILMPM/finance-me-project.git'
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Docker Build & Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        sh """
                        docker build -t ${IMAGE}:latest .
                        docker push ${IMAGE}:latest
                        """
                    }
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent (credentials: ['ec2-ssh-key']) {
                    sh '''
                    ssh -o StrictHostKeyChecking=no ubuntu@54.167.8.65 << EOF
                      docker pull ramachandrampm/financeme-image:latest
                      docker stop financeme-container || true
                      docker rm financeme-container || true
                      docker run -d --name financeme-container -p 8080:8080 ramachandrampm/financeme-image:latest
                    EOF
                    '''
                }
            }
        }
    }
}
