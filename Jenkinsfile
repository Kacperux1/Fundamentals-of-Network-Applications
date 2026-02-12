pipeline {
    agent any
    options {
        disableResume()
    }
    stages {
          stage('zbudowanie aplikacji frontendowej') {
            steps {
                dir("spa") {
                    sh 'npm ci'
                    sh 'npm run build'
                    sh 'rm -rf /var/www/html/*'
                    sh 'cp -r ./dist/* /var/www/html/'
                }
            }
        }
        stage ('zbudowanie aplikacji backendowej') {
            steps {
               sh 'MAVEN_OPTS="-Xmx512m -Xms256m" mvn clean package -DskipTests'
                sh 'cp ./target/facility_rental-0.0.1-SNAPSHOT.jar /opt/facility_rental/facility_rental.jar'
                 dir("Docker_single") {
                    sh 'docker compose up -d'
                }
                sh 'systemctl restart facility_rental'
            }
        }
    }
}
    
