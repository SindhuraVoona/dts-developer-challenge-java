terraform {
  required_version = ">= 1.7"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }

  # Uncomment to store state in S3 (recommended for team use):
  # backend "s3" {
  #   bucket         = "your-terraform-state-bucket"
  #   key            = "task-manager/terraform.tfstate"
  #   region         = var.aws_region
  #   dynamodb_table = "terraform-state-lock"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project     = "task-manager"
      Environment = var.environment
      ManagedBy   = "terraform"
    }
  }
}
