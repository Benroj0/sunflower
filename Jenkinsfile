pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        // Alineados perfectamente con tu docker-compose.yml
        DOCKER_PROJECT_NAME = 'sunflowerapp'
        APP_CONTAINER_NAME  = 'ecommerce_app'
        DB_CONTAINER_NAME   = 'ecommerce_mysql'
        SONARQUBE_URL       = 'http://sonarqube:9000'
        SONARQUBE_TOKEN     = credentials('Sonarqube')
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

        stage('Start Test Database') {
            steps {
                echo '🗄️ === INICIANDO MYSQL PARA PRUEBAS ==='
                dir('docker') {
                    // Usamos "mysql" que es el nombre real del servicio en tu docker-compose.yml
                    sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d mysql"
                    echo '⏳ Esperando 20 segundos a que MySQL inicialice por completo...'
                    sleep 20
                }
            }
        }

        stage('Test') {
            environment {
                // Forzamos a Spring Boot a usar el puerto 3307 que expone tu contenedor hacia el host
                SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3307/ecommerce_db?serverTimezone=America/Lima&useSSL=false&allowPublicKeyRetrieval=true'
                SPRING_DATASOURCE_USERNAME = 'root'
                SPRING_DATASOURCE_PASSWORD = 'benja'
            }
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

        stage('Docker Final Deploy') {
            steps {
                echo '🚀 === DESPLIEGUE FINAL DE LA APLICACIÓN WEB ==='
                script {
                    // Remueve el contenedor viejo usando el nombre real: ecommerce_app
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
                            if curl -f http://localhost:8081/home > /dev/null 2>&1; then
                                echo "✅ ¡La app Sunflower está respondiendo con éxito en el puerto 8081!"
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
            echo '🌐 Tu app está lista en: http://localhost:8081/home'
        }
        failure {
            echo '💥 Falló alguna etapa. Limpiando contenedores...'
            dir('docker') {
                sh "docker-compose -p ${DOCKER_PROJECT_NAME} down -v || true"
            }
        }
    }
}