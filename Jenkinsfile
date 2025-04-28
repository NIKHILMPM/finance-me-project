pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE_NAME = 'ramachandrampm/financeme-image'
        SSH_CREDENTIALS = 'ec2-ssh-key'
        EC2_IP = '54.167.8.65' // If you are not using dynamic Terraform output
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

        stage('Configure Server with Ansible') {
            steps {
                sh '''
                    ansible-playbook -i inventory.ini setup-financeme.yml
                '''
            }
        }

        stage('Deploy Docker Container') {
            steps {
                sshagent (credentials: ["${SSH_CREDENTIALS}"]) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker pull ${IMAGE_NAME}:latest"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker stop financeme-container || true"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker rm financeme-container || true"
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} '
                            CONTAINER_ID=\$(docker ps --filter "publish=8081" --format "{{.ID}}");
                            if [ ! -z "\$CONTAINER_ID" ]; then
                                docker rm -f \$CONTAINER_ID;
                            fi
                        '
                        ssh -o StrictHostKeyChecking=no ubuntu@${EC2_IP} "docker run -d --name financeme-container -p 8081:8080 ${IMAGE_NAME}:latest"
                    """
                }
            }
        }

    }
}
