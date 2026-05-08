# ── ECR Repositories ─────────────────────────────────────────────────────────
# Free tier: 500 MB/month private storage, unlimited public pulls.
# Push your Docker images here and reference the URI in values.yaml.

locals {
  ecr_repos = ["backend-service", "database-service", "frontend"]
}

resource "aws_ecr_repository" "services" {
  for_each             = toset(local.ecr_repos)
  name                 = each.key
  image_tag_mutability = "MUTABLE"

  image_scanning_configuration {
    scan_on_push = true   # free ECR Basic scanning
  }

  tags = { Name = each.key }
}

# Lifecycle policy: keep only the last 5 images to stay within free tier storage
resource "aws_ecr_lifecycle_policy" "services" {
  for_each   = aws_ecr_repository.services
  repository = each.value.name

  policy = jsonencode({
    rules = [{
      rulePriority = 1
      description  = "Keep last 5 images"
      selection = {
        tagStatus   = "any"
        countType   = "imageCountMoreThan"
        countNumber = 5
      }
      action = { type = "expire" }
    }]
  })
}
