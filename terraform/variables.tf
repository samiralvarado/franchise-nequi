variable "aws_region" {
  description = "AWS region where the infrastructure will be created."
  type        = string
  default     = "us-east-2"
}

variable "project_name" {
  description = "Name used to prefix AWS resources."
  type        = string
  default     = "franchise-api"
}

variable "container_image" {
  description = "Full Docker image URI. Leave empty to use the ECR repository created by this stack with the latest tag."
  type        = string
  default     = ""
}

variable "container_port" {
  description = "Port exposed by the Spring Boot container."
  type        = number
  default     = 8080
}

variable "desired_count" {
  description = "Number of ECS tasks to run."
  type        = number
  default     = 1
}

variable "task_cpu" {
  description = "Fargate task CPU units."
  type        = number
  default     = 512
}

variable "task_memory" {
  description = "Fargate task memory in MiB."
  type        = number
  default     = 1024
}

variable "db_username" {
  description = "Master username for the DocumentDB cluster."
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Master password for the DocumentDB cluster."
  type        = string
  sensitive   = true
}

variable "db_instance_count" {
  description = "Number of DocumentDB instances to create in the cluster."
  type        = number
  default     = 1
}

variable "db_instance_class" {
  description = "The instance type for the DocumentDB instances (e.g., db.t3.medium)."
  type        = string
  default     = "db.t3.medium"
}

variable "vpc_id" {
  description = "The VPC ID where resources will be deployed."
  type        = string
}

variable "subnet_ids" {
  description = "List of subnet IDs for the ALB and ECS tasks."
  type        = list(string)
}