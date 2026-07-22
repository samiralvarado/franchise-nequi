variable "aws_region" {
  description = "AWS region."
  type        = string
}

variable "project_name" {
  description = "Name used to prefix ECS resources."
  type        = string
}

variable "vpc_id" {
  description = "VPC id where ECS and ALB are deployed."
  type        = string
}

variable "subnet_ids" {
  description = "Subnet ids used by ALB and ECS tasks."
  type        = list(string)
}

variable "container_image" {
  description = "Docker image used by ECS."
  type        = string
}

variable "container_port" {
  description = "Application container port."
  type        = number
}

variable "desired_count" {
  description = "ECS desired task count."
  type        = number
}

variable "task_cpu" {
  description = "Fargate task CPU units."
  type        = number
}

variable "task_memory" {
  description = "Fargate task memory in MiB."
  type        = number
}

variable "mongo_uri" {
  description = "MongoDB connection string provided by the DocumentDB module."
  type        = string
  sensitive   = true
}

variable "tags" {
  description = "Tags applied to all resources."
  type        = map(string)
  default     = {}
}