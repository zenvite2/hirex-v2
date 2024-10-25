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

                        # Tag and push main service image
                        docker tag main:latest $DOCKER_REPO/main:latest
                        docker push $DOCKER_REPO/main:latest

                        # Tag and push websocket service image
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
                        sshpass -p "$HIREX_VPS_PW" ssh -o StrictHostKeyChecking=no "$HIREX_VPS_USER@$HIREX_VPS" bash << 'EOF'
                            docker compose down
                            docker compose -f /root/hirex/deploy/docker-compose.yml pull
                            docker compose -f /root/hirex/deploy/docker-compose.yml up -d --remove-orphans
                        << EOF
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
