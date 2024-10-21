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

        stage('Capture Logs') {
            steps {
                echo "Creating logs directory and capturing service logs..."
                sh '''
                    mkdir -p ~/hirex/logs
                    docker compose logs main-service > ~/hirex/logs/main-service.log
                    docker compose logs websocket-service > ~/hirex/logs/websocket-service.log
                '''
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
