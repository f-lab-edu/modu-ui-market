echo "---build start---"

pipeline {
  agent any
  stages{
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

    stage('Deploy') {
      steps {
        sshagent(credentials: ['deploy_server_ssh_key']) {
          sh "ssh -o StrictHostKeyChecking=no moma@${env.DEPLOY_HOST} -p ${env.DEPLOY_PORT} uptime"
          sh "scp -P ${env.DEPLOY_PORT} ./build/libs/modu-0.0.1-SNAPSHOT.jar moma@${env.DEPLOY_HOST}:/home/moma/modu"
          sh "ssh -o StrictHostKeyChecking=no -t moma@${env.DEPLOY_HOST} -p ${env.DEPLOY_PORT} ./deploy.sh"
        }
        echo 'deploy success'
      }
    }
  }
}