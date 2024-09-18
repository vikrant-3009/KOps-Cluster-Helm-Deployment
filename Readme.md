# Kubernetes Assignment

- Create a K8S cluster on AWS account using kops (1 master, 1 worker node) via terraform.
- Create a CI-CD pipeline that would build/perform code.
- Use any code scanner to identify vulnerabilities/code quality (Example-SonarQube or SonarCloud)
- Setup Quality Gates in SonarQube to 70%.
- If code does not meet code quality job should fail and email should be sent to relevant stakeholders.
- If code passes 70% quality, then it should proceed with next steps defined in pipeline.
- Store all the artifacts with proper versioning in any DMS like artifactory/nexus.
- Create a docker file to containerize application and push to ECR with proper tagging.
- Pull the latest image from ECR and deploy using Helm charts on a K8S cluster [setup to be done via Kops]
- Deploy few more applications on cluster (nginx, tomcat) so that multiple applications are hosted on same K8S Cluster.
- Use Prometheus to capture metrics of these applications and visualize the same on Grafana Dashboard.


### Jenkins Plugins Used

- SonarQube Scanner
- Nexus Artifactory Uploader
- Pipeline Utility Steps
- AWS Credentials
- Docker
- Amazon ECR