pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE_NAME = 'ramachandrampm/financeme-image'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
                // Tests will run automatically during Maven build
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

        stage('Get Terraform Output (Dynamic EC2 IP)') {
            steps {
                script {
                    env.EC2_IP = sh(script: "cd terraform && terraform output -raw ec2_public_ip", returnStdout: true).trim()
                    echo "New EC2 Public IP: ${env.EC2_IP}"
                }
            }
        }

        stage('Cleanup Old Inventory File') {
            steps {
                sh 'rm -f inventory.ini || true'
            }
        }

        stage('Configure Server with Ansible') {
            steps {
                script {
                    writeFile file: 'inventory.ini', text: "[finance_servers]\n${env.EC2_IP} ansible_user=ubuntu ansible_ssh_private_key_file=/var/lib/jenkins/jjk.pem"
                }
                sh 'ANSIBLE_HOST_KEY_CHECKING=False ansible-playbook -i inventory.ini setup-finance.yml'
            }
        }

        stage('Wait for App to Start') {
            steps {
                sh 'echo "Waiting for application to be ready..." && sleep 40'
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

    post {
        success {
            echo '✅ Pipeline finished successfully!'
        }
        failure {
            echo '❌ Pipeline failed!'
        }
    }
}
