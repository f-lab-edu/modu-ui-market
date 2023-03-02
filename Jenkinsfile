echo "---build start---"

node {
  stage('Git Checkout') {
    steps {
      checkout scm
      echo 'Git Checkout Success!'
    }
  }

  stage('Test') {
    steps {
      sh 'chmod +x gradlew'
      sh './gradlew test'
      echo 'test success'
    }
  }

  stage('Build') {
    steps {
      sh './gradlew clean build -x test'
      echo 'build success'
    }
  }
}