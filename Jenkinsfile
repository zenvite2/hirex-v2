pipeline {
    agent any // Use any available agent

    environment {
        IMAGE_NAME = 'main'
        IMAGE_TAG = 'latest'
        DOCKERFILE_PATH = 'main/Dockerfile'
    }

    stages {
        stage('Build') {
            steps {
                echo "Building the Docker image ${IMAGE_NAME}:${IMAGE_TAG}..."

                dir('.') {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -f ${DOCKERFILE_PATH} ."
                }
            }
        }

        stage('Deployment') {
            steps {
                echo "Updating Docker Compose to use the latest image..."
                dir('.') {
                    // Use the new version of Docker Compose without the dash (-)
                    sh "docker compose -f ./main/docker-compose.yml up -d --build"
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
