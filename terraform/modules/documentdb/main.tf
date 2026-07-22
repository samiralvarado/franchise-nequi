resource "aws_security_group" "documentdb" {
  name        = "${var.project_name}-documentdb"
  description = "Security group for DocumentDB cluster"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = ["172.31.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = var.tags
}

resource "aws_docdb_cluster_parameter_group" "default" {
  family      = "docdb5.0"
  name        = "${var.project_name}-docdb-param-group"
  description = "DocDB cluster parameter group"

  parameter {
    name  = "tls"
    value = "enabled"
  }

  tags = var.tags
}

resource "aws_docdb_subnet_group" "default" {
  name       = "${var.project_name}-docdb-subnet-group"
  subnet_ids = var.subnet_ids

  tags = var.tags
}

resource "aws_docdb_cluster" "api" {
  cluster_identifier              = "${var.project_name}-docdb-cluster"
  engine                          = "docdb"
  master_username                 = var.db_username
  master_password                 = var.db_password
  db_subnet_group_name            = aws_docdb_subnet_group.default.name
  vpc_security_group_ids          = [aws_security_group.documentdb.id]
  skip_final_snapshot             = true
  db_cluster_parameter_group_name = aws_docdb_cluster_parameter_group.default.name
  apply_immediately               = true

  tags = var.tags
}

resource "aws_docdb_cluster_instance" "cluster_instances" {
  count              = var.db_instance_count
  identifier         = "${var.project_name}-docdb-instance-${count.index}"
  cluster_identifier = aws_docdb_cluster.api.id
  instance_class     = var.db_instance_class
  engine             = "docdb"

  tags = var.tags
}