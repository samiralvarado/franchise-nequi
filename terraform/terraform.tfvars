aws_region        = "us-east-2"
project_name      = "franchise-api"
container_image   = "945100415747.dkr.ecr.us-east-2.amazonaws.com/franchise-api:latest"
container_port    = 8080
desired_count     = 1
task_cpu          = 512
task_memory       = 1024

db_username       = "franchiseadmin"
db_password       = "8Hj1210lxwl*"
db_instance_count = 1
db_instance_class = "db.t3.medium"

vpc_id            = "vpc-054245818f58328e6"
subnet_ids        = [
  "subnet-014e73a574eb6595a",
  "subnet-07b12c4024785c977"
]