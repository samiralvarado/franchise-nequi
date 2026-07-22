# Franchise API - Bancolombia Clean Architecture

API de gestión de franquicias implementada con la arquitectura Clean Architecture de Bancolombia, Spring Boot 3.3.6, Java 21, MongoDB y despliegue en AWS.

## 📋 Tabla de Contenidos

- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos Previos](#requisitos-previos)
- [Setup Local](#setup-local)
- [Ejecución Local con Docker Compose](#ejecución-local-con-docker-compose)
- [Compilación y Testing](#compilación-y-testing)
- [API Endpoints](#api-endpoints)
- [Despliegue en AWS con Terraform](#despliegue-en-aws-con-terraform)
- [Documentación de Arquitectura](#documentación-de-arquitectura)
- [Solución de Problemas](#solución-de-problemas)

---

## 📁 Estructura del Proyecto

```
franchise-api/
├── applications/
│   └── app-service/              # Entry point de la aplicación Spring Boot
│       ├── src/main/java/co/com/bancolombia/
│       │   ├── FranchiseApplication.java     # @SpringBootApplication
│       │   └── config/                       # Beans y configuración
│       └── src/main/resources/
│           └── application.yml               # Configuración de aplicación
├── domain/
│   ├── model/                    # Entidades y Value Objects del negocio
│   └── usecase/                  # Casos de uso (lógica de aplicación)
├── infrastructure/
│   ├── driven-adapters/
│   │   └── mongo/                # Adaptador MongoDB (persistencia)
│   └── entry-points/
│       └── reactive-web/         # Controladores REST con Spring WebFlux
├── deployment/
│   ├── Dockerfile                # Build de imagen Docker multi-stage
│   └── docker-compose.yml        # Orquestación local (app + MongoDB)
├── terraform/                    # Infraestructura AWS (IaC)
│   ├── main.tf                   # Recursos: ECR, ECS, DocumentDB
│   ├── variables.tf              # Variables de configuración
│   ├── outputs.tf                # Outputs (URLs, endpoints)
│   └── README.md                 # Instrucciones de despliegue AWS
└── build.gradle                  # Configuración Gradle multiproyecto
```

### Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│ Application Layer (applications/app-service)            │
│ - Spring Boot entry point                               │
│ - Bean wiring y dependency injection                     │
│ - Configuration & ComponentScan                          │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────┴──────────────────────────────────────┐
│ Domain Layer (domain/)                                  │
│ ├─ Model: Entities, Value Objects, Business Rules      │
│ └─ UseCases: Application logic (interfaces)             │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────┴──────────────────────────────────────┐
│ Infrastructure Layer (infrastructure/)                  │
│ ├─ Driven Adapters: MongoDB, APIs externas             │
│ └─ Entry Points: REST Controllers, WebFlux handlers     │
└─────────────────────────────────────────────────────────┘
```

---

## 🔧 Requisitos Previos

### Desarrollo Local
- **Java 21+**: [OpenJDK 21](https://openjdk.java.net/projects/jdk/21/) o Eclipse Temurin
- **Gradle 8.5+**: Incluido con wrapper (`./gradlew`)
- **Docker Desktop**: Para MongoDB local
- **Git**: Control de versiones

### Despliegue AWS
- **AWS CLI**: Configurada con credenciales
- **Terraform 1.5+**: Infrastructure as Code
- **Docker**: Para build de imagen

---

## 🚀 Setup Local

### 1. Clonar Repositorio

```bash
git clone https://github.com/samiralvarado/franchise-nequi.git
cd franchise-api
```

### 2. Compilar el Proyecto

```bash
# Compilar todos los módulos
./gradlew build -x test

# O si deseas incluir tests
./gradlew build
```

**Notas sobre compilación:**
- El proyecto usa Gradle multiproyecto con módulos interdependientes
- Las dependencias de Jackson (Spring Boot) y Lombok (3.x) están configuradas para evitar conflictos
- La configuración cache de Gradle está deshabilitada (`gradle.properties`) por compatibilidad con el plugin de validación de estructura de Bancolombia

### 3. Variables de Entorno (opcional para desarrollo local)

Crea un archivo `.env` en la raíz (no se versionará):

```env
MONGO_URI=mongodb://localhost:27017/franchise_db
SERVER_PORT=8080
```

---

## 🐳 Ejecución Local con Docker Compose

### Inicio Rápido

```bash
# Desde la carpeta deployment/
cd deployment

# Inicia MongoDB y construye la app en Docker
docker-compose up --build

# La API estará disponible en: http://localhost:8080
```

### Servicios en docker-compose.yml

| Servicio | Puerto | Descripción |
|----------|--------|-------------|
| **mongodb** | 27017 | Base de datos MongoDB |
| **api** | 8080 | Aplicación Spring Boot |

### Detener Servicios

```bash
docker-compose down -v  # -v para eliminar volúmenes
```

### Verificar que todo funciona

```bash
# Health check
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

---

## 🧪 Compilación y Testing

### Build sin Tests

```bash
./gradlew build -x test
```

### Build con Tests

```bash
./gradlew build
```

### Ejecutar solo tests

```bash
./gradlew test
```

### Limpiar build anterior

```bash
./gradlew clean
```

### Verificar dependencias

```bash
./gradlew dependencies
```

---

## 🔌 API Endpoints

### Base URL
```
http://localhost:8080
```

### Documentación Interactiva (Swagger UI)
```
http://localhost:8080/swagger-ui.html
```

### Healthcheck
```http
GET /actuator/health
Content-Type: application/json

Respuesta:
{
  "status": "UP"
}
```

### Crear Franquicia
```http
POST /franchises
Content-Type: application/json

Body:
{
  "name": "Franquicia Centro",
  "code": "FC-001"
}

Respuesta (201 Created):
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Franquicia Centro",
  "code": "FC-001",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

### Obtener Franquicia
```http
GET /franchises/{id}

Respuesta (200 OK):
{
  "id": "507f1f77bcf86cd799439011",
  "name": "Franquicia Centro",
  "code": "FC-001"
}
```

---

## ☁️ Despliegue en AWS con Terraform

### Prerequisitos AWS

1. **AWS Account** con credenciales configuradas
2. **VPC e Subnets**: Tener los IDs de VPC y subnets disponibles
3. **ECR, ECS, DocumentDB**: Servicios habilitados en la región

### Archivos de Configuración

```
terraform/
├── main.tf              # Recursos principales
├── variables.tf         # Variables de entrada
├── outputs.tf           # Outputs (URLs)
├── versions.tf          # Provider configuration
├── terraform.tfvars     # Valores (NO subir a Git)
├── terraform.tfvars.example  # Template
└── modules/
    ├── ecr/             # Elastic Container Registry
    ├── ecs-fargate/     # Container orchestration
    └── documentdb/      # MongoDB managed (AWS)
```

### Paso 1: Preparar Variables

```bash
cd terraform

# Copiar template
cp terraform.tfvars.example terraform.tfvars

# Editar con tus valores AWS
nano terraform.tfvars
```

**terraform.tfvars requerido:**

```hcl
aws_region           = "us-east-2"
project_name         = "franchise-api"
vpc_id               = "vpc-xxxxxxxx"
subnet_ids           = ["subnet-xxxxxxxx", "subnet-yyyyyyyy"]
db_username          = "admin"
db_password          = "SecurePassword123!"
db_instance_count    = 1
db_instance_class    = "db.t3.medium"
container_port       = 8080
desired_count        = 1
task_cpu             = 512
task_memory          = 1024
```

### Paso 2: Inicializar Terraform

```bash
terraform init
```

### Paso 3: Crear ECR Repository

```bash
terraform apply -target=module.ecr -auto-approve
```

### Paso 4: Build y Push de Imagen Docker

```bash
# Obtener URL del ECR
ECR_URL=$(terraform output -raw ecr_repository_url)
ECR_REGISTRY=$(echo "$ECR_URL" | cut -d/ -f1)
AWS_REGION=$(terraform output -raw ecr_repository_url | cut -d. -f4)

# Login en ECR
aws ecr get-login-password --region "$AWS_REGION" \
  | docker login --username AWS --password-stdin "$ECR_REGISTRY"

# Build de imagen
docker build -t franchise-api ..

# Tag para ECR
docker tag franchise-api:latest "$ECR_URL:latest"

# Push
docker push "$ECR_URL:latest"
```

### Paso 5: Desplegar Infraestructura

```bash
terraform apply
```

Cuando Terraform pregunte, confirma con `yes`.

### Paso 6: Obtener URLs

```bash
# URL del Load Balancer
terraform output load_balancer_url

# URL base de la API
API_URL=$(terraform output -raw load_balancer_url)
echo "API URL: $API_URL"

# Verificar que funciona
curl "$API_URL/actuator/health"
```

### Actualizar API después de cambios de código

```bash
# Build nueva imagen
docker build -t franchise-api ..

# Tag y push
ECR_URL=$(terraform output -raw ecr_repository_url)
AWS_REGION=$(terraform output -raw ecr_repository_url | cut -d. -f4)

docker tag franchise-api:latest "$ECR_URL:latest"
docker push "$ECR_URL:latest"

# Redeploy
terraform apply

# O forzar redeploy sin cambiar infraestructura
aws ecs update-service \
  --region "$AWS_REGION" \
  --cluster franchise-api \
  --service franchise-api \
  --force-new-deployment
```

### Destruir Recursos AWS

```bash
terraform destroy
```

**⚠️ Advertencia**: Esto eliminará todos los recursos creados incluyendo DocumentDB, ECS tasks y ECR images.

---
## 📝 Git Workflow

### Crear feature branch (ejemplo completado)

```bash
# Feature branch ya existe
git checkout feature/franchise-nequi

# Hacer cambios
git add .
git commit -m "feat: description in English"

# Hacer push
git push origin feature/franchise-nequi

# Crear Pull Request
# URL: https://github.com/samiralvarado/franchise-nequi/pull/new/feature/franchise-nequi
```

### Merge a main

```bash
git checkout main
git pull origin main
git merge feature/franchise-nequi
git push origin main
```

---

## 📖 Referencias

- [Bancolombia Clean Architecture](https://github.com/bancolombia/scaffold)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Terraform AWS Provider](https://registry.terraform.io/providers/hashicorp/aws/latest/docs)
- [Docker Documentation](https://docs.docker.com/)

---

## 📄 Licencia

Este proyecto usa la arquitectura Clean Architecture de Bancolombia.

---

## 👥 Contacto

Para preguntas o issues, abrir un issue en el repositorio.
