output "ecs_security_group_id" {
  description = "The ID of the security group for the ECS tasks."
  value       = aws_security_group.ecs.id
}

output "container_image" {
  description = "The container image deployed to ECS."
  value       = aws_ecs_task_definition.api.container_definitions
}

output "load_balancer_url" {
  description = "The DNS name of the load balancer."
  value       = aws_lb.api.dns_name
}