pipeline {
    agent any // ok

    environment {
        APP_NAME = 'main'
        JAR_NAME = 'main-1.0.jar'
        MAIN_DIR = '/root/hirex/main'
        TARGET_DIR = '/main/target' // relative to the build directory
    }

    stages {
        stage('Preparing') {
            steps {
                script {
                    def javaVersion = sh(script: 'java -version', returnStdout: true).trim()
                    echo "Java Version: ${javaVersion}"
                }

                script {
                    def mavenVersion = sh(script: 'mvn -v', returnStdout: true).trim()
                    echo "Maven Version: ${mavenVersion}"
                }
            }
        }

        stage('Build') {
            steps {
                echo "Building the ${APP_NAME} application..."
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                echo "Deploying the ${APP_NAME} application..."
                sh "bash ./stop.sh"
                sh "cp ${TARGET_DIR}/${JAR_NAME} ${MAIN_DIR}/"
                sh "bash ./run.sh"
            }
        }
    }

    post {
        success {
            echo "${APP_NAME} has been successfully deployed!"
        }
        failure {
            echo "There was an error deploying ${APP_NAME}."
        }
    }
}
