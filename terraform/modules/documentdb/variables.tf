variable "project_name" {
  description = "Name used to prefix AWS resources."
  type        = string
}

variable "vpc_id" {
  description = "The ID of the VPC where DocumentDB will be deployed."
  type        = string
}

variable "subnet_ids" {
  description = "A list of subnet IDs where DocumentDB instances will be launched."
  type        = list(string)
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

variable "tags" {
  description = "A map of tags to assign to the resources."
  type        = map(string)
  default     = {}
}