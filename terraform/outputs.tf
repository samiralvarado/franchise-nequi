output "aws_account_id" {
  description = "AWS account where resources were created."
  value       = data.aws_caller_identity.current.account_id
}

output "ecr_repository_url" {
  description = "ECR repository URL where the Docker image must be pushed."
  value       = module.ecr.repository_url
}

output "load_balancer_url" {
  description = "Public URL for the API."
  value       = module.ecs_fargate.load_balancer_url
}

output "container_image" {
  description = "The container image deployed to ECS."
  value       = module.ecs_fargate.container_image
  sensitive   = true
}
