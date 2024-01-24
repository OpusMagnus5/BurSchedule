pipeline {
    agent any

    tools {
        maven "Maven"
        git "Git"
        dockerTool "Docker"
    }

    stages {
        stage('Download repo') {
            steps {
                git branch: 'main', url: 'https://github.com/OpusMagnus5/BurSchedule'
            }
        }


        stage('Build App') {
            steps {
                sh "mvn clean install -T 0.5C"
            }
        }

        stage('Build Docker Image') {
            steps {
                script{
                    def dockerImageName = "szaddaj/burschedule:${DOCKER_IMAGE_VERSION}"
                    def dockerfile = "${WORKSPACE}/docker/Dockerfile"
                    docker.build(dockerImageName, "-f ${dockerfile} ${WORKSPACE}")
                }

            }
        }

        stage('Push Image') {
            steps {
                script{
                    def dockerImageName = "szaddaj/burschedule:${DOCKER_IMAGE_VERSION}"
                    docker.withRegistry('https://registry.hub.docker.com', 'docker_pass') {
                        docker.image(dockerImageName).push()
                    }
                }

            }
        }
    }
}
