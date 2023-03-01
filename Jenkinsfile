def mainBranch = false

pipeline {
  agent any
  environment {
          PATH = "/opt/gradle/gradle-6.3/bin:$PATH"
      }

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