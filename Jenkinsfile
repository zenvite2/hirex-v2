pipeline {
    agent any

    environment {
        IMAGE_TAG = 'latest'
    }

    stages {
        stage('Build and deploy') {
            steps {
                echo "Building Docker images for main and websocket services..."
                sh "docker compose down && docker compose -f ./docker-compose.yml up --build -d"
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
