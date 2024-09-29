pipeline {
    agent any

    environment {
        MVN_HOME = tool 'Maven'
        JDK_HOME = tool 'JDK21'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Cutiepie4/hirex-v2.git'
            }
        }

        stage('Build') {
            steps {
                sh "${MVN_HOME}/bin/mvn clean install"
            }
        }

        stage('Test') {
            steps {
                sh "${MVN_HOME}/bin/mvn test"
            }
        }

        stage('Deploy Spring Boot Module') {
            steps {
                dir('main') {
                    sh "${MVN_HOME}/bin/mvn spring-boot:run"
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
