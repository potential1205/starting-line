pipeline {
    agent any
    stages {
        stage('Prepare Secret') {
            steps {
                //test용 주석 추가
                withCredentials([string(credentialsId: 'application-secrets', variable: 'APPLICATION_SECRETS')]) {
                    sh '''
                        echo "${APPLICATION_SECRETS}" > ./src/main/resources/secret.properties
                        cat ./src/main/resources/secret.properties
                    '''
                }
            }
        }
        stage('Prepare SSH Key') {
            steps {
                // 'ec2-key'를 secret text로 받습니다.
                withCredentials([string(credentialsId: 'ec2-key', variable: 'EC2_KEY_CONTENT')]) {
                 sh '''
                     # Secret text로 받은 내용을 ec2_key.pem 파일로 저장
                     echo "${EC2_KEY_CONTENT}" > ec2_key.pem
                     chmod 400 ec2_key.pem
                     ls -l ec2_key.pem
                 '''
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t your-dockerhub-username/your-spring-app:latest .'
                sh 'docker push your-dockerhub-username/your-spring-app:latest'
            }
        }
        stage('Deploy') {
            steps {
                // 오직 springboot 서비스만 업데이트 (MySQL 등 다른 서비스는 그대로 유지)
                sh 'docker-compose -f /home/ubuntu/docker-compose.yml up -d springboot'
                // springboot 컨테이너 로그 저장 (선택적)
                sh 'docker logs springboot-app > /home/ubuntu/app.log &'
            }
        }
    }
    post {
        failure {
            echo "Build or deployment failed. Please check the logs."
        }
    }
}
