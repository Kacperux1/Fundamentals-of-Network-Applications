pipeline {
    agent any
    options {
        disableResume()
    }
    stages {
        stage('utworzenie i uruchomienie kontenerów') {
            steps {
                dir("Docker_single") {
                    sh 'docker compose up -d'
                }
            }
        }
        stage ('zbudowanie aplikacji backendowej') {
            steps {
                sh 'mvn clean package'
                sh 'cp ./target/facility_rental-0.0.1-SNAPSHOT.jar /opt/facility_rental/facility_rental.jar'
                sh 'systemctl restart facility_rental'
            }
        }
        stage('zbudowanie aplikacji frontendowej') {
            steps {
                dir("spa") {
                    sh 'npm run build'
                    sh 'cp -r ./build/* /var/www/html/'
                    sh 'systemctl restart apache2'
                }
            }
        }
       
    }
}
    
