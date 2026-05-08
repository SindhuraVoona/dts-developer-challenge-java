output "eks_cluster_name" {
  description = "EKS cluster name — use in: aws eks update-kubeconfig --name <value>"
  value       = aws_eks_cluster.main.name
}

output "eks_cluster_endpoint" {
  description = "EKS API server endpoint"
  value       = aws_eks_cluster.main.endpoint
}

output "eks_cluster_ca" {
  description = "EKS cluster certificate authority data"
  value       = aws_eks_cluster.main.certificate_authority[0].data
  sensitive   = true
}

output "ecr_repository_urls" {
  description = "ECR image URIs — paste these into helm/task-manager/values.yaml"
  value = {
    for name, repo in aws_ecr_repository.services : name => repo.repository_url
  }
}

output "vpc_id" {
  value = aws_vpc.main.id
}

output "kubeconfig_command" {
  description = "Run this to configure kubectl after apply"
  value       = "aws eks update-kubeconfig --region ${var.aws_region} --name ${aws_eks_cluster.main.name}"
}
