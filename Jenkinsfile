pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        DOCKER_PROJECT_NAME = 'sunflowerapp'
        APP_CONTAINER_NAME = 'sunflower_web_app'
        DB_CONTAINER_NAME = 'sunflower_db_mysql'
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONARQUBE_TOKEN = credentials('Sonarqube')
    }

    stages {
        stage('Checkout') {
            steps {
                cleanWs() 
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                echo '🔨 === COMPILANDO ARCHIVO JAR ==='
                sh 'mvn package -DskipTests'
            }
        }

        // 🗄️ Levantamos MySQL antes de los tests para que Cucumber no falle
        stage('Start Test Database') {
            steps {
                echo '🗄️ === INICIANDO MYSQL PARA PRUEBAS ==='
                dir('docker') {
                    // Levanta solo el contenedor de la base de datos
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d ${DB_CONTAINER_NAME} || true"
                    echo '⏳ Esperando 15 segundos a que MySQL acepte conexiones...'
                    sleep 15
                }
            }
        }

        // 🧪 Corre todas las pruebas (Unitarias con Mockito + Integración con Cucumber)
        stage('Test') {
            steps {
                echo '🧪 === EJECUTANDO TODAS LAS PRUEBAS (CUCUMBER Y UNITARIAS) ==='
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '📊 === ANÁLISIS EN SONARQUBE ==='
                    withSonarQubeEnv('sonarqube') {
                        sh '''
                            mvn sonar:sonar \
                                -Dsonar.host.url=${SONARQUBE_URL} \
                                -Dsonar.login=${SONARQUBE_TOKEN} \
                                -Dsonar.projectKey=ecommerce-lp2 \
                                -Dsonar.projectName="E-commerce LP2" \
                                -Dsonar.projectVersion=${BUILD_NUMBER} \
                                -Dsonar.sources=src/main \
                                -Dsonar.tests=src/test \
                                -Dsonar.java.binaries=target/classes
                        '''
                    }
                }
            }
        }

        // 🚀 El despliegue de la aplicación web queda al final
        stage('Docker Final Deploy') {
            steps {
                echo '🚀 === DESPLIEGUE FINAL DE LA APLICACIÓN WEB ==='
                script {
                    sh "docker rm -f ${APP_CONTAINER_NAME} || true"
                }
                dir('docker') {
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d --build"
                }
            }
        }

        stage('Health Check') {
            steps {
                echo '🏥 === VERIFICANDO DISPONIBILIDAD DE LA APP ==='
                script {
                    sh '''
                        echo "⏳ Esperando arranque interno de Spring Boot..."
                        sleep 20
                        COUNTER=1
                        while [ $COUNTER -le 15 ]; do
                            if curl -f http://host.docker.internal:8081/home > /dev/null 2>&1; then
                                echo "✅ ¡La app Sunflower está respondiendo con éxito!"
                                exit 0
                            fi
                            echo "Intento $COUNTER de 15... Reintentando..."
                            COUNTER=$((COUNTER + 1))
                            sleep 5
                        done
                        exit 1
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '🎉 ¡Pipeline completado con éxito!'
        }
        failure {
            echo '💥 Falló alguna etapa. Limpiando contenedores...'
            dir('docker') {
                sh "docker-compose -p ${DOCKER_PROJECT_NAME} down -v || true"
            }
        }
    }
}