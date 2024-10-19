pipeline {
    agent any // Use any available agent

    environment {
        IMAGE_NAME = 'main'
        IMAGE_TAG = 'latest'
    }

    stages {
        stage('Build') {
            steps {
                echo "Building the Docker image ${IMAGE_NAME}:${IMAGE_TAG}..."

                dir('./main') {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Deployment') {
            steps {
                echo "Updating Docker Compose to use the latest image..."
                dir('./main') {
                  sh "docker compose -f docker-compose.yml up -d --build"
                }
            }
        }
    }

    post {
        success {
            echo "Deployment successful!"
        }
        failure {
            echo "Deployment failed."
        }
    }
}
