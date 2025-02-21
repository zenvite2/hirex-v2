pipeline {
    agent any

    environment {
        HIREX_VPS = "${env.HIREX_VPS}"
        HIREX_VPS_USER = "${env.HIREX_VPS_USER}"
        HIREX_VPS_PW = "${env.HIREX_VPS_PW}"
        DOCKER_USER = "${env.DOCKER_USER}"
        DOCKER_PW = "${env.DOCKER_PW}"
        DOCKER_REPO = "${env.DOCKER_REPO}"
    }

    stages {
        stage('Build images') {
            steps {
                echo "Building Docker images for main and websocket services...."
                sh "docker compose -f ./docker-compose.yml build"
            }
        }

        stage('Push images') {
            steps {
                script {
                    echo "Tagging and pushing Docker images to the registry...."

                    sh '''
                        echo "$DOCKER_PW" | docker login -u "$DOCKER_USER" --password-stdin

                        docker tag main:latest $DOCKER_REPO/main:latest
                        docker push $DOCKER_REPO/main:latest

                        docker tag websocket:latest $DOCKER_REPO/websocket:latest
                        docker push $DOCKER_REPO/websocket:latest
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    echo "Deploying Docker containers to prod server...."
                    sh '''
                        sudo -u viet bash -c "
                        cd /home/viet/code/deploy/services && \
                        docker compose -f docker-compose.yml down && \
                        docker compose -f docker-compose.yml pull && \
                        docker compose -f docker-compose.yml up -d
                        "
                    '''
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
