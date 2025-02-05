pipeline {
    agent any
    stages {
        stage('Prepare Secret') {
            steps {
                //test용 주석 추가
                withCredentials([string(credentialsId: 'application-secrets', variable: 'APPLICATION_SECRETS')]) {
                    sh '''
                        echo "${APPLICATION_SECRETS}" > ./src/main/resources/secret.properties
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
                        keyFileVariable: 'SSH_KEY_FILE', // Jenkins가 임시 파일 경로를 저장할 변수명
                        usernameVariable: 'SERVER_USER'  // SSH 사용자명을 저장할 변수명
                    ),
                    string(credentialsId: 'server-ip', variable: 'SERVER_IP')
                ]) {
                        // 빌드 결과물 위치 확인: build/libs 디렉토리 내 파일 목록 출력
                        sh '''
                            echo "Listing files in build/libs:"
                            ls -l build/libs
                        '''
                        // 파일 경로 확인 후 전송 (빌드 결과물이 build/libs 폴더에 있다고 가정)
                        sh '''
                            echo "Transferring JAR file to remote server..."
                            scp -i "$SSH_KEY_FILE" -o StrictHostKeyChecking=no build/libs/gogoma-0.0.1-SNAPSHOT.jar ${SERVER_USER}@${SERVER_IP}:/home/ubuntu/backend/
                        '''
                        // 원격 서버에서 docker-compose 실행
                        sh '''
                            echo "Updating springboot container on remote server..."
                            ssh -i "$SSH_KEY_FILE" -o StrictHostKeyChecking=no ${SERVER_USER}@${SERVER_IP} "cd /home/ubuntu/backend && sudo docker-compose -f /home/ubuntu/docker-compose.yml up -d springboot"
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
