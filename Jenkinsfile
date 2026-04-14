pipeline {
    agent any

    environment {
        IMAGE_NAME = 'cicd-demo'
        CONTAINER_NAME = 'cicd-demo-app'
        APP_PORT = '8082'
    }

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Test And Package') {
            steps {
                sh 'docker build -t ${IMAGE_NAME}:latest .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    if docker ps -a --format '{{.Names}}' | grep -Eq "^${CONTAINER_NAME}$"; then
                      docker rm -f "${CONTAINER_NAME}"
                    fi

                    docker image inspect "${IMAGE_NAME}:latest" >/dev/null 2>&1

                    docker run -d \
                      --name "${CONTAINER_NAME}" \
                      -p "${APP_PORT}:8080" \
                      --restart unless-stopped \
                      "${IMAGE_NAME}:latest"
                '''
            }
        }

        stage('Health Check') {
            steps {
                sh '''
                    for i in $(seq 1 30); do
                      if docker run --rm --network "container:${CONTAINER_NAME}" curlimages/curl:8.12.1 \
                        --silent --fail http://127.0.0.1:8080/health > /tmp/cicd-demo-health.json; then
                        cat /tmp/cicd-demo-health.json
                        exit 0
                      fi
                      sleep 2
                    done

                    echo "Uygulama saglik kontrolunden gecemedi."
                    docker logs "${CONTAINER_NAME}" || true
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline tamamlandi. Uygulama Docker uzerinde guncel haliyle calisiyor.'
        }
        failure {
            echo 'Pipeline basarisiz oldu. Jenkins konsol kayitlarini kontrol edin.'
        }
    }
}
