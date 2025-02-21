pipeline {
    agent any
    stages {
        stage('Prepare Secrets') {
            steps {
                // withCredentials를 사용해 secret file을 TEMP_FILE 변수에 할당
                withCredentials([file(credentialsId: 'secret-properties', variable: 'SECRET_FILE')]) {
                    sh '''
                        # 기존 secrets.properties 파일 삭제 (존재하지 않아도 오류 없이 진행)
                        rm -f src/main/resources/secrets.properties
                        # 새로운 secrets.properties 파일 복사
                        cp "$SECRET_FILE" src/main/resources/secrets.properties
                    '''
                }
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew build -x test'
            }
        }
        stage('Deploy') {
            steps {
                withCredentials([
                    sshUserPrivateKey(
                        credentialsId: 'server-ssh',    // SSH Username with private key 타입으로 등록한 credentials ID
                        keyFileVariable: 'SSH_KEY_FILE', // Jenkins가 임시 파일 경로를 저장할 변수
                        usernameVariable: 'SERVER_USER'  // ssh 사용자명
                    ),
                    string(credentialsId: 'server-ip', variable: 'SERVER_IP')
                ]) {
                        // 빌드
                        sh '''
                            scp -i "$SSH_KEY_FILE" -o StrictHostKeyChecking=no build/libs/gogoma-0.0.1-SNAPSHOT.jar ${SERVER_USER}@${SERVER_IP}:/home/ubuntu/backend/
                        '''
                        // 원격 서버에서 docker-compose 실행
                        sh '''
                            ssh -i "$SSH_KEY_FILE" -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_IP} "cd /home/ubuntu && sudo docker-compose stop springboot && sudo docker-compose up -d --build springboot"
                        '''
                }
            }
        }
    }
     post {
       failure {
           echo "Build or deployment failed. Please check the logs."
       }
   }
}
