echo "---build start---"

node {
  stage('Git Checkout') {
    checkout scm
    echo 'Git Checkout Success!'
  }

  stage('Test') {
    sh 'chmod +x gradlew'
    sh './gradlew test'
    echo 'test success'
  }

  stage('Build') {
    sh './gradlew clean build -x test'
    echo 'build success'
  }
}