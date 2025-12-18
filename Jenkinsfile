pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker image') {
            steps {
                sh 'docker build -t blvckm4gic/todo-app:latest .'
            }
        }

        stage('Run container') {
            steps {
                sh '''
                docker rm -f todo-app || true
                docker run -d -p 8080:8080 --name todo-app blvckm4gic/todo-app:latest
                '''
            }
        }
    }
}
