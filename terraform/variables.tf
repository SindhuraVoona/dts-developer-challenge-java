variable "aws_region" {
  description = "AWS region to deploy into"
  type        = string
  default     = "eu-west-2"   # London — change to your preferred region
}

variable "environment" {
  description = "Deployment environment (dev / staging / prod)"
  type        = string
  default     = "dev"
}

variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
  default     = "task-manager"
}

variable "kubernetes_version" {
  description = "EKS Kubernetes version"
  type        = string
  default     = "1.31"
}

variable "node_instance_type" {
  description = "EC2 instance type for EKS worker nodes"
  type        = string
  # t3.small is the minimum practical size for running Spring Boot pods.
  # ⚠ NOT AWS free tier. Estimated cost: ~$0.023/hr per node (~$17/month for 1 node).
  # EKS control plane itself costs $0.10/hr (~$72/month).
  default     = "t3.small"
}

variable "node_desired_count" {
  type    = number
  default = 2
}

variable "node_min_count" {
  type    = number
  default = 1
}

variable "node_max_count" {
  type    = number
  default = 3
}

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}
