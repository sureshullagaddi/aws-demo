pipeline {
    agent any
    stages {
        stage('Checkout and build') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'gitCredentials', url: 'https://github.com/sureshullagaddi/aws-demo.git']]
                )
                sh './gradlew clean build'
            }
        }
        stage('Build docker image') {
            steps {
                script {
                    // Use the correct repository name and tag the image properly
                    sh 'docker build -t 7829099824/aws-demo:latest .'
                }
            }
        }
        stage('Push docker image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'Dockerhub-pass', variable: 'DOCKER_PASSWORD')]) {
                        // Securely login using password via stdin
                        sh 'echo ${DOCKER_PASSWORD} | docker login -u 7829099824 --password-stdin'
                    }
                    // Push the image with the correct tag
                    sh 'docker push 7829099824/aws-demo:latest'
                }
            }
        }
    }
}