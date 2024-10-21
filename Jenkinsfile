pipeline {
    agent any

    environment {
        IMAGE_NAME = 'main'
        IMAGE_TAG = 'latest'
    }

    stages {
        stage('Build') {
            steps {
                echo "Building the Docker image ${IMAGE_NAME}:${IMAGE_TAG}..."

                dir('.') {
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
