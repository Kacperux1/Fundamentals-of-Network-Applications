pipeline {
    agent any

    environment {
        DOCKER_LOGIN = credentials("docker_login")
        DOCKER_PASS = credentials("docker_pass")
    }
    options {
        disableResume()
    }
    stages {
          stage('Uruchomienie środowizka konteneryzacyjnego z pomocą zbudowanmych obrazów umieszczonych na Docker Hubie') {
            steps {
                dir("env_prod") {
                    sh 'docker compose down'
                    sh 'sleep 60'
                    sh 'docker login -u ${DOCKER_LOGIN} -p ${DOCKER_PASS}'
                    sh 'docker compose up -d'
                }
            }
        }
    }
}
    
