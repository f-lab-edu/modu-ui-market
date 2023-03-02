echo "---build start---"

node {
  def DEPLOY_HOST = '106.10.52.101'
  def DEPLOY_PORT = '2022'

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

  stage('Deploy') {
    sshagent(credentials: ['deploy_server_ssh_key']) {
      sh "ssh -o StrictHostKeyChecking=no moma@${DEPLOY_HOST} -p ${DEPLOY_PORT} uptime"
      sh "scp -P ${DEPLOY_PORT} ./build/libs/modu-0.0.1-SNAPSHOT.jar moma@${DEPLOY_HOST}:/home/moma/modu"
      sh "ssh -o StrictHostKeyChecking=no -t moma@${DEPLOY_HOST} -p ${DEPLOY_PORT} ./deploy.sh"
    }
    echo 'deploy success'
  }
}