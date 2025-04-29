pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = 'dockerhub-creds'
        IMAGE_NAME = 'ramachandrampm/financeme-image'
        // No SSH_CREDENTIALS needed now
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
        
        stage('Test App with Selenium') {
            steps {
                withEnv(["APP_URL=http://${EC2_IP}:8081"]) {
                    sh '''
                        echo "Waiting 40 seconds for app to be ready..."
                        sleep 40
                        python3 selenium_test.py
                    '''
                }
            }
        }

    }
}
