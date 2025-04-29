pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE_NAME = 'ramachandrampm/financeme-image'
        SSH_CREDENTIALS = 'ec2-ssh-key'
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

        stage('Provision Infrastructure with Terraform') {
            steps {
                withCredentials([
                    string(credentialsId: 'AWS_ACCESS_KEY_ID', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'AWS_SECRET_ACCESS_KEY', variable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    sh '''
                        cd terraform
                        terraform init
                        terraform apply -auto-approve
                    '''
                }
            }
        }

        stage('Get Terraform Output (Dynamic IP)') {
            steps {
                script {
                    env.EC2_IP = sh(script: "cd terraform && terraform output -raw ec2_public_ip", returnStdout: true).trim()
                    echo "Dynamic EC2 Public IP: ${env.EC2_IP}"
                }
            }
        }

        stage('Configure Server and Deploy App') {
            steps {
                sshagent (credentials: ["${SSH_CREDENTIALS}"]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} '
                          sudo apt update &&
                          sudo apt install -y docker.io &&
                          sudo systemctl start docker &&
                          sudo systemctl enable docker &&
                          docker pull ${IMAGE_NAME}:latest &&
                          docker stop financeme-container || true &&
                          docker rm financeme-container || true &&
                          CONTAINER_ID=\$(docker ps --filter "publish=8081" --format "{{.ID}}")
                          if [ ! -z "\$CONTAINER_ID" ]; then
                              docker rm -f \$CONTAINER_ID
                          fi
                          docker run -d --name financeme-container -p 8081:8080 ${IMAGE_NAME}:latest
                        '
                    """
                }
            }
        }

        stage('Test App with Selenium') {
            steps {
                withEnv(["APP_URL=http://${EC2_IP}:8081"]) {
                    sh 'python3 selenium_test.py'
                }
            }
        }
    }
}
