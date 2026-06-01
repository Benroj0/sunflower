# Docker Setup para E-commerce LP2

Este directorio contiene la configuración necesaria para containerizar la aplicación e-commerce con Docker, Jenkins y SonarQube.

## Requisitos previos

- Docker instalado (versión 20.10+)
- Docker Compose instalado (versión 1.29+)
- Maven 3.8+
- Java 17+
- Git

## Estructura

```
docker/
├── Dockerfile           # Imagen Docker de la aplicación
├── docker-compose.yml   # Orquestación de servicios
└── README.md           # Este archivo
```

## Inicio rápido

### 1. Compilar la aplicación

```bash
mvn clean package -DskipTests
```

### 2. Levantar los servicios con Docker Compose

```bash
cd docker
docker-compose up -d
```

Esto levantará:
- **Aplicación E-commerce**: http://localhost:8080
- **MySQL Database**: localhost:3306
- **SonarQube**: http://localhost:9000
- **PostgreSQL** (para SonarQube): localhost:5432

### 3. Verificar que los servicios estén activos

```bash
docker-compose ps
```

### 4. Ver logs de la aplicación

```bash
docker-compose logs -f app
```

## Detener los servicios

```bash
docker-compose down
```

Para eliminar también los volúmenes de datos:

```bash
docker-compose down -v
```

## Configuración de SonarQube

1. Accede a http://localhost:9000
2. Login con credenciales por defecto (admin/admin)
3. Crea un nuevo proyecto:
   - Project key: `ecommerce-lp2`
   - Display name: `E-commerce LP2`
4. Genera un token de autenticación
5. Usa el token en el Jenkinsfile

## Configuración de Jenkins

1. Configura un new Pipeline Job en Jenkins
2. Apunta al repositorio Git de este proyecto
3. En "Pipeline script from SCM", selecciona:
   - Git URL: tu repositorio
   - Script Path: `Jenkinsfile`
4. En las credentials globales de Jenkins, añade:
   - sonarqube-token: Tu token de SonarQube

## Ejecutar tests

### Tests unitarios

```bash
mvn test
```

### Tests BDD con Cucumber

```bash
mvn test -Dtest=CucumberTest
```

### Cobertura de código (JaCoCo)

```bash
mvn clean test jacoco:report
```

Los reportes se generan en `target/site/jacoco/`

## Solucionar problemas

### "docker-compose: command not found"
```bash
# En Windows
# Asegúrate de tener Docker Desktop instalado con Compose incluido

# En Linux
sudo apt install docker-compose
```

### "Permission denied while trying to connect to the Docker daemon"
```bash
# En Linux
sudo usermod -aG docker $USER
# Luego reinicia la sesión o ejecuta
newgrp docker
```

### "Cannot connect to SonarQube"
Verifica que PostgreSQL esté corriendo:
```bash
docker-compose logs postgres
```

### "MySQL connection refused"
Espera 10-15 segundos a que MySQL se inicialice completamente

## Variables de entorno

Puedes personalizar variables en `docker-compose.yml`:

- `MYSQL_ROOT_PASSWORD`: Contraseña de MySQL (por defecto: root)
- `MYSQL_DATABASE`: Nombre de la base de datos (por defecto: ecommerce_db)
- `SONARQUBE_URL`: URL de SonarQube (por defecto: http://sonarqube:9000)

## Recursos útiles

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Cucumber/BDD Testing](https://cucumber.io/)
