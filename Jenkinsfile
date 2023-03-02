echo "---build start---"

node {
  def deploy_server = '106.10.52.101'
  def deploy_server_port = '2022'

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
    sh 'pwd'
  }

  stage('Deploy') {
    sshagent(credentials: ['deploy_server_ssh_key']) {
      echo "server : ${deploy_server}, port : ${deploy_server_port}"
      sh "ssh -o StrictHostKeyChecking=no moma@${deploy_server} -p ${deploy_server_port} uptime"
      //sh 'scp -P ${deploy_server_port} /var/lib/jenkins/workspace/modu-ui-market/build/libs/modu-0.0.1-SNAPSHOT.jar moma@${deploy_server}:/home/moma/modu'
      //sh 'ssh -t moma@${deploy_server} -p ${deploy_server_port} ./deploy.sh'
    }
    echo 'deploy success'
  }
}