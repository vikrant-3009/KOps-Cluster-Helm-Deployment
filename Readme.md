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

### Installations

- **AWS CLI** => run "aws configure", then set access-key, secret-key, region.
- **KOps** => refer to their latest stable release from official GitHub repo
- **Terraform**
- **kubectl**
- **helm**

***
##### Extra Info:
1. Check out more info regarding kops commands.
   - kops create cluster --help | less
   
2. Get instance groups. 
   - kops get ig --name vikrant.k8s.local

3. SSH inside the control or worker node
   - ssh to the master: ssh -i ~/.ssh/id_rsa ubuntu@<public-ip-of-instance
***

## Next Steps

### 1. SetUp Kubernetes Cluster using KOps on AWS

1. Create an S3 bucket, and its enable versioning.
    - aws s3api create-bucket --bucket vikrant-kops-store --region us-east-1
    - aws s3api put-bucket-versioning --bucket vikrant-kops-store --versioning-configuration Status=Enabled
   
2. Export the S3 bucket, so that we don't have to specify the bucket with --state flag with every KOps command.
    - export KOPS_STATE_STORE="s3://vikrant-kops-store"

3. Create Kubernetes Cluster.
   - kops create cluster --name vikrant.k8s.local --zones us-east-1a \
      --control-plane-size t2.medium --node-size t2.medium --node-count 1 \
      --out . --target terraform
   - terraform init
   - terraform plan
   - terraform apply
   - kops export kubecfg --name vikrantkops.k8s.local --state s3://vikrant-kops-store
   - kops get clusters (It will list all the clusters)

   OR

   - kops create cluster --name vikrant.k8s.local --zones us-east-1a \
     --control-plane-size t2.medium --node-size t2.medium --node-count 1
   - kops edit cluster --name vikrant.k8s.local (if, we want to make any changes to the cluster configuration)
   - kops edit ig nodes-us-east-1a --name vikrant.k8s.local (if, we want to make any changes to the worker node configuration)
   - kops edit ig control-plane-us-east-1a --name vikrant.k8s.local (if, we want to make any changes to the master node configuration)
   - kops update cluster --name vikrant.k8s.local --yes
   - kops export kubecfg --name vikrantkops.k8s.local --state s3://vikrant-kops-store --admin
   - kops rolling-update cluster --name vikrant.k8s.local --yes (run this command, if made any changes to the cluster/node configuration)

4. Verify the cluster creation setup (cluster creation may take up to 10-15 min).
   - kops validate cluster --name vikrant.k8s.local
   - kubectl get nodes (If, correctly setup, all nodes master and worker will show up)
   - kubectl get pods -n kube-system

5. Delete the cluster.
   - kops delete cluster --name vikrant.k8s.local --yes

### 2. Deploy the application on the cluster using Helm Chart

1. Authenticate Docker to ECR.
   - aws ecr get-login-password --region eu-north-1 | docker login --username AWS --password-stdin 876724398547.dkr.ecr.eu-north-1.amazonaws.com/vikrantkatoch/calculator-app

2. Pull the latest docker image from ECR.
   - docker pull 876724398547.dkr.ecr.eu-north-1.amazonaws.com/vikrantkatoch/calculator-app:${BUILD_NUMBER}
   
3. Create Helm Chart
   - helm create calculator-app

4. Modify the image section inside the values.yaml file.

5. Deploy Your Application to the Kubernetes Cluster
   - kubectl create namespace test-1
   - helm install calc-app ./calculator-app --namespace test-1

6. Verify the Deployment
   - helm list --namespace test-1 (List all the helm deployments)
   - kubectl get pods --namespace test-1 (Check your Kubernetes pods)
   - kubectl logs calc-app-calculator-app-7476d77676-8kjsc --namespace test-1 (Check logs or troubleshoot a pod)
   - kubectl describe pod calc-app-calculator-app-7476d77676-8kjsc --namespace test-1
   - kubectl get services --namespace test-1
   - kubectl get all -n test-1

7. Update the Deployment (If needed) 
   - helm upgrade calc-app ./calculator-app --namespace test-1
   - helm upgrade calc-app ./calculator-app --namespace test-1 \
     --set image.repository=876724398547.dkr.ecr.eu-north-1.amazonaws.com/vikrantkatoch/calculator-app,image.tag=46

8. Clean Up (If needed)
   - helm delete calc-app --namespace test-1
   - kops delete cluster --name vikrant.k8s.local --yes

### 3. SetUp Prometheus-Grafana Monitoring using Helm

   - helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
   - helm repo update
   - helm install prometheus prometheus-community/kube-prometheus-stack --namespace test-1
   - kubectl patch svc prometheus-grafana -p '{"spec": {"type": "LoadBalancer"}}' -n test-1 (Change the prometheus-grafana service from "ClusterIP" type to "LoadBalancer" type)