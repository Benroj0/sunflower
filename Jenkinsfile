pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        IMAGE_NAME = 'ecommerce-app'
        CONTAINER_NAME = 'ecommerce-container'
        SONARQUBE_URL = 'http://sonarqube:9000'
        SONARQUBE_TOKEN = credentials('sonarqube-token')
    }

    stages {
        // 1. Checkout: Descarga del código
        stage('Checkout') {
            steps {
                echo '📥 === ETAPA 1: CHECKOUT ==='
                echo "Workspace: ${env.WORKSPACE}"
                checkout scm
            }
        }

        // 2. Build & Test: Ejecución de pruebas y generación de reportes
        stage('Build & Test') {
            steps {
                timeout(time: 15, unit: 'MINUTES') {
                    echo '🔨 === ETAPA 2: BUILD & TEST ==='
                    sh 'mvn clean test -X'
                    sh 'mvn package -DskipTests'
                }
            }
        }

        // 3. SonarQube Analysis: Envío de métricas al servidor de Sonar
        stage('SonarQube Analysis') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '📊 === ETAPA 3: ANÁLISIS DE CALIDAD CON SONARQUBE ==='
                    withSonarQubeEnv('sonarqube') {
                        sh '''
                            mvn sonar:sonar \
                                -Dsonar.projectKey=ecommerce-lp2 \
                                -Dsonar.projectName="E-commerce LP2" \
                                -Dsonar.projectVersion=${BUILD_NUMBER} \
                                -Dsonar.sources=src/main \
                                -Dsonar.tests=src/test \
                                -Dsonar.java.binaries=target/classes
                        '''
                    }
                    echo '✅ === FIN: ANÁLISIS DE CALIDAD COMPLETADO ==='
                }
            }
        }

        // 4. Docker Build & Deploy: Construcción de imagen y despliegue
        stage('Docker Build & Deploy') {
            steps {
                echo '🚀 === ETAPA 4: DOCKER BUILD & DEPLOY ==='
                script {
                    echo '1️⃣ Limpiando contenedores anteriores...'
                    try {
                        sh "docker stop ${CONTAINER_NAME}"
                        sh "docker rm ${CONTAINER_NAME}"
                    } catch (Exception e) {
                        echo "⚠️ No se encontraron contenedores activos previos."
                    }

                    echo '2️⃣ Construyendo imagen Docker desde la raíz del proyecto...'
                    sh "docker build -t ${IMAGE_NAME}:latest -f docker/Dockerfile ."

                    echo '3️⃣ Desplegando el contenedor en el puerto 8080...'
                    sh "docker run -d --name ${CONTAINER_NAME} -p 8080:8080 --net docker_default ${IMAGE_NAME}:latest"
                    
                    echo '✅ ¡Despliegue finalizado!'
                }
            }
        }

        // 5. Health Check: Verificación de que la aplicación está activa
        stage('Health Check') {
            steps {
                echo '🏥 === ETAPA 5: VERIFICACIÓN DE SALUD ==='
                script {
                    sleep(time: 5, unit: 'SECONDS')
                    sh '''
                        for i in {1..10}; do
                            if curl -f http://localhost:8080/home > /dev/null 2>&1; then
                                echo "✅ Aplicación respondiendo en puerto 8080"
                                exit 0
                            fi
                            echo "Intento $i de 10..."
                            sleep 3
                        done
                        echo "❌ La aplicación no responde después de 10 intentos"
                        exit 1
                    '''
                }
            }
        }
    }

    post {
        success {
            echo '🎉 ¡Pipeline completado con éxito!'
            echo "📦 Imagen creada: ${IMAGE_NAME}:latest"
            echo "🌐 Aplicación disponible en: http://localhost:8080"
            echo "📊 Reportes SonarQube: ${SONARQUBE_URL}"
        }
        failure {
            echo '💥 El pipeline falló en alguna etapa.'
            script {
                try {
                    sh "docker stop ${CONTAINER_NAME}"
                    sh "docker rm ${CONTAINER_NAME}"
                } catch (Exception e) {
                    echo "No se pudieron limpiar los contenedores"
                }
            }
        }
        always {
            echo '🧹 Limpieza de archivos temporales...'
            cleanWs()
        }
    }
}
