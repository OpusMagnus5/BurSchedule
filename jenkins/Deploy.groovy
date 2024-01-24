pipeline {
    agent any

    tools {
        dockerTool "Docker"
        git "Git"
    }

    stages {

        stage('Download repo') {
            steps {
                git branch: 'main', url: 'https://github.com/OpusMagnus5/BurSchedule'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    sh """
                        docker stop burschedule &&
                        cp docker/docker-compose.yml /home/debian/burschedule/ &&
                        cd .. &&
                        cd /home/debian/burschedule &&
                        docker-compose up -d app
                    """
                }
            }
        }
    }
}