def mainBranch = false

pipeline {
  agent any

  stages {

    stage('Git Checkout') {
      steps {
        checkout scm
        echo 'Git Checkout Success!'
      }
    }

    stage('Test') {
      steps {
        sh './gradlew test'
        echo 'test success'
      }
    }

    stage('Build') {
      steps {
        sh 'gradle clean build --exclude-task test'
        echo 'build success'
      }
    }
  }
}