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
        sh 'exit 1' //TEST
        sh 'chmod +x gradlew'
        sh './gradlew test'
        echo 'test success'
      }
      post {
        failure {
          echo "[${env.STAGE_NAME}] stage failed..."
          setBuildStatus("Build failed [stage:'${env.STAGE_NAME}']", "FAILURE");
          emailext subject: '''${DEFAULT_SUBJECT}''',
            body: "<p>${PROJECT_NAME} - Build # ${BUILD_NUMBER} - ${BUILD_STATUS}:</p><p>Check console output at ${BUILD_URL} to view the results.</p><p>Stage : ${env.STAGE_NAME}</p>",
            //to: "jungcali94@gmail.com,ckdbwls11@naver.com"
            to: "ckdbwls11@naver.com"
        }
      }
    }

    stage('Build') {
      steps {
        sh './gradlew clean build -x test'
        echo 'build success'
      }
    }

    stage('Deploy') {
      when{
        branch 'develop'
      }
      steps {
        sshagent(credentials: ['deploy_server_ssh_key']) {
          sh "ssh -o StrictHostKeyChecking=no moma@${env.DEPLOY_HOST} -p ${env.DEPLOY_PORT} uptime"
          sh "scp -P ${env.DEPLOY_PORT} ./build/libs/modu-0.0.1-SNAPSHOT.jar moma@${env.DEPLOY_HOST}:/home/moma/modu"
          sh "ssh -o StrictHostKeyChecking=no -t moma@${env.DEPLOY_HOST} -p ${env.DEPLOY_PORT} ./deploy.sh product ${env.DB_URL} ${env.DB_USERNAME} ${env.DB_PASSWORD}"
        }
        echo 'deploy success'
      }
    }
  }
  post {
    success {
      setBuildStatus("Build succeeded", "SUCCESS");
    }
  }
}

void setBuildStatus(String message, String state){
  step([
    $class: "GitHubCommitStatusSetter",
    reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/f-lab-edu/modu-ui-market"],
    contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/modu-ui-market/build-status"],
    errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result:"UNSTABLE"]],
    statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
  ]);
}
