pipeline {
    agent any
    parameters {
        string(name: 'DOCKER_REPO', defaultValue: '7829099824/aws-demo', description: 'Docker repository to push the image')
        string(name: 'DOCKER_TAG', defaultValue: 'latest', description: 'Tag for the Docker image')
    }
    stages {
        stage('Checkout & gradle build') {
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
                    // Use dynamic Docker repository and tag
                    sh "docker build -t ${params.DOCKER_REPO}:${params.DOCKER_TAG} ."
                }
            }
        }
        stage('Push docker image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'Dockerhub-pass', variable: 'DOCKER_PASSWORD')]) {
                        // Securely login using password via stdin
                        sh '''echo ${DOCKER_PASSWORD} | docker login -u 7829099824 --password-stdin'''
                    }
                    // Push the image dynamically
                    sh "docker push ${params.DOCKER_REPO}:${params.DOCKER_TAG}"
                }
            }
        }

        stage('Deploy to kubernetes'){
            steps{
                script{
                    kubernetesDeploy (configs: 'k8s-deployment.yaml', kubeconfigId: 'k8configpwd')
                }
            }
        }
    }
}
