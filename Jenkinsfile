pipeline {
  environment {
    registry = "avi3s/cg-blog"
    registryCredential = 'cf528b82-b2cb-4ab9-9969-de40f06e7ab3'
    dockerImage = ''
    dockerImageLatest = ''
  }
  agent any
  stages {
    stage('Cloning Git') {
      steps {
        git branch: 'main',
          credentialsId: 'cf528b82-b2cb-4ab9-9969-de40f06e7ab3',
          url: 'https://github.com/avi3s/cg-blog.git'
      }

    }
    stage('Build') {
      steps {
        bat 'mvnw clean install'
      }
    }
    stage('Building Docker Image') {
      steps {
        script {
          dockerImage = docker.build registry + ":$BUILD_NUMBER"
          dockerImageLatest = dockerImage
        }
      }
    }
    stage('Push Image to Docker Hub') {
      steps {
        script {
          docker.withRegistry('', registryCredential) {
            dockerImage.push()
            dockerImageLatest.push('latest')
          }
        }
      }
    }
    stage('Remove Unused docker image') {
      steps {
        bat "docker rmi $registry:$BUILD_NUMBER"
        bat "docker rmi $registry:latest"
      }
    }

  }
}