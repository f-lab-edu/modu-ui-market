echo "---build start---"

pipeline {
  agent any
  environment {
    FROM_EMAIL = 'modu-ui-market jenkins <yujin.moma@gmail.com>';
    //TO_EMAIL = 'jungcali94@gmail.com,ckdbwls11@naver.com';
    TO_EMAIL = 'ckdbwls11@naver.com';
  }
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
          doFailPost();
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

void doFailPost(){
  echo "[${env.STAGE_NAME}] stage failed..."
  setBuildStatus("Build failed [stage:${env.STAGE_NAME}]", 'FAILURE');
  script{ def recipients = emailextrecipients([ [$class: 'DevelopersRecipientProvider'],[$class: 'CulpritsRecipientProvider']])
  echo "recipients = ${recipients}"
//   emailext subject: "${env.BRANCH_NAME} - Build#${currentBuild.number} - ${currentBuild.currentResult}!",
//     body: """<strong>branch</strong> : ${env.BRANCH_NAME}<br>
//             <strong>url</strong> : <a href=\"${env.JOB_URL}\">${env.JOB_URL}</a><br>
//             <strong>build number</strong> : Build#${currentBuild.number}<br>
//             <strong>stage</strong> : ${env.STAGE_NAME}<br>
//             <strong>result</strong> : ${currentBuild.currentResult}<br>
//             <strong>duration</strong> : ${currentBuild.duration/1000}s""",
//     from: "${env.FROM_EMAIL}",
//     to: "${env.FROM_EMAIL}"
    //recipientProviders : [[$class: 'DevelopersRecipientProvider']]
    //recipientProviders : [contributor()]
}