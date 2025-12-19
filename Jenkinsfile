pipeline {
    agent any

    environment {
        REGISTRY = "blvckm4gic"
        IMAGE_NAME = "todo-app"
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                  docker build -t $REGISTRY/$IMAGE_NAME:$IMAGE_TAG .
                  docker tag $REGISTRY/$IMAGE_NAME:$IMAGE_TAG $REGISTRY/$IMAGE_NAME:latest
                """
            }
        }

        stage('Push Docker Image') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh """
                      echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                      docker push $REGISTRY/$IMAGE_NAME:$IMAGE_TAG
                      docker push $REGISTRY/$IMAGE_NAME:latest
                    """
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'build/libs/*.jar'
            echo "✅ Pipeline SUCCESS"
        }

        failure {
            echo "❌ Pipeline FAILED"
        }
    }
}
