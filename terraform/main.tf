locals {
  tags = {
    Project = var.project_name
  }
}

data "aws_caller_identity" "current" {}

module "ecr" {
  source       = "./modules/ecr"
  project_name = var.project_name
  tags         = local.tags
}

module "ecs_fargate" {
  source = "./modules/ecs-fargate"

  aws_region      = var.aws_region
  project_name    = var.project_name
  vpc_id          = var.vpc_id
  subnet_ids      = var.subnet_ids
  container_image = var.container_image != "" ? var.container_image : "${module.ecr.repository_url}:latest"
  container_port  = var.container_port
  desired_count   = var.desired_count
  task_cpu        = var.task_cpu
  task_memory     = var.task_memory
  tags            = local.tags

  mongo_uri = module.documentdb.documentdb_connection_string
}

module "documentdb" {
  source = "./modules/documentdb"

  project_name        = var.project_name
  vpc_id              = var.vpc_id
  subnet_ids          = var.subnet_ids
  db_username         = var.db_username
  db_password         = var.db_password
  db_instance_count   = var.db_instance_count
  db_instance_class   = var.db_instance_class
  tags                = local.tags
}

resource "aws_security_group_rule" "ecs_to_documentdb_egress" {
  type                     = "egress"
  from_port                = 27017
  to_port                  = 27017
  protocol                 = "tcp"
  security_group_id        = module.ecs_fargate.ecs_security_group_id
  source_security_group_id = module.documentdb.documentdb_security_group_id
  description              = "Allow ECS tasks to connect to DocumentDB"
}

resource "aws_security_group_rule" "documentdb_to_ecs_ingress" {
  type                     = "ingress"
  from_port                = 27017
  to_port                  = 27017
  protocol                 = "tcp"
  security_group_id        = module.documentdb.documentdb_security_group_id
  source_security_group_id = module.ecs_fargate.ecs_security_group_id
  description              = "Allow DocumentDB to receive connections from ECS tasks"
}