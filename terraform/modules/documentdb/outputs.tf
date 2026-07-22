output "documentdb_connection_string" {
  description = "The connection string for the DocumentDB cluster."
  value       = "mongodb://${aws_docdb_cluster.api.master_username}:${aws_docdb_cluster.api.master_password}@${aws_docdb_cluster.api.endpoint}:${aws_docdb_cluster.api.port}/franchise_db?tls=true&replicaSet=rs0"
  sensitive   = true
}

output "documentdb_security_group_id" {
  description = "The ID of the DocumentDB security group."
  value       = aws_security_group.documentdb.id
}

