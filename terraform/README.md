# AWS Deploy With Terraform

This Terraform stack deploys the Spring Boot API to AWS using:

- ECR for the Docker image
- ECS Fargate for the container
- Application Load Balancer on port 80
- CloudWatch Logs
- Default VPC and default subnets

MongoDB is not created here. Pass a reachable MongoDB connection string with `mongo_uri`, for example Mongo Atlas or DocumentDB.

## Requirements

- AWS CLI configured with credentials
- Terraform installed
- Docker running locally

## Deploy

From the repository root:

```bash
cd infra
cp terraform.tfvars.example terraform.tfvars
```

Edit `terraform.tfvars` and set a real `mongo_uri`.

Create the ECR repository first:

```bash
terraform init
terraform apply -target=module.ecr
```

Build and push the image:

```bash
ECR_URL=$(terraform output -raw ecr_repository_url)
ECR_REGISTRY=$(echo "$ECR_URL" | cut -d/ -f1)
AWS_REGION=$(terraform output -raw ecr_repository_url | cut -d. -f4)

aws ecr get-login-password --region "$AWS_REGION" \
  | docker login --username AWS --password-stdin "$ECR_REGISTRY"

docker build -t franchise-api ..
docker tag franchise-api:latest "$ECR_URL:latest"
docker push "$ECR_URL:latest"
```

Create the rest of the infrastructure:

```bash
terraform apply
```

Get the API URL:

```bash
terraform output load_balancer_url
```

## Update The API

After code changes:

```bash
ECR_URL=$(terraform output -raw ecr_repository_url)
AWS_REGION=$(terraform output -raw ecr_repository_url | cut -d. -f4)
docker build -t franchise-api ..
docker tag franchise-api:latest "$ECR_URL:latest"
docker push "$ECR_URL:latest"
terraform apply
```

If ECS does not restart automatically because the image tag is still `latest`, force a new deployment:

```bash
aws ecs update-service \
  --region "$AWS_REGION" \
  --cluster franchise-api \
  --service franchise-api \
  --force-new-deployment
```

## Destroy

```bash
terraform destroy
```
